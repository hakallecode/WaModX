package dev.soughtxp.wamodx.core;

import dev.soughtxp.wamodx.util.PrefsUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

import X.ChatJid;
import X.SendReadReceiptJob;
import X.WaJobManager;

public class StatusViewPrivacy {

    private static final Set<String> REPLAY_IDS = ConcurrentHashMap.newKeySet();
    
    private static final Map<Object, String[]> MESSAGE_IDS = Collections.synchronizedMap(new WeakHashMap<Object, String[]>());
    private static final Map<Object, String> JIDS = Collections.synchronizedMap(new WeakHashMap<Object, String>());
    private static final Map<Object, String> PARTICIPANTS = Collections.synchronizedMap(new WeakHashMap<Object, String>());

    private static volatile String lastJid;
    private static volatile String lastParticipant;
    private static volatile String[] lastIds;
    private static volatile SendReadReceiptJob lastJob;

    public static boolean hideStatus(String jid, String[] messageIds) {
        if (jid == null || !jid.contains("status@broadcast")) return false;

        if (isReplayedReceipt(messageIds)) return false;

        boolean hide = PrefsUtils.PREFERENCES.getBoolean("antiStatusView", false);
        if (hide) {
            lastJid = jid;
            lastParticipant = null;
            lastIds = messageIds;
            return true;
        }
        return false;
    }

    public static boolean hideStatus(Object statusObject, String jid, String participant, String[] messageIds) {
        boolean result = hideStatus(jid, messageIds);
        
        if (result && statusObject != null) {
            synchronized (MESSAGE_IDS) {
                MESSAGE_IDS.put(statusObject, messageIds);
                JIDS.put(statusObject, jid);
                if (participant != null) {
                    PARTICIPANTS.put(statusObject, participant);
                }
            }
        }
        
        return result;
    }

    public static void onJob(SendReadReceiptJob job) {
        if (job == null) return;
        lastJob = job;
        lastJid = job.jid;
        lastParticipant = job.participant;
        lastIds = job.messageIds;
    }

    public static boolean sendReceipt(Object statusObject, WaJobManager manager) {
        if (statusObject == null || manager == null) return false;

        try {
            String[] ids;
            String jid;
            String participant;
            
            synchronized (MESSAGE_IDS) {
                ids = MESSAGE_IDS.get(statusObject);
                jid = JIDS.get(statusObject);
                participant = PARTICIPANTS.get(statusObject);
            }
            
            if (ids == null || ids.length == 0) {
                return fallbackSend(manager);
            }

            REPLAY_IDS.addAll(Arrays.asList(ids));

            if (jid == null) {
                jid = lastJid;
                participant = lastParticipant;
            }
            
            if (jid == null) return false;
            
            boolean result = enqueueJob(manager, jid, participant, new LinkedHashSet<>(Arrays.asList(ids)));
            
            if (result) {
                synchronized (MESSAGE_IDS) {
                    MESSAGE_IDS.remove(statusObject);
                    JIDS.remove(statusObject);
                    PARTICIPANTS.remove(statusObject);
                }
            }
            
            return result;
        } catch (Throwable t) {
            return false;
        }
    }

    private static boolean fallbackSend(WaJobManager manager) {
        if (manager == null) return false;

        String[] ids = lastIds;
        if (ids == null || ids.length == 0) return false;

        REPLAY_IDS.addAll(Arrays.asList(ids));

        SendReadReceiptJob job = lastJob;
        if (job != null) {
            manager.enqueue(job);
            return true;
        }

        String jid = lastJid;
        if (jid == null) return false;
        return enqueueJob(manager, jid, lastParticipant, new LinkedHashSet<>(Arrays.asList(ids)));
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
            REPLAY_IDS.addAll(idSet);

            SendReadReceiptJob job = new SendReadReceiptJob(
                    chat, participantJid, null, null, "status",
                    messageIds, 0L, 0L, 0L, false);
            manager.enqueue(job);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    private static boolean isReplayedReceipt(String[] ids) {
        if (ids == null || ids.length == 0) return false;
        boolean hit = false;
        for (String id : ids) {
            if (REPLAY_IDS.remove(id)) {
                hit = true;
            }
        }
        return hit;
    }
}
