package com.grinner.tarkov.tmodel;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Node {
    private List<Node> children;
    private Node parent;
    private String type;

    private String id;
    private String name;
    public Node() {
        this.children = new ArrayList<>();
        this.id = "未知";
        this.name = "未知";
    }
    public Node(String id, String name, String type) {
        this.children = new ArrayList<>();
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public void addChild(Node child) {
        this.children.add(child);
    }
}
