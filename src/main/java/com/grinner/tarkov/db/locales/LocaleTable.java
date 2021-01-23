package com.grinner.tarkov.db.locales;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Map;
//
@Data
public class LocaleTable {
    @JSONField(name = "interface")
    private Map<String, String> interface_;
    private Map<String, String> error;
    private Map<String, String> mail;
    private Map<String, QuestLocale> quest;
    private Map<String, PresetLocale> preset;
    private Map<String, String> handbook;
    private Map<String, String> season;
    private Map<String, TemplateLocale> templates;
    private Map<String, TemplateLocale> locations;

    private Map<String, TraderLocale> trading;

}
