package com.jesseoberstein.alert.models;

import com.jesseoberstein.alert.R;

public class CustomListItem {
    private Object id;
    private int icon;               // R.drawable
    private String primaryText;
    private String secondaryText;
    private String tertiaryText;
    private String info;
    private int chevron;           // R.drawable
    private boolean isDivider;

    CustomListItem() {
        this.isDivider = false;
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

    CustomListItem withId(Object id) {
        this.id = id;
        return this;
    }

    CustomListItem withIcon(int icon) {
        this.icon = icon;
        return this;
    }

    CustomListItem withPrimaryText(String primaryText) {
        this.primaryText = primaryText;
        return this;
    }

    CustomListItem withSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
        return this;
    }

    CustomListItem withTertiaryText(String tertiaryText) {
        this.tertiaryText = tertiaryText;
        return this;
    }

    CustomListItem withInfo(String info) {
        this.info = info;
        return this;
    }

    CustomListItem withChevron(int chevron) {
        this.chevron = chevron;
        return this;
    }

    CustomListItem asDivider() {
        this.isDivider = true;
        return this;
    }

    /************************
     * Getters
     ************************/
    public Object getId() { return this.id; }
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
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
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
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + icon;
        result = 31 * result + (primaryText != null ? primaryText.hashCode() : 0);
        result = 31 * result + (secondaryText != null ? secondaryText.hashCode() : 0);
        result = 31 * result + (tertiaryText != null ? tertiaryText.hashCode() : 0);
        result = 31 * result + (info != null ? info.hashCode() : 0);
        result = 31 * result + chevron;
        result = 31 * result + (isDivider ? 1 : 0);
        return result;
    }
}
