package com.grinner.tarkov.tmodel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ItemValue {

    private String id;

    private String name;

    private String description;


    //数量
    @JSONField(name = "Weight")
    private float weight;

    @JSONField(name = "Height")
    private int height;

    @JSONField(name = "Width")
    private int width;

    private int totalCell;

    //价格
    @JSONField(name = "CreditsPrice")
    private float creditsPrice;

    private float maxPrice;

    private float cellPrice;

    private String maxPriceTrader;


    @JSONField(name = "QuestItem")
    private boolean questItem;

    @JSONField(name = "SpawnChance")
    private int spawnChance;

}
