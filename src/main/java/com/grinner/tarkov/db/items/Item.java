package com.grinner.tarkov.db.items;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

//储存item属性的对象，包括从item.json的_prop解析的内容和其他内容
@Data
public class Item {

    private String id;

    //Item表直接关联的分类
    private String category;
    //翻译后的名字
    private String categoryName;
    //price表商人购买的分类
    private String sellCategory;
    //翻译后的名字
    private String sellCategoryName;

    //翻译后的名字
    private String name;

    //价格
    @JSONField(name = "CreditsPrice")
    private float creditsPrice;

    private float maxPrice;

    private String maxPriceTrader;

    private float cellPrice;

    private int totalCell;

    @JSONField(name = "Width")
    private int width;

    @JSONField(name = "Height")
    private int height;
    //重量
    @JSONField(name = "Weight")
    private float weight;

    //稀有程度
    @JSONField(name = "Rarity")
    private String rarity;

    @JSONField(name = "SpawnChance")
    private int spawnChance;

    //任务条件
    @JSONField(name = "QuestItem")
    private boolean questItem;
    private int questNum;
    private String requiredQuests;

    //任务奖励
    private boolean rewardItem;
    private int rewardNum;
    private String rewardQuests;

    //藏身处升级原料
    private boolean hideOutItem;
    private int hideOutNum;
    private String levelUps;

    //藏身处生产原料
    private boolean productionItem;
    private String productions;
}
