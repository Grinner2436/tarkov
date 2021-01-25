package com.grinner.tarkov.db.hideout.levelup;

import lombok.Data;

@Data
public class AreaRequirement extends HideoutLevelUpRequirement {
    private int areaType;

    private String type;

    private int requiredLevel;
}
