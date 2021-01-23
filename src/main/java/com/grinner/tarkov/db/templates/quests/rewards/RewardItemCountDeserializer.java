package com.grinner.tarkov.db.templates.quests.rewards;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;
import java.util.List;

public
class RewardItemCountDeserializer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
//        List<JSONObject> sourceObject = parser.parseArray(JSONObject.class);
//        if (sourceObject != null && !sourceObject.isEmpty()) {
            JSONObject upd = parser.parseObject();
            Integer count = upd.getInteger("StackObjectsCount");
            return  (T) count;
//        }
//        return  (T) Integer.valueOf(1);
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}