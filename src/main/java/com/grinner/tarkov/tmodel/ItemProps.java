package com.grinner.tarkov.tmodel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ItemProps {

    @JSONField(name = "Weight")
    private float weight;

    @JSONField(name = "Height")
    private int height;

    @JSONField(name = "Width")
    private int width;

    @JSONField(name = "QuestItem")
    private boolean questItem;

    @JSONField(name = "SpawnChance")
    private int spawnChance;

    @JSONField(name = "CreditsPrice")
    private int creditsPrice;
}

