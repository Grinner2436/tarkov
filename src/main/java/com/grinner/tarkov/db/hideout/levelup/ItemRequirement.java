package com.grinner.tarkov.db.hideout.levelup;

import lombok.Data;

@Data
public class ItemRequirement extends HideoutLevelUpRequirement {
    private int count;
    private String templateId;
}
