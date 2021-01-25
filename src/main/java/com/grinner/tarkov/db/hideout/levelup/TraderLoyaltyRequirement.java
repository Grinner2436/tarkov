package com.grinner.tarkov.db.hideout.levelup;

import lombok.Data;

@Data
public class TraderLoyaltyRequirement extends HideoutLevelUpRequirement {
    private int loyaltyLevel;
    private String traderId;
}
