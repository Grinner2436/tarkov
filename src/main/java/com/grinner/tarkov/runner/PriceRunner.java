package com.grinner.tarkov.runner;

import com.grinner.tarkov.db.items.Item;
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
import java.util.List;
import java.util.stream.Collectors;

//计算所有商品回购价格
public class PriceRunner {

    private static String rootId = "54009119af1c881c07000029";

    public static void main(String[] args) {
//        NodeParser<Item> rootNodeParser = (tree, item) -> {
//            Node<Item> rootNode = new Node<Item>(null) {
//                @Override
//                public List<String> getParentIds() {
//                    return new ArrayList<>();
//                }
//
//                @Override
//                public List<String> getSpecialParentIds() {
//                    return new ArrayList<>();
//                }
//            };
//            return rootNode;
//        };
//        NodeParser<Item> questNodeParser = (tree, item) -> {
//            Node<Item> node = new Node<Item>(item, item.getId(), LocaleUtil.getName(item.getId())) {
//                @Override
//                public List<String> getParentIds() {
//                    List<String> result =  new ArrayList<>();
//                    Item itemSource = getSource();
//                    String parentId = itemSource.getCategory();
//                    if (parentId != null && !"".equals(parentId)) {
//                        result.add(parentId);
//                    }
//                    //处理父节点
//                    if (result .isEmpty()) {
//                        //没有任务作条件，挂在根节点
//                        result.add(rootId);
//                    }
//                    return result;
//                }
//
//                @Override
//                public List<String> getSpecialParentIds() {
//                    List<String> result =  new ArrayList<>();
//                    return result;
//                }
//
//            };
//            return node;
//        };
//        Map<String, Item> itemsMap = ItemUtil.itemsMap;
//        Tree<Item> tree = new Tree(questNodeParser, itemsMap);
//
//        Item root = new Item();
//        root.setName( "总类");
//        root.setId( "54009119af1c881c07000029");
//        Node<Item> rootNode = rootNodeParser.parseNode(tree, root);
//        tree.setRoot(rootNode);
//        tree.addNodesFromChild(itemsMap.values());
        List<Item> items = new ArrayList<>(ItemUtil.itemsMap.values()).stream()
                .filter(item -> ItemUtil.priceItemsMap.containsKey(item.getId()))
                .collect(Collectors.toList());
        write(items);
    }

    private static void write(List<Item> items) {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet1 = wb.createSheet("物品价格表");
        {
            String[] headers = new String[] {
                    "Id","所属分类","出售目录","名称",
                    "原价","最高售价","最高出价者","每格价值",
                    "总占用空间","宽度","高度","总重量",
                    "稀有程度","生成机会",
                    "任务","任务用量",
                    "奖励","奖励数量",
                    "藏身处","藏身处用量",
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
                    if (item.getRequiredQuests() != null) {
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
                    if (item.getRewardQuests() != null) {
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
                    if (item.getLevelUps() != null) {
                        item.setLevelUps(item.getLevelUps().substring(0,item.getLevelUps().length() - 1));
                    }
                    cell.setCellValue(item.getLevelUps());
                }
                {
                    Cell cell = row.createCell(19);
                    cell.setCellValue(item.getHideOutNum());
                }
            }
        }
        try (OutputStream fileOut = new FileOutputStream("塔科夫商人回购价目表.xlsx")) {
            wb.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
