package com.grinner.tarkov.ntree;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tree<T> {
    private NodeParser<T> parser;
    @Getter
    @Setter
    private Node<T> root;
    //物品表里所有物品
    @Getter
    private Map<String, T> sourceMap;
    //所有物品的树节点
    @Getter
    private Map<String, Node<T>> nodesMap;

    public Tree(NodeParser<T> parser, Map<String, T> sourceMap) {
         this.nodesMap = new HashMap<>();
         this.sourceMap = sourceMap;
         this.parser = parser;
    }

    public Node<T> addNodeFromChild(Node<T> currentNode) {
        String id = currentNode.getId();
        if (nodesMap.containsKey(id)) {
            return null;
        }

        //父节点列表
        List<String> parentIds = currentNode.getParentIds();
        if (parentIds == null || parentIds.isEmpty()) {
            parentIds = currentNode.getSpecialParentIds();
        }

        //挂在根节点
        if (parentIds == null || parentIds.isEmpty()) {
            Node<T> parentNode = root;
            parentNode.addChild(currentNode);
            currentNode.addParent(parentNode);
        }

        //挂在父节点
        for (String parentId :parentIds) {
            Node<T> parentNode = nodesMap.get(parentId);
            if (parentNode == null) {
                T parentItem = sourceMap.get(parentId);
                if (parentItem == null) {
                    System.out.println("source Parent不存在：" + parentId);
                } else {
                    Node<T> newParentNode = parser.parseNode(this, parentItem);
                    parentNode = addNodeFromChild(newParentNode);
                }
            }
            if (parentNode != null) {
                parentNode.addChild(currentNode);
                currentNode.addParent(parentNode);
            }
        }
        //保存当前节点
        nodesMap.put(id, currentNode);
        return currentNode;
    }

    public void addNodesFromChild(Collection<T> sourceObjects) {
        if (sourceObjects != null) {
            sourceObjects.forEach(sourceObject -> {
                Node<T> newNode = parser.parseNode(this, sourceObject);
                this.addNodeFromChild(newNode);
            });
        }
    }

    public void addNodeFromParent(Node<T> currentNode) {
        String id = currentNode.getId();
        if (nodesMap.containsKey(id)) {
            return;
        }
    }
}
