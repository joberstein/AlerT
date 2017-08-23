package com.jesseoberstein.alert.listeners.routes;

import android.app.Activity;
import android.app.DialogFragment;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.Adapter;

import com.jesseoberstein.alert.fragments.dialog.route.AddRouteDialog;
import com.jesseoberstein.alert.utils.AlertUtils;

import java.util.stream.IntStream;

import static android.support.v7.widget.SearchView.OnSuggestionListener;

public class SelectRouteOnClick implements OnSuggestionListener {
    public static final String SELECTED_ROUTE = "selected";

    private Activity activity;
    private Adapter adapter;
    private String[] columnNames;

    public SelectRouteOnClick(Activity activity, Adapter adapter, String[] columnNames) {
        this.activity = activity;
        this.adapter = adapter;
        this.columnNames = columnNames;
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
            Bundle bundle = new Bundle();
            MatrixCursor selected = (MatrixCursor) adapter.getItem(position);
            IntStream.range(0, this.columnNames.length).forEach(columnIndex ->
                    AlertUtils.putExtraByField(bundle, selected, columnIndex));

            DialogFragment dialog = new AddRouteDialog();
            dialog.setArguments(bundle);
            dialog.show(this.activity.getFragmentManager(), "AddRouteDialog");
        }

        return true;
    }
}
