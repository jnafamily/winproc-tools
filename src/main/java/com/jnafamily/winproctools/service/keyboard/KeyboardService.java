package com.jnafamily.winproctools.service.keyboard;

import com.jnafamily.winproctools.client.KeyboardClient;
import com.jnafamily.winproctools.config.KeyboardConstraints;
import com.jnafamily.winproctools.model.keyboard.KeyCombination;
import com.jnafamily.winproctools.model.keyboard.VirtualKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeyboardService {

    private final KeyboardConstraints keyboardConstraints;
    private final KeyboardClient keyboardClient;

    public void sendKeyPressEvent(long windowHandle, KeyCombination keyCombination) {
        sendKeyPressEvent(windowHandle, keyCombination.keys());
    }

    public void sendKeyPressEvent(long windowHandle, VirtualKeys... keys) {
        sendKeyPressEvent(windowHandle, Arrays.stream(keys).toList());
    }

    public void sendKeyPressEvent(long windowHandle, List<VirtualKeys> keys) {
        if (keys.size() > keyboardConstraints.getMaxKeysToPress()) {
            throw new IllegalArgumentException("Too many keys to press");
        }
        keyboardClient.sendKeyPressEvent(windowHandle, keys, keyboardConstraints.getKeyPressEventHoldTimeMillis());
    }
}
