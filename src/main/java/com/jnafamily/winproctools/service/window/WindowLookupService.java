package com.jnafamily.winproctools.service.window;

import com.jnafamily.winproctools.jna.api.WindowApi;
import com.jnafamily.winproctools.utils.HwndUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WindowLookupService {
    private final WindowApi windowApi;

    public List<Long> findWindowHandles(String title) {
        return windowApi.findWindows(hwd -> {
            String windowTile = windowApi.getWindowTile(hwd);

            return windowApi.isWindowVisible(hwd)
                    && !StringUtils.isBlank(windowTile)
                    && windowTile.equals(title);
        }, HwndUtils::toLong);
    }
}
