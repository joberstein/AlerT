package com.jesseoberstein.alert.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.listeners.ToggleColorOnClick;
import com.jesseoberstein.alert.models.CustomListItem;
import com.jesseoberstein.alert.utils.ListProp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static com.jesseoberstein.alert.utils.AlertUtils.listsToMap;
import static com.jesseoberstein.alert.utils.Constants.CUSTOM_LIST_IDS;
import static com.jesseoberstein.alert.utils.Constants.CUSTOM_LIST_PROPS;

public class ButtonListAdapter extends ArrayAdapter<String> {
    private final LayoutInflater inflater;
    private final Context context;
    private final int themeColor;

    public ButtonListAdapter(Context context, int view, int themeColor, Collection<String> itemList) {
        super(context, view, new ArrayList<>(itemList));
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.themeColor = themeColor;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        String item = getItem(position);
        final ToggleButton button = (ToggleButton) this.inflater.inflate(R.layout.button_direction, parent, false);
        button.setOnClickListener(new ToggleColorOnClick(themeColor, R.color.white, context));
        button.setText(item);
        button.setTextOn(item);
        button.setTextOff(item);
        button.performClick();  // Use two clicks to force the button's inactive colors.
        button.performClick();
        return button;
    }
}