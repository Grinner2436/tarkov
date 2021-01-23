package com.grinner.tarkov.db.templates.quests.conditions;

import lombok.Data;

import java.util.List;

@Data
public class QuestCondition  extends AbstractCondition {

    private String target;
    private List<Integer> status;

    @Override
    public boolean valid() {
        return false;
    }
}
