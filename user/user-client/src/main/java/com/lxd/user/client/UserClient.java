package com.lxd.user.client;

import com.lxd.common.api.ApiResult;
import com.lxd.common.constants.ServiceNames;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(value = ServiceNames.USER_APP,contextId = "userClient")
public interface UserClient {
    @GetMapping("/user/{userNo}")
    ApiResult getUserName(@PathVariable("userNo") String userNo);
}
