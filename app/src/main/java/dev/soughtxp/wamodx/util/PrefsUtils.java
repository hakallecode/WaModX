package dev.soughtxp.wamodx.util;

import android.content.Context;
import android.content.SharedPreferences;

import dev.soughtxp.wamodx.App;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public enum PrefsUtils {

    REVOKED_IDS("revoked_ids"),
    PREFERENCES("preferences"),
    TICKS("ticks");

    private final String fileName;

    PrefsUtils(String fileName) {
        this.fileName = fileName;
    }

    private SharedPreferences getPrefs() {
        Context context = App.getContext();
        if (context == null) {
            throw new IllegalStateException("Context not available");
        }
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public Map<String, ?> getAll() {
        return getPrefs().getAll();
    }

    public void setInt(String key, int value) {
        getPrefs().edit().putInt(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        return getPrefs().getInt(key, defaultValue);
    }

    public void setLong(String key, long value) {
        getPrefs().edit().putLong(key, value).apply();
    }

    public long getLong(String key, long defaultValue) {
        return getPrefs().getLong(key, defaultValue);
    }

    public void setString(String key, String value) {
        getPrefs().edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return getPrefs().getString(key, defaultValue);
    }

    public void setBoolean(String key, boolean value) {
        getPrefs().edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return getPrefs().getBoolean(key, defaultValue);
    }

    public void setStringSet(String key, Set<String> value) {
        getPrefs().edit().putStringSet(key, value).apply();
    }

    public Set<String> getStringSet(String key, Set<String> defaultValue) {
        return getPrefs().getStringSet(key, defaultValue);
    }

    public boolean contains(String key) {
        return getPrefs().contains(key);
    }

    public void remove(String key) {
        getPrefs().edit().remove(key).apply();
    }

    public void clear() {
        getPrefs().edit().clear().apply();
    }

    public Map<String, Set<String>> loadPending(String owner) {
        Map<String, Set<String>> byGroup = new LinkedHashMap<>();
        String prefix = owner + ",";
        for (String storedKey : getPrefs().getAll().keySet()) {
            if (storedKey.startsWith(prefix)) {
                String group = storedKey.replace(prefix, "");
                Set<String> ids = getStringSet(storedKey, null);
                if (ids != null && !ids.isEmpty()) {
                    byGroup.put(group, new LinkedHashSet<>(ids));
                }
            }
        }
        return byGroup;
    }

    public void addPending(String owner, String group, String id) {
        String storedKey = owner + "," + (group == null ? "" : group);
        Set<String> ids = new LinkedHashSet<>();
        Set<String> current = getStringSet(storedKey, null);
        if (current != null) {
            ids.addAll(current);
        }
        ids.add(id);
        setStringSet(storedKey, ids);
    }

    public void clearPending(String owner) {
        String prefix = owner + ",";
        SharedPreferences.Editor editor = getPrefs().edit();
        for (String storedKey : getPrefs().getAll().keySet()) {
            if (storedKey.startsWith(prefix)) {
                editor.remove(storedKey);
            }
        }
        editor.apply();
    }
}
