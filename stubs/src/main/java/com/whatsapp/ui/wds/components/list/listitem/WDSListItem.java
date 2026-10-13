package com.whatsapp.ui.wds.components.list.listitem;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.whatsapp.ui.coreui.base.WaTextView;

/**
 * A WhatsApp Design System list row (title + optional subtitle). Used by the
 * secret settings screen; the subtitle view holds the current privacy mode text.
 */
public class WDSListItem extends LinearLayout {

    public WaTextView subTextView; // A08

    public WDSListItem(Context context) {
        super(context);
    }

    public WDSListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WDSListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WaTextView getItemSubTextView() {
        return subTextView;
    }
}
