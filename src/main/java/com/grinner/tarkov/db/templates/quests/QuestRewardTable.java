package com.grinner.tarkov.db.templates.quests;

import com.alibaba.fastjson.annotation.JSONField;
import com.grinner.tarkov.db.templates.quests.rewards.Reward;
import com.grinner.tarkov.db.templates.quests.rewards.RewardDeserializer;
import lombok.Data;

import java.util.List;

@Data
public class QuestRewardTable {

    @JSONField(name = "Started", deserializeUsing = RewardDeserializer.class)
    private List<Reward> startRewards;

    @JSONField(name = "Success", deserializeUsing = RewardDeserializer.class)
    private List<Reward> finishRewards;

    private List<QuestItem> rewardItems;
}

