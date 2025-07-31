package com.jnafamily.winproctools.jna.api;

import com.jnafamily.winproctools.ext.User32Ext;
import com.jnafamily.winproctools.model.Vector2;
import com.sun.jna.platform.win32.WinDef;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CursorApi {
    private final User32Ext user32Ext;

    public boolean setCursorPosition(int x, int y) {
        return user32Ext.SetCursorPos(x, y);
    }

    public Vector2 getCursorPosition() {
        WinDef.POINT p = new WinDef.POINT();
        user32Ext.GetCursorPos(p);
        return new Vector2(p.x, p.y);
    }
}
