package dev.soughtxp.wamodx.core;

import dev.soughtxp.wamodx.util.PrefsUtils;

public class LastSeenPrivacy {

    public static boolean freezeLastSeen() {
        try {
            return PrefsUtils.PREFERENCES.getBoolean("freezeLastSeen", false);
        } catch (Throwable t) {
            return false;
        }
    }
}
