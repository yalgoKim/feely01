package com.project.feeely.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("test REST API")
                .description("test REST API Document")
                .version("1.0")
                .build();
    }

    @Bean
    public Docket saveSwagger() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("save API")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.project.feeely.controller.member"))
                .paths(PathSelectors.ant("/member/**"))
                .build()
                .useDefaultResponseMessages(false);
    }
}
