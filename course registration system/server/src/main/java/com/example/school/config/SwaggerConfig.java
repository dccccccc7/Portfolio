package com.example.school.config;

import com.fasterxml.classmate.TypeResolver;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import com.example.school.entity.baseEntity.*;
import com.example.school.entity.complexEntity.*;

@EnableOpenApi
@Configuration
@EnableKnife4j
public class SwaggerConfig {
    @Value("${swagger.enable}")
    private Boolean enable;
    enum ENTITY{
        arrange(arrange.class),
        course(course.class),
        currentchoose(currentchoose.class),
        currentsemester(currentchoose.class),
        flags(flags.class),
        historycourse(historycourse.class),
        prechoose(prechoose.class),
        root(root.class),
        student(student.class),
        teacher(teacher.class),
        setScoreInfo(setScoreInfo.class),
        viewCourse(viewCourse.class),
        viewTableBlock(viewTableBlock.class),
        viewUser(viewUser.class),
        allclass(allclass.class),
        major(major.class);
        Class type;
        ENTITY(Class type){
            this.type = type;
        }
    }
//    http://localhost:9090/swagger-ui/index.html#/
//    http://localhost:9090/doc.html#/
    @Bean
    public Docket createRestApi(){
        TypeResolver typeResolver = new TypeResolver();
        Docket answer = new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build().enable(enable);
        for(ENTITY i : ENTITY.values()){
            answer.additionalModels(typeResolver.resolve(i.type));
        }
        return answer;
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("学生信息管理系统文档")
                .description("前端接收信息时，请统一取出response.data，在其中有自己的code、msg、data。" +
                        "如有bug请双手合十祈祷(-.-)")
                .contact(new Contact("张凌霄",null,"1832189171@qq.com"))
                .version("1.5")
                .build();
    }
}
