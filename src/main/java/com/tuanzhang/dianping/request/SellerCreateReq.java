package com.tuanzhang.dianping.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SellerCreateReq {

    @NotBlank(message = "商户姓名不能为空！")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
