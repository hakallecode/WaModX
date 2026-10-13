package dev.soughtxp.wamodx.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import dev.soughtxp.wamodx.util.CacheUtils;
import dev.soughtxp.wamodx.util.PrefsUtils;
import dev.soughtxp.wamodx.util.ResUtils;
import com.whatsapp.ui.wds.components.list.listitem.WDSListItem;
import com.whatsapp.ui.wds.components.toggle.WDSSwitch;

import X.BaseActivity;

public class SecretActivity extends BaseActivity {

    private static final String KEY_CONTACTS = "contacts_privacy";
    private static final String KEY_GROUPS = "groups_privacy";
    private static final int PRIVACY_NORMAL = 0;
    private static final int PRIVACY_BLUE_REPLY = 2;
    private static final String GITHUB_URL = "https://github.com/SoughtXp/WaModX";

    private static final String KEY_FREEZE_LAST_SEEN = "freezeLastSeen";
    private static final String KEY_HIDE_COMPOSING = "hideComposing";
    private static final String KEY_ANTI_STATUS_VIEW = "antiStatusView";

    private WDSListItem contactsItem;
    private WDSListItem groupsItem;
    private View warningContainer;
    private WDSListItem githubItem;
    private WDSListItem clearCacheItem;

    private String[] privacyOptions;
    private String cachedCacheSize = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(ResUtils.STRING.get("title"));
        setupToolbar();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setContentView(ResUtils.LAYOUT.get("settings"));
        setupViews();
        setupNewListViewItems();
        setupSwitches();
        updateSubtitles();
        updateWarningVisibility();
        updateCacheSizeSubtitle();
    }

    private void setupSwitches() {
        setupSwitch("freeze_lastseen_row", "freeze_lastseen_switch", KEY_FREEZE_LAST_SEEN);
        setupSwitch("hide_composing_row", "hide_composing_switch", KEY_HIDE_COMPOSING);
        setupSwitch("anti_status_view_row", "anti_status_view_switch", KEY_ANTI_STATUS_VIEW);
    }

    private void setupSwitch(String rowId, String switchId, final String key) {
        final WDSSwitch toggle = findViewById(ResUtils.ID.get(switchId));
        View row = findViewById(ResUtils.ID.get(rowId));
        if (toggle == null || row == null) {
            return;
        }
        toggle.setChecked(PrefsUtils.PREFERENCES.getBoolean(key, false));
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean newValue = !toggle.isChecked();
                toggle.setChecked(newValue);
                PrefsUtils.PREFERENCES.setBoolean(key, newValue);
            }
        });
    }

    private void setupViews() {
        contactsItem = findViewById(ResUtils.ID.get("contacts_item"));
        groupsItem = findViewById(ResUtils.ID.get("groups_item"));
        warningContainer = findViewById(ResUtils.ID.get("caution_strip"));

        if (contactsItem != null) {
            contactsItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPrivacyDialog(true);
                }
            });
        }

        if (groupsItem != null) {
            groupsItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPrivacyDialog(false);
                }
            });
        }
    }

    private void setupNewListViewItems() {
        githubItem = findViewById(ResUtils.ID.get("github_item"));
        if (githubItem != null) {
            if (githubItem.getItemSubTextView() != null) {
                githubItem.getItemSubTextView().setText(ResUtils.STRING.get("github_subtitle"));
            }
            githubItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openGitHub();
                }
            });
        }

        clearCacheItem = findViewById(ResUtils.ID.get("clear_cache_item"));
        if (clearCacheItem != null) {
            clearCacheItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showClearCacheConfirmation();
                }
            });
        }
    }

    private void openGitHub() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(GITHUB_URL));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
        }
    }

    private void updateCacheSizeSubtitle() {
        CacheUtils.calculateCacheSize(this, new CacheUtils.CacheCallback() {
            @Override
            public void onSizeCalculated(long totalSize, String formattedSize) {
                cachedCacheSize = formattedSize;
                if (clearCacheItem != null && clearCacheItem.getItemSubTextView() != null) {
                    clearCacheItem.getItemSubTextView().setText(formattedSize);
                }
            }

            @Override
            public void onClearComplete(boolean success) {
            }
        });
    }

    private void showClearCacheConfirmation() {
        if (cachedCacheSize == null || cachedCacheSize.equals("0 B")) {
            Toast.makeText(this, ResUtils.STRING.get("clear_cache_nothing"), Toast.LENGTH_SHORT).show();
            return;
        }

        String message = ResUtils.STRING.get("clear_cache_confirm_message");
        message = message.replace("%s", cachedCacheSize);

        new AlertDialog.Builder(this)
                .setTitle(ResUtils.STRING.get("clear_cache_confirm_title"))
                .setMessage(message)
                .setPositiveButton(ResUtils.STRING.get("clear_cache_confirm_yes"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearCache();
                    }
                })
                .setNegativeButton(ResUtils.STRING.get("clear_cache_confirm_no"), null)
                .show();
    }

    private void clearCache() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(ResUtils.STRING.get("clear_cache_dialog_title"));
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        progressDialog.show();

        CacheUtils.clearCache(this, progressDialog, new CacheUtils.CacheCallback() {
            @Override
            public void onSizeCalculated(long totalSize, String formattedSize) {
            }

            @Override
            public void onClearComplete(boolean success) {
                if (success) {
                    Toast.makeText(SecretActivity.this, ResUtils.STRING.get("clear_cache_complete"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SecretActivity.this, ResUtils.STRING.get("clear_cache_error"), Toast.LENGTH_SHORT).show();
                }
                updateCacheSizeSubtitle();
            }
        });
    }

    private String[] getPrivacyOptions() {
        if (privacyOptions == null) {
            privacyOptions = new String[]{
                    ResUtils.STRING.get("privacy_option_normal"),
                    ResUtils.STRING.get("privacy_option_hide_ticks"),
                    ResUtils.STRING.get("privacy_option_show_after_reply")
            };
        }
        return privacyOptions;
    }

    private void showPrivacyDialog(boolean isContacts) {
        final String key = isContacts ? KEY_CONTACTS : KEY_GROUPS;
        int currentValue = PrefsUtils.PREFERENCES.getInt(key, PRIVACY_NORMAL);

        String title = isContacts
                ? ResUtils.STRING.get("contacts_privacy_title")
                : ResUtils.STRING.get("groups_privacy_title");

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setSingleChoiceItems(getPrivacyOptions(), currentValue, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PrefsUtils.PREFERENCES.setInt(key, which);
                        updateSubtitles();
                        updateWarningVisibility();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void updateSubtitles() {
        int contactsValue = PrefsUtils.PREFERENCES.getInt(KEY_CONTACTS, PRIVACY_NORMAL);
        int groupsValue = PrefsUtils.PREFERENCES.getInt(KEY_GROUPS, PRIVACY_NORMAL);

        String[] options = getPrivacyOptions();
        if (contactsItem != null && contactsItem.getItemSubTextView() != null) {
            contactsItem.getItemSubTextView().setText(options[contactsValue]);
        }
        if (groupsItem != null && groupsItem.getItemSubTextView() != null) {
            groupsItem.getItemSubTextView().setText(options[groupsValue]);
        }
    }

    private void updateWarningVisibility() {
        int contactsValue = PrefsUtils.PREFERENCES.getInt(KEY_CONTACTS, PRIVACY_NORMAL);
        int groupsValue = PrefsUtils.PREFERENCES.getInt(KEY_GROUPS, PRIVACY_NORMAL);

        boolean showWarning = contactsValue == PRIVACY_BLUE_REPLY || groupsValue == PRIVACY_BLUE_REPLY;
        if (warningContainer != null) {
            warningContainer.setVisibility(showWarning ? View.VISIBLE : View.GONE);
        }
    }
}
