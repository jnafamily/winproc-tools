package com.jnafamily.winproctools.service.cursor;

import com.jnafamily.winproctools.jna.api.CursorApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CursorService {
    private final CursorApi cursorApi;

}
