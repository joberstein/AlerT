package com.jesseoberstein.alert.listeners.routes;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.jesseoberstein.alert.fragments.dialog.route.RemoveRouteDialog;
import com.jesseoberstein.alert.models.CustomListItem;

import static android.widget.AdapterView.OnItemLongClickListener;
import static com.jesseoberstein.alert.activities.routes.SearchRoutes.COLUMN_ROUTE;

public class RemoveRouteOnLongClick implements OnItemLongClickListener {
    private Activity activity;

    public RemoveRouteOnLongClick(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        CustomListItem item = (CustomListItem) parent.getItemAtPosition(position);
        Bundle bundle = new Bundle();
        bundle.putString(COLUMN_ROUTE, item.getPrimaryText());

        DialogFragment dialog = new RemoveRouteDialog();
        dialog.setArguments(bundle);
        dialog.show(this.activity.getFragmentManager(), "RemoveRouteDialog");
        return true;
    }
}
