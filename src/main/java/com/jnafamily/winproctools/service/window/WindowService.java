package com.jnafamily.winproctools.service.window;

import com.jnafamily.winproctools.client.WindowClient;
import com.jnafamily.winproctools.model.window.WindowInfo;
import com.jnafamily.winproctools.utils.HwndUtils;
import com.sun.jna.platform.win32.WinDef;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WindowService {
    private final WindowClient windowClient;

    public List<Long> findWindowHandles(String title) {
        return windowClient.findWindows(hwd -> {
            String windowTile = windowClient.getWindowTile(hwd);

            return windowClient.isWindowVisible(hwd)
                    && !StringUtils.isBlank(windowTile)
                    && windowTile.equals(title);
        }, HwndUtils::toLong);
    }

    public WindowInfo loadWindowInfo(long handle) {
        WindowInfo.Descriptor descriptor = loadWindowDescriptor(handle);
        WindowInfo.State state = loadWindowState(handle);

        return new WindowInfo(descriptor, state);
    }

    public WindowInfo.Descriptor loadWindowDescriptor(long handle) {
        WinDef.HWND hwnd = HwndUtils.fromLong(handle);
        var className = windowClient.getWindowClassName(hwnd);
        var processId = windowClient.getWindowProcessId(hwnd);
        var threadId = windowClient.getWindowThreadId(hwnd);

        return WindowInfo.Descriptor.builder()
                .className(className)
                .processId(processId)
                .threadId(threadId)
                .handle(HwndUtils.toLong(hwnd))
                .build();
    }

    public void updateState(WindowInfo windowInfo) {
        long handle = windowInfo.getDescriptor().handle();
        WindowInfo.State state = loadWindowState(handle);
        windowInfo.setState(state);
    }

    public WindowInfo.State loadWindowState(long handle) {
        WinDef.HWND hwnd = HwndUtils.fromLong(handle);
        var title = windowClient.getWindowTile(hwnd);
        var size = windowClient.getWindowSize(hwnd);
        var position = windowClient.getWindowPosition(hwnd);
        var isVisible = windowClient.isWindowVisible(hwnd);
        var isEnabled = windowClient.isWindowEnabled(hwnd);
        var isActive = windowClient.isWindowActive(hwnd);

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
