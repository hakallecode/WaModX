package com.whatsapp.infra.core.jid;

import android.os.Parcelable;

/**
 * Base type for every WhatsApp jid (user, group, device...). Exposes the raw
 * jid string used as the storage key and for building read receipts.
 */
public abstract class Jid implements Parcelable, Comparable<Jid> {

    public abstract String getUser();

    public abstract String getRawString();

    public abstract String getServer();
}
