package com.grinner.tarkov.db.templates.quests;

import com.alibaba.fastjson.annotation.JSONField;
import com.grinner.tarkov.db.templates.quests.conditions.Condition;
import com.grinner.tarkov.db.templates.quests.conditions.StartConditionDeserializer;
import lombok.Data;

import java.util.List;

@Data
public class QuestConditionTable {

    @JSONField(name = "AvailableForStart", deserializeUsing = StartConditionDeserializer.class)
    private List<Condition> startConditions;

//    @JSONField(name = "AvailableForFinish", deserializeUsing = FinishConditionDeserializer.class)
//    private List<Condition> finishConditions;
}

