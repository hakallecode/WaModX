package X;

/**
 * The job that sends an outgoing end-to-end message. Its onRun is where we
 * detect that the user replied to a chat, reading the target jid.
 * 
 * Real class: {@code com.whatsapp.jobqueue.job.SendE2EMessageJob}
 */
public class SendE2EMessageJob {

    public String jid; // jid

    public JobRunner jobRunner; // A17 (LX/1Ck)
}
