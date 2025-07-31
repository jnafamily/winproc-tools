package com.jnafamily.winproctools.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TimeService {

    public void suspendCurrentThreadFor(long value, TimeUnit timeUnit) {
        try {
            long millis = timeUnit.toMillis(value);
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}
