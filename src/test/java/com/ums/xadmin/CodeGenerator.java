package com.ums.xadmin;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/xdb";       // 与配置文件 一致
        String username = "root";
        String password = "123456";
        String author = "liubo";
        String moduleName = "sys";              // 系统管理的代码包,生成其他代码时仅需要改此处与数据库处第18行
        String mapperLocation = "D:\\Program Files (x86)\\CampusLostAndFound\\CampusLostAndFound\\x-admin\\src\\main\\resources\\mapper" + moduleName ;
        String tables = "x_menu,x_role,x_role_menu,x_user,x_user_role";     // 与数据库中的表名一致，逗号隔开

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author(author) // 设置作者
//                        .enableSwagger() // 开启 swagger 模式
//                        .fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\Program Files (x86)\\CampusLostAndFound\\CampusLostAndFound\\x-admin\\src\\main\\java"); // 指定输出目录
                })

                .packageConfig(builder -> {
                    builder.parent("com.ums") // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, mapperLocation)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tables) // 设置需要生成的表名
                            .addTablePrefix("x_"); // 设置过滤表前缀， x_menu 生成的类实体无 x_ 前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
