package com.grinner.tarkov.db.locales;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
//
@Data
public class TraderLocale {

    @JSONField(name = "FullName")
    private String fullName;

    @JSONField(name = "FirstName")
    private String firstName;

    @JSONField(name = "Nickname")
    private String nickname;

    @JSONField(name = "Location")
    private String location;

    @JSONField(name = "Description")
    private String description;
}
