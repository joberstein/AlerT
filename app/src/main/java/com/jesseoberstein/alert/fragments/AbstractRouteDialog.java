package com.jesseoberstein.alert.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.interfaces.OnRouteDialogClick;

import static android.app.AlertDialog.Builder;
import static com.jesseoberstein.alert.activities.routes.SearchRoutes.COLUMN_ROUTE;

public abstract class AbstractRouteDialog extends DialogFragment {
    OnRouteDialogClick clickListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            clickListener = (OnRouteDialogClick) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnRouteDialogClick");
        }
    }

    Builder createRouteDialogBuilder(Bundle savedInstanceState) {
        return new Builder(getActivity())
                .setNegativeButton(R.string.cancel, (dialog, id) ->
                        clickListener.onCancelSelectedRoute(getSelectedRoute()));
    }

    Bundle getSelectedRoute() {
        return getArguments();
    }

    String getRouteName() {
        return getSelectedRoute().getString(COLUMN_ROUTE);
    }

    OnRouteDialogClick getClickListener() {
        return this.clickListener;
    }
}
