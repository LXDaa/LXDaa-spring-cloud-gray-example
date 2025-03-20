package com.lxd.gray.enums;


import lombok.Getter;

public enum GrayStatusEnum {
    ALL("ALL","可以调用全部版本的服务"),
    PROD("PROD","只能调用生产版本的服务"),
    GRAY("GRAY","只能调用灰度版本的服务");
    GrayStatusEnum(String val, String desc) {
        this.val = val;
        this.desc = desc;
    }
    @Getter
    private final String val;
    private final String desc;

    public static GrayStatusEnum getByVal(String val){

        for (GrayStatusEnum value : values()) {
            if(value.val.equals(val)){
                return value;
            }
        }
        return null;
    }
}
