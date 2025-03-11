package com.example.app.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.app.filter.AuthFilter;

@Configuration
public class ApplicationConfig implements WebMvcConfigurer {

	// バリデーションメッセージのカスタマイズ
	@Override
	public Validator getValidator() {
		var validator = new LocalValidatorFactoryBean();
		validator.setValidationMessageSource(messageSource());
		return validator;
	}

	@Bean
	ResourceBundleMessageSource messageSource() {
		var messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("validation");
		return messageSource;
	}

	// 認証用フィルタの有効化
	@Bean
	FilterRegistrationBean<AuthFilter> authFilter() {
		var bean = new FilterRegistrationBean<AuthFilter>(new AuthFilter());
		bean.addUrlPatterns("/recipe/**");
		return bean;
	}

	// uploadsフォルダをリソースとして利用可能にする
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/**", "/static/**")
				.addResourceLocations("file:///C:/Users/zd3N02/uploads/", "classpath:/static/");
		// TODO 自動生成されたメソッド・スタブ
	}

}