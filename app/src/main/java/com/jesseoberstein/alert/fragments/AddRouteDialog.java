package com.jesseoberstein.alert.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.interfaces.OnAddRouteDialogClick;

import static com.jesseoberstein.alert.activities.routes.SearchRoutes.COLUMN_ROUTE;

public class AddRouteDialog extends DialogFragment {
    OnAddRouteDialogClick clickListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            clickListener = (OnAddRouteDialogClick) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnAddRouteDialogClick");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle selectedRoute = getArguments();
        String routeName = selectedRoute.getString(COLUMN_ROUTE);

        return new AlertDialog.Builder(getActivity())
                .setMessage(getResources().getString(R.string.add_route_dialog, routeName))
                .setPositiveButton(R.string.add, (dialog, id) -> clickListener.onAddSelectedRoute(selectedRoute))
                .setNegativeButton(R.string.cancel, (dialog, id) -> clickListener.onCancelSelectedRoute(selectedRoute))
                .create();
    }
}
