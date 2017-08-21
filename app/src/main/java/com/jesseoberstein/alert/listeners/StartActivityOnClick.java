package com.jesseoberstein.alert.listeners;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.jesseoberstein.alert.models.CustomListItem;
import com.jesseoberstein.alert.models.UserRoute;

import java.util.Optional;

import static android.view.View.OnClickListener;
import static com.jesseoberstein.alert.utils.Constants.COLOR;
import static com.jesseoberstein.alert.utils.Constants.ROUTE;
import static com.jesseoberstein.alert.utils.Constants.THEME;

public class StartActivityOnClick implements OnClickListener, OnItemClickListener {
    private Activity origin;
    private Class<?> destination;
    private Bundle extras;
    private String action;
    private int requestCode;

    public StartActivityOnClick(Activity origin, Class<?> destination) {
        this.origin = origin;
        this.destination = destination;
        this.requestCode = -1;
    }

    public StartActivityOnClick withAction(String action) {
        this.action = action;
        return this;
    }

    public StartActivityOnClick withBundle(Bundle extras) {
        this.extras = extras;
        return this;
    }

    public StartActivityOnClick withRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    @Override
    public void onClick(View view) {
        this.startActivity();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (this.extras == null) {
            this.extras = new Bundle();
        }

        switch (this.origin.getClass().getSimpleName()) {
            case "ViewRoutes":
                CustomListItem selected = (CustomListItem) parent.getItemAtPosition(position);
                UserRoute userRoute = new UserRoute(selected.getPrimaryText());
                this.extras.putString(ROUTE, userRoute.getRouteName());
                this.extras.putInt(COLOR, userRoute.getColor());
                this.extras.putInt(THEME, userRoute.getTheme());
                break;
        }

        this.startActivity();
    }

    private void startActivity() {
        Intent intent = new Intent(this.origin, this.destination);
        Optional.ofNullable(this.extras).ifPresent(intent::putExtras);
        Optional.ofNullable(this.action).ifPresent(intent::setAction);
        this.origin.startActivityForResult(intent, this.requestCode);
    }
}
