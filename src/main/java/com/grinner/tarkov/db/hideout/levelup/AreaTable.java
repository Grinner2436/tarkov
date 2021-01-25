package com.grinner.tarkov.db.hideout.levelup;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Map;

@Data
public class AreaTable {
    @JSONField(name = "_id")
    private String id;
    //需要燃料
    private boolean needsFuel;

    private int type;
    //
    private Map<String, StageTable> stages;
}
