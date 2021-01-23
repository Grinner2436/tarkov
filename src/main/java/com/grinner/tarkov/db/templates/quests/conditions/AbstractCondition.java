package com.grinner.tarkov.db.templates.quests.conditions;

import lombok.Data;

@Data
public abstract class AbstractCondition implements Condition {
    private int index;
    private String parentId;
    private String id;
}
