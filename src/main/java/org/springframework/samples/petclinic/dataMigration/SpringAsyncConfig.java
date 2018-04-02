package org.springframework.samples.petclinic.dataMigration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableScheduling
@EnableAsync
public class SpringAsyncConfig {
    @Bean(name = "ConsistencyCheckerThread")
    public Executor ConsistencyCheckerThread() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean(name = "ShadowWriteThread")
    public Executor ShadowWriteThread() {
        return new ThreadPoolTaskExecutor();
    }
}
