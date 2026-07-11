package com.hmall.gateway.filter;

import cn.hutool.core.text.AntPathMatcher;
import com.hmall.common.exception.UnauthorizedException;
import com.hmall.gateway.config.AuthProperties;
import com.hmall.gateway.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Author: ${董靖涛}
 * Date: 2026/7/11
 */
@Component
@RequiredArgsConstructor
public class AuthGlobalFilter  implements GlobalFilter, Ordered {
    private final AuthProperties authProperties;
    private final JwtTool jwtTool;
    //用来处理放行路径的匹配
    private final AntPathMatcher antPathMatcher=new AntPathMatcher();
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取request
        ServerHttpRequest request=exchange.getRequest();
        //2.判断是否拦截
        if(isExclude(request.getURI().getPath())){
            return chain.filter(exchange);
        }
        //3.获取token
        String token=null;
        List<String> headers = request.getHeaders().get("Authorization");
        if(headers!=null && headers.size()>0){
            token=headers.get(0);
        }
        //4.校验并解析token
        Long userId=null;
        try {
            userId=jwtTool.parseToken(token);
        }catch (UnauthorizedException e){
            //设置响应状态码为401
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        //5.传递用户信息
        System.out.println("用户id:"+userId);
        //6.放行
        return chain.filter(exchange);
    }

    private boolean isExclude(String path) {
        for(String pattern:authProperties.getExcludePaths()){
            if(antPathMatcher.match(pattern,path)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
