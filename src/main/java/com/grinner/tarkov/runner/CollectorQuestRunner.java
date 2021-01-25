package com.grinner.tarkov.runner;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.grinner.tarkov.db.templates.quests.Quest;
import com.grinner.tarkov.db.templates.quests.QuestConditionTable;
import com.grinner.tarkov.db.templates.quests.conditions.Condition;
import com.grinner.tarkov.db.templates.quests.conditions.QuestCondition;
import com.grinner.tarkov.ntree.Node;
import com.grinner.tarkov.ntree.NodeParser;
import com.grinner.tarkov.ntree.Tree;
import com.grinner.tarkov.util.FileUtil;
import com.grinner.tarkov.util.LocaleUtil;
import com.grinner.tarkov.util.QuestUtil;
import com.grinner.tarkov.util.TreeUtils;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CollectorQuestRunner {
    public static void main(String[] args) {
        try {

            NodeParser<Quest> rootNodeParser = (tree, quest) -> {
                Node<Quest> rootNode = new Node<Quest>(null) {
                    @Override
                    public List<String> getParentIds() {
                        return new ArrayList<>();
                    }

                    @Override
                    public List<String> getSpecialParentIds() {
                        return new ArrayList<>();
                    }
                };
                return rootNode;
            };
            NodeParser<Quest> questNodeParser = (tree, quest) -> {
                Node<Quest> node = new Node<Quest>(quest, quest.getId(), LocaleUtil.getName(quest.getId())) {
                    @Override
                    public List<String> getParentIds() {
                        List<String> result =  new ArrayList<>();
                        Quest quest = getSource();
                        QuestConditionTable questConditionTable = quest.getConditions();
                        if (questConditionTable == null) {
                            return result;
                        }
                        List<Condition> conditions = quest.getConditions().getStartConditions();
                        String questTraderName = LocaleUtil.getName(quest.getTraderId());
                        //条件变Quest
                        List<String> parentQuestIds = conditions.stream().map(condition -> {
                            if (!(condition instanceof QuestCondition)) {
                                return null;
                            }
                            QuestCondition childQuestCondition = (QuestCondition) condition;
                            String questId = childQuestCondition.getTarget();
                            Quest parentQuest = tree.getSourceMap().get(questId);
                            return parentQuest;
                        //条件变Id
                        }).filter(mappedQuest -> mappedQuest != null).map(mappedQuest -> {
                            if (!mappedQuest.getTraderId().equals(quest.getTraderId())) {
                                String sourceQuest  = LocaleUtil.getName(mappedQuest.getId());
                                String sourceTrader = LocaleUtil.getName(mappedQuest.getTraderId());

                                StringBuffer message = new StringBuffer();
                                message.append(quest.getId()).append("(").append(LocaleUtil.getName(quest.getId())).append(")-->")
                                        .append(mappedQuest.getId()).append("(").append(LocaleUtil.getName(mappedQuest.getId())).append(")");
//                                message.append(questTraderName).append("[").append(LocaleUtil.getName(quest.getId()))
//                                        .append("]前置任务：")
//                                        .append(sourceTrader).append("[").append(sourceQuest).append("]");
                                System.out.println(message);
//                                String newName = "%【@" + sourceTrader + "（" + sourceQuest + "）】" + this.getName() + "%";
//                                quest.setName(newName);
//                                this.setName(newName);
                                return quest.getTraderId();
                            }
                            return mappedQuest.getId();
                        }).filter(questId -> questId != null && !questId.equals("")).collect(Collectors.toList());
                        //处理父节点
                        if (parentQuestIds .isEmpty()) {
                            //没有任务作条件，挂在商人下面
                            result.add(quest.getTraderId());
                        } else {
                            result = parentQuestIds;
                        }
                        return result;
                    }

                    @Override
                    public List<String> getSpecialParentIds() {
                        Quest quest = getSource();
                        List<String> result =  new ArrayList<>();
                        result.add(quest.getTraderId());
                        return result;
                    }

                };
                return node;
            };
            Map<String, Quest> questMap = QuestUtil.questMap;
            Tree<Quest> tree = new Tree(questNodeParser, questMap);

            Quest rootQuest = new Quest("root无任务ID", "任务根", "root无商人ID");
            Node<Quest> rootNode = rootNodeParser.parseNode(tree, rootQuest);
            tree.setRoot(rootNode);

            {
                Quest traderQuest = new Quest("54cb50c76803fa8b248b4571", "俄商任务", "");
                Node<Quest> traderNode = questNodeParser.parseNode(tree, traderQuest);
                traderNode.getParents().add(rootNode);
                rootNode.getChildren().add(traderNode);
                tree.addNodeFromChild(traderNode);
            }

            {
                Quest traderQuest = new Quest("54cb57776803fa99248b456e", "医生任务", "");
                Node<Quest> traderNode = questNodeParser.parseNode(tree, traderQuest);
                traderNode.getParents().add(rootNode);
                rootNode.getChildren().add(traderNode);
                rootNode.getChildren().add(traderNode);
                rootNode.getChildren().add(traderNode);
                tree.addNodeFromChild(traderNode);
            }


            {
                Quest traderQuest = new Quest("579dc571d53a0658a154fbec", "黑商任务", "");
                Node<Quest> traderNode = questNodeParser.parseNode(tree, traderQuest);
                traderNode.getParents().add(rootNode);
                rootNode.getChildren().add(traderNode);
                tree.addNodeFromChild(traderNode);
            }


            {
                Quest traderQuest = new Quest("58330581ace78e27b8b10cee", "配件商任务", "");
                Node<Quest> traderNode = questNodeParser.parseNode(tree, traderQuest);
                traderNode.getParents().add(rootNode);
                rootNode.getChildren().add(traderNode);
                tree.addNodeFromChild(traderNode);
            }


            {
                Quest traderQuest = new Quest("5935c25fb3acc3127c3d8cd9", "美商任务", "");
                Node<Quest> traderNode = questNodeParser.parseNode(tree, traderQuest);
                traderNode.getParents().add(rootNode);
                rootNode.getChildren().add(traderNode);
                tree.addNodeFromChild(traderNode);
            }


            {
                Quest traderQuest = new Quest("5a7c2eca46aef81a7ca2145d", "枪商任务", "");
                Node<Quest> traderNode = questNodeParser.parseNode(tree, traderQuest);
                traderNode.getParents().add(rootNode);
                rootNode.getChildren().add(traderNode);
                tree.addNodeFromChild(traderNode);
            }


            {
                Quest traderQuest = new Quest("5ac3b934156ae10c4430e83c", "服装任务", "");
                Node<Quest> traderNode = questNodeParser.parseNode(tree, traderQuest);
                traderNode.getParents().add(rootNode);
                rootNode.getChildren().add(traderNode);
                tree.addNodeFromChild(traderNode);
            }


            {
                Quest traderQuest = new Quest("5c0647fdd443bc2504c2d371", "猎人任务", "");
                Node<Quest> traderNode = questNodeParser.parseNode(tree, traderQuest);
                traderNode.getParents().add(rootNode);
                rootNode.getChildren().add(traderNode);
                tree.addNodeFromChild(traderNode);
            }

            tree.addNodesFromChild(questMap.values());
            TreeUtils.treeToGraph(rootNode, Paths.get("").toString());
            System.out.println();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
