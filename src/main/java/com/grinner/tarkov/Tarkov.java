package com.grinner.tarkov;

import com.alibaba.fastjson.JSONObject;
import com.grinner.tarkov.tmodel.Node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Tarkov {
    public static final String SERVER_PATH = "D:\\ChenKai\\SPT-AKI Alpha R6\\Server\\packages";
//    private static TreeMap<String, Object> knownProp = new TreeMap<>();
    private static Map<String, List<String>> parentPropMap = new HashMap<>();
    private static Map<String, Node> nodeMap = new HashMap<>();
    private static JSONObject nameMap;
    private static Map<String, JSONObject> itemMap;
    private static Node root = new Node("54009119af1c881c07000029", "总类", "Node");
    static {
        String localeFilePath  = "eft-database\\db\\locales\\global\\ch.json";
        Map<String, JSONObject> nameMaps = JSONObject.parseObject(getFileContent(localeFilePath), Map.class);
        nameMap = nameMaps.get("templates");

        String itemsFilePath  = "eft-database\\db\\templates\\items.json";
        itemMap = JSONObject.parseObject(getFileContent(itemsFilePath), Map.class);

        nodeMap.put("54009119af1c881c07000029", root);
    }


    public static void main(String[] args) {
        try {
            test();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void test() throws IOException {
        Collection<JSONObject> itemWithNode = itemMap.values();
        itemWithNode.forEach(item -> {
//            if (item.getString("_type").equals("Item")) {
//                addItem(item);
//            } else if (item.getString("_type").equals("Node")) {
                addNode(item, item.getString("_type"));
//            }
            //                .filter(item -> item.getString("_type").equals("Item"))
//                .filter(item -> item.getString("_type").equals("Node"))
        });
        System.out.println();
    }

    public static Node addNode(JSONObject nodeItem, String type) {
        String id = nodeItem.getString("_id");
        if (nodeMap.containsKey(id)) {
            return null;
        }

        Node currentNode = new Node(id, getName(id), type);
        String parentId = nodeItem.getString("_parent");
        Node parentNode = root;
        if (parentId != null) {
            Node savedParentNode = nodeMap.get(parentId);
            if (savedParentNode == null) {
                JSONObject parentItem = itemMap.get(parentId);
                if (parentItem != null) {
                    parentNode = addNode(parentItem, parentItem.getString("_type"));
                }
//                parentNode = new Node(parentId, getName(parentId));
//                nodeMap.put(parentId, parentNode);
            } else {
                parentNode = savedParentNode;
            }
//            parentNode.addChild(currentNode);
        }
        parentNode.addChild(currentNode);
        currentNode.setParent(parentNode);

        JSONObject props = nodeItem.getJSONObject("_props");

        nodeMap.put(id, currentNode);
        return currentNode;
    }

    public static void addItem(JSONObject item) {
        String parentId = item.getString("_parent");
        if (parentId == null) {
            parentId = "parent_" + item.getString("_id");
        }

        String parentName = getName(parentId);
        List<String> propNames = parentPropMap.get(parentName);
        if (propNames == null) {
            propNames = new ArrayList<>();
            parentPropMap.put(parentName, propNames);
        }
        JSONObject props = item.getJSONObject("_props");
        for(Map.Entry<String, Object> propEntry : props.entrySet()){
            String propKey = propEntry.getKey();
            Object propValue = propEntry.getValue();
            if(!propNames.contains(propKey)) {
                System.out.println(propKey);
                propNames.add(propKey);
            }
        }
        System.out.println();
    }
    public static String getName(String id) {
        JSONObject nameObject = nameMap.getJSONObject(id);
        if (nameObject == null) {
            String name = "未知";
            JSONObject item = itemMap.get(id);
            if (item != null) {
                name = item.getString("_name");
            }
            return name;
        }
        String nodeName = nameObject.getString("Name");
        return nodeName;
    }
    public static String getFileContent(String path) {
        Path server = Paths.get(SERVER_PATH);
        Path filePath  = server.resolve(path);
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String json = new String(bytes);
        return json;
    }


}
