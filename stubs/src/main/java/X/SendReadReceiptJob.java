package X;

import com.whatsapp.infra.core.jid.DeviceJid;

/**
 * The job that sends a read receipt ("blue ticks") to a chat.
 * BlueTicks reads its fields at the hide gate and builds a fresh instance
 * to replay the receipt when the user replies.
 * 
 * Real class: {@code com.whatsapp.messaging.receipts.jobqueue.job.SendReadReceiptJob}
 */
public class SendReadReceiptJob {

    public String jid; // jid

    public String participant; // participant

    public String[] messageIds; // messageIds

    public SendReadReceiptJob(ChatJid jid,
                              ChatJid participant,
                              ChatJid recipient,
                              DeviceJid remoteSender,
                              String receiptClass,
                              String[] messageIds,
                              long originalMessageTimestamp,
                              long loggableStanzaId,
                              long messageServerStoreTimeMicros,
                              boolean shouldForceReadSelfReceipt) {
        this.jid = jid != null ? jid.getRawString() : null;
        this.participant = participant != null ? participant.getRawString() : null;
        this.messageIds = messageIds;
    }
}
