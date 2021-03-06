package com.grinner.tarkov.db.items;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
//物品价格表，项目对象
@Data
public class HandBookItemRow {

    @JSONField(name = "Id")
    private String id;

    @JSONField(name = "ParentId")
    private String category;

    @JSONField(name = "Price")
    private Integer price;
}