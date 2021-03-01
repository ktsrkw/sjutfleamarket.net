package com.wt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    //这个配置类用来添加文件访问的访问路径映射规则
    //由于在实际操作过程中发现，上传图片后无法立即访问到，工程目录已经存在图片但target文件夹下不存在
    //重启项目后便可以访问，但在实际项目运行时不可能上传个文件就重启一下
    //于是设置了文件路径的映射，把文件写入到一个绝对路径下（见UploadController类），然后设置映射来访问
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //需要添加映射的绝对路径（路径最后的/一定要加）
        String imgPath = "D:\\codes\\resources\\sjut-flea-market-user-upload\\";
        //设置映射规则，源路径（ResourceLocations）被设置成可以通过映射路径（ip:port/images）来访问
        registry.addResourceHandler("/images/**").
                addResourceLocations("file:" + imgPath);
        //以本项目为例，未映射时，访问上传的图片需要 localhost:8080/useruploads/xxx.jpg （我在static目录下放的uploads目录）
        //需要重启来访问新上传的文件
        //设置了映射后，不需要重启，直接 localhost：8080/images/xxx.jpg 就能访问到了
    }
}