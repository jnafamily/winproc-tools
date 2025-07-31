package com.jnafamily.winproctools.client;

import com.jnafamily.winproctools.jna.api.NativeKeyboardApi;
import com.jnafamily.winproctools.mapper.MessageParameterMapper;
import com.jnafamily.winproctools.model.keyboard.VirtualKeys;
import com.jnafamily.winproctools.service.TimeService;
import com.jnafamily.winproctools.utils.HwndUtils;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class KeyboardClient {
    private final NativeKeyboardApi keyboardApi;
    private final MessageParameterMapper messageParameterMapper;
    private final TimeService timeService;

    public void sendText(long windowHandle, String text) {
        WinDef.HWND hwnd = HwndUtils.fromLong(windowHandle);
        sendText(hwnd, text);
    }

    public void sendText(WinDef.HWND hwd, String text) {
        for (char symbol : text.toCharArray()) {
            keyboardApi.SendMessage(hwd, User32.WM_CHAR, new WinDef.WPARAM(symbol), new WinDef.LPARAM(0));
        }
    }

    /**
     * There can be two inputs per press - the app may be reading the event twice or getting malformed input.
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
     *         Also, <code>WM_KEYDOWN/WM_KEYUP</code> expect specific <code>LPARAM</code> flags that describe
     *         the scan code, repeat count, etc.
     *     </li>
     * </ul>
     */
    public void sendKeyPressEvent(long windowHandle, List<VirtualKeys> keys, int holdTimeMillis) {
        List<WinDef.WPARAM> messageParams = messageParameterMapper.map(keys);
        WinDef.HWND hwnd = HwndUtils.fromLong(windowHandle);

        sendKeyPressEvent(hwnd, messageParams, holdTimeMillis);
    }

    private void sendKeyPressEvent(WinDef.HWND hwnd, List<WinDef.WPARAM> messagesInfo, int holdTimeMillis) {
        for (var wParam : messagesInfo) {
            keyboardApi.PostMessage(hwnd, WinUser.WM_KEYDOWN, wParam, new WinDef.LPARAM(0));
        }
        timeService.suspendCurrentThreadFor(holdTimeMillis, TimeUnit.MILLISECONDS);
        for (var info : messagesInfo) {
            keyboardApi.PostMessage(hwnd, WinUser.WM_KEYUP, info, new WinDef.LPARAM(0));
        }
        timeService.suspendCurrentThreadFor(holdTimeMillis, TimeUnit.MILLISECONDS);
    }

    private void sendKeyReleaseEvent(WinDef.HWND hwnd, Set<WinDef.WPARAM> messagesInfo) {
        for (var info : messagesInfo) {
            keyboardApi.PostMessage(hwnd, WinUser.WM_KEYUP, info, new WinDef.LPARAM(0));
        }
    }
}
