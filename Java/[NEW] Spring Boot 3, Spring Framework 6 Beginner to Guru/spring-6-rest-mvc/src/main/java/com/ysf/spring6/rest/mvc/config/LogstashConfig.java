package com.ysf.spring6.rest.mvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.HttpLogFormatter;
import org.zalando.logbook.Sink;
import org.zalando.logbook.json.JsonHttpLogFormatter;
import org.zalando.logbook.logstash.LogstashLogbackSink;

@Configuration
public class LogstashConfig {

    // prevents nested json from getting escaped
    @Bean
    public Sink logstashLogbackSink(){
        HttpLogFormatter formatter = new JsonHttpLogFormatter();
        return new LogstashLogbackSink(formatter);
    }
}
