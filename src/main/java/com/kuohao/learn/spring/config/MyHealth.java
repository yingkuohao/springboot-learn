package com.kuohao.learn.spring.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/5/31
 * Time: 下午2:12
 * CopyRight: taobao
 * Descrption:
 */
@Component
public class MyHealth implements HealthIndicator {
    @Override
    public Health health() {
             int errorCode = check(); // perform some specific health check
             if (errorCode != 0) {
                 return Health.down().withDetail("Error Code", errorCode).build();
             }
             return Health.up().build();
    }

    private int check() {
        return 0;
    }
}
