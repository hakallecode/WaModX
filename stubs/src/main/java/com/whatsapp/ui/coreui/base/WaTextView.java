package com.whatsapp.ui.coreui.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * WhatsApp's themed TextView subclass. Used as the subtitle view inside a
 * {@link com.whatsapp.ui.wds.components.list.listitem.WDSListItem}.
 */
public class WaTextView extends TextView {

    public WaTextView(Context context) {
        super(context);
    }

    public WaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
