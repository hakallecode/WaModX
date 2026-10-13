package X;

/**
 * A "message revoked" (deleted-for-everyone) protocol message.
 * Exposes the id of the message that was revoked, which AntiRevoke
 * persists so the message can still be shown.
 * 
 * Real class: {@code LX/1Vk}
 */
public class RevokeMessage extends FMessage { // LX/1Vk

    public String revokedMessageId; // A01
}
