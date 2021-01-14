package com.grinner.tarkov.jmodel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class PriceItem {

    @JSONField(name = "Id")
    private String id;

    @JSONField(name = "ParentId")
    private String category;

    @JSONField(name = "Price")
    private Integer price;

}