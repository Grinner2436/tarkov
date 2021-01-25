package com.grinner.tarkov.db.hideout.levelup;

import lombok.Data;

import java.util.List;

@Data
public class StageTable {
    private List<HideoutLevelUpRequirement> requirements;
    private List<Bonus> bonuses;
}
