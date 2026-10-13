package dev.soughtxp.wamodx.core;


import X.FMessage;
import X.SendE2EMessageJob;
import X.SendReadReceiptJob;
import X.WaJobManager;

public class ReadReceiptPrivacy {

    private static volatile WaJobManager jobManager;

    public static boolean onReceiptJob(Object job) {
        if (job == null) return false;
        try {
            SendReadReceiptJob receiptJob = (SendReadReceiptJob) job;
            
            if (receiptJob.jid != null && receiptJob.jid.contains("status@broadcast")) {
                boolean hide = StatusViewPrivacy.hideStatus(receiptJob.jid, receiptJob.messageIds);
                if (hide) {
                    StatusViewPrivacy.onJob(receiptJob);
                }
                return hide;
            }
            
            return BlueTickManager.onReceiptJob(job);
        } catch (Throwable t) {
            return false;
        }
    }

    public static boolean onReadReceipt(Object owner, Object messageObject) {
        if (messageObject == null) return false;
        try {
            FMessage message = (FMessage) messageObject;
            if (message.key == null || message.key.remoteJid == null) return false;

            String jid = message.key.remoteJid.getRawString();
            if (jid != null && jid.contains("status@broadcast")) {
                String[] ids = new String[]{message.key.messageId};
                String participant = null;
                if (message.getSenderUserJid() != null) {
                    participant = message.getSenderUserJid().getRawString();
                }
                return StatusViewPrivacy.hideStatus(messageObject, jid, participant, ids);
            }
            
            return BlueTickManager.onReadReceipt(owner, messageObject);
        } catch (Throwable t) {
            return false;
        }
    }

    public static void onReply(Object sendJob) {
        if (sendJob == null) return;
        try {
            SendE2EMessageJob messageJob = (SendE2EMessageJob) sendJob;
            if (jobManager == null && messageJob.jobRunner != null) {
                jobManager = messageJob.jobRunner.jobManager;
            }
            BlueTickManager.onUserReply(sendJob);
        } catch (Throwable t) {
        }
    }

    public static boolean sendStatusReceipt(Object statusObject) {
        return StatusViewPrivacy.sendReceipt(statusObject, jobManager);
    }
}
