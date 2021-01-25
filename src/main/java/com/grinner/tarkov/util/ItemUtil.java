package com.grinner.tarkov.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.grinner.tarkov.db.items.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ItemUtil {
    //整个Item表
    private static Map<String, ItemRow> itemRowsMap = new HashMap<>();
    public static Map<String, Item> itemsMap = new HashMap<>();
    private static HandBookTable handBook;
    public static Map<String, HandBookItemRow> priceItemsMap = new HashMap<>();
    private static Map<String, HandBookItemRow> priceCategoryMap = new HashMap<>();
    //商人表
    private static Map<String, Trader> tradersMap;
    private static Set<String> acceptedCategory;

    static {
        String tradersFilePath  = "eft-database\\db\\traders";
        tradersMap = FileUtil.getTradersMap(tradersFilePath);
        acceptedCategory = tradersMap.values().stream().map(Trader::getSellCategory)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        String itemsFilePath  = "eft-database\\db\\templates\\items.json";
        itemRowsMap = JSONObject.parseObject(FileUtil.getFileContent(itemsFilePath), new TypeReference<Map<String, ItemRow>>(){});
        //所有物品的价格和收购类别
        String handbookFilePath  = "eft-database\\db\\templates\\handbook.json";
        handBook = JSONObject.parseObject(FileUtil.getFileContent(handbookFilePath), HandBookTable.class);

        handBook.getCategories().forEach(priceCategoryItem -> {
            priceCategoryMap.put(priceCategoryItem.getId(), priceCategoryItem);
        });

        //替换成可购买的目录
        handBook.getItems().forEach(priceItem -> {
            priceItemsMap.put(priceItem.getId(), priceItem);
            String category = priceItem.getCategory();
            while (category != null) {
                if (!acceptedCategory.contains(category)) {
                    category = priceCategoryMap.get(category).getCategory();
                    continue;
                } else {
                    break;
                }
            }
            if (category != null) {
                priceItem.setCategory(category);
            } else {
//                System.out.println("没有商人购买此商品：" + LocaleUtil.getName(priceItem.getId()) + ":" + LocaleUtil.getName(priceItem.getCategory()));
            }
        });

        itemRowsMap.values().stream().filter(itemRow -> {
            return !"Not_exist".equals(itemRow.getProps().getRarity()) && priceItemsMap.containsKey(itemRow.getId());
        }).forEach(itemRow -> {
            Item item = itemRow.getProps();
            String id = itemRow.getId();
            item.setId(id);
            item.setName(LocaleUtil.getName(itemRow.getId()));
            item.setCategory(itemRow.getParentId());
            item.setCategoryName(LocaleUtil.getName(itemRow.getParentId()));
            HandBookItemRow handBookItemRow = priceItemsMap.get(itemRow.getId());
            if (handBookItemRow != null) {
                String category = handBookItemRow.getCategory();
                String categoryName = LocaleUtil.getName(category);
                if (category != null) {
                    if (!category.equals(item.getCategory())) {
//                        System.out.println(LocaleUtil.getName(item.getCategory()) + "】:【" + categoryName);
                    }
                }
                item.setSellCategory(category);
                item.setSellCategoryName(categoryName);
            } else {
                System.out.println("不存在的价格" + handBookItemRow);
            }
            item.setTotalCell(item.getHeight() * item.getWidth());
            //任务需求物品
            if (QuestUtil.requiredQuestItemMap.containsKey(id)) {
                AtomicInteger total = QuestUtil.requiredQuestItemNumMap.get(id);
                StringBuffer quests = QuestUtil.requiredQuestItemMap.get(id);
                item.setQuestItem(true);
                item.setQuestNum(total.get());
                item.setRequiredQuests(quests.toString());
            }
            //奖励物品
            if (QuestUtil.rewardQuestItemMap.containsKey(id)) {
                AtomicInteger total = QuestUtil.rewardQuestItemNumMap.get(id);
                StringBuffer quests = QuestUtil.rewardQuestItemMap.get(id);
                item.setRewardItem(true);
                item.setRewardNum(total.get());
                item.setRewardQuests(quests.toString());
            }

            //藏身处升级物品
            if (HideoutUtil.requiredAreaItemMap.containsKey(id)) {
                AtomicInteger total = HideoutUtil.requiredAreaItemNumMap.get(id);
                StringBuffer areas = HideoutUtil.requiredAreaItemMap.get(id);
                item.setHideOutItem(true);
                item.setHideOutNum(total.get());
                item.setLevelUps(areas.toString());
            }
            //商人价格
            Trader maxPriceTrader = getMaxPriceTrader(item.getSellCategory());
            if (maxPriceTrader == null) {
                return;
            }
            float maxPrice = item.getCreditsPrice() * (1F - (maxPriceTrader.getDiscount() / 100F));
            item.setMaxPrice(maxPrice);
            item.setMaxPriceTrader(LocaleUtil.getName(maxPriceTrader.getId()));
            item.setCellPrice(item.getCreditsPrice() / item.getTotalCell());
            itemsMap.put(id, item);
        });

    }

    private static Trader getMaxPriceTrader(String categoryId) {
        List<Trader> traders = new ArrayList<>();
        tradersMap.forEach((traderId, trader) -> {
            List<String> category = trader.getSellCategory();
            if (category.contains(categoryId)) {
                traders.add(trader);
                return;
            }
        });

        if (traders.size() > 0) {
            traders.sort(Comparator.comparingDouble(Trader::getDiscount));
            return traders.get(0);
        }
        return null;
    }

}
