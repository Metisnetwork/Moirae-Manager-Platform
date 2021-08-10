package com.platon.rosettaflow.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.platon.rosettaflow.common.constants.SysConstant;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @date 2021/7/20
 */

@Configuration
@EnableOpenApi
@EnableKnife4j
@Profile({"local", "dev", "hdn"})
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {
    static final String TOKEN_KEY_DESC = "User's Access-Token";

    @Bean
    public Docket docketCreateRestApi() {
        RequestParameterBuilder tokenPar = new RequestParameterBuilder();
        List<RequestParameter> pars = new ArrayList<>();
        tokenPar.name(SysConstant.HEADER_TOKEN_KEY).description(TOKEN_KEY_DESC).in(ParameterType.HEADER)
                .required(false).query(q -> q.model(m -> m.scalarModel(ScalarType.STRING))).build();
        pars.add(tokenPar.build());

        return new Docket(DocumentationType.OAS_30).globalRequestParameters(pars).apiInfo(apiInfoApiInfo()).select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();

    }

    private ApiInfo apiInfoApiInfo() {

        return new ApiInfoBuilder()
                .title("RosettaFlow管理后台接口文档")
                .description("本文档主要提供RosettaFlow对外开放的接口功能，用于描述清楚调用方需要传递的参数信息及响应的数据。")
                .contact(new Contact("platon", "www.platon.com", "platon@platon.com"))
                .version("1.0.0")
                .build();
    }
}
