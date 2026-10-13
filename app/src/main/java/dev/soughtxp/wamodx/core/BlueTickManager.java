package dev.soughtxp.wamodx.core;

import dev.soughtxp.wamodx.util.PrefsUtils;

import X.FMessage;
import X.ReadReceiptManager;
import X.SendE2EMessageJob;
import X.SendReadReceiptJob;
import X.WaJobManager;

import com.whatsapp.infra.core.jid.UserJid;

public class BlueTickManager {

    private static final int MODE_NORMAL = 0;
    private static final int MODE_HIDE = 1;
    private static final int MODE_AFTER_REPLY = 2;

    private static volatile WaJobManager jobManager;

    public static boolean onReceiptJob(Object job) {
        if (job == null) return false;
        try {
            SendReadReceiptJob receiptJob = (SendReadReceiptJob) job;
            String jid = receiptJob.jid;
            
            if (jid != null && jid.contains("status@broadcast")) {
                return false;
            }
            
            return hideReceipt(jid, receiptJob.participant, receiptJob.messageIds);
        } catch (Throwable t) {
            return false;
        }
    }

    public static boolean onReadReceipt(Object owner, Object messageObject) {
        obtainJobManager(owner);
        if (messageObject == null) return false;
        try {
            FMessage message = (FMessage) messageObject;
            if (message.key == null || message.key.remoteJid == null) return false;

            String jid = message.key.remoteJid.getRawString();
            String[] ids = new String[]{message.key.messageId};

            if (jid != null && jid.contains("status@broadcast")) {
                return false;
            }

            String participant = null;
            if (jid != null && jid.contains("@g.us")) {
                UserJid sender = message.getSenderUserJid();
                if (sender != null) {
                    participant = sender.getRawString();
                }
            }
            
            return hideReceipt(jid, participant, ids);
        } catch (Throwable t) {
            return false;
        }
    }

    public static void onUserReply(Object sendJob) {
        if (sendJob == null) return;
        try {
            SendE2EMessageJob messageJob = (SendE2EMessageJob) sendJob;
            if (jobManager == null && messageJob.jobRunner != null) {
                jobManager = messageJob.jobRunner.jobManager;
            }
            onReply(messageJob.jid);
        } catch (Throwable t) {
        }
    }

    private static void obtainJobManager(Object owner) {
        if (jobManager != null || !(owner instanceof ReadReceiptManager)) return;
        try {
            Object resolved = ((ReadReceiptManager) owner).jobManagerProvider.holder.get();
            if (resolved instanceof WaJobManager) {
                jobManager = (WaJobManager) resolved;
            }
        } catch (Throwable t) {
        }
    }

    private static boolean hideReceipt(String jid, String participant, String[] messageIds) {
        if (jid == null) return false;

        if (ReceiptJobFilter.isTrackedJob(messageIds)) return false;

        int mode = getPrivacyMode(jid);
        switch (mode) {
            case MODE_HIDE:
                return true;
            case MODE_AFTER_REPLY:
                ReceiptQueue.holdReceipts(jid, participant, messageIds);
                return true;
            default:
                return false;
        }
    }

    private static void onReply(String jid) {
        if (jid == null || getPrivacyMode(jid) != MODE_AFTER_REPLY) return;

        final String chatJid = jid;
        new Thread(new Runnable() {
            @Override
            public void run() {
                ReceiptQueue.flushReceipts(chatJid, jobManager);
            }
        }).start();
    }

    private static int getPrivacyMode(String jid) {
        try {
            if (jid.contains("broadcast") || jid.contains("@newsletter")) {
                return MODE_NORMAL;
            }
            String prefKey = jid.contains("@g.us") ? "groups_privacy" : "contacts_privacy";
            return PrefsUtils.PREFERENCES.getInt(prefKey, MODE_NORMAL);
        } catch (Throwable t) {
            return MODE_NORMAL;
        }
    }
}
