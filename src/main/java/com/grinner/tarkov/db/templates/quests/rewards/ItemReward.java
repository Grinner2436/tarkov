package com.grinner.tarkov.db.templates.quests.rewards;

import lombok.Data;

import java.util.List;

//Experience
@Data
public class ItemReward extends Reward {
    private List<RewardItem> items;
}


