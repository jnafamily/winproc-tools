package com.jnafamily.winproctools.config;


import com.jnafamily.winproctools.jna.api.NativeKeyboardApi;
import com.jnafamily.winproctools.jna.api.NativeWindowApi;
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
    public static final String USER_32_LIB_NAME = "user32";

    @Bean
    public NativeKeyboardApi nativeKeyboardApi() {
        return load(USER_32_LIB_NAME, NativeKeyboardApi.class);
    }

    @Bean
    public NativeWindowApi nativeWindowApi() {
        return load(USER_32_LIB_NAME, NativeWindowApi.class);
    }

    // There’s no memory waste if the same lib is loaded twice for different interfaces - native libraries are cached
    // globally by JNA.
    private <T extends Library> T load(String name, Class<T> type) {
        log.debug("Loading the '{}' library...", name);
        T loaded = Native.load(name, type, W32APIOptions.DEFAULT_OPTIONS);
        log.debug("'{}' is loaded", name);
        return loaded;
    }
}
