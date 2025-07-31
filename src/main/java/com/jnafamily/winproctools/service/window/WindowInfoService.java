package com.jnafamily.winproctools.service.window;

import com.jnafamily.winproctools.jna.api.WindowApi;
import com.jnafamily.winproctools.model.window.WindowInfo;
import com.jnafamily.winproctools.utils.HwndUtils;
import com.sun.jna.platform.win32.WinDef;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WindowInfoService {
    private final WindowApi windowApi;

    public WindowInfo loadWindowInfo(long handle) {
        WindowInfo.Descriptor descriptor = loadWindowDescriptor(handle);
        WindowInfo.State state = loadWindowState(handle);

        return new WindowInfo(descriptor, state);
    }

    public WindowInfo.Descriptor loadWindowDescriptor(long handle) {
        WinDef.HWND hwnd = HwndUtils.fromLong(handle);
        var className = windowApi.getWindowClassName(hwnd);
        var processId = windowApi.getWindowProcessId(hwnd);
        var threadId = windowApi.getWindowThreadId(hwnd);

        return WindowInfo.Descriptor.builder()
                .className(className)
                .processId(processId)
                .threadId(threadId)
                .handle(HwndUtils.toLong(hwnd))
                .build();
    }

    public WindowInfo.State loadWindowState(long handle) {
        WinDef.HWND hwnd = HwndUtils.fromLong(handle);
        var title = windowApi.getWindowTile(hwnd);
        var size = windowApi.getWindowSize(hwnd);
        var position = windowApi.getWindowPosition(hwnd);
        var isVisible = windowApi.isWindowVisible(hwnd);
        var isEnabled = windowApi.isWindowEnabled(hwnd);
        var isActive = windowApi.isWindowActive(hwnd);

        return WindowInfo.State.builder()
                .title(title)
                .size(size)
                .position(position)
                .isVisible(isVisible)
                .isEnabled(isEnabled)
                .isActive(isActive)
                .build();
    }
}
