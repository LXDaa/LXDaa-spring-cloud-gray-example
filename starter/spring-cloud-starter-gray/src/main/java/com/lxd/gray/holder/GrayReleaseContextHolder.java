package com.lxd.gray.holder;

import com.lxd.gray.enums.GrayStatusEnum;
import com.alibaba.ttl.TransmittableThreadLocal;


public class GrayReleaseContextHolder {
    
    private static final TransmittableThreadLocal<GrayStatusEnum> CONTEXT = new TransmittableThreadLocal<>();

    public static void setGrayTag(final GrayStatusEnum tag) {
        CONTEXT.set(tag);
    }

    public static GrayStatusEnum getGrayTag() {
        return CONTEXT.get();
    }

    public static void remove() {
        CONTEXT.remove();
    }
}