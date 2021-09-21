package com.baeldung.application;

import com.baeldung.application.entities.*;
import com.baeldung.application.sclm.BaseMain;
import com.baeldung.application.sclm.JiraUtils;
import com.baeldung.application.sclm.XlsLoadData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {
    @Bean
    public XlsLoadData xlsLoadData(){
        return new XlsLoadData();
    }

    @Bean
    public Load load(){
        return new Load();
    }

    @Bean
    @Scope("prototype")
    public Promote promote(){
        return new Promote();
    }

    @Bean
    public BaseMain baseMain(){
        return new BaseMain();
    }

    @Bean
    public JiraUtils jiraUtils(){
        return new JiraUtils();
    }

    @Bean
    public User user() {return new User();}

    @Bean
    public Issue issue() {return new Issue();}

    @Bean
    public Release release() {return new Release();}


}
