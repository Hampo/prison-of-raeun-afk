package org.zhbot.prison_of_raeun_afk;

public enum DurationMode {
    TICKS("Ticks"),
    SECONDS("Seconds");

    private final String name;

    DurationMode(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
