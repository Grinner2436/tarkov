package com.grinner.tarkov.jmodel;

import com.alibaba.fastjson.annotation.JSONField;
import com.grinner.tarkov.tmodel.ItemValue;
import lombok.Data;
//物品详情表items.json，项目对象
@Data
public class Item {

    @JSONField(name = "_parent")
    private String parent;


    @JSONField(name = "_name")
    private String name;

    @JSONField(name = "_proto")
    private String proto;

    @JSONField(name = "_type")
    private String type;

    @JSONField(name = "_id")
    private String id;

    private String category;
//    private String parentCategory;

    @JSONField(name = "_props")
    private ItemValue props;

}
