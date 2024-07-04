//package ru.kata.spring.boot_security.demo.configs;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import java.io.IOException;
//
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class FaviconConfiguration {
//
//    @Bean
//    public FilterRegistrationBean<Filter> faviconFilter() {
//        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new FaviconFilter());
//        registrationBean.addUrlPatterns("/favicon.ico");
//        registrationBean.setOrder(Integer.MIN_VALUE);
//        return registrationBean;
//    }
//
//    public static class FaviconFilter implements Filter {
//        @Override
//        public void init(FilterConfig filterConfig) throws ServletException {
//        }
//
//        @Override
//        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//                throws IOException, ServletException {
//            response.getWriter().write("");
//        }
//
//        @Override
//        public void destroy() {
//        }
//    }
//}
