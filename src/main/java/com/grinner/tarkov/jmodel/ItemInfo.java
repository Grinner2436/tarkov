package com.grinner.tarkov.jmodel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
//翻译表，对应item的项目对象
@Data
public class ItemInfo {

    @JSONField(name = "Description")
    private String description;

    @JSONField(name = "ShortName")
    private String shortName;

    @JSONField(name = "Name")
    private String name;
}
