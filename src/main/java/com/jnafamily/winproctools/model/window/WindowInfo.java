package com.jnafamily.winproctools.model.window;

import com.jnafamily.winproctools.model.Vector2;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class WindowInfo {
    private Descriptor descriptor;
    private State state;

    /**
     * Represents the immutable/static part of window data.
     */
    @Builder
    public record Descriptor(
            long handle,
            int processId,
            int threadId,
            String className
    ) {
    }

    /**
     * Represents the mutable/dynamic part of the window data that may change over time and needs to be updated
     * periodically.
     */
    @Builder
    public record State(
            String title,
            Vector2 position,
            Vector2 size,
            boolean isVisible,
            boolean isEnabled,
            boolean isActive
    ) {
    }
}
