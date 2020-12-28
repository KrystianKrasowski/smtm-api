package com.smtm.application.categories;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.smtm.application.categories.v1.CategoriesController;

@Configuration
public class TestCategoriesConfiguration {

    @Bean
    public CategoriesController categoriesController() {
        return new CategoriesController();
    }
}
