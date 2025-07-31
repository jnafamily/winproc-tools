package com.jnafamily.winproctools.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class KeyboardConstraints {

    @Value("${winproctools.keyboard.constrains.hold-time.millis:30}")
    private int keyPressEventHoldTimeMillis;

    @Value("${winproctools.keyboard.constrains.key-events.max-count:10}")
    private int maxKeysToPress;
}
