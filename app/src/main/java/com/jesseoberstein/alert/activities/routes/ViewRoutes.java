package com.jesseoberstein.alert.activities.routes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarms.ViewAlarms;
import com.jesseoberstein.alert.adapters.CustomListAdapter;
import com.jesseoberstein.alert.listeners.StartActivityOnClick;
import com.jesseoberstein.alert.models.CustomListItem;
import com.jesseoberstein.alert.utils.AlertUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.jesseoberstein.alert.activities.routes.SearchRoutes.COLUMN_ROUTE;
import static com.jesseoberstein.alert.listeners.routes.SelectRouteOnClick.SELECTED_ROUTE;
import static com.jesseoberstein.alert.models.CustomListItem.buildRoutesListItem;

public class ViewRoutes extends AppCompatActivity {
    private CustomListAdapter myRoutesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_view_routes);
        Optional<ActionBar> supportActionBarOptional = Optional.ofNullable(getSupportActionBar());
        supportActionBarOptional.ifPresent(bar -> {
            bar.setTitle(R.string.routes_page);
        });

        if (myRoutesAdapter == null) {
            myRoutesAdapter = new CustomListAdapter(this, R.layout.list_routes, new ArrayList<>());
        }
        AdapterView.OnItemClickListener goToAlarms = new StartActivityOnClick(this, ViewAlarms.class);

        ListView listView = (ListView) findViewById(R.id.route_list);
        listView.setAdapter(myRoutesAdapter);
        listView.setOnItemClickListener(goToAlarms);
        updateAddRouteListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (SearchRoutes.REQUEST_CODE == requestCode)  {
            if (RESULT_OK == resultCode) {
                Bundle selected = data.getBundleExtra(SELECTED_ROUTE);
                String routeName = selected.getString(COLUMN_ROUTE);
                int icon = AlertUtils.getRouteIcon(routeName);
                myRoutesAdapter.addItem(buildRoutesListItem(icon, routeName, ""));
                updateAddRouteListener();
            }
        }
    }

    // Static test route data.
    private ArrayList<CustomListItem> generateMyRoutes(){
        return new ArrayList<>(Arrays.asList(
            buildRoutesListItem(R.drawable.circle_blue, getString(R.string.blue_line), "1"),
            buildRoutesListItem(R.drawable.circle_green, getString(R.string.green_line), ""),
            buildRoutesListItem(R.drawable.circle_orange, getString(R.string.orange_line), ""),
            buildRoutesListItem(R.drawable.circle_red, getString(R.string.red_line), "5")
        ));
    }

    /**
     * Set a new listener on the add route button.  Called in 'onCreate' and every time a new route
     * is added.
     */
    private void updateAddRouteListener() {
        FloatingActionButton addRouteView = (FloatingActionButton) findViewById(R.id.add_route);
        addRouteView.setOnClickListener(
                new StartActivityOnClick(this, SearchRoutes.class)
                        .withAction(Intent.ACTION_SEARCH)
                        .withBundle(this.getRoutesListBundle())
                        .withRequestCode(SearchRoutes.REQUEST_CODE));
    }

    /**
     * Get a bundle of the user's current routes. These will be filtered out when the user searches
     * for more routes to add.
     */
    private Bundle getRoutesListBundle() {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("query", myRoutesAdapter.getItems().stream()
                .map(CustomListItem::getPrimaryText)
                .collect(Collectors.toCollection(ArrayList::new)));
        return bundle;
    }
}
