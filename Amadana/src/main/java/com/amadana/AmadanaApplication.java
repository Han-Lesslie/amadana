package com.amadana;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;

/**
 * @author Han
 */
@Configuration
@SpringBootApplication
@MapperScan("com.amadana.dao")
//项目入口类
public class AmadanaApplication extends SpringBootServletInitializer {
    private static  final Logger LOGGER = LoggerFactory.getLogger(AmadanaApplication.class);
    public static void main(String[] args) {
        LOGGER.info("Application Start ............");
        SpringApplication.run(AmadanaApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(AmadanaApplication.class);
    }

    /*@Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation("C:\\Users\\LENOVO\\Desktop\\myproject\\static");
        return factory.createMultipartConfig();
    }*/
}
