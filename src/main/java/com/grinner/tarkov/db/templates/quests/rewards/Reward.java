package com.grinner.tarkov.db.templates.quests.rewards;

import lombok.Data;

import java.io.Serializable;

@Data
public class Reward {
    //奖励项目ID，无意义
    private String id;
    //目标，商人的ID或者物品的ID
    private String target;
    //数量
    private String value;
    //奖励类型
    private String type;

    //索引，奖励项目在所有奖品中的排序
    private int index;
}
