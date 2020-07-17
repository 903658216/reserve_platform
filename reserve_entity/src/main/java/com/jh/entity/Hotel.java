package com.jh.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Document(indexName = "hotel_index",shards = 1,replicas = 0)
public class Hotel implements Serializable {


  @TableId(type = IdType.AUTO)
  @Field(type = FieldType.Integer,index = false)
  private Integer id;

  @MultiField(
          mainField = @Field(type = FieldType.Text,analyzer = "ik_max_word"),
          otherFields = {
                  @InnerField(type = FieldType.Text, suffix = "pinyin", analyzer = "pinyin"),
                  @InnerField(type = FieldType.Keyword, suffix = "keyword")
          }
  )
  private String hotelName;
  @Field(type = FieldType.Keyword,index = false)
  private String hotelImage;
  @Field(type = FieldType.Integer)
  private Integer type;
  @MultiField(
          mainField = @Field(type = FieldType.Text,analyzer = "ik_max_word"),
          otherFields = @InnerField(type = FieldType.Text,suffix = "pinyin",analyzer = "pinyin")
  )
  private String hotelInfo;
  @Field(type = FieldType.Text,analyzer = "ik_max_word")
  private String keyword;
  @Transient
  private Double lon;
  @Transient
  private Double lat;

  @GeoPointField
  @TableField(exist = false)
  private Double[] myLocation = new Double[2];

  @Field(type = FieldType.Integer)
  private Integer star;
  @MultiField(
          mainField = @Field(type = FieldType.Text,analyzer = "ik_max_word"),
          otherFields = @InnerField(type = FieldType.Text,suffix = "pinyin",analyzer = "pinyin")
  )
  private String brand;
  @Field(type = FieldType.Text,analyzer = "ik_max_word")
  private String address;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Field(type = FieldType.Date,format = DateFormat.date)
  @JsonFormat(pattern = "yyyy年MM月dd日")
  private Date openTime;

  @Transient
  private Integer cid;

  @MultiField(
          mainField = @Field(type = FieldType.Text,analyzer = "ik_max_word"),
          otherFields = {
                  @InnerField(type = FieldType.Text, suffix = "pinyin", analyzer = "pinyin"),
                  @InnerField(type = FieldType.Keyword, suffix = "keyword")
          }
  )
  private String district;
  @Transient
  private Date createTime = new Date();
  @Transient
  private Integer status =0;

  @TableField(exist = false)
  @Field(type = FieldType.Nested)
  private City city;

  @TableField(exist = false)
  @Field(type = FieldType.Nested)
  private List<Room> roomList = new ArrayList<>();

  @TableField(exist = false)
  @ScriptedField
  private double avgPrice;

  @TableField(exist = false)
  @ScriptedField
  private double distance;

  @TableField(exist = false)
  @Field(type = FieldType.Long)
  private  Integer djl = 0;

  public Hotel setLon(Double lon) {
    this.lon = lon;
    this.myLocation[0] = lon;
    return this;
  }



  public Hotel setLat(Double lat) {
    this.lat = lat;
    this.myLocation[1]=lat;
    return this;
  }

  /**
   * 设置搜索时，酒店客房在指定时间段的平均价格
   * @param avgPrice
   */
//  public void setAvgPrice(double avgPrice) {
//    //格式化平均价格
//    DecimalFormat decimalFormat = new DecimalFormat("#.00");
//
//    try {
//      this.avgPrice = (double) decimalFormat.parse(avgPrice+"");
//    } catch (ParseException e) {
//      e.printStackTrace();
//    }
//  }

  /**
   * 在搜索时，记录当前用户位置与酒店位置之间的距离
   * @param distance
   */
//  public void setDistance(double distance) {
//    //格式化距离
//    DecimalFormat decimalFormat = new DecimalFormat("#.00");
//
//    try {
//      this.distance = (double) decimalFormat.parse(distance+"");
//    } catch (ParseException e) {
//      e.printStackTrace();
//    }
//  }


}
