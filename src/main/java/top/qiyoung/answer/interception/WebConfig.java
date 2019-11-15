package top.qiyoung.answer.interception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SessionInterception sessionInterception;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) { // 访问自定义静态资源
        registry.addResourceHandler("/upload/**").addResourceLocations("file:"+System.getProperty("user.dir")+"\\src\\main\\resources\\static\\upload\\");
        registry.addResourceHandler("/template/**").addResourceLocations("file:"+System.getProperty("user.dir")+"\\src\\main\\resources\\static\\template\\");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {  // 拦截路径
        registry.addInterceptor(sessionInterception).addPathPatterns("/**");
    }
}
