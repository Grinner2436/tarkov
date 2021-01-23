package com.grinner.tarkov.db.locales;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
//
@Data
public class TemplateLocale {

    @JSONField(name = "Name")
    private String name;

    @JSONField(name = "ShortName")
    private String shortName;

    @JSONField(name = "Description")
    private String description;
}
