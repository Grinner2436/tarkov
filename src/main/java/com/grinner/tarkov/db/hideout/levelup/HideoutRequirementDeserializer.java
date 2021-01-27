package com.grinner.tarkov.db.hideout.levelup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public
class HideoutRequirementDeserializer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        List<HideoutLevelUpRequirement> result = new ArrayList<>();
        List<JSONObject> sourceArray = parser.parseArray(JSONObject.class);
        sourceArray.forEach(sourceObject -> {
            String sourceStr = sourceObject.toJSONString();
            String clazz = sourceObject.getString("type");
            if ("Item".equals(clazz)) {
                result.add(JSON.parseObject(sourceStr, ItemRequirement.class));
            } else if ("TraderLoyalty".equals(clazz)) {
                result.add(JSON.parseObject(sourceStr, TraderLoyaltyRequirement.class));
            } else if ("Area".equals(clazz)) {
                result.add(JSON.parseObject(sourceStr, AreaRequirement.class));
            } else {
//                System.out.println("技能奖励：" + sourceStr);
            }
        });
        return (T) result;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}