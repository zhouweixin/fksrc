package com.hnu.fk.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 15:08 2018/8/1
 * @Modified By:
 */
@Configuration
@EnableSwagger2
public class SwaggerUtil {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hnu.fk.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo("凡口信息化系统",
                "接口文件生成页面",
                "1.0",
                "",
                "2712220318@qq.com",
                "&copy长沙矿冶研究院版权所有",
                "");
        return apiInfo;
    }
}
