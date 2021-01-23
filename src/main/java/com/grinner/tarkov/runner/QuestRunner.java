package com.grinner.tarkov.runner;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.grinner.tarkov.db.templates.quests.Quest;
import com.grinner.tarkov.db.templates.quests.QuestConditionTable;
import com.grinner.tarkov.db.templates.quests.conditions.Condition;
import com.grinner.tarkov.db.templates.quests.conditions.QuestCondition;
import com.grinner.tarkov.ntree.NodeParser;
import com.grinner.tarkov.ntree.Tree;
import com.grinner.tarkov.ntree.Node;
import com.grinner.tarkov.util.FileUtil;
import com.grinner.tarkov.util.TreeUtils;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class QuestRunner {
    public static void main(String[] args) {
        try {
            //基本设施
            String questsFilePath  = "eft-database\\db\\templates\\quests.json";
            Map<String, Quest> questMap = JSONObject.parseObject(FileUtil.getFileContent(questsFilePath), new TypeReference<Map<String, Quest>>(){});

            NodeParser<Quest> rootNodeParser = (tree, quest) -> {
                Node<Quest> rootNode = new Node<Quest>(null) {
                    @Override
                    public String getParentId() {
                        return "根节点无父任务ID";
                    }

                    @Override
                    public String getSpecialParentId() {
                        return "根节点无父任务ID";
                    }
                };
                return rootNode;
            };
            NodeParser<Quest> questNodeParser = (tree, quest) -> {
                Node<Quest> node = new Node<Quest>(quest, quest.getId(), quest.getName()) {
                    @Override
                    public String getParentId() {
                        String parentId = "";
                        Quest quest = getSource();
                        QuestConditionTable questConditionTable = quest.getConditions();
                        if (questConditionTable == null) {
                            return parentId;
                        }
                        List<Condition> conditions = quest.getConditions().getStartConditions();
                        for (Condition condition : conditions) {
                            if (!(condition instanceof QuestCondition)) {
                                continue;
                            }
                            QuestCondition childQuestCondition = (QuestCondition) condition;
                            parentId = childQuestCondition.getTarget();
                            Node<Quest> parentNode = tree.getNodesMap().get(parentId);
                            if (parentNode == null) {
                                break;
                            }
                            String parentTraderId = parentNode.getSource().getTraderId();
                            if (!parentTraderId.equals(quest.getTraderId())) {
                                parentId = quest.getTraderId();
                            }
                        }
                        if (parentId == null || parentId.equals("")) {
                            parentId = quest.getTraderId();
                        }
                        return parentId;
                    }

                    @Override
                    public String getSpecialParentId() {
                        return getSource().getTraderId();
                    }
                };
                return node;
            };

            Tree<Quest> tree = new Tree(questNodeParser, questMap);

            Quest rootQuest = new Quest("root无任务ID", "任务根", "root无商人ID");
            Node<Quest> rootNode = rootNodeParser.parseNode(tree, rootQuest);
            tree.setRoot(rootNode);

            {
                Quest traderQuest = new Quest("54cb50c76803fa8b248b4571", "俄商任务", "");
                Node<Quest> traderNode = questNodeParser.parseNode(tree, traderQuest);
                traderNode.setParent(rootNode);
                rootNode.getChildren().add(traderNode);
                tree.addNode(traderNode);
            }

            {
                Quest traderQuest = new Quest("54cb57776803fa99248b456e", "医生任务", "");
                Node<Quest> traderNode = questNodeParser.parseNode(tree, traderQuest);
                traderNode.setParent(rootNode);
                rootNode.getChildren().add(traderNode);
                rootNode.getChildren().add(traderNode);
                rootNode.getChildren().add(traderNode);
                tree.addNode(traderNode);
            }


            {
                Quest traderQuest = new Quest("579dc571d53a0658a154fbec", "黑商任务", "");
                Node<Quest> traderNode = questNodeParser.parseNode(tree, traderQuest);
                traderNode.setParent(rootNode);
                rootNode.getChildren().add(traderNode);
                tree.addNode(traderNode);
            }


            {
                Quest traderQuest = new Quest("58330581ace78e27b8b10cee", "配件商任务", "");
                Node<Quest> traderNode = questNodeParser.parseNode(tree, traderQuest);
                traderNode.setParent(rootNode);
                rootNode.getChildren().add(traderNode);
                tree.addNode(traderNode);
            }


            {
                Quest traderQuest = new Quest("5935c25fb3acc3127c3d8cd9", "美商任务", "");
                Node<Quest> traderNode = questNodeParser.parseNode(tree, traderQuest);
                traderNode.setParent(rootNode);
                rootNode.getChildren().add(traderNode);
                tree.addNode(traderNode);
            }


            {
                Quest traderQuest = new Quest("5a7c2eca46aef81a7ca2145d", "枪商任务", "");
                Node<Quest> traderNode = questNodeParser.parseNode(tree, traderQuest);
                traderNode.setParent(rootNode);
                rootNode.getChildren().add(traderNode);
                tree.addNode(traderNode);
            }


            {
                Quest traderQuest = new Quest("5ac3b934156ae10c4430e83c", "服装任务", "");
                Node<Quest> traderNode = questNodeParser.parseNode(tree, traderQuest);
                traderNode.setParent(rootNode);
                rootNode.getChildren().add(traderNode);
                tree.addNode(traderNode);
            }


            {
                Quest traderQuest = new Quest("5c0647fdd443bc2504c2d371", "猎人任务", "");
                Node<Quest> traderNode = questNodeParser.parseNode(tree, traderQuest);
                traderNode.setParent(rootNode);
                rootNode.getChildren().add(traderNode);
                tree.addNode(traderNode);
            }

            tree.addNodes(questMap.values());
            TreeUtils.treeToGraph(rootNode, Paths.get("").toString());
            System.out.println();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
