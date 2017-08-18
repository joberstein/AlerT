package com.jesseoberstein.alert.activities.routes;

import android.app.Activity;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.interfaces.OnRouteDialogClick;
import com.jesseoberstein.alert.listeners.routes.QueryRoutesListener;
import com.jesseoberstein.alert.listeners.routes.SelectRouteOnClick;
import com.jesseoberstein.alert.providers.RoutesProvider;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static android.support.v7.appcompat.R.id;
import static com.jesseoberstein.alert.listeners.routes.SelectRouteOnClick.SELECTED_ROUTE;

public class SearchRoutes extends AppCompatActivity implements OnRouteDialogClick {
    public static final int REQUEST_CODE = 1;
    public static final String COLUMN_ROUTE = "route";
    public static final String[] COLUMN_NAMES = new String[]{BaseColumns._ID, COLUMN_ROUTE};
    private RoutesProvider routesProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activites_search_routes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.routes_search_bar);
        setSupportActionBar(toolbar);
        Optional<ActionBar> supportActionBar = Optional.ofNullable(getSupportActionBar());
        supportActionBar.ifPresent(bar -> bar.setDisplayHomeAsUpEnabled(true));

        routesProvider = RoutesProvider.init(getAssets());

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        List<String> query = intent.getStringArrayListExtra(SearchManager.QUERY);
        setupSearchView(toolbar, query);
    }

    private void setupSearchView(Toolbar toolbar, List<String> query) {
        SearchView searchView = (SearchView) toolbar.findViewById(R.id.routes_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.setFocusable(true);
        searchView.requestFocus();
        setSearchViewSuggestionSettings(searchView, query);
    }

    private void setSearchViewSuggestionSettings(SearchView searchView, List<String> query) {
        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(this, R.layout.list_search, null,
                        new String[]{"route"}, new int[]{R.id.search_row_text},
                        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        List<String> queryVal = Optional.ofNullable(query).orElse(Collections.emptyList());
        QueryRoutesListener queryTextListener = new QueryRoutesListener(
                this,
                COLUMN_NAMES,
                routesProvider.getRouteNames().stream()
                        .filter(route -> !queryVal.contains(route))
                        .toArray(String[]::new),
                adapter);

        searchView.setSuggestionsAdapter(adapter);
        searchView.setOnQueryTextListener(queryTextListener);
        searchView.setOnSuggestionListener(new SelectRouteOnClick(this, adapter, COLUMN_NAMES));

        AutoCompleteTextView suggestions = (AutoCompleteTextView) searchView.findViewById(id.search_src_text);
        suggestions.setDropDownBackgroundResource(android.R.color.transparent);

        View anchor = searchView.findViewById(suggestions.getDropDownAnchor());
        Optional.ofNullable(anchor).ifPresent(dropdown -> {
            dropdown.addOnLayoutChangeListener((view, i, i1, i2, i3, i4, i5, i6, i7) ->
                suggestions.setDropDownWidth(ViewGroup.LayoutParams.MATCH_PARENT));
        });
    }

    @Override
    public void onAddSelectedRoute(Bundle selectedRoute) {
        Intent intent = new Intent();
        intent.putExtra(SELECTED_ROUTE, selectedRoute);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onRemoveSelectedRoute(Bundle selectedRoute) {}

    @Override
    public void onCancelSelectedRoute(Bundle selectedRoute) {}
}
