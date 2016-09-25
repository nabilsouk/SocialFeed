package com.internship.socialfeed.settings;

/**
 * Created by Nader on 25-Sep-16.
 */

public class SettingsItem {
    private String settingText;
    private int settingIcon;

    public SettingsItem(String settingText, int settingIcon) {
        this.settingText = settingText;
        this.settingIcon = settingIcon;
    }

    public String getSettingText() {
        return settingText;
    }

    public void setSettingText(String settingText) {
        this.settingText = settingText;
    }

    public int getSettingIcon() {
        return settingIcon;
    }

    public void setSettingIcon(int settingIcon) {
        this.settingIcon = settingIcon;
    }
}
