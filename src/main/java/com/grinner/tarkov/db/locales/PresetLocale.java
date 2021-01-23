package com.grinner.tarkov.db.locales;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class PresetLocale {
    @JSONField(name = "Name")
    private String name;
}
