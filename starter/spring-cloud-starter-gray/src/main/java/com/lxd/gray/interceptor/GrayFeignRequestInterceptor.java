package com.lxd.gray.interceptor;

import com.lxd.gray.constant.GrayConstant;
import com.lxd.gray.enums.GrayStatusEnum;
import com.lxd.gray.holder.GrayReleaseContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.Collections;

public class GrayFeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        // 如果灰度标记存在，将灰度标记通过HttpHeader传递下去
        GrayStatusEnum grayStatusEnum = GrayReleaseContextHolder.getGrayTag();
        if (grayStatusEnum != null ) {
            template.header(GrayConstant.GRAY_HEADER, Collections.singleton(grayStatusEnum.getVal()));
        }
    }
}