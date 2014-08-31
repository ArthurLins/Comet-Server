package com.cometproject.server.game.navigator.types.featured;

public enum BannerType {
    BIG,
    SMALL, Locale;

    public static BannerType get(String t) {
        if (t.equals("big")) {
            return BIG;
        }
        return SMALL;
    }
}
