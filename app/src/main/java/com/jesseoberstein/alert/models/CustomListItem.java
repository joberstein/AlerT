package com.jesseoberstein.alert.models;

import com.jesseoberstein.alert.R;

public class CustomListItem {
    private int icon;               // R.drawable
    private String primaryText;
    private String secondaryText;
    private String tertiaryText;
    private String info;
    private int chevron;           // R.drawable
    private boolean isDivider;

    private CustomListItem() {
        this.isDivider = false;
    }

    /************************
     * Builders
     ************************/

    /**
     * Build a new route list item.
     */
    public static CustomListItem buildRoutesListItem(int icon, String name, String alerts) {
        return buildRoutesListItem(new CustomListItem(), icon, name, alerts);
    }

    /**
     * Build a route list item with the given list item.
     */
    public static CustomListItem buildRoutesListItem(CustomListItem item, int icon, String name, String alerts) {
        String info = (!alerts.isEmpty() && Integer.parseInt(alerts) > 0) ? alerts : null;
        return item
                .withIcon(icon)
                .withPrimaryText(name)
                .withInfo(info)
                .withChevron(R.drawable.ic_chevron_right);
    }

    /**
     * Build a new alarm list item.
     */
    public static CustomListItem buildAlarmListItem(String nickname, String station, String direction, boolean isActive) {
        return buildAlarmListItem(new CustomListItem(), nickname, station, direction, isActive);
    }

    /**
     * Build an alarm list item with the given list item.
     */
    public static CustomListItem buildAlarmListItem(CustomListItem item, String nickname, String station, String direction, boolean isActive) {
        int icon = isActive ? R.drawable.circle_light_green : R.drawable.circle_light_gray;
        return item
                .withIcon(icon)
                .withPrimaryText(nickname)
                .withSecondaryText(station)
                .withTertiaryText(direction)
                .withChevron(R.drawable.ic_chevron_right);
    }

    /**
     * Build a simple list item with only primary text.
     */
    public static CustomListItem buildSimpleTextItem(String primaryText) {
        return new CustomListItem().withPrimaryText(primaryText);
    }

    public static CustomListItem makeDivider() {
        return new CustomListItem().asDivider();
    }

    private CustomListItem withIcon(int icon) {
        this.icon = icon;
        return this;
    }

    private CustomListItem withPrimaryText(String primaryText) {
        this.primaryText = primaryText;
        return this;
    }

    private CustomListItem withSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
        return this;
    }

    private CustomListItem withTertiaryText(String tertiaryText) {
        this.tertiaryText = tertiaryText;
        return this;
    }

    private CustomListItem withInfo(String info) {
        this.info = info;
        return this;
    }

    private CustomListItem withChevron(int chevron) {
        this.chevron = chevron;
        return this;
    }

    private CustomListItem asDivider() {
        this.isDivider = true;
        return this;
    }

    /************************
     * Getters
     ************************/
    public int getIcon() { return this.icon; }
    public String getPrimaryText() { return this.primaryText; }
    public String getSecondaryText() { return this.secondaryText; }
    public String getTertiaryText() { return this.tertiaryText; }
    public String getInfo() { return this.info; }
    public int getChevron() { return this.chevron; }
    public boolean isDivider() { return this.isDivider; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomListItem that = (CustomListItem) o;

        if (icon != that.icon) return false;
        if (chevron != that.chevron) return false;
        if (isDivider != that.isDivider) return false;
        if (primaryText != null ? !primaryText.equals(that.primaryText) : that.primaryText != null)
            return false;
        if (secondaryText != null ? !secondaryText.equals(that.secondaryText) : that.secondaryText != null)
            return false;
        if (tertiaryText != null ? !tertiaryText.equals(that.tertiaryText) : that.tertiaryText != null)
            return false;
        return info != null ? info.equals(that.info) : that.info == null;
    }

    @Override
    public int hashCode() {
        int result = icon;
        result = 31 * result + (primaryText != null ? primaryText.hashCode() : 0);
        result = 31 * result + (secondaryText != null ? secondaryText.hashCode() : 0);
        result = 31 * result + (tertiaryText != null ? tertiaryText.hashCode() : 0);
        result = 31 * result + (info != null ? info.hashCode() : 0);
        result = 31 * result + chevron;
        result = 31 * result + (isDivider ? 1 : 0);
        return result;
    }
}
