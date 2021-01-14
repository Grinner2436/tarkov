package com.grinner.tarkov.jmodel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

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
