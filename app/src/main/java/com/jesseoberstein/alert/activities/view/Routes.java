package com.jesseoberstein.alert.activities.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.StartActivityOnClick;
import com.jesseoberstein.alert.StartActivityOnItemClick;
import com.jesseoberstein.alert.adapters.CustomListAdapter;
import com.jesseoberstein.alert.models.CustomListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static com.jesseoberstein.alert.models.CustomListItem.buildRoutesListItem;

public class Routes extends AppCompatActivity {
    private CustomListAdapter myRoutesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_view_routes);
        Optional<ActionBar> supportActionBarOptional = Optional.ofNullable(getSupportActionBar());
        supportActionBarOptional.ifPresent(bar -> {
            bar.setTitle(R.string.routes_page);
        });

        myRoutesAdapter = new CustomListAdapter(this, R.layout.list_routes, generateMyRoutes());
        AdapterView.OnItemClickListener goToAlarms = new StartActivityOnItemClick(this, Alarms.class);

        ListView listView = (ListView) findViewById(R.id.route_list);
        listView.setAdapter(myRoutesAdapter);
        listView.setOnItemClickListener(goToAlarms);

        Bundle bundle = new Bundle();
        // Pass in routes to filter (user already has them in their routes list)
        bundle.putString("query", "Green Line");

        FloatingActionButton addRouteView = (FloatingActionButton) findViewById(R.id.add_route);
        addRouteView.setOnClickListener(
                new StartActivityOnClick(this, com.jesseoberstein.alert.activities.search.Routes.class)
                        .withAction(Intent.ACTION_SEARCH)
                        .withBundle(bundle));
    }

    // Static test route data.
    private ArrayList<CustomListItem> generateMyRoutes(){
        return new ArrayList<>(Arrays.asList(
            buildRoutesListItem(R.drawable.circle_blue, getString(R.string.blue_line), "1"),
            buildRoutesListItem(R.drawable.circle_green, getString(R.string.green_line), ""),
            buildRoutesListItem(R.drawable.circle_orange, getString(R.string.orange_line), ""),
            buildRoutesListItem(R.drawable.circle_red, getString(R.string.red_line), "5"),
            buildRoutesListItem(R.drawable.circle_silver, getString(R.string.silver_line), "")
        ));
    }
}
