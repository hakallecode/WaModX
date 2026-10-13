package dev.soughtxp.wamodx.core;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ReceiptJobFilter {

    private static final Set<String> RECEIPT_JOB_IDS = ConcurrentHashMap.newKeySet();

    public static void addJobIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            RECEIPT_JOB_IDS.add(id);
        }
    }

    public static boolean isTrackedJob(String[] ids) {
        if (ids == null || ids.length == 0) return false;
        boolean tracked = false;
        for (String id : ids) {
            if (RECEIPT_JOB_IDS.remove(id)) {
                tracked = true;
            }
        }
        return tracked;
    }
}
