package com.grinner.tarkov.util;

import com.alibaba.fastjson.JSONObject;
import com.grinner.tarkov.db.locales.LocaleTable;

import java.util.HashMap;
import java.util.Map;

public class LocaleUtil {
    //整个翻译表
    private static LocaleTable localeTable;

    //翻译表里所有翻译
    private static Map<String, String> nameMap = new HashMap<>();

    static {
        String localeFilePath  = "eft-database\\db\\locales\\global\\ch.json";
        localeTable = JSONObject.parseObject(FileUtil.getFileContent(localeFilePath), LocaleTable.class);
        nameMap.putAll(localeTable.getInterface_());
        nameMap.putAll(localeTable.getError());
        nameMap.putAll(localeTable.getMail());
        localeTable.getQuest().forEach((id, questLocale) -> {
            nameMap.put(id, questLocale.getName());
        });
        localeTable.getPreset().forEach((id, presetLocale) -> {
            nameMap.put(id, presetLocale.getName());
        });
        nameMap.putAll(localeTable.getHandbook());
        nameMap.putAll(localeTable.getSeason());
        localeTable.getTemplates().forEach((id, itemInfo) -> {
            nameMap.put(id, itemInfo.getName());
        });
        localeTable.getLocations().forEach((id, itemInfo) -> {
            nameMap.put(id, itemInfo.getName());
        });
        nameMap.put("54cb50c76803fa8b248b4571", "俄商Prapor");
        nameMap.put("54cb57776803fa99248b456e", "医生Therapist");
        nameMap.put("579dc571d53a0658a154fbec", "黑商");
        nameMap.put("58330581ace78e27b8b10cee", "配件商人Skier");
        nameMap.put("5935c25fb3acc3127c3d8cd9", "美商Peacekeeper");
        nameMap.put("5a7c2eca46aef81a7ca2145d", "枪商Mechanic");
        nameMap.put("5ac3b934156ae10c4430e83c", "服装商Ragman");
        nameMap.put("5c0647fdd443bc2504c2d371", "猎人Jaeger");
    }

    public static String getName(String id){
        String result = "未知翻译";
        if (nameMap.containsKey(id)) {
            result = safeName(nameMap.get(id));

        }
        return result;
    }

    public static String safeName(String source) {
        return source.replace("—","-")
                .replace("？","?")
                .replace("\"","'")
                .replace("%","\"")
                .replace("”","'")
                .replace("“","'")
                .replace("\n","'")
                .replace("\r","'")
        ;
    }

    public static void main(String[] args) {
        getName("");
    }
}
