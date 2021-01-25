package com.grinner.tarkov.db.templates.quests.conditions;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.grinner.tarkov.db.templates.quests.QuestItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public
class FinishConditionDeserializer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        List<QuestItem> result = new ArrayList<>();
        List<JSONObject> sourceArray = parser.parseArray(JSONObject.class);
        sourceArray.forEach(sourceObject -> {
            String clazz = sourceObject.getString("_parent");
            String props = sourceObject.getString("_props");
            if ("HandoverItem".equals(clazz)) {
                QuestItem questItem = new QuestItem();
                JSONObject source = JSON.parseObject(props);
                questItem.setItemId(source.getJSONArray("target").getString(0));
                questItem.setValue(source.getIntValue("value"));
                result.add(questItem);
            }
        });
        return (T) result;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}