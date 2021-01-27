package com.grinner.tarkov.runner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.grinner.tarkov.db.items.Item;
import com.grinner.tarkov.util.FileUtil;
import com.grinner.tarkov.util.ItemUtil;
import com.grinner.tarkov.util.LocaleUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//计算所有商品回购价格
public class CommonFundRunner {
    private static Workbook wb = new XSSFWorkbook();
    private static String rootId = "54009119af1c881c07000029";

    public static void main(String[] args) {
        Map<String, String> newName = new HashMap<>();
        newName.put("5937ef2b86f77408a47244b3", "海关宿舍三层205棕色夹克->机械钥匙");
        newName.put("5914944186f774189e5e76c2", "海关检查站后备箱夹克->宿舍204钥匙");
        newName.put("59387ac686f77401442ddd61", "海关检查站后备箱夹克->宿舍114钥匙");
        newName.put("578f8778245977358849a9b5", "普通夹克->所有钥匙");

        newName.put("5909d7cf86f77470ee57d75a", "4x4武器箱");
        newName.put("5909d5ef86f77467974efbd8", "5x2武器箱");
        newName.put("5909d89086f77472591234a0", "5x5武器箱");
        newName.put("5909d76c86f77471e53d2adf", "6x3武器箱");

        List<Item> jacketItems = new ArrayList<>();
        List<Item> weaponItems = new ArrayList<>();
        String itemsFilePath  = "eft-database\\db\\loot\\statics.json";
        JSONObject staticLootTable = JSONObject.parseObject(FileUtil.getFileContent(itemsFilePath));
        for (Map.Entry<String, Object> entry : staticLootTable.entrySet()) {
            String lootTableId = entry.getKey();
            String lootTableName = LocaleUtil.getName(lootTableId);

            JSONObject info = (JSONObject) entry.getValue();
            JSONArray lootItems = info.getJSONArray("items");
            List<Item> items = new ArrayList<>();

            if (lootTableName.contains("夹克")) {
                for (int i = 0; i < lootItems.size(); i++) {
                    JSONObject lootItem = lootItems.getJSONObject(i);
                    String itemId = lootItem.getString("id");
                    Item item = ItemUtil.itemsMap.get(itemId);
                    if (item == null) {
                        continue;
                    }
                    int chance = lootItem.getIntValue("cumulativeChance");
                    item.setSpawnChance(chance);
                    item.setCategoryName(newName.get(lootTableId));
                    jacketItems.add(item);
                }
            } else if (lootTableName.contains("武器箱")) {
                for (int i = 0; i < lootItems.size(); i++) {
                    JSONObject lootItem = lootItems.getJSONObject(i);
                    String itemId = lootItem.getString("id");
                    Item item = ItemUtil.itemsMap.get(itemId);
                    if (item == null) {
                        continue;
                    }
                    int chance = lootItem.getIntValue("cumulativeChance");
                    item.setSpawnChance(chance);
                    item.setCategoryName(newName.get(lootTableId));
                    weaponItems.add(item);
                }
            } else {
                for (int i = 0; i < lootItems.size(); i++) {
                    JSONObject lootItem = lootItems.getJSONObject(i);
                    String itemId = lootItem.getString("id");
                    Item item = ItemUtil.itemsMap.get(itemId);
                    if (item == null) {
                        continue;
                    }
                    int chance = lootItem.getIntValue("cumulativeChance");
                    item.setSpawnChance(chance);
                    items.add(item);
                }

                if (!items.isEmpty()) {
                    if (newName.containsKey(lootTableId)) {
                        lootTableName = newName.get(lootTableId);
                    }
                    write(lootTableName, items);
                }
            }
        }

        //不同夹克物品统一到一张表
        write("夹克", jacketItems);
        write("武器箱", weaponItems);

        //夹克钥匙
        List<String> allkeyId = ItemUtil.itemsMap.values().stream().filter(item -> {
            return item.getName().contains("钥匙");
        }).map(Item::getId).collect(Collectors.toList());
        {
            String lootTableName = "衣服口袋不能找到的钥匙";
            JSONObject info = staticLootTable.getJSONObject("578f8778245977358849a9b5");
            JSONArray lootItems = info.getJSONArray("items");
            for (int i = 0; i < lootItems.size(); i++) {
                JSONObject lootItem = lootItems.getJSONObject(i);
                String itemId = lootItem.getString("id");
                allkeyId.remove(itemId);
            }

            List<Item> items = new ArrayList<>();
            for (int i = 0; i < allkeyId.size(); i++) {
                String itemId = allkeyId.get(i);
                Item item = ItemUtil.itemsMap.get(itemId);
                items.add(item);
            }
            write(lootTableName, items);
        }

        //文件柜钥匙
        {
            String lootTableName = "文件柜抽屉能找到的钥匙";
            JSONObject info = staticLootTable.getJSONObject("578f87b7245977356274f2cd");
            JSONArray lootItems = info.getJSONArray("items");
            List<Item> items = new ArrayList<>();
            for (int i = 0; i < lootItems.size(); i++) {
                JSONObject lootItem = lootItems.getJSONObject(i);
                String itemId = lootItem.getString("id");
                Item item = ItemUtil.itemsMap.get(itemId);
                if (item != null && item.getName().contains("钥匙")) {
                    int chance = lootItem.getIntValue("cumulativeChance");
                    item.setSpawnChance(chance);
                    items.add(item);
                }
            }
            write(lootTableName, items);
        }

        try (OutputStream fileOut = new FileOutputStream("物品生成几率表.xlsx")) {
            wb.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void write(String sheetName,List<Item> items) {
        Sheet sheet = wb.getSheet(sheetName);
        if (sheet != null) {
            sheetName = sheetName + System.currentTimeMillis();
        }
        Sheet sheet1 = wb.createSheet(sheetName);
        {
            String[] headers = new String[] {
                    "Id","所属分类","出售目录","名称",
                    "原价","最高售价","最高出价者","每格价值",
                    "总占用空间","宽度","高度","总重量",
                    "稀有程度","生成机会",
                    "任务","任务用量",
                    "奖励","奖励数量",
                    "藏身处","藏身处用量",
                    "总计用量",
//                    "生产","配方用量"
            };
            Row row = sheet1.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(headers[i]);
            }
        }
        for (int i = 1; i <= items.size(); i++) {
            Row row = sheet1.createRow(i);
            Item item = items.get(i - 1);
            {
                Cell cell = row.createCell(0);
                cell.setCellValue(item.getId());
            }
            {
                Cell cell = row.createCell(1);
                cell.setCellValue(item.getCategoryName());
            }
            {
                Cell cell = row.createCell(2);
                cell.setCellValue(item.getSellCategoryName());
            }
            {
                Cell cell = row.createCell(3);
                cell.setCellValue(item.getName());
            }
            {
                Cell cell = row.createCell(4);
                cell.setCellValue(item.getCreditsPrice());
            }
            {
                Cell cell = row.createCell(5);
                cell.setCellValue(item.getMaxPrice());
            }
            {
                Cell cell = row.createCell(6);
                cell.setCellValue(item.getMaxPriceTrader());
            }
            {
                Cell cell = row.createCell(7);
                cell.setCellValue(item.getCellPrice());
            }
            {
                Cell cell = row.createCell(8);
                cell.setCellValue(item.getTotalCell());
            }
            {
                Cell cell = row.createCell(9);
                cell.setCellValue(item.getWidth());
            }
            {
                Cell cell = row.createCell(10);
                cell.setCellValue(item.getHeight());
            }
            {
                Cell cell = row.createCell(11);
                cell.setCellValue(item.getWeight());
            }
            {
                Cell cell = row.createCell(12);
                cell.setCellValue(LocaleUtil.getName(item.getRarity()));
            }
            {
                Cell cell = row.createCell(13);
                cell.setCellValue(item.getSpawnChance());
            }
            if (item.isQuestItem()) {
                {
                    Cell cell = row.createCell(14);
                    if (item.getRequiredQuests() != null && !item.getRequiredQuests().equals("")) {
                        item.setRequiredQuests(item.getRequiredQuests().substring(0,item.getRequiredQuests().length() - 1));
                    }
                    cell.setCellValue(item.getRequiredQuests());
                }
                {
                    Cell cell = row.createCell(15);
                    cell.setCellValue(item.getQuestNum());
                }
            }
            if (item.isRewardItem()) {
                {
                    Cell cell = row.createCell(16);
                    if (item.getRewardQuests() != null && !item.getRewardQuests().equals("")) {
                        item.setRewardQuests(item.getRewardQuests().substring(0,item.getRewardQuests().length() - 1));
                    }
                    cell.setCellValue(item.getRewardQuests());
                }
                {
                    Cell cell = row.createCell(17);
                    cell.setCellValue(item.getRewardNum());
                }
            }
            if (item.isHideOutItem()) {
                {
                    Cell cell = row.createCell(18);
                    if (item.getLevelUps() != null && !item.getLevelUps().equals("")) {
                        item.setLevelUps(item.getLevelUps().substring(0,item.getLevelUps().length() - 1));
                    }
                    cell.setCellValue(item.getLevelUps());
                }
                {
                    Cell cell = row.createCell(19);
                    cell.setCellValue(item.getHideOutNum());
                }
            }
            int totalNeed = item.getHideOutNum() + item.getQuestNum();
            if (totalNeed > 0) {
                Cell cell = row.createCell(20);
                cell.setCellValue(totalNeed);
            }
        }
    }
}
