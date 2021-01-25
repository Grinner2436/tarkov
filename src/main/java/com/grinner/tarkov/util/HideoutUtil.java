package com.grinner.tarkov.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.grinner.tarkov.db.hideout.levelup.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class HideoutUtil {

    private static List<AreaTable> areaList;
    //item-questNames
    public static Map<String, StringBuffer> requiredAreaItemMap = new HashMap<>();
    //item-needNum
    public static Map<String, AtomicInteger> requiredAreaItemNumMap = new HashMap<>();
    static {
        String areaFilePath = "eft-database\\db\\hideout\\areas.json";
        areaList = JSONObject.parseObject(FileUtil.getFileContent(areaFilePath), new TypeReference<List<AreaTable>>(){});
        areaList.forEach(areaTable -> {
            Map<String, StageTable> stages = areaTable.getStages();
            stages.forEach((level, stage) -> {
                List<HideoutLevelUpRequirement> requirements = stage.getRequirements();
                requirements.forEach(requirement -> {
                    if (requirement instanceof ItemRequirement) {
                        ItemRequirement itemRequirement = (ItemRequirement) requirement;
                        StringBuffer areaStageNames =  requiredAreaItemMap.get(itemRequirement.getTemplateId());
                        if (areaStageNames == null) {
                            areaStageNames = new StringBuffer();
                            requiredAreaItemMap.put(itemRequirement.getTemplateId(), areaStageNames);
                        }

                        String nameKey = "hideout_area_"+ areaTable.getType() +"_name";
                        areaStageNames.append(LocaleUtil.getName(nameKey))
                                .append(level).append("级,");
                        AtomicInteger total =  requiredAreaItemNumMap.get(itemRequirement.getTemplateId());
                        if (total == null) {
                            total = new AtomicInteger();
                            requiredAreaItemNumMap.put(itemRequirement.getTemplateId(), total);
                        }
                        total.addAndGet(itemRequirement.getCount());
                    }
                });
            });
        });
    }
}
