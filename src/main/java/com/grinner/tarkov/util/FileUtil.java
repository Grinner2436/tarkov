package com.grinner.tarkov.util;

import com.alibaba.fastjson.JSONObject;
import com.grinner.tarkov.jmodel.Trader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileUtil {
    public static final String SERVER_PATH = "D:\\ChenKai\\SPT-AKI Alpha R6\\Server\\packages";
    public static Map<String, Trader> getTradersMap(String path) {
        Map<String, Trader> tradersMap = new HashMap<>();
        Path dirPath = Paths.get(SERVER_PATH, path);
        File dir = dirPath.toFile();
        for (File file : dir.listFiles()) {
            String id = file.getName();
            if (id.equals("ragfair")) {
                continue;
            }
            Path baseFilePath = file.toPath().resolve("base.json");
            String fileContent =  getFileContent(baseFilePath);
            Trader trader = JSONObject.parseObject(fileContent, Trader.class);
            tradersMap.put(id, trader);
        }
        return tradersMap;
    }
    public static String getFileContent(Path filePath) {
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String json = new String(bytes);
        return json;
    }
    public static String getFileContent(String path) {
        Path server = Paths.get(SERVER_PATH);
        Path filePath  = server.resolve(path);
        return getFileContent(filePath);
    }


}
