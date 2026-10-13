package dev.soughtxp.wamodx.core;

import dev.soughtxp.wamodx.util.PrefsUtils;

public class ComposingPrivacy {

    public static boolean hideComposing() {
        try {
            return PrefsUtils.PREFERENCES.getBoolean("hideComposing", false);
        } catch (Throwable t) {
            return false;
        }
    }
}
