package com.jnafamily.winproctools.client;

import com.jnafamily.winproctools.exception.JnaOperationException;
import com.jnafamily.winproctools.jna.api.NativeWindowApi;
import com.jnafamily.winproctools.model.Vector2;
import com.sun.jna.Native;
import com.sun.jna.platform.WindowUtils;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.IntByReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class WindowClient {
    private final NativeWindowApi nativeWindowApi;

    public Vector2 getWindowPosition(WinDef.HWND hwnd) {
        WinDef.RECT rect = new WinDef.RECT();
        if (nativeWindowApi.GetWindowRect(hwnd, rect)) {
            return new Vector2(rect.left, rect.top);
        }
        throw new JnaOperationException("Could not get window position");
    }

    public Vector2 getWindowSize(WinDef.HWND hwnd) {
        WinDef.RECT rect = new WinDef.RECT();
        if (nativeWindowApi.GetWindowRect(hwnd, rect)) {
            int width = rect.right - rect.left;
            int height = rect.bottom - rect.top;
            return new Vector2(width, height);
        }
        throw new JnaOperationException("Could not get window size");
    }

    public int getWindowProcessId(WinDef.HWND hwnd) {
        IntByReference pidRef = new IntByReference();
        nativeWindowApi.GetWindowThreadProcessId(hwnd, pidRef);
        return pidRef.getValue();
    }

    public boolean isWindowEnabled(WinDef.HWND hwnd) {
        return nativeWindowApi.IsWindowEnabled(hwnd);
    }

    public boolean isWindowActive(WinDef.HWND hwnd) {
        WinDef.HWND foreground = nativeWindowApi.GetForegroundWindow();
        return hwnd.equals(foreground);
    }

    public int getWindowThreadId(WinDef.HWND hwnd) {
        return nativeWindowApi.GetWindowThreadProcessId(hwnd, new IntByReference());
    }

    public String getWindowClassName(WinDef.HWND hwnd) {
        char[] classNameBuffer = new char[512];
        nativeWindowApi.GetClassName(hwnd, classNameBuffer, classNameBuffer.length);
        return Native.toString(classNameBuffer);
    }

    public String getWindowTile(WinDef.HWND hwnd) {
        return WindowUtils.getWindowTitle(hwnd);
    }

    public boolean isWindowVisible(WinDef.HWND hwnd) {
        return nativeWindowApi.IsWindowVisible(hwnd);
    }

    public <T> List<T> findWindows(Predicate<WinDef.HWND> filter, Function<WinDef.HWND, T> mapper) {
        List<T> result = new LinkedList<>();

        nativeWindowApi.EnumWindows((hWnd, data) -> {
            if (filter.test(hWnd)) {
                result.add(mapper.apply(hWnd));
            }
            return true;
        }, null);

        return result;
    }
}
