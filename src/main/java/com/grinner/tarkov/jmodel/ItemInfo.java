package com.grinner.tarkov.jmodel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ItemInfo {

    @JSONField(name = "Description")
    private String description;

    @JSONField(name = "ShortName")
    private String shortName;

    @JSONField(name = "Name")
    private String name;
}
