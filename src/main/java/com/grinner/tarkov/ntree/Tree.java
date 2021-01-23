package com.grinner.tarkov.ntree;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Tree<T> {
    private NodeParser<T> parser;
    @Getter
    @Setter
    private Node<T> root;
    //物品表里所有物品
    private Map<String, T> sourceMap;
    //所有物品的树节点
    @Getter
    private Map<String, Node<T>> nodesMap;

    public Tree(NodeParser<T> parser, Map<String, T> sourceMap) {
         this.nodesMap = new HashMap<>();
         this.sourceMap = sourceMap;
         this.parser = parser;
    }
    public Node<T> addNode(Node<T> currentNode) {
        String id = currentNode.getId();
        if (nodesMap.containsKey(id)) {
            return null;
        }
//        Node<T> currentNode = parser.parseNode(this, parentItem);
//        new Node<T>(id, LocaleUtil.getName(id), null);

        String parentId = currentNode.getParentId();
        Node<T> parentNode = root;
        if (parentId != null && !parentId.equals("")) {
            Node<T> savedParentNode = nodesMap.get(parentId);
            if (savedParentNode == null) {
                T parentItem = sourceMap.get(parentId);
                if (parentItem == null) {
                    System.out.println("父类为空：" + parentId);
                } else {
                    Node<T> newParentNode = parser.parseNode(this, parentItem);
                    parentNode = addNode(newParentNode);
                }
            } else {
                parentNode = savedParentNode;
            }
        } else {
            parentNode = nodesMap.get(currentNode.getSpecialParentId());
        }
        if (parentNode != null) {
            parentNode.addChild(currentNode);
            currentNode.setParent(parentNode);
        }
        nodesMap.put(id, currentNode);
        return currentNode;
    }

    public void addNodes(Collection<T> sourceObjects) {
        if (sourceObjects != null) {
            sourceObjects.forEach(sourceObject -> {
                Node<T> newNode = parser.parseNode(this, sourceObject);
                this.addNode(newNode);
            });
        }
    }
}
