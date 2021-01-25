package com.grinner.tarkov.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.grinner.tarkov.db.templates.quests.Quest;
import com.grinner.tarkov.db.templates.quests.QuestItem;
import com.grinner.tarkov.db.templates.quests.QuestRewardTable;
import com.grinner.tarkov.db.templates.quests.rewards.ItemReward;
import com.grinner.tarkov.db.templates.quests.rewards.Reward;
import com.grinner.tarkov.db.templates.quests.rewards.RewardItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class QuestUtil {
    public static Map<String, Quest> questMap;
    //item-questNames
    public static Map<String, StringBuffer> requiredQuestItemMap = new HashMap<>();
    //item-needNum
    public static Map<String, AtomicInteger> requiredQuestItemNumMap = new HashMap<>();
    //item-questNames
    public static Map<String, StringBuffer> rewardQuestItemMap = new HashMap<>();
    //item-needNum
    public static Map<String, AtomicInteger> rewardQuestItemNumMap = new HashMap<>();

    static {
        //基本设施
        String questsFilePath  = "eft-database\\db\\templates\\quests.json";
        questMap = JSONObject.parseObject(FileUtil.getFileContent(questsFilePath), new TypeReference<Map<String, Quest>>(){});

        questMap.values().stream().forEach(quest -> {
            //需要的物品
            List<QuestItem> questItems = quest.getConditions().getFinishConditions();
            if (questItems == null || questItems.isEmpty()) {
                return;
            }
            for (QuestItem questItem : questItems) {
                StringBuffer questNames =  requiredQuestItemMap.get(questItem.getItemId());
                if (questNames == null) {
                    questNames = new StringBuffer();
                    requiredQuestItemMap.put(questItem.getItemId(), questNames);
                }
                questNames.append(LocaleUtil.getName(quest.getId())).append(",");
                AtomicInteger total =  requiredQuestItemNumMap.get(questItem.getItemId());
                if (total == null) {
                    total = new AtomicInteger();
                    requiredQuestItemNumMap.put(questItem.getItemId(), total);
                }
                total.addAndGet(questItem.getValue());
            }
            //奖励的物品
            QuestRewardTable rewards = quest.getRewards();
            List<Reward> startRewards = rewards.getStartRewards();
            List<Reward> finishRewards = rewards.getFinishRewards();
            countRewardItems(startRewards, quest.getId());
            countRewardItems(finishRewards, quest.getId());
        });
    }

    private static void countRewardItems(List<Reward> rewards, String questId) {
        if (rewards != null) {
            rewards.forEach(reward -> {
                if ("Item".equals(reward.getType())) {
                    ItemReward itemReward = (ItemReward) reward;
                    List<RewardItem> rewardItems = itemReward.getItems();
                    rewardItems.forEach(rewardItem -> {
                        String itemId = rewardItem.getTemplateId();
                        int count = rewardItem.getCount();
                        StringBuffer questNames =  rewardQuestItemMap.get(itemId);
                        if (questNames == null) {
                            questNames = new StringBuffer();
                            rewardQuestItemMap.put(itemId, questNames);
                        }
                        questNames.append(LocaleUtil.getName(questId)).append(",");
                        AtomicInteger rewardTotal =  rewardQuestItemNumMap.get(itemId);
                        if (rewardTotal == null) {
                            rewardTotal = new AtomicInteger();
                            rewardQuestItemNumMap.put(itemId, rewardTotal);
                        }
                        rewardTotal.addAndGet(count);
                    });
                }
            });
        }
    }
}
