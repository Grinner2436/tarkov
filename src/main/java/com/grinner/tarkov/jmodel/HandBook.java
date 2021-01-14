package com.grinner.tarkov.jmodel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class HandBook {
    @JSONField(name = "Items")
    private List<PriceItem> items;

    @JSONField(name = "Categories")
    private List<PriceItem> categories;
}
