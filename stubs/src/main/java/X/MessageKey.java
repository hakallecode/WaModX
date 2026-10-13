package X;

/**
 * Identifies a message: its id, whether we sent it, and the chat it belongs to.
 * 
 * Real class: {@code LX/1UM}
 */
public class MessageKey { // LX/1UM

    public String messageId; // A01

    public boolean isFromMe; // A02

    public ChatJid remoteJid; // A00
}
