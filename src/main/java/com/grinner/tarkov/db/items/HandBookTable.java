package com.grinner.tarkov.db.items;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;
//物品价格表handbook.json，表对象
@Data
public class HandBookTable {
    @JSONField(name = "Items")
    private List<HandBookItemRow> items;

    @JSONField(name = "Categories")
    private List<HandBookItemRow> categories;
}
