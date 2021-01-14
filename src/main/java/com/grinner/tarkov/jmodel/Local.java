package com.grinner.tarkov.jmodel;

import lombok.Data;

import java.util.Map;

@Data
public class Local {
    private Map<String, TraderInfo> trading;
    private Map<String, String> handbook;
    private Map<String, ItemInfo> templates;
//    private Map<String, String> quest;
}
