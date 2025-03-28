package ru.project.runjournal.run_journal;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @brief класс конфигурации
 * 
 * Добавляет контроллеры для страниц, которые не требуют обработки сервером (статические)
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer{

    @Override
    public void addViewControllers(@SuppressWarnings("null") ViewControllerRegistry registry){
        registry.addViewController("/run-journal/login").setViewName("login");
        registry.addViewController("/run-journal/errorpage").setViewName("errorpage");
        //registry.addViewController("/addactivity").setViewName("addactivity");
        //registry.addViewController("/activitydetails").setViewName("activitydetails");
        
    }

}