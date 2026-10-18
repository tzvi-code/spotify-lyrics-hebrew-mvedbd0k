package com.generated.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {

    private Button btnBack;
    private RadioGroup rgFontSize;
    private RadioButton rbFontSmall;
    private RadioButton rbFontMedium;
    private RadioButton rbFontLarge;
    private CheckBox cbShowOriginal;
    private CheckBox cbAutoscroll;
    private Button btnOpenNotifSettings;
    private Button btnOpenOverlaySettings;
    private TextView tvNotifStatus;
    private TextView tvOverlayStatus;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("lyrics_prefs", MODE_PRIVATE);

        bindViews();
        loadSettings();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePermissionStatuses();
    }

    private void bindViews() {
        btnBack = findViewById(R.id.btn_back);
        rgFontSize = findViewById(R.id.rg_font_size);
        rbFontSmall = findViewById(R.id.rb_font_small);
        rbFontMedium = findViewById(R.id.rb_font_medium);
        rbFontLarge = findViewById(R.id.rb_font_large);
        cbShowOriginal = findViewById(R.id.cb_show_original);
        cbAutoscroll = findViewById(R.id.cb_autoscroll);
        btnOpenNotifSettings = findViewById(R.id.btn_open_notif_settings);
        btnOpenOverlaySettings = findViewById(R.id.btn_open_overlay_settings);
        tvNotifStatus = findViewById(R.id.tv_notif_status);
        tvOverlayStatus = findViewById(R.id.tv_overlay_status);
    }

    private void loadSettings() {
        float size = prefs.getFloat("font_size", 18f);
        if (size <= 14.5f) {
            rbFontSmall.setChecked(true);
        } else if (size >= 21.5f) {
            rbFontLarge.setChecked(true);
        } else {
            rbFontMedium.setChecked(true);
        }

        cbShowOriginal.setChecked(prefs.getBoolean("show_original", false));
        cbAutoscroll.setChecked(prefs.getBoolean("autoscroll", true));
    }

    private void setupListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rgFontSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                float newSize = 18f;
                if (checkedId == R.id.rb_font_small) {
                    newSize = 14f;
                } else if (checkedId == R.id.rb_font_large) {
                    newSize = 22f;
                }
                prefs.edit().putFloat("font_size", newSize).apply();
            }
        });

        cbShowOriginal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.edit().putBoolean("show_original", isChecked).apply();
            }
        });

        cbAutoscroll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.edit().putBoolean("autoscroll", isChecked).apply();
            }
        });

        btnOpenNotifSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
            }
        });

        btnOpenOverlaySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                } else {
                    Toast.makeText(SettingsActivity.this, "הרשאה זו מאושרת אוטומטית במכשירך", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updatePermissionStatuses() {
        boolean hasNotif = SpotifyNotificationListenerService.isNotificationAccessEnabled(this);
        tvNotifStatus.setText(hasNotif ? "הרשאה מאושרת ומאזינה למדיה ✓" : "הרשאה אינה מאושרת (נחוצה לזיהוי)");
        tvNotifStatus.setTextColor(hasNotif ? 0xFF1DB954 : 0xFFE57373);

        boolean hasOverlay = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasOverlay = Settings.canDrawOverlays(this);
        }
        tvOverlayStatus.setText(hasOverlay ? "הרשאה מאושרת ✓" : "הרשאה חסרה לחלון צף");
        tvOverlayStatus.setTextColor(hasOverlay ? 0xFF1DB954 : 0xFFE57373);
    }
}
