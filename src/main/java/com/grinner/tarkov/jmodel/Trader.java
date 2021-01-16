package com.grinner.tarkov.jmodel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;
//商人信息表traders/base.json表对象
@Data
public class Trader {

    //保费
    private String insurance;

    //维修费
    private String repair;

    //最高级别
    private String loyalty;

    //折扣
    @JSONField(name = "discount")
    private float discount;

    private String nickname;

    @JSONField(name = "_id")
    private String id;

    @JSONField(name = "sell_category")
    private List<String> sellCategory;
}
