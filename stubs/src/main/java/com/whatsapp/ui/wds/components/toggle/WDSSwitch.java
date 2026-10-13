package com.whatsapp.ui.wds.components.toggle;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;

/**
 * A WhatsApp Design System on/off switch, used by the secret settings screen.
 */
public class WDSSwitch extends CompoundButton {

    public WDSSwitch(Context context) {
        super(context);
    }

    public WDSSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WDSSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isChecked() {
        return false;
    }

    @Override
    public void setChecked(boolean checked) {
    }

    @Override
    public void toggle() {
    }
}
