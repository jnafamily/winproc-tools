package com.jnafamily.winproctools.mapper;

import com.jnafamily.winproctools.model.keyboard.VirtualKeys;
import com.sun.jna.platform.win32.WinDef;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageParameterMapper {

    public List<WinDef.WPARAM> map(List<VirtualKeys> keys) {
        return keys.stream()
                .map(this::map)
                .toList();
    }

    public WinDef.WPARAM map(VirtualKeys key) {
        return new WinDef.WPARAM(key.getCode());
    }
}
