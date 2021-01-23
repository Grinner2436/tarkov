package com.grinner.tarkov.ntree;

/**
 *
 * @param <T>
 */
@FunctionalInterface
public interface NodeParser<T> {
    Node parseNode(Tree<T> tree, T source);
}
