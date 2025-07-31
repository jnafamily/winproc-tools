package com.jnafamily.winproctools.config;


import com.jnafamily.winproctools.ext.Gdi32Ext;
import com.jnafamily.winproctools.ext.Kernel32Ext;
import com.jnafamily.winproctools.ext.User32Ext;
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
    public User32Ext user32Ext() {
        return load("user32", User32Ext.class);
    }

    @Bean
    public Kernel32Ext kernel32Ext() {
        return load("kernel32", Kernel32Ext.class);
    }

    @Bean
    public Gdi32Ext gdi32Ext() {
        return load("gdi32", Gdi32Ext.class);
    }

    private <T extends Library> T load(String name, Class<T> type) {
        log.debug("Loading the '{}' library...", name);
        T loaded = Native.load(name, type, W32APIOptions.DEFAULT_OPTIONS);
        log.debug("'{}' is loaded", name);
        return loaded;
    }
}