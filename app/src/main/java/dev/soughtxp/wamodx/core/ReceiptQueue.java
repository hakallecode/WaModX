package dev.soughtxp.wamodx.core;

import dev.soughtxp.wamodx.util.PrefsUtils;

import java.util.Map;
import java.util.Set;

import X.ChatJid;
import X.SendReadReceiptJob;
import X.WaJobManager;

public class ReceiptQueue {

    public static void holdReceipts(String jid, String participant, String[] messageIds) {
        if (jid == null || messageIds == null || messageIds.length == 0) return;
        
        String key = buildChatKey(jid);
        for (String id : messageIds) {
            PrefsUtils.TICKS.addPending(key, participant, id);
        }
    }

    public static void flushReceipts(String jid, WaJobManager manager) {
        if (manager == null || jid == null) return;

        String key = buildChatKey(jid);
        Map<String, Set<String>> pending = PrefsUtils.TICKS.loadPending(key);
        if (pending.isEmpty()) return;

        for (Map.Entry<String, Set<String>> group : pending.entrySet()) {
            enqueueJob(manager, jid, group.getKey(), group.getValue());
        }
        PrefsUtils.TICKS.clearPending(key);
    }

    private static boolean enqueueJob(WaJobManager manager, String jid, String participant, Set<String> idSet) {
        try {
            ChatJid chat = ChatJid.PARSER.parse(jid);
            if (chat == null) return false;

            ChatJid participantJid = null;
            if (participant != null && !participant.isEmpty()) {
                participantJid = ChatJid.PARSER.parse(participant);
            }

            String[] messageIds = idSet.toArray(new String[0]);
            ReceiptJobFilter.addJobIds(messageIds);

            SendReadReceiptJob job = new SendReadReceiptJob(
                    chat, participantJid, null, null, null,
                    messageIds, 0L, 0L, 0L, false);
            manager.enqueue(job);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    private static String buildChatKey(String jid) {
        int end = jid.indexOf('@');
        if (end < 0) end = jid.length();
        int dot = jid.indexOf('.');
        if (dot >= 0 && dot < end) end = dot;
        return jid.substring(0, end);
    }
}
