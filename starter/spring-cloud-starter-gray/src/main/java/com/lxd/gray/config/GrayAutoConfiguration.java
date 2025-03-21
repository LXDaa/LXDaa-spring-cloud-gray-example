package com.lxd.gray.config;

import com.lxd.gray.filter.GrayGatewayAfterFilter;
import com.lxd.gray.filter.GrayGatewayBeginFilter;
import com.lxd.gray.handler.GrayGatewayExceptionHandler;
import com.lxd.gray.interceptor.GrayFeignRequestInterceptor;
import com.lxd.gray.interceptor.GrayMvcHandlerInterceptor;
import com.lxd.gray.properties.GrayGatewayProperties;
import com.lxd.gray.properties.GrayVersionProperties;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 定义配置类，使用@Configuration注解
@Configuration
// 可以通过@ConditionalOnProperty设置是否开启灰度自动配置 默认是不加载的
@ConditionalOnProperty(value = "lxd.tool.gray.load", havingValue = "true")
// 启用GrayVersionProperties配置属性
@EnableConfigurationProperties(GrayVersionProperties.class)
public class GrayAutoConfiguration {

    // 内部配置类，用于配置GrayGateway相关的过滤器和异常处理器
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(value = GlobalFilter.class)
    @EnableConfigurationProperties(GrayGatewayProperties.class)
    static class GrayGatewayFilterAutoConfiguration {
        // 创建GrayGatewayBeginFilter实例
        @Bean
        public GrayGatewayBeginFilter grayGatewayBeginFilter() {
            return new GrayGatewayBeginFilter();
        }

        // 创建GrayGatewayAfterFilter实例
        @Bean
        public GrayGatewayAfterFilter grayGatewayAfterFilter() {
            return new GrayGatewayAfterFilter();
        }

        // 创建GrayGatewayExceptionHandler实例
        @Bean
        public GrayGatewayExceptionHandler grayGatewayExceptionHandler() {
            return new GrayGatewayExceptionHandler();
        }
    }

    // 内部配置类，用于配置Spring MVC拦截器
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(value = WebMvcConfigurer.class)
    static class GrayWebMvcAutoConfiguration {
        /**
         * Spring MVC 请求拦截器
         * @return WebMvcConfigurer
         */
        @Bean
        public WebMvcConfigurer webMvcConfigurer() {
            return new WebMvcConfigurer() {
                // 重写addInterceptors方法，添加GrayMvcHandlerInterceptor拦截器
                @Override
                public void addInterceptors(InterceptorRegistry registry) {
                    registry.addInterceptor(new GrayMvcHandlerInterceptor());
                }
            };
        }
    }

    // 内部配置类，用于配置Feign拦截器
    @Configuration
    @ConditionalOnClass(value = RequestInterceptor.class)
    static class GrayFeignInterceptorAutoConfiguration {
        /**
         * Feign拦截器
         * @return GrayFeignRequestInterceptor
         */
        @Bean
        public GrayFeignRequestInterceptor grayFeignRequestInterceptor() {
            return new GrayFeignRequestInterceptor();
        }
    }
}