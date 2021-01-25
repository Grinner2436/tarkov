package com.grinner.tarkov.db.hideout.levelup;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class StageTable {
    @JSONField(deserializeUsing = HideoutRequirementDeserializer.class)
    private List<HideoutLevelUpRequirement> requirements;
    private List<Bonus> bonuses;
}
