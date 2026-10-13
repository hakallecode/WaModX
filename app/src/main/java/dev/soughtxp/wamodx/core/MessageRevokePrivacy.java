package dev.soughtxp.wamodx.core;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;

import dev.soughtxp.wamodx.util.PrefsUtils;

import X.FMessage;
import X.RevokeMessage;

public class MessageRevokePrivacy {

    public static boolean antiRevoke(Object obj, TextView textView) {
        if (textView == null) {
            FMessage message = (FMessage) obj;

            if (message.key.isFromMe) {
                return false;
            }

            if (obj instanceof RevokeMessage) {
                PrefsUtils.REVOKED_IDS.setLong(((RevokeMessage) obj).revokedMessageId, System.currentTimeMillis());
            }

            return true;
        }

        FMessage message = (FMessage) obj;
        final String id = message.key.messageId;

        textView.post(new Runnable() {
            @Override
            public void run() {
                if (PrefsUtils.REVOKED_IDS.getAll().containsKey(id)) {
                    GradientDrawable indicator = new GradientDrawable();
                    indicator.setShape(GradientDrawable.OVAL);
                    indicator.setColor(Color.RED);
                    indicator.setBounds(0, 0, 20, 20);

                    textView.setCompoundDrawables(indicator, null, null, null);
                    textView.setCompoundDrawablePadding(8);
                    textView.postInvalidate();
                    textView.requestLayout();
                } else {
                    textView.setCompoundDrawables(null, null, null, null);
                }
            }
        });

        return false;
    }
}
