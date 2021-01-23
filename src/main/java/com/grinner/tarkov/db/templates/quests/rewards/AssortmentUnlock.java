package com.grinner.tarkov.db.templates.quests.rewards;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

public class AssortmentUnlock extends Reward implements Serializable {

    private int loyaltyLevel;
    private String traderId;
    private List<RewardUnlockItem> items;

    @Data
    public static class RewardUnlockItem extends RewardItem {
        private int location;
    }
}
