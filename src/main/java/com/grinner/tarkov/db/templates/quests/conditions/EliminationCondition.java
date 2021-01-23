package com.grinner.tarkov.db.templates.quests.conditions;

import com.alibaba.fastjson.annotation.JSONField;
import com.grinner.tarkov.db.templates.quests.rewards.RewardDeserializer;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EliminationCondition extends AbstractCondition{

    @JSONField(deserializeUsing = RewardDeserializer.class)
    private Counter counter;
    private String value;

    @Override
    public boolean valid() {
        return false;
    }

    @Data
    public static class Counter {
        private String id;
        private List<KillsCondition> conditions;
    }

    @Data
    public static class KillsCondition extends AbstractCondition implements Serializable {

        private String compareMethod;
        private String value;
        private String target;
        private List<String> savageRole;

        @Override
        public boolean valid() {
            return false;
        }
    }

    @Data
    public static class Location extends AbstractCondition {
        private String id;
        private List<String> target;

        @Override
        public boolean valid() {
            return false;
        }
    }
}
