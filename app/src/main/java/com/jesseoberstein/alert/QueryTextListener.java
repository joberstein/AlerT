package com.jesseoberstein.alert;

import android.database.MatrixCursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;

import com.jesseoberstein.alert.models.RouteDataNode;

import java.util.Arrays;
import java.util.stream.IntStream;

public class QueryTextListener implements SearchView.OnQueryTextListener {
    private final String[] columnNames;
    private String[] suggestions;
    private SimpleCursorAdapter adapter;

    public QueryTextListener(String[] columnNames,
                             String[] suggestions,
                             SimpleCursorAdapter adapter) {
        this.columnNames = columnNames;
        this.suggestions = suggestions;
        this.adapter = adapter;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        MatrixCursor cursor = getNewCursor();
        IntStream.range(0, this.suggestions.length).boxed()
                .map(index -> new RouteDataNode(index, this.suggestions[index]))
                .filter(dbSuggestion -> dbSuggestion.getRoute().toLowerCase().startsWith(newText.toLowerCase()))
                .forEach(valid -> cursor.addRow(new String[] {valid.getId(), valid.getRoute()}));

        this.adapter.changeCursor(cursor);
        return false;
    }

    private MatrixCursor getNewCursor() {
        return new MatrixCursor(columnNames);
    }
}
