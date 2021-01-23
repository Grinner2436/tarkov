package com.grinner.tarkov.db.templates.quests.conditions;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public
class StartConditionDeserializer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        List<Condition> result = new ArrayList<>();
        List<JSONObject> sourceArray = parser.parseArray(JSONObject.class);
        sourceArray.forEach(sourceObject -> {
            String clazz = sourceObject.getString("_parent");
            String props = sourceObject.getString("_props");
            if ("Level".equals(clazz)) {
                result.add(JSON.parseObject(props, LevelCondition.class));
            } else if ("Quest".equals(clazz)) {
                result.add(JSON.parseObject(props, QuestCondition.class));
            } else if ("TraderLoyalty".equals(clazz)) {
                result.add(JSON.parseObject(props, TraderLoyaltyCondition.class));
            } else {
                System.out.println("未识别的任务开启条件：" + props);
            }
        });
        return (T) result;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}