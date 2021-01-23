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
class FinishConditionDeserializer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        List<Condition> result = new ArrayList<>();
        List<JSONObject> sourceArray = parser.parseArray(JSONObject.class);
        sourceArray.forEach(sourceObject -> {
            String clazz = sourceObject.getString("_parent");
            String props = sourceObject.getString("_props");
            if ("Elimination".equals(clazz)) {
                result.add(JSON.parseObject(props, EliminationCondition.class));
            } else if ("Kills".equals(clazz)) {
                result.add(JSON.parseObject(props, EliminationCondition.KillsCondition.class));
            } else if ("Location".equals(clazz)) {
                result.add(JSON.parseObject(props, EliminationCondition.Location.class));
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