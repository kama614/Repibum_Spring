// Spring Boot アプリケーションの設定を行うためのクラス
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

@Configuration // Spring の設定クラスであることを示すアノテーション
public class ApplicationConfig implements WebMvcConfigurer {
	// WebMvcConfigurer インターフェースを実装することで、Web 関連の設定（リソースハンドリング、ビューの設定など）を追加

	// バリデーションの設定(メッセージのカスタマイズ)
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
		// setBasename("validation") は、validation.properties というプロパティファイルを使用することを指定
		return messageSource;
	}

	// 認証用フィルタの有効化
	@Bean
	FilterRegistrationBean<AuthFilter> authFilter() {
		var bean = new FilterRegistrationBean<AuthFilter>(new AuthFilter());
		bean.addUrlPatterns("/recipe/**");
		// URL が /recipe/ で始まるリクエストに対してのみAuthFilter適用
		return bean;
	}

	// uploadsフォルダをリソースとして利用可能にする
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/**")
				.addResourceLocations("file:///C:/Users/zd3N02/uploads/");
	}

}

/*
@Beanアノテーションによって、
messageSourceとauthFilterは Spring コンテナに登録され、アプリケーション全体で使用可能。

*/