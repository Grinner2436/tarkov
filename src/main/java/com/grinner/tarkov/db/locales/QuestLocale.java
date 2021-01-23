package com.grinner.tarkov.db.locales;

import lombok.Data;

import java.util.Map;

//
@Data
public class QuestLocale {
    private String name;
    private String note;
    private Map<String, String> conditions;
    private String description;
    private String location;
}
