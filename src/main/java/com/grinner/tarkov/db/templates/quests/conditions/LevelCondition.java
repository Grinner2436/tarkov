package com.grinner.tarkov.db.templates.quests.conditions;

import lombok.Data;

@Data
public class LevelCondition extends AbstractCondition{

    private String compareMethod;
    private String value;

    @Override
    public boolean valid() {
        return false;
    }
}
