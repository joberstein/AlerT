package com.jesseoberstein.alert.models;

import com.jesseoberstein.alert.R;

public class CustomListItem {
    private int icon;  // R.drawable
    private String name;  // R.string
    private String info;  // String
    private int chevron;  // R.drawable

    /************************
     * Builders
     ************************/
    public static CustomListItem buildRoutesListItem(int icon, String name, String alerts) {
        return buildRoutesListItem(new CustomListItem(), icon, name, alerts);
    }

    public static CustomListItem buildRoutesListItem(CustomListItem item, int icon, String name, String alerts) {
        item.withIcon(icon)
                .withName(name)
                .withChevron(R.drawable.ic_chevron_right);

        alerts = (!alerts.isEmpty() && Integer.parseInt(alerts) > 0) ? alerts : null;
        return item.withInfo(alerts);
    }

    private CustomListItem withIcon(int icon) {
        this.icon = icon;
        return this;
    }

    private CustomListItem withName(String name) {
        this.name = name;
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

    /************************
     * Getters
     ************************/
    public int getIcon() { return this.icon; }
    public String getName() { return this.name; }
    public String getInfo() { return this.info; }
    public int getChevron() { return this.chevron; }
}
