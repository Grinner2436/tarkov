package com.grinner.tarkov.db.templates.quests.rewards;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
@Data
public class RewardItem implements Serializable {

    @JSONField(name = "_id")
    private String id;

    @JSONField(name = "_tpl")
    private String templateId;
    //跟哪个奖励项目关联
    private String parentId;
    //在哪里存储，有些在其他物品的配件插槽里
    private String slotId;
    //数量
    @JSONField(name = "upd", deserializeUsing = RewardItemCountDeserializer.class)
    private int count;

    @Data
    public static class Upd implements Serializable {
        private int StackObjectsCount;
    }
}
