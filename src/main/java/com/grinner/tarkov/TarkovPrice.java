package com.grinner.tarkov;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.grinner.tarkov.jmodel.*;
import com.grinner.tarkov.tmodel.ItemValue;
import com.grinner.tarkov.tmodel.Node;
import com.grinner.tarkov.util.FileUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

public class TarkovPrice {
    //物品表里所有物品
    private static Map<String, Item> itemsMap;
    //翻译表里所有翻译
    private static Map<String, String> nameMap = new HashMap<>();
//    private static Map<String, Integer> priceMap = new HashMap<>();
    //当前二级父类别的一级父类别
    private static Map<String, String> secondLevelToFirstLevels = new HashMap<>();
    //一级父类的二级儿子们
    private static Map<String, List<String>> firstLevelToSecondLevels = new HashMap<>();
    //整个翻译表
    private static Local locale;
    //商人表
    private static Map<String, Trader> tradersMap;
    //所有物品的树节点
    private static Map<String, Node> nodesMap = new HashMap<>();

    private static Node root = new Node("54009119af1c881c07000029", "总类", "Node");
    static {
        //先有根节点
        nodesMap.put("54009119af1c881c07000029", root);
        nameMap.put("54009119af1c881c07000029", "总类");
        //结点依赖名字
        String localeFilePath  = "eft-database\\db\\locales\\global\\ch.json";
        locale = JSONObject.parseObject(FileUtil.getFileContent(localeFilePath), Local.class);
        locale.getTemplates().forEach((id, itemInfo) -> {
            nameMap.put(id, itemInfo.getName());
        });
        nameMap.putAll(locale.getHandbook());
        //所有物品
        String itemsFilePath  = "eft-database\\db\\templates\\items.json";
        itemsMap = JSONObject.parseObject(FileUtil.getFileContent(itemsFilePath), new TypeReference<Map<String, Item>>(){});
        itemsMap.forEach((id, item) -> {
            addNode(item, item.getType());
        });
        //所有物品的价格和收购类别
        String handbookFilePath  = "eft-database\\db\\templates\\handbook.json";
        HandBook handBook = JSONObject.parseObject(FileUtil.getFileContent(handbookFilePath), HandBook.class);
        handBook.getItems().forEach(priceItem -> {
            String priceItemId = priceItem.getId();
            Item item = itemsMap.get(priceItemId);
            if (item != null) {
                item.setCategory(priceItem.getCategory());
            }
//            priceMap.put(priceItem.getId(),priceItem.getPrice());
        });
        handBook.getCategories().forEach(priceItem -> {
            String firstLevelParent= priceItem.getCategory();
            if (firstLevelParent == null || "".equals(firstLevelParent)) {
                return;
            }
            List<String> secondLevels = firstLevelToSecondLevels.get(firstLevelParent);
            if (secondLevels == null) {
                secondLevels = new ArrayList<>();
                firstLevelToSecondLevels.put(firstLevelParent, secondLevels);
            }
            secondLevelToFirstLevels.put(priceItem.getId(), firstLevelParent);
            secondLevels.add(priceItem.getId());
        });
        nameMap.put("54cb50c76803fa8b248b4571", "俄商Prapor");
        nameMap.put("54cb57776803fa99248b456e", "医生Therapist");
        nameMap.put("579dc571d53a0658a154fbec", "黑商");
        nameMap.put("58330581ace78e27b8b10cee", "配件商人Skier");
        nameMap.put("5935c25fb3acc3127c3d8cd9", "美商Peacekeeper");
        nameMap.put("5a7c2eca46aef81a7ca2145d", "枪商Mechanic");
        nameMap.put("5ac3b934156ae10c4430e83c", "服装商Ragman");
        nameMap.put("5c0647fdd443bc2504c2d371", "猎人Jaeger");
        String tradersFilePath  = "eft-database\\db\\traders";
        tradersMap = FileUtil.getTradersMap(tradersFilePath);
        tradersMap.forEach((id, trader) -> {
            List<String> result = new ArrayList<>();
            List<String> sellCategory  = trader.getSellCategory();
            if (sellCategory != null) {
                sellCategory.forEach(traderAcceptedLevel -> {
                    result.add(traderAcceptedLevel);
                    List<String> secondLevels = firstLevelToSecondLevels.get(traderAcceptedLevel);
                    if (secondLevels != null) {
                        result.addAll(secondLevels);
                    }
                });
            }
            trader.setSellCategory(result);
        });

    }

    public static void main(String[] args) {
        test();
    }
    public static void test() {
        List<ItemValue> itemValues = new ArrayList<>();
        itemsMap.forEach((id, item) -> {
            if (item.getType().equals("Node")) {
                return;
            }
            ItemValue itemValue = item.getProps();
            if (itemValue.isQuestItem()) {
                return;
            }
            Trader maxPriceTrader = getMaxPriceTrader(item);
//            if(maxPriceTrader == null) {
//                System.out.println("没人买:" + nameMap.get(id));
//                return;
//            }

            itemValue.setId(item.getId());
            itemValue.setName(nameMap.get(id));
            itemValue.setTotalCell(itemValue.getHeight() * itemValue.getWidth());

            float maxPrice = itemValue.getCreditsPrice() * (1F - (maxPriceTrader.getDiscount() / 100F));
            itemValue.setMaxPrice(maxPrice);
            itemValue.setMaxPriceTrader(nameMap.get(maxPriceTrader.getId()));
            itemValue.setCellPrice(itemValue.getCreditsPrice() / itemValue.getTotalCell());
            itemValues.add(itemValue);
        });
//        write(itemValues);
    }

    private static Trader getMaxPriceTrader(Item item) {
        List<Trader> traders = new ArrayList<>();
        tradersMap.forEach((traderId, trader) -> {
            List<String> category = trader.getSellCategory();
            if (category.contains(item.getCategory())) {
                traders.add(trader);
                return;
            }
        });
//
//        if (traders.size() == 0 && ) {
//
//        }
        if (traders.size() > 0) {
            traders.sort(Comparator.comparingDouble(Trader::getDiscount));
            return traders.get(0);
        }
        return null;
    }

    public static Node addNode(Item nodeItem, String type) {
        String id = nodeItem.getId();
        if (nodesMap.containsKey(id)) {
            return null;
        }

        Node currentNode = new Node(id, nameMap.get(id), type);
        String parentId = nodeItem.getParent();
        Node parentNode = root;
        if (parentId != null) {
            Node savedParentNode = nodesMap.get(parentId);
            if (savedParentNode == null) {
                Item parentItem = itemsMap.get(parentId);
                if (parentItem == null) {
                    System.out.println("父类为空：" + parentId);
                } else {
                    parentNode = addNode(parentItem,parentItem.getType());
                }
            } else {
                parentNode = savedParentNode;
            }
        }
        parentNode.addChild(currentNode);
        currentNode.setParent(parentNode);
        nodesMap.put(id, currentNode);
        return currentNode;
    }

    private static void write(List<ItemValue> itemValues) {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet1 = wb.createSheet("物品价格表");
        {
            Row row = sheet1.createRow(0);
            {
                Cell cell = row.createCell(0);
                cell.setCellValue("Id");
            }
            {
                Cell cell = row.createCell(1);
                cell.setCellValue("名称");
            }
            {
                Cell cell = row.createCell(2);
                cell.setCellValue("原价");
            }
            {
                Cell cell = row.createCell(3);
                cell.setCellValue("最高售价");
            }

            {
                Cell cell = row.createCell(4);
                cell.setCellValue("最高出价者");
            }
            {
                Cell cell = row.createCell(5);
                cell.setCellValue("总占用空间");
            }
            {
                Cell cell = row.createCell(6);
                cell.setCellValue("每格价值");
            }
            {
                Cell cell = row.createCell(7);
                cell.setCellValue("宽度");
            }
            {
                Cell cell = row.createCell(8);
                cell.setCellValue("高度");
            }
            {
                Cell cell = row.createCell(9);
                cell.setCellValue("生成机会");
            }
            {
                Cell cell = row.createCell(10);
                cell.setCellValue("总重量");
            }
            {
                Cell cell = row.createCell(11);
                cell.setCellValue("描述");
            }
        }
        for (int i = 1; i <= itemValues.size(); i++) {
            Row row = sheet1.createRow(i);
            ItemValue itemValue = itemValues.get(i - 1);
            {
                Cell cell = row.createCell(0);
                cell.setCellValue(itemValue.getId());
            }
            {
                Cell cell = row.createCell(1);
                cell.setCellValue(itemValue.getName());
            }
            {
                Cell cell = row.createCell(2);
                cell.setCellValue(itemValue.getCreditsPrice());
            }
            {
                Cell cell = row.createCell(3);
                cell.setCellValue(itemValue.getMaxPrice());
            }

            {
                Cell cell = row.createCell(4);
                cell.setCellValue(itemValue.getMaxPriceTrader());
            }
            {
                Cell cell = row.createCell(5);
                cell.setCellValue(itemValue.getTotalCell());
            }
            {
                Cell cell = row.createCell(6);
                cell.setCellValue(itemValue.getCellPrice());
            }
            {
                Cell cell = row.createCell(7);
                cell.setCellValue(itemValue.getWidth());
            }
            {
                Cell cell = row.createCell(8);
                cell.setCellValue(itemValue.getHeight());
            }
            {
                Cell cell = row.createCell(9);
                cell.setCellValue(itemValue.getSpawnChance());
            }
            {
                Cell cell = row.createCell(10);
                cell.setCellValue(itemValue.getWeight());
            }
            {
                Cell cell = row.createCell(11);
                cell.setCellValue(itemValue.getDescription());
            }
        }
        try (OutputStream fileOut = new FileOutputStream("塔科夫商人回购价目表.xlsx")) {
            wb.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
