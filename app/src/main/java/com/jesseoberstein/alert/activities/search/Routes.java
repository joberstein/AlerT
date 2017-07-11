package com.jesseoberstein.alert.activities.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.widget.CursorAdapter;

import com.jesseoberstein.alert.QueryTextListener;
import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.utils.AlertUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Routes extends AppCompatActivity {
    private List<String> getRoutes() {
        return Arrays.asList(
                getString(R.string.blue_line),
                getString(R.string.green_line),
                getString(R.string.orange_line),
                getString(R.string.red_line),
                getString(R.string.silver_line)
        );
    }

    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activites_search_routes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.routes_search_bar);
        setSupportActionBar(toolbar);
        Optional<ActionBar> supportActionBar = Optional.ofNullable(getSupportActionBar());
        supportActionBar.ifPresent(bar -> bar.setDisplayHomeAsUpEnabled(true));

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        boolean isSearchIntent = Intent.ACTION_SEARCH.equals(intent.getAction());
        String query = isSearchIntent ? intent.getStringExtra(SearchManager.QUERY) : "";
        setupSearchView(toolbar, query);
    }

    private void setupSearchView(Toolbar toolbar, String query) {
        SearchView searchView = (SearchView) toolbar.findViewById(R.id.routes_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.setFocusable(true);
        searchView.requestFocus();
        setSearchViewSuggestionSettings(searchView);
    }

    private void setSearchViewSuggestionSettings(SearchView searchView) {
        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(this, R.layout.list_search, null,
                        new String[]{"route"}, new int[]{R.id.search_row_text},
                        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        QueryTextListener queryTextListener = new QueryTextListener(
                new String[]{BaseColumns._ID, "route"},
                this.getRoutes().toArray(new String[]{}),
                adapter);

        searchView.setSuggestionsAdapter(adapter);
        searchView.setOnQueryTextListener(queryTextListener);
    }
}