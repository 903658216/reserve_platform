
//全局变量，表示当前的动态字段的数量
var  index = 0;

function  addDynamicField() {

    var html = "";
    html += "<div id='field_div"+index+ "' >";
    html += "<hr/>";
    html += "动态字段的标识（页面显示的名称）：";
    html += "<input id='field_title"+index +"' /><br/>";
    html += "动态字段的名称（name属性）：";
    html += "<input id='field_name"+index + "' /><br/>";
    html += "动态字段输入类型：";
    html += "<select id='field_type"+index + "' onchange='fieldTypeChange("+index+",this.value);'>";
    html += "<option value='0'>文本框</option>";
    html += "<option value='1'>密码框</option>";
    html += "<option value='2'>日期选择框</option>";
    html += "<option value='3'>单选框</option>";
    html += "<option value='4'>复选框</option>";
    html += "<option value='5'>下拉框</option>";
    html += "</select>";
    html += "&nbsp;&nbsp;&nbsp;&nbsp;<span id='field_dic" + index + "'></span>";
    html += "&nbsp;&nbsp;&nbsp;&nbsp;<button type='button' onclick='removeDynamicField(" + index + ");'>-</button><br/>";
    html += "</div>";

    //将动态字段的属性输入，动态添加到页面上
    $("#dynamic_div").append(html);
    index++;
}

/**
 * 移除掉index处的动态字段
 * @param index
 */
function removeDynamicField(index){
    $("#field_div" + index).remove();
}


/**
 * 动态字段类型选择触发事件
 */
function fieldTypeChange(index, type){
    //获得当前选择的类型
    if(type == 3 || type == 4 || type == 5){
        //需要显示字典选择
        $.ajax({
            url: "http://localhost/couponDictionaries/getDictionariesList",
            success: function(data){
                if (data.code == 200){
                    //获得字典列表
                    var dics = data.data;
                    //将字典列表转换成下拉框
                    var dicSelect = "<select id='field_dic" + index + "'>";
                    //循环字典列表
                    for(var i = 0; i < dics.length; i++){
                        dicSelect += "<option value='" + dics[i].id + "'>" + dics[i].name + "</option>";
                    }
                    dicSelect += "</select>";

                    $("#field_dic" + index).html(dicSelect);
                }
            }
        });
    } else {
        //无需选择字典
        $("#field_dic" + index).html("");
    }
}


/**
 * 提交优惠券模板表单
 */
function submitTemplate() {

   //动态字段的数组
    var fields = [];

    //获得所有的动态字段
    for (var i = 0; i < index ; i++) {
        //当前动态元素被删除
        if ($("#field_div" + i).length == 0){
            continue;
        }

        //创建一个字段对象
        var field ={};
        //获得第i个动态字段的标识
        var title = $("#field_title"+i).val();
        field.title = title;
        //动态字段的name属性
        var name = $("#field_name"+i).val();
        field.name=name;
        //动态字段的输入类型
        var type =$("#field_type"+i).val();
        field.type=type;

        if (type ==3 || type == 4 || type==5){
            //字典的id
            var dic = $("#field_dic"+i).find("option:selected").val();
            field.dic=dic;
        }

        //将字段对象放入数组对象中
        fields.push(field);
    }
    //将动态字段拼接json
    console.log(JSON.stringify(fields));
    //将动态属性放入到隐藏域中，方便提交
    $("#templateDynamic").val(JSON.stringify(fields));

    //提交整个表单
    $("#formId").submit();

}

/**
 * 动态请求限制模板和规则模板，展示到页面上
 */
function getTemplate() {

    $.ajax({
        url:"http://localhost/couponTemplate/getCouponTemplates",
        success: function (data) {

            //请求正常响应
            if (data.code == 200){

                console.log("动态请求模板的结果"+ JSON.stringify(data.data));

                var ruleHtml = "<select id='rule_select' onchange='showDynamicField(0,this);'>";
                var limitHtml = "<select id='limit_select' onchange='showDynamicField(1,this);'>";

                ruleHtml += "<option>--请选择--</option>";
                limitHtml += "<option>--请选择--</option>";

                //获得模板的列表
                var  templates = data.data;
                console.log("动态请求限制模板和规则模板的响应结果"+JSON.stringify(templates));

                for (var i = 0; i < templates.length; i++) {
                    //其中的一个模板
                    var  template = templates[i];
                    var  templateHtml="";
                    templateHtml += "<option value='"+ template.id+"' templateClass='"+ template.templateClass +"' templateField='"+template.templateDynamic+"'>"+template.tname+"</option>";

                    //判断模板的类型
                    if (template.templateType ==0){
                        //规则模板
                        ruleHtml += templateHtml;
                    }else if (template.templateType ==1){
                        //限制模板
                        limitHtml += templateHtml;
                    }

                }

                ruleHtml += "</select>";
                limitHtml += "</select>";

                //分别将其设置到指定的div中
                $("#rule_div").html(ruleHtml);
                $("#limit_div").html(limitHtml);
            }
        }
    });

}

/**
 * TODO 动态展示模板的动态字段
 * @param type
 * @param element
 */
function showDynamicField(type ,element) {

    //获得当前需要展示的动态字段
    var templateClass = $(element).find("option:selected").attr("templateClass");
    var templateField = $(element).find("option:selected").attr("templateField");


    //处理动态字段
    var  fieldHtml ="";
    var  templateFieldJson = JSON.parse(templateField);
    //循环遍历动态属性字段
    for (var i = 0; i <templateFieldJson.length ; i++) {
        //没循环一次就代表一个属性的字段
        fieldHtml += templateFieldJson[i].title +"&nbsp;&nbsp;";
        fieldHtml += getFieldHtml(templateFieldJson[i],type);
        //console.log("fieldHtml"+fieldHtml);
        // console.log("type"+type);
        fieldHtml += "<br/>";
    }

    //将实现类保存到隐藏域，等提交表单时处理
    if (type ==0){
        $("#ruleClass").val(templateClass);
        $("#rule_field_div").html(fieldHtml);
    }else if(type == 1){
        $("#limitClass").val(templateClass);
        $("#limit_field_div").html(fieldHtml);
    }

}


/**
 * 根据动态字段的类型返回字段的输入html
 * @param fieldJson
 * @param type
 */
function getFieldHtml(fieldJson,type) {

    //获得当前字段输入类型
    var inputType = fieldJson.type;
    var inputHtml = "<span inputType='"+inputType+"' type='"+ type+"'>";

    switch (parseInt(inputType)) {

        case 0:
            //输入框
            // console.log("inputType0000");
            console.log(fieldJson);
            inputHtml += "<input type='text' name='"+ fieldJson.name +"'/>";
            break;
        case 1:
            //密码框
            inputHtml += "<input type='password' name='" + fieldJson.name + "'/>";
            break;
        case 2:
            //日期选择框
            alert("inputType222");
            inputHtml += "<input type='date' name='" + fieldJson.name + "'/>";
            break;
        case 3:
        case 4:
        case 5:
            //单选框
            //复选框
            //下拉库
            // alert("inputType345");

            //请求字典数据
            var  dic = fieldJson.dic;
            //ajax请求数据
            $.ajax({
                //设置ajax为同步请求
                async: false,
                url:"http://localhost/couponDictionaries/getDictionariesContentsByDId",
                data:{
                    did:dic
                },
                success:function (data) {

                    if (data.code == 200){
                        //获得字典的数据列表
                        var dicContents = data.data;

                        console.log("ajax请求字典的结果"+JSON.stringify(dicContents));

                        if (inputType == 5){
                            inputHtml += "<select name='"+ fieldJson.name+"'>";
                        }

                        for (var j = 0; j < dicContents.length ; j++) {
                            if(inputType == 3){
                                //单选框
                                console.log("type333"  + dicContents[j].value);
                                inputHtml += "<input type='radio' name='" + fieldJson.name + "' value='" + dicContents[j].value + "'/>" + dicContents[j].name;
                            } else if (inputType == 4){
                                console.log("type444"  + dicContents[j].value);
                                //复选框
                                inputHtml += "<input type='checkbox' name='" + fieldJson.name + "' value='" + dicContents[j].value + "'/>" + dicContents[j].name;
                            } else if(inputType == 5){
                                console.log("type555"  + dicContents[j].value);
                                //下拉框
                                inputHtml += "<option value='" + dicContents[j].value + "'>" + dicContents[j].name + "</option>";
                            }
                        }

                        if (inputType == 5){
                            inputHtml +="</select>";
                        }
                    }
                }
            });

            break;

    }
    inputHtml += "</span>";
    return inputHtml;

}

/**
 * 提交优惠券 -- 处理动态表单的数据
 */
function submitCoupon() {
    //处理规则
    var couponRule = getDynamicField(0);
    $("#ruleInfo").val(JSON.stringify(couponRule));

    //处理限制
    var couponLimit = getDynamicField(1);
    $("#limitInfo").val(JSON.stringify(couponLimit));

    console.log("couponLimit"+JSON.stringify(couponLimit));
    //提交表单
    $("#formId").submit();

}


/**
 * 提交表单时，获得动态表单的输入内容
 * @param type
 */
function getDynamicField(type){
    //获得所有规则的动态属性
    var fields = $("[inputType][type='" + type + "']");

    //获得对应的实现类
    var myClass = $("#" + (type == 0 ? "ruleClass" : "limitClass")).val();
    console.log("myClass ==="+myClass);

    //准备一个字段的数组
    var fieldsArray = [];

    //最终的对象
    var couponObj = {};
    couponObj.myclass = myClass;

    for(var i = 0; i <fields.length; i++){
        var span = fields[i];
        //获得span下的输入类型
        var inputType = $(span).attr("inputType");

        //创建动态属性对象
        var field = {};
        field.type = inputType;

        switch (parseInt(inputType)) {
            case 0:
            case 1:
            case 2:
                //文本框
                var input = $(span).find(":first-child");
                field.name = input.attr("name");
                field.value = input.val();
                break;
            case 3:
                //单选框
                var inputRadio = $(span).find("input[type='radio']:checked");
                field.name = inputRadio.attr("name");
                field.value = inputRadio.val();
                break;
            case 4:
                //复选框
                var inputCheckbox = $(span).find("input[type='checkbox']:checked");
                field.name = inputCheckbox.attr("name");
                var array = [];
                for(var j = 0; j < inputCheckbox.length; j++){
                    array.push(inputCheckbox[j].value);
                }
                field.value = array;
                break;
            case 5:
                //下拉框
                var select = $(span).find("select");
                field.name = select.attr("name");
                var option = $(select).find("option:selected");
                field.value = option.val();
                break;

        }
        fieldsArray.push(field);
    }

    //打印当前的动态字段
    couponObj.fields = fieldsArray;
    return couponObj;
}