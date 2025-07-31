package com.jnafamily.winproctools.utils;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HwndUtils {

    public static long toLong(WinDef.HWND hwnd) {
        return Pointer.nativeValue(hwnd.getPointer());
    }

    public static WinDef.HWND fromLong(long hwndValue) {
        return new WinDef.HWND(Pointer.createConstant(hwndValue));
    }
}
