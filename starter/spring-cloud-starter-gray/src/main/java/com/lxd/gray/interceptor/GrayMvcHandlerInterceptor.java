package com.lxd.gray.interceptor;

import com.lxd.gray.constant.GrayConstant;
import com.lxd.gray.enums.GrayStatusEnum;
import com.lxd.gray.holder.GrayReleaseContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("all")
public class GrayMvcHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String grayTag = request.getHeader(GrayConstant.GRAY_HEADER);
        // 如果HttpHeader中灰度标记存在，则将灰度标记放到holder中，如果需要就传递下去
        System.out.println(">>>>>> " + request.getRequestURI() + " ==> GrayMvcHandlerInterceptor#preHandle()");
        if (grayTag != null) {
            GrayReleaseContextHolder.setGrayTag(GrayStatusEnum.getByVal(grayTag));
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println(">>>>>> " + request.getRequestURI() + " ==> GrayMvcHandlerInterceptor#afterCompletion()");
        GrayReleaseContextHolder.remove();
    }
}