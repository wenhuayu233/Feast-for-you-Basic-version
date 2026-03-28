package com.tqwc.feastweb.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.Collections;

/**
 * @author Tang
 * @date 2026/3/28 15:42
 *
 * MyBatis-Plus 代码生成器
 *
 * 运行位置：
 * 1. 放在 feast-web 模块中
 * 2. 直接运行 main 方法
 *
 * 生成结果：
 * 1. entity / mapper / service / serviceImpl -> feast-web
 * 2. mapper.xml -> feast-web/src/main/resources/mapper
 *
 * 注意：
 * 1. controller 默认不生成
 * 2. 后续你再把 entity 包剪切到 feast-common
 */
public class CodeGenerator {

    /**
     * ========== 数据库配置 ==========
     */
    private static final String DB_HOST = "111.229.181.254";
    private static final String DB_PORT = "13306";
    private static final String DB_NAME = "feast_for_you";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "feastforyou123456";

    /**
     * ========== 包配置 ==========
     */
    private static final String PARENT_PACKAGE = "com.tqwc.feastweb";

    /**
     * ========== 作者信息 ==========
     */
    private static final String AUTHOR = "Tang";

    /**
     * ========== 要生成的表 ==========
     */
    private static final String[] TABLES = {
            "user",
            "dish",
            "order_item",
            "order_record",
            "bind_request",
            "favorite",
            "like_record",
            "love_diary",
            "relationship"
    };

    public static void main(String[] args) {

        // 当前运行目录：feast-web
        String projectRoot = System.getProperty("user.dir");
        String webModulePath = projectRoot + "/feast-web";

        // Java 输出目录
        String javaOutputDir = webModulePath + "/src/main/java";

        // Mapper XML 输出目录
        String xmlOutputDir = webModulePath + "/src/main/resources/mapper";

        // 数据库连接 URL
        String jdbcUrl = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
                + "?useUnicode=true"
                + "&characterEncoding=UTF-8"
                + "&serverTimezone=Asia/Shanghai"
                + "&useSSL=false"
                + "&allowPublicKeyRetrieval=true";

        System.out.println("==================================================");
        System.out.println("当前运行模块路径: " + webModulePath);
        System.out.println("Java 输出目录: " + javaOutputDir);
        System.out.println("XML 输出目录: " + xmlOutputDir);
        System.out.println("==================================================");

        FastAutoGenerator.create(jdbcUrl, DB_USERNAME, DB_PASSWORD)
                // 全局配置
                .globalConfig(builder -> builder
                        .author(AUTHOR)
                        .disableOpenDir()
                        .outputDir(javaOutputDir)
                        .commentDate("yyyy-MM-dd")
                )

                // 包配置
                .packageConfig(builder -> builder
                        .parent(PARENT_PACKAGE)
                        .entity("entity")
                        .mapper("mapper")
                        .service("service")
                        .serviceImpl("service.impl")
                        .pathInfo(Collections.singletonMap(OutputFile.xml, xmlOutputDir))
                )

                // 策略配置
                .strategyConfig(builder -> builder
                        .addInclude(TABLES)

                        // 实体类配置
                        .entityBuilder()
                        .enableLombok()
                        .enableTableFieldAnnotation()
                        .naming(NamingStrategy.underline_to_camel)
                        .columnNaming(NamingStrategy.underline_to_camel)
                        .logicDeleteColumnName("deleted")
                        .enableFileOverride()

                        // Mapper 配置
                        .mapperBuilder()
                        .enableMapperAnnotation()
                        .enableBaseResultMap()
                        .enableBaseColumnList()
                        .enableFileOverride()

                        // Service 配置
                        .serviceBuilder()
                        .formatServiceFileName("%sService")
                        .formatServiceImplFileName("%sServiceImpl")
                        .enableFileOverride()

                        // 不生成 Controller
                        .controllerBuilder()
                        .disable()
                )

                // 使用 Velocity 模板引擎
                .templateEngine(new VelocityTemplateEngine())

                // 执行生成
                .execute();

        System.out.println("==================================================");
        System.out.println("代码生成完成！");
        System.out.println("请先到 feast-web 模块中查看生成结果。");
        System.out.println("之后再把 entity 包剪切到 feast-common。");
        System.out.println("==================================================");
    }
}