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
    private List<Node<T>> parents;

    private T source;
    public Node(T source) {
        this(source, "未知", "未知");
    }

    public Node(T source, String id, String name) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.children = new ArrayList<>();
        this.parents = new ArrayList<>();
    }

    public void addChild(Node<T> child) {
        this.children.add(child);
    }
    public void addParent(Node<T> parent) {
        this.parents.add(parent);
    }

    public abstract List<String> getParentIds();
    public abstract List<String> getSpecialParentIds();
//    public abstract List<String> getChildrenIds();
//    public abstract List<String> getSpecialChildrenIds();
}
