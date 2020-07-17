package com.jh.service.impl;

import com.jh.entity.Hotel;
import com.jh.entity.Room;
import com.jh.entity.RoomPrice;
import com.jh.entity.SearchInfo;
import com.jh.feign.HotelFeign;
import com.jh.service.ISearchService;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.sort.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SearchServiceImpl implements ISearchService {


    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Autowired
    private HotelFeign hotelFeign;


    /**
     * 创建索引库
     * @return
     */
    @Override
    public boolean createIndex() {
        IndexOperations indexOperations = restTemplate.indexOps(Hotel.class);
       //如果索引库不存在才会构建
        if (!indexOperations.exists()){
            System.out.println("索引库不存在，就进行创建！");
            indexOperations.create();
            //创建索引库的映射类型
            Document document = indexOperations.createMapping();
            indexOperations.putMapping(document);

            //将数据库中的数据同步到索引库中
            syncDataBase();
        }
//        System.out.println("索引库已经存在，不创建索引库");

        return false;
    }

    /**
     * 删除索引库
     * @return
     */
    @Override
    public boolean deleteIndex() {

        IndexOperations indexOperations =  restTemplate.indexOps(Hotel.class);

        return indexOperations.delete();
    }

    /**
     * 添加文档
     * @param hotel
     * @return
     */
    @Override
    public boolean insertDoc(Hotel hotel) {
        Hotel hotel1 = restTemplate.save(hotel);
        return hotel1 != null;
    }

    @Override
    public boolean insertRoom(Room room) {
        //将Room映射成Map对象
        Map<String,Object> roomMap = new HashMap<>();
        roomMap.put("id",room.getId());
        roomMap.put("hid",room.getHid());
        roomMap.put("title",room.getTitle());
        roomMap.put("info",room.getInfo());
        roomMap.put("number",room.getNumber());
        roomMap.put("roomPriceList",new ArrayList<>());

        Map<String,Object> params = new HashMap<>();
        params.put("roomInfo",roomMap);

        UpdateQuery updateQuery = UpdateQuery.builder(room.getHid()+"")
                .withScript("ctx._source.roomList.add(params.roomInfo)")
                .withParams(params)
                .build();

        UpdateResponse updateResponse = restTemplate.update(updateQuery,IndexCoordinates.of("hotel_index"));
        return updateResponse.getResult() == UpdateResponse.Result.UPDATED;
    }

    /**
     * 新增酒店客房类型时，对应的客房价格
     * @param hid
     * @param roomPriceList
     * @return
     */
    @Override
    public boolean insertRoomPrice(Integer hid,List<RoomPrice> roomPriceList){

        List<UpdateQuery> updateQueryList = new ArrayList<>();

        //循环客房价格集合
        roomPriceList.stream().forEach(roomPrice -> {

            //将RoomPrice映射成map集合
            Map<String,Object> roomPriceMap = new HashMap<>();
            roomPriceMap.put("id",roomPrice.getId());
            roomPriceMap.put("rid",roomPrice.getRid());
            roomPriceMap.put("price",roomPrice.getPrice());
            roomPriceMap.put("number",roomPrice.getNumber());
            roomPriceMap.put("hasNumber",roomPrice.getHasNumber());
            roomPriceMap.put("date",new SimpleDateFormat("yyyy-MM-dd").format(roomPrice.getDate()));

            //脚本的参数集合
            Map<String,Object> params = new HashMap<>();
            params.put("rid",roomPrice.getRid());
            params.put("priceInfo",roomPriceMap);

            String script ="for(r in ctx._source.roomList){ if(r.id == params.rid){r.roomPriceList.add(params.priceInfo); } }";
            UpdateQuery updateQuery = UpdateQuery.builder(hid+"")
                    .withScript(script)
                    .withParams(params)
                    .build();

            updateQueryList.add(updateQuery);

            });
        //进行批量的更改
        restTemplate.bulkUpdate(updateQueryList,IndexCoordinates.of("hotel_index"));

        return true;
    }

    /**
     * 调整酒店客房价格的信息
     * @param hid
     * @param roomPrice
     * @return
     */
    @Override
    public boolean updateRoomPrice(Integer hid,RoomPrice roomPrice){

        Map<String,Object> roomPriceMap = new HashMap<>();
        roomPriceMap.put("rid",roomPrice.getRid());
        roomPriceMap.put("roomPriceId",roomPrice.getId());
        roomPriceMap.put("roomPricePrice",roomPrice.getPrice());
//String script1 = "for(r in ctx._source.rooms){ if(r.id == params.rid) { for(rp in r.prices) { if(rp.id == params.rpid) { rp.price = params.price; } } } }";
        String script = "for(r in ctx._source.roomList){ if(r.id == params.rid ){ for(rp in r.roomPriceList){ if(rp.id == params.roomPriceId){rp.roomPriceList = params.roomPricePrice} } } }";
        UpdateQuery updateQuery = UpdateQuery.builder(hid+"")
                .withScript(script)
                .withParams(roomPriceMap)
                .build();

        UpdateResponse response = restTemplate.update(updateQuery,IndexCoordinates.of("hotel_index"));
        return true;
    }

    /**
     * 修改文档
     * @param params
     * @param id 索引的id
     * @return
     */
    @Override
    public boolean updateDoc(Map<String, Object> params, Integer id) {

        Document document = Document.create();
        params.entrySet().forEach(entry->{
            //将map、params中的键值对转成document内容
            document.append(entry.getKey(),entry.getValue());
        });

        UpdateQuery updateQuery = UpdateQuery.builder(id+"")
                .withDocument(document)
                .build();

        UpdateResponse response = restTemplate.update(updateQuery, IndexCoordinates.of("hotel_index"));


        return response.getResult() == UpdateResponse.Result.UPDATED;
    }

    /**
     * 删除文档
     * @param id
     * @return
     */
    @Override
    public boolean deleteDocById(Integer id) {

        String result = restTemplate.delete(id+"",Hotel.class);
        System.out.println("删除的结果"+ result);

        return result != null;
    }

    /**
     * 同步数据库和索引库的信息
     * 将数据库中的数据同步到ES索引库中
     */
    @Override
    public void syncDataBase() {

        //查询数据库
        List<Hotel> hotelList = hotelFeign.hotelList().getData();

        //循环遍历，将hotel添加到ES中
        hotelList.forEach(hotel -> {

//            List<Room> roomList = hotelFeign.selectRoomListByHid(hotel.getId()).getData();
//            hotel.setRoomList(roomList != null && roomList.size()>0? roomList:hotel.getRoomList());
//           roomList.stream().forEach(room -> {
//               List<RoomPrice> roomPriceList = hotelFeign.selectRoomPriceListByRid(room.getId()).getData();
//               room.setRoomPriceList(roomPriceList != null && roomPriceList.size()>0 ? roomPriceList : room.getRoomPriceList());
//           });
            this.insertDoc(hotel);
        });

    }

    /**
     * 搜索酒店信息
     * @param queryBuilder
     * @return
     */
    @Override
    public List<Hotel> search(QueryBuilder queryBuilder , List<SortBuilder> sort, ScriptField... scriptFields) {
        //本地（索引库）搜索查询
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryBuilder,null,sort);

        //处理脚本的自定义字段
        if (scriptFields!= null){
            //设置自定义字段
            nativeSearchQuery.addScriptField(scriptFields);
            //设置显示的字段
            String [] exclued = {"roomList","openTime"};
            nativeSearchQuery.addSourceFilter(new FetchSourceFilter(null,exclued));

        }




        //进行分页的设置
//        PageRequest pageRequest = PageRequest.of(0,2);
//        nativeSearchQuery.setPageable(pageRequest);


        //进行排序的设置--根据id进行升序排序
//        nativeSearchQuery.addSort(Sort.by(Sort.Order.asc("id")));

        //设置按照距离排序
//        Sort.Order myLocation = new GeoDistanceOrder("myLocation",new GeoPoint(22.625555,113.851466))
//                //根据平面的方式
//                .with(GeoDistanceOrder.DistanceType.plane)
//                //设置单位
//                .withUnit("km");

        //进行高亮设置
//        HighlightBuilder highlightBuilder = new HighlightBuilder();
//        highlightBuilder.field("hotelName").preTags("<font color='red'>").postTags("</font>");
//        nativeSearchQuery.setHighlightQuery(new HighlightQuery(highlightBuilder));





        //进行索引查询
        SearchHits<Hotel> hits = restTemplate.search(nativeSearchQuery,Hotel.class);
//        //总共命中了几个记录
//        long total = hits.getTotalHits();
//        System.out.println("符合本次查询的文档总共有："+total+"个");
//        //查询结果中，最高评分是
//        float maxScore = hits.getMaxScore();
//        System.out.println("本次查询中，最高文档的评分是："+maxScore);


        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        List<Hotel> hotelList = new ArrayList<>();
        hits.getSearchHits().stream().forEach( hotelSearchHit -> {

            //hotel的查询结果为
//            String id =hotelSearchHit.getId();
//            Hotel hotel = hotelSearchHit.getContent();
//
//            List<Object> sortValues = hotelSearchHit.getSortValues();
//            System.out.println("排序的字段为:"+sortValues);
//
//            List<String> hotelName =hotelSearchHit.getHighlightField("hotelName");
//            hotel.setHotelName(hotelName !=null && hotelName.size() > 0 ? hotelName.get(0) : hotel.getHotelName());
//            hotelList.add(hotel);

            Hotel hotel = hotelSearchHit.getContent();
            if(hotel.getAvgPrice() > 0){

                //手动格式化价格和距离
                hotel.setAvgPrice(Double.parseDouble(decimalFormat.format(hotel.getAvgPrice())));
                hotel.setDistance(Double.parseDouble(decimalFormat.format(hotel.getDistance())));

                hotelList.add(hotel);
            }
        });

        return hotelList;
    }

    /**
     * 根据客户端的请求数据进行搜索符合要求的酒店
     *
     *  搜索条件逻辑分析
     *  对于关键字进行着重点分析
     *   一级权重  -hotelName、keyword、brand、district、roomList.title
     *   二级权重  - hotelInfo、address
     *
     *  时间范围：该酒店在该时间范围内必须满足有客房是空余的
     *   - 开始入住时间
     *   - 离店时间
     *
     *  价格范围 ：该酒店满足有空余房间的同时，还需要满足该客房在入住期间，客房的平均价格在用户指定的价格期间，
     *      如果有多个客房同时满足，则选取平均价格最低的一个客房的平均价为返回的价格
     *   - 最小价格
     *   - 最大价格
     *
     *  位置 ：根据当前用户所定义的经纬度，和酒店的经纬度按照平面距离计算后，返回
     *
     * @param searchInfo 搜索信息实体类
     * @return
     */
    @Override
    public List<Hotel> searchByKeyword(SearchInfo searchInfo){

        //酒店符合查询
        BoolQueryBuilder hotelQuery = QueryBuilders.boolQuery();

        //匹配时间范围
        if (searchInfo.getBeginTime() != null && searchInfo.getEndTime() != null) {

            //房间价格表中的时间范围
            RangeQueryBuilder timeRangeQuery = QueryBuilders.rangeQuery("date")
                    .gte(searchInfo.getBeginTime())
                    .lt(searchInfo.getEndTime());

            //判断客房是否已经有预订满的了 -- 脚本查询
            ScriptQueryBuilder scriptQueryBuilder = QueryBuilders.scriptQuery(new Script("doc['roomList.roomPriceList.number'].value == doc['roomList.roomPriceList.hasNumber'].value"));

            //是否房间价格--有一天已经被预订满了的房间
            BoolQueryBuilder roomPriceListQuery = QueryBuilders.boolQuery()
                    .must(timeRangeQuery)
                    .must(scriptQueryBuilder);

            //房间价格的嵌套查询
            NestedQueryBuilder roomPriceListNestedQuery = QueryBuilders.nestedQuery("roomList.roomPriceList", roomPriceListQuery, ScoreMode.Avg);

            //客房列表的查询
            BoolQueryBuilder roomListBooleanQuery = QueryBuilders.boolQuery()
                    .mustNot(roomPriceListNestedQuery);

            //酒店房间的嵌套查询
            NestedQueryBuilder roomNestedQuery = QueryBuilders.nestedQuery("roomList", roomListBooleanQuery, ScoreMode.Avg);
            //时间范围的限制查询
            hotelQuery.must(roomNestedQuery);

        }

        //关键词查询匹配
        if (StringUtils.isEmpty(searchInfo.getKeyword())){
            MultiMatchQueryBuilder keywordQuery = QueryBuilders.multiMatchQuery(searchInfo.getKeyword())
                    .field("hotelName",5)
                    .field("hotelName.pinyin")
                    .field("brand",4)
                    .field("brand.pinyin")
                    .field("keyword",3)
                    .field("keyword.pinyin")
                    .field("district",3)
                    .field("district.pinyin");
            //设置muster
            hotelQuery.must(keywordQuery);
        }

        //根据城市查询
        NestedQueryBuilder cityQuery = QueryBuilders.nestedQuery("city",QueryBuilders.matchQuery("city.cityName",searchInfo.getCityName()),ScoreMode.Avg);
        //总查询
        BoostingQueryBuilder execQuery = QueryBuilders.boostingQuery(hotelQuery,cityQuery).negativeBoost(10);

        //加上点击率的评分机制，将原本的总查询语句嵌套到该评分查询机制
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(execQuery, ScoreFunctionBuilders.fieldValueFactorFunction("djl").setWeight(1f))
                .scoreMode(FunctionScoreQuery.ScoreMode.SUM)
                .boostMode(CombineFunction.MULTIPLY);
        //===================================
        //计算符合上述查询的平均价格
        String script = " double avgPirce = Integer.MAX_VALUE;\n" +
                "          SimpleDateFormat sdf = new SimpleDateFormat(\"yyyy-MM-dd\");\n" +
                "          \n" +
                "          if(params._source.roomList== null || params._source.roomList.length == 0){\n" +
                "            return -1;\n" +
                "          }\n" +
                "          \n" +
                "          //酒店的房型集合，依次循环所有房型\n" +
                "          for(r in params._source.roomList) {\n" +
                "              //当前房型的总价格\n" +
                "              double roomSumPrice = 0;\n" +
                "              int days = 0;\n" +
                "              //找到房型中的所有天数的价格集合\n" +
                "              for(rp in r.roomPriceList) {\n" +
                "                  \n" +
                "                  if(sdf.parse(rp.date).getTime() >= sdf.parse(params.beginTime).getTime() && sdf.parse(rp.date).getTime() < sdf.parse(params.endTime).getTime()) {\n" +
                "                    \n" +
                "                      //判断这个酒店在指定范围内是否有一天已经满了\n" +
                "                      if(rp.number == rp.hasNumber){\n" +
                "                        roomSumPrice = -1;\n" +
                "                        break;\n" +
                "                      }\n" +
                "                    \n" +
                "                      roomSumPrice += rp.price;\n" +
                "                      days++;\n" +
                "                  }\n" +
                "              }\n" +
                "  \n" +
                "              //结算当前房型的平均价\n" +
                "              if(roomSumPrice != -1){\n" +
                "                double roomAvgPirce = roomSumPrice / days;\n" +
                "                if (roomAvgPirce >= params.minPrice && roomAvgPirce <= params.maxPrice){\n" +
                "                    avgPirce = Math.min(avgPirce, roomAvgPirce);\n" +
                "                }\n" +
                "              }\n" +
                "          }\n" +
                "  \n" +
                "          return avgPirce == Integer.MAX_VALUE ? -1 : avgPirce;";

        //计算酒店与当前定位的距离
        String script2 = "return doc.myLocation.planeDistance(params.lat,params.lon)/1000;";

        //自定义脚本的参数
        Map<String, Object> params = new HashMap<>();
        params.put("beginTime",searchInfo.getBeginTime());
        params.put("endTime",searchInfo.getEndTime());
        params.put("minPrice",searchInfo.getMinPrice());
        params.put("maxPrice",searchInfo.getMaxPrice());
        params.put("lat",searchInfo.getLat());
        params.put("lon",searchInfo.getLon());

//        System.out.println("搜索服务接收到的经纬度为："+searchInfo.getLat()+","+searchInfo.getLon());

        //设置自定义字段
        ScriptField avgPrice = new ScriptField("avgPrice",new Script(ScriptType.INLINE,"painless",script,params));

        //设置距离的自定义字段
        ScriptField distance = new ScriptField("distance",new Script(ScriptType.INLINE,"painless",script2,params));


        //处理排序
        List<SortBuilder> sortBuilders = new ArrayList<>();
        switch (searchInfo.getSortType()){
            case 1:
                //智能排序
                break;
            case 2:
                //价格排序
                ScriptSortBuilder priceSortBuilder = SortBuilders.scriptSort(new Script(ScriptType.INLINE,"painless",script,params),ScriptSortBuilder.ScriptSortType.NUMBER).order(SortOrder.ASC);
                sortBuilders.add(priceSortBuilder);
                break;
            case 3:
                //按照距离排序
                GeoDistanceSortBuilder geoDistanceSortBuilder = SortBuilders.geoDistanceSort("myLocation",searchInfo.getLat(),searchInfo.getLon()).order(SortOrder.ASC);
                sortBuilders.add(geoDistanceSortBuilder);
                break;
        }
        //执行查询
        List<Hotel> hotelList = search(functionScoreQueryBuilder, sortBuilders,avgPrice, distance);
//        List<Hotel> hotelList = search(execQuery, sortBuilders,avgPrice, distance);

        System.out.println("查询的结果为："+hotelList);
        //        search();
        return hotelList;
    }

    /**
     * 根据订单服务的订单信息来修改elasticSearch中酒店的客房预订数量（指定时间范围）
     * @param hid
     * @param rid
     * @param rNumber
     * @param beginTime
     * @param endTime
     * @return
     */
    @Override
    public boolean updateRoomNumber(Integer hid, Integer rid, Integer rNumber, Date beginTime, Date endTime) {

        //TODO 根据订单信息修改ES中的酒店客房数量脚本
        String script = "SimpleDateFormat dfs = new SimpleDateFormat('yyyy-MM-dd');" +
                "for(r in ctx._source.roomList){" +
                "if(r.id == params.rid){" +
                " for(rp in r.roomPriceList) {" +
                " if(dfs.parse(rp.date).getTime() >= dfs.parse(params.beginTime).getTime() && dfs.parse(rp.date).getTime() < dfs.parse(params.endTime).getTime()){" +
                " rp.number += params.number " +
                "}}}}";
        Map<String,Object> params = new HashMap<>();
        params.put("rid",rid);
        params.put("number",rNumber);
        params.put("beginTime",beginTime);
        params.put("endTime",endTime);
        UpdateQuery updateQuery = UpdateQuery.builder(hid+"")
                .withScript(script)
                .withParams(params)
                .build();

        UpdateResponse response = restTemplate.update(updateQuery,IndexCoordinates.of("hotel_index"));

        return true;

    }


    /**
     * 更新ES中酒店的点击率
     * @param hid
     * @param djl
     */
    @Override
    public void updateDjl(Integer hid, Integer djl) {

        Map<String,Object> params = new HashMap<>();
        params.put("djl",djl);

        UpdateQuery updateQuery = UpdateQuery.builder(hid + "")
                .withScript("ctx._source.djl += params.djl")
                .withParams(params)
                .build();

        //TODO 更新ES中的酒店点击率事件
        restTemplate.update(updateQuery,IndexCoordinates.of("hotel_index"));
    }
}
