package com.jnafamily.winproctools.model.keyboard;

import java.util.List;

public record KeyCombination(List<VirtualKeys> keys, String name) {
}
