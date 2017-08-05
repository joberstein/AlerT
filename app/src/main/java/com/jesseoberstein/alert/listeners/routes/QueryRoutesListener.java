package com.jesseoberstein.alert.listeners.routes;

import android.app.Activity;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.jesseoberstein.alert.models.RouteDataNode;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.jesseoberstein.alert.activities.routes.SearchRoutes.COLUMN_ROUTE;

public class QueryRoutesListener implements SearchView.OnQueryTextListener {
    private Activity activity;
    private final String[] columnNames;
    private String[] suggestions;
    private SimpleCursorAdapter adapter;

    public QueryRoutesListener(Activity activity, String[] columnNames, String[] suggestions,
                               SimpleCursorAdapter adapter) {
        this.activity = activity;
        this.columnNames = columnNames;
        this.suggestions = suggestions;
        this.adapter = adapter;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        Cursor cursor = this.adapter.getCursor();
        cursor.moveToFirst();

        int position = getQueryIndex(cursor, query);
        if (position >= 0) {
            SelectRouteOnClick selectOnClick = new SelectRouteOnClick(this.activity, this.adapter, this.columnNames);
            selectOnClick.onSuggestionClick(position);
        }
        else {
            Toast.makeText(this.activity, "Please select a valid route.", Toast.LENGTH_SHORT).show();
        }

        return true;
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

    /**
     * Get a new cursor using with these column names.
     * @return A new cursor.
     */
    private MatrixCursor getNewCursor() {
        return new MatrixCursor(columnNames);
    }

    /**
     * Get the index of the query in the list of routes. Return behavior is identical to 'indexOf'.
     * @param cursor The cursor to search through.
     * @param query The query to check against in the list of routes. May/may not be a valid route name.
     * @return The index of the query (case-insensitive) in the list, or -1 if not present.
     */
    private int getQueryIndex(Cursor cursor, String query) {
        return IntStream.range(0, cursor.getCount())
                .mapToObj(i -> {
                    MatrixCursor row = (MatrixCursor) this.adapter.getItem(i);
                    String suggestion = row.getString(cursor.getColumnIndex(COLUMN_ROUTE)).toLowerCase();
                    cursor.moveToNext();
                    return suggestion.toLowerCase();
                })
                .collect(Collectors.toList())
                .indexOf(query.toLowerCase());
    }
}
