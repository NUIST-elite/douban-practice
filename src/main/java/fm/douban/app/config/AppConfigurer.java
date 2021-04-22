package fm.douban.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import fm.douban.app.interceptor.UserInterceptor;

@Configuration
public class AppConfigurer implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        registry.addInterceptor(new UserInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/sign") // 登录操作不需要登录
                .excludePathPatterns("/login")        // 登录页面不需要登录
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/register")
                .excludePathPatterns("/authenticate")// 静态资源为文件不需要登录
                .excludePathPatterns("/error");            // 系统错误页面不需要登录
    }
}
