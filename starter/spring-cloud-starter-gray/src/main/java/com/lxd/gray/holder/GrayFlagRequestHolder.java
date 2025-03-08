package com.lxd.gray.holder;


import com.lxd.gray.enums.GrayStatusEnum;

public class GrayFlagRequestHolder {
    /**
     * 标记是否使用灰度版本
     * 具体描述请查看 {@link GrayStatusEnum}
     */
    private static final ThreadLocal<GrayStatusEnum> grayFlag = new ThreadLocal<>();

    public static void setGrayTag(final GrayStatusEnum tag) {
        grayFlag.set(tag);
    }

    public static GrayStatusEnum getGrayTag() {
        return grayFlag.get();
    }

    public static void remove() {
        grayFlag.remove();
    }

}