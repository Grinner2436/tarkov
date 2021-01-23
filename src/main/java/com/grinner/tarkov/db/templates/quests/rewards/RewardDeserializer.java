package com.grinner.tarkov.db.templates.quests.rewards;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public
class RewardDeserializer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        List<Reward> result = new ArrayList<>();
        List<JSONObject> sourceArray = parser.parseArray(JSONObject.class);
        sourceArray.forEach(sourceObject -> {
            String sourceStr = sourceObject.toJSONString();
            String clazz = sourceObject.getString("type");
            if ("Experience".equals(clazz)) {
                result.add(JSON.parseObject(sourceStr, Experience.class));
            } else if ("Item".equals(clazz)) {
                result.add(JSON.parseObject(sourceStr, ItemReward.class));
            } else if ("TraderStanding".equals(clazz)) {
                result.add(JSON.parseObject(sourceStr, TraderStanding.class));
            } else if ("AssortmentUnlock".equals(clazz)) {
                result.add(JSON.parseObject(sourceStr, AssortmentUnlock.class));
            } else if ("Skill".equals(clazz)) {
                result.add(JSON.parseObject(sourceStr, Skill.class));
            } else if ("TraderUnlock".equals(clazz)) {
                result.add(JSON.parseObject(sourceStr, TraderUnlock.class));
            } else {
                System.out.println("未识别的任务奖励：" + sourceStr);
            }
        });
        return (T) result;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}