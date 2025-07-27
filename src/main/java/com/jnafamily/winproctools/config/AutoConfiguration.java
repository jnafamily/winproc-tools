package com.jnafamily.winproctools.config;


import com.jnafamily.winproctools.api.Gdi32Api;
import com.jnafamily.winproctools.api.Kernel32Api;
import com.jnafamily.winproctools.api.User32Api;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ComponentScan(basePackages = "com.jnafamily.winproctools")
public class AutoConfiguration {

    @Bean
    public User32Api user32Api() {
        return load("user32", User32Api.class);
    }

    @Bean
    public Kernel32Api kernel32Api() {
        return load("kernel32", Kernel32Api.class);
    }

    @Bean
    public Gdi32Api gdi32Api() {
        return load("gdi32", Gdi32Api.class);
    }

    private <T extends Library> T load(String name, Class<T> type) {
        log.debug("Loading '{}'...", name);
        T loaded = Native.load(name, type, W32APIOptions.DEFAULT_OPTIONS);
        log.debug("'{}' is loaded", name);
        return loaded;
    }
}