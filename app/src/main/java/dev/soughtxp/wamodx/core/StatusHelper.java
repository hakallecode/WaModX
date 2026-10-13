package dev.soughtxp.wamodx.core;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import dev.soughtxp.wamodx.App;
import dev.soughtxp.wamodx.util.PrefsUtils;
import dev.soughtxp.wamodx.util.ResUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import X.FMessage;
import X.FMessageMedia;
import X.MediaStatusData;
import X.StatusData;
import X.TextStatusData;

public class StatusHelper {

    public static boolean isMediaStatus(Object obj) {
        if (obj == null) return false;
        return (obj instanceof StatusData) && !(obj instanceof TextStatusData);
    }

    public static String getStatusText(Object obj) {
        if (obj instanceof TextStatusData) {
            return ((TextStatusData) obj).getText();
        }
        return null;
    }

    public static void copyToClipboard(String text) {
        Context context = App.getContext();
        if (context == null) return;
        if (text == null) return;

        Object service = context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (service instanceof ClipboardManager) {
            ClipboardManager clipboard = (ClipboardManager) service;
            String label = ResUtils.STRING.get("status_text_label");
            String clipText = label + text;
            clipboard.setPrimaryClip(ClipData.newPlainText(label, clipText));
            String msg = ResUtils.STRING.get("text_copied");
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void toast(String message) {
        Context context = App.getContext();
        if (context == null) return;
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static File resolveMediaFile(Object obj) {
        if (!(obj instanceof MediaStatusData)) return null;
        MediaStatusData statusData = (MediaStatusData) obj;
        FMessageMedia mediaInfo = statusData.getMediaInfo();
        return mediaInfo != null ? mediaInfo.mediaFile : null;
    }

    public static boolean copyFile(File source, File dest) {
        try (InputStream input = new FileInputStream(source);
             OutputStream output = new FileOutputStream(dest)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = input.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void downloadMedia(Object obj) {
        if (obj == null) {
            toast((String) ResUtils.STRING.get("error_status_null"));
            return;
        }

        File source = resolveMediaFile(obj);
        if (source == null) {
            toast((String) ResUtils.STRING.get("error_resolve_media"));
            return;
        }

        if (!source.exists()) {
            toast(String.format((String) ResUtils.STRING.get("error_source_not_found"), source.getAbsolutePath()));
            return;
        }

        Context context = App.getContext();
        if (context == null) {
            toast((String) ResUtils.STRING.get("error_no_context"));
            return;
        }

        File externalFilesDir = context.getExternalFilesDir(null);
        File base = null;
        if (externalFilesDir != null && externalFilesDir.getParentFile() != null
                && externalFilesDir.getParentFile().getParentFile() != null) {
            base = externalFilesDir.getParentFile().getParentFile().getParentFile();
        }
        File destDir = new File(base, "media/" + context.getPackageName() + "/WaModX");

        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        File dest = new File(destDir, source.getName());
        if (copyFile(source, dest)) {
            toast(String.format((String) ResUtils.STRING.get("saved_to"), dest.getAbsolutePath()));
        } else {
            toast((String) ResUtils.STRING.get("error_copy_failed"));
        }
    }

    public static void setupStatusActionDelayed(final Object obj, final TextView textView) {
        if (obj == null || textView == null) return;

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                applyStatusActionIcon(obj, textView);
            }
        });
    }

    private static boolean isRevoked(Object obj) {
        try {
            FMessage message = (FMessage) obj;
            if (message.key != null && message.key.messageId != null) {
                return PrefsUtils.REVOKED_IDS.getAll().containsKey(message.key.messageId);
            }
        } catch (Throwable t) {
        }
        return false;
    }

    public static void applyStatusActionIcon(final Object obj, final TextView textView) {
        if (obj == null || textView == null) return;

        final boolean showEye = !isRevoked(obj)
                && PrefsUtils.PREFERENCES.getBoolean("antiStatusView", false);
        final boolean isMedia = isMediaStatus(obj);

        Drawable actionIcon = isMedia ? new StatusDrawables.DownloadDrawable() : new StatusDrawables.CopyDrawable();
        actionIcon.setBounds(0, 0, actionIcon.getIntrinsicWidth(), actionIcon.getIntrinsicHeight());

        Drawable[] existing = textView.getCompoundDrawables();

        final Drawable seenIcon;
        if (showEye) {
            seenIcon = new StatusDrawables.SeenDrawable();
            seenIcon.setBounds(0, 0, seenIcon.getIntrinsicWidth(), seenIcon.getIntrinsicHeight());
        } else {
            seenIcon = existing[0];
        }

        textView.setCompoundDrawables(seenIcon, existing[1], actionIcon, existing[3]);
        textView.setCompoundDrawablePadding(8);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isMedia) {
                        downloadMedia(obj);
                    } else {
                        String text = getStatusText(obj);
                        if (text != null) {
                            copyToClipboard(text);
                        }
                    }
                } catch (Throwable t) {
                }
            }
        });

        if (showEye) {
            textView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    try {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            int region = textView.getPaddingLeft()
                                    + seenIcon.getIntrinsicWidth()
                                    + textView.getCompoundDrawablePadding();
                            if (event.getX() <= region) {
                                markStatusAsSeen(obj);
                                return true;
                            }
                        }
                    } catch (Throwable t) {
                    }
                    return false;
                }
            });
        }

        textView.setClickable(true);
    }

    public static void markStatusAsSeen(Object statusObject) {
        try {
            if (ReadReceiptPrivacy.sendStatusReceipt(statusObject)) {
                toast((String) ResUtils.STRING.get("status_marked_seen"));
            }
        } catch (Throwable t) {
        }
    }
}
