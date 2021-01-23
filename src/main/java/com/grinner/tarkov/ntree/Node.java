package com.grinner.tarkov.ntree;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class Node<T> {
//    private String type;

    private String id;
    private String name;

    private List<Node<T>> children;
    private Node<T> parent;

    private T source;
    public Node(T source) {
        this(source, "未知", "未知");
    }

    public Node(T source, String id, String name) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.children = new ArrayList<>();
    }

    public void addChild(Node<T> child) {
        this.children.add(child);
    }

    public abstract String getParentId();
    public abstract String getSpecialParentId();

}
