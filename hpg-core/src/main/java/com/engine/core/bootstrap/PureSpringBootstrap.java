package com.engine.core.bootstrap;

import com.engine.core.config.AppConfig;
import com.engine.core.config.PersistenceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public final class PureSpringBootstrap {
    private static final Logger log = LoggerFactory.getLogger(PureSpringBootstrap.class);

    private PureSpringBootstrap() {

    }

    public static void main(String[] args) {
        try(AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class, PersistenceConfig.class)) {

            int beanCount = ctx.getBeanDefinitionCount();
            log.info("Pure Spring context started. Bean definition count: {}", beanCount);

            ctx.getBeanDefinitionNames();
            for(String name : ctx.getBeanDefinitionNames()) {
                if(name.contains("Config") || name.contains("Manager") || name.contains("DataSource")) {
                    log.info("Registered bean: {}", name);
                }
            }

            log.info("Persistence stack is wired. Shutting down.");
        }
    }
}
