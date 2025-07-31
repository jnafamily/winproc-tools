package com.jnafamily.winproctools.service.keyboard;

import com.jnafamily.winproctools.mapper.MessageParameterMapper;
import com.jnafamily.winproctools.model.keyboard.VirtualKeys;
import com.jnafamily.winproctools.service.TimeService;
import com.jnafamily.winproctools.utils.HwndUtils;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class KeyboardService {
    private static final int KEY_PRESS_HOLD_TIME_MILLIS = 30;

    private final TimeService timeService;
    private final MessageParameterMapper messageParameterMapper;

    public void sendKeyPressEvent(long windowHandle, VirtualKeys... keys) {
        sendKeyPressEvent(windowHandle, Arrays.stream(keys).toList());
    }

    public void sendText(long windowHandle, String text) {
        WinDef.HWND hwnd = HwndUtils.fromLong(windowHandle);
        sendText(hwnd, text);
    }

    public void sendText(WinDef.HWND hwd, String text) {
        for (char c : text.toCharArray()) {
            User32.INSTANCE.SendMessage(hwd, User32.WM_CHAR, new WinDef.WPARAM(c), new WinDef.LPARAM(0));
        }
    }

    public void sendKeyPressEvent(long windowHandle, List<VirtualKeys> keys) {
        // TODO: if (keys.size() > N) do throw

        List<WinDef.WPARAM> messageParams = messageParameterMapper.map(keys);
        WinDef.HWND hwnd = HwndUtils.fromLong(windowHandle);

        sendKeyPressEvent(hwnd, messageParams);
    }

    /**
     * There can be two inputs per press - the app is reading the event twice or getting malformed input.
     * <p></p>
     * <p>The <code>PostMessage</code> API does:</p>
     * <ul>
     *     <li>
     *         Posts a message to the message queue of the target window, but does not synthesize real keyboard input.
     *     </li>
     *     <li>
     *         If the target window uses both low-level keyboard hooks
     *         (or reads input from both message queue and raw input), it may interpret your simulated press twice.
     *     </li>
     *     <li>
     *         Also, WM_KEYDOWN/WM_KEYUP expect specific LPARAM flags that describe the scan code, repeat count, etc.
     *     </li>
     * </ul>
     */
    private void sendKeyPressEvent(WinDef.HWND hwnd, List<WinDef.WPARAM> messagesInfo) {
        for (var wParam : messagesInfo) {
            User32.INSTANCE.PostMessage(hwnd, WinUser.WM_KEYDOWN, wParam, new WinDef.LPARAM(0));
        }
        timeService.suspendCurrentThreadFor(KEY_PRESS_HOLD_TIME_MILLIS, TimeUnit.MILLISECONDS);
        for (var info : messagesInfo) {
            User32.INSTANCE.PostMessage(hwnd, WinUser.WM_KEYUP, info, new WinDef.LPARAM(0));
        }
        timeService.suspendCurrentThreadFor(KEY_PRESS_HOLD_TIME_MILLIS, TimeUnit.MILLISECONDS);
    }

    private void release(WinDef.HWND windowHandle, Set<WinDef.WPARAM> messagesInfo) {
        for (var info : messagesInfo) {
            User32.INSTANCE.PostMessage(windowHandle, WinUser.WM_KEYUP, info, new WinDef.LPARAM(0));
        }
    }
}
