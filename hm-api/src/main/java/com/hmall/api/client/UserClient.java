package com.hmall.api.client;


import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Author: ${董靖涛}
 * Date: 2026/6/30
 */
@FeignClient("user-service")
public interface UserClient {
    @ApiOperation("扣减余额")
    @PutMapping("/users/money/deduct")
    void deductMoney(@RequestParam("pw") String pw, @RequestParam("amount") Integer amount);

}

