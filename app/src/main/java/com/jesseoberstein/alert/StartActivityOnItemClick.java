package com.jesseoberstein.alert;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.jesseoberstein.alert.models.CustomListItem;

import java.util.ArrayList;
import java.util.Optional;

import static java.util.Arrays.*;

public class StartActivityOnItemClick implements OnItemClickListener {
    private Activity origin;
    private Class<?> destination;
    private Bundle extras;
    private String action;

    public StartActivityOnItemClick(Activity origin, Class<?> destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public StartActivityOnItemClick withAction(String action) {
        this.action = action;
        return this;
    }

    public StartActivityOnItemClick withBundle(Bundle extras) {
        this.extras = extras;
        return this;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this.origin, this.destination);
        CustomListItem item = (CustomListItem) parent.getItemAtPosition(position);
        Optional.ofNullable(this.action).ifPresent(intent::setAction);

        this.extras = Optional.ofNullable(this.extras).orElseGet(Bundle::new);
        this.extras.putStringArrayList("routeInfo", new ArrayList<>(asList(item.getPrimaryText())));
        this.origin.startActivity(intent);
    }
}
