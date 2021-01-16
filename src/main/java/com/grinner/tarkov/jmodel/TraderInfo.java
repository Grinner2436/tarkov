package com.grinner.tarkov.jmodel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
//翻译表，对应trader的项目对象
@Data
public class TraderInfo {

    @JSONField(name = "Description")
    private String description;

    @JSONField(name = "FirstName")
    private String firstName;

    @JSONField(name = "FullName")
    private String fullName;

    @JSONField(name = "Nickname")
    private String nickname;

    @JSONField(name = "Location")
    private String location;
}
