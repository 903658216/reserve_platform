package com.jh.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Room implements Serializable {


  @TableId(type = IdType.AUTO)
  @Field(type = FieldType.Integer)
  private Integer id;
  @Field(type = FieldType.Integer)
  private Integer hid;
  @MultiField(
          mainField = @Field(type = FieldType.Text,analyzer = "ik_max_word"),
          otherFields = @InnerField(type = FieldType.Text,suffix = "pinyin",analyzer = "pinyin")
  )
  private String title;
  @Transient
  private String area;
  @Transient
  private Integer num;
  @Transient
  private String image;
  @Field(type = FieldType.Text,analyzer = "ik_max_word")
  private String info;
  @Transient
  private String bed;
  @Field(type = FieldType.Integer)
  private Integer number;
  @Transient
  private BigDecimal price;
  @Transient
  private BigDecimal defaultPrice;
  @Transient
  private Integer status;

  @TableField(exist = false)
  @Field(type = FieldType.Nested)
  private List<RoomPrice> roomPriceList = new ArrayList<>();

}
