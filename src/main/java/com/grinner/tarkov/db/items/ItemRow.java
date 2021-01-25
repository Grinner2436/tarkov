package com.grinner.tarkov.db.items;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
//物品详情表items.json，项目对象
@Data
public class ItemRow {

    @JSONField(name = "_id")
    private String id;

    @JSONField(name = "_name")
    private String ename;

    @JSONField(name = "_parent")
    private String parentId;

    @JSONField(name = "_type")
    private String type;

    @JSONField(name = "_props")
    private Item props;

    @JSONField(name = "_proto")
    private String protoId;
}
