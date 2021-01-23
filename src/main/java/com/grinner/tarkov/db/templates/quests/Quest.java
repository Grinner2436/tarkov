package com.grinner.tarkov.db.templates.quests;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;

//任务对象
@Data
public class Quest {

    @JSONField(name = "_id")
    private String id;

    private String name;

    //发放任务的商人
    private String traderId;

    public Quest() {

    }
    public Quest(String id, String name, String traderId) {
        this.id = id;
        this.name = name;
        this.traderId = traderId;
    }

    //完成任务的地图
    private String location;

    //任务类型
    private String type;

    //关键任务
    private boolean isKey;

    //能否重启
    private boolean restartable;

    //不用交就完成
    private boolean instantComplete;

    //秘密任务
    private boolean secretQuest;

    //接任务等级
    @JSONField(name = "min_level")
    private int minLevel;

    //游戏内通知
    private boolean canShowNotificationsInGame;

    @JSONField(name = "rewards")
    private QuestRewardTable rewards;

    @JSONField(name = "conditions")
    private QuestConditionTable conditions;

}
