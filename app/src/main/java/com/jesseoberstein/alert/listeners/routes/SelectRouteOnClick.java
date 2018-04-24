package com.jesseoberstein.alert.listeners.routes;

import android.app.Activity;
import android.app.DialogFragment;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.Adapter;

import com.jesseoberstein.alert.fragments.dialog.route.AddRouteDialog;
import com.jesseoberstein.alert.models.mbta.Route;

import java.util.Arrays;

import static android.support.v7.widget.SearchView.OnSuggestionListener;
import static com.jesseoberstein.alert.utils.Constants.ROUTE;

public class SelectRouteOnClick implements OnSuggestionListener {
    public static final String SELECTED_ROUTE = "selected";

    private Activity activity;
    private String[] columnNames;
    private Route[] routes;
    private Adapter adapter;

    public SelectRouteOnClick(Activity activity, String[] columnNames, Route[] routes, Adapter adapter) {
        this.activity = activity;
        this.columnNames = columnNames;
        this.routes = routes;
        this.adapter = adapter;
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        SimpleCursorAdapter adapter = (SimpleCursorAdapter) this.adapter;
        Cursor cursor = adapter.getCursor();

        if (cursor.moveToPosition(position)) {
            MatrixCursor selected = (MatrixCursor) adapter.getItem(position);
            int routeColumnIdx = Arrays.asList(this.columnNames).indexOf(ROUTE);
            String routeId = Arrays.stream(this.routes)
//                    .filter(route -> RoutesProvider.getRouteName(route).equals(selected.getString(routeColumnIdx)))
                    .findFirst()
                    .map(Route::getId).orElse("");

            Bundle bundle = new Bundle();
            bundle.putString(ROUTE, routeId);

            DialogFragment dialog = new AddRouteDialog();
            dialog.setArguments(bundle);
            dialog.show(this.activity.getFragmentManager(), "AddRouteDialog");
        }

        return true;
    }
}
