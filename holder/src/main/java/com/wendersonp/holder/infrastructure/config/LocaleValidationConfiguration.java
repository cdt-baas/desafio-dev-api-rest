package com.wendersonp.holder.infrastructure.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Configuration
public class LocaleValidationConfiguration {

    @Bean
    public LocaleResolver localeResolver() {
        var slr = new AcceptHeaderLocaleResolver();
        slr.setDefaultLocale(new Locale("pt", "BR"));
        return slr;
    }

    @Bean
    public MessageSource messageSource() {
        var ms = new ReloadableResourceBundleMessageSource();
        ms.setBasenames("classpath:ValidationMessages");
        ms.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return ms;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator(MessageSource messageSource) {
        var factory = new LocalValidatorFactoryBean();
        factory.setValidationMessageSource(messageSource);
        return factory;
    }
}
