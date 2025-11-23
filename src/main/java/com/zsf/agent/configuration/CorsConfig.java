package com.zsf.agent.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        // 1. 配置跨域规则
        CorsConfiguration config = new CorsConfiguration();
        // 允许的前端源（开发环境用具体端口，生产环境替换为真实域名，如"https://xxx.com"）
        config.addAllowedOrigin("http://localhost:5173");
        // 允许跨域请求携带Cookie（如果前端需要传Cookie，必须开启，且allowedOrigin不能用"*"）
        config.setAllowCredentials(true);
        // 允许的请求方法（GET/POST/PUT/DELETE/OPTIONS等，预检请求会校验）
        config.addAllowedMethod("*");
        // 允许的请求头（如Content-Type、Authorization等）
        config.addAllowedHeader("*");
        // 预检请求的缓存时间（单位：秒，减少OPTIONS请求次数）
        config.setMaxAge(3600L);

        // 2. 配置拦截所有路径
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 所有接口都应用跨域规则

        // 3. 返回CorsFilter过滤器
        return new CorsFilter(source);
    }
}