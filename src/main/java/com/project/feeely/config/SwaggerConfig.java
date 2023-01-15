package com.project.feeely.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Value("Authorization")
    private String SPRING_JWT_HEADER;

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


    private ApiKey apiKey() {
        return new ApiKey("JWT", SPRING_JWT_HEADER, "header");
    }

    @Bean
    public Docket loginSwagger() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("test API")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.project.feeely.controller.member.authorization"))
                .paths(PathSelectors.ant("/auth/**")) // requestMapping
                .build()
                .securitySchemes(List.of(apiKey()))
                .securityContexts(Collections.singletonList(userSecurityContext()))
                .useDefaultResponseMessages(false);
    }

    private SecurityContext userSecurityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.ant("/auth/**"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = {authorizationScope};

        return List.of(new SecurityReference("JWT", authorizationScopes));
    }



}
