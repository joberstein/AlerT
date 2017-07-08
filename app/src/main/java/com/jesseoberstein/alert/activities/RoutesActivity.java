package com.jesseoberstein.alert.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.StartActivityOnClick;
import com.jesseoberstein.alert.adapters.CustomListAdapter;
import com.jesseoberstein.alert.models.CustomListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static com.jesseoberstein.alert.models.CustomListItem.buildRoutesListItem;

public class RoutesActivity extends AppCompatActivity {
    private CustomListAdapter myRoutesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        Optional<ActionBar> supportActionBarOptional = Optional.ofNullable(getSupportActionBar());
        supportActionBarOptional.ifPresent(bar -> {
            bar.setTitle(R.string.routes_page);
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            bar.setCustomView(R.layout.action_bar_centered);
        });

        myRoutesAdapter = new CustomListAdapter(this, generateMyRoutes());
        ListView listView = (ListView) findViewById(R.id.route_list);
        listView.setAdapter(myRoutesAdapter);

        Bundle bundle = new Bundle();
        // Pass in routes to filter (user already has them in their routes list)
        bundle.putString("query", "Green Line");
        FloatingActionButton addRouteView = (FloatingActionButton) findViewById(R.id.add_route);
        addRouteView.setOnClickListener(
                new StartActivityOnClick(this, RoutesSearchActivity.class)
                        .withAction(Intent.ACTION_SEARCH)
                        .withBundle(bundle));
    }

    private ArrayList<CustomListItem> generateMyRoutes(){
        return new ArrayList<>(Arrays.asList(
            buildRoutesListItem(R.drawable.circle_blue, getString(R.string.blue_line), "1"),
            buildRoutesListItem(R.drawable.circle_green, getString(R.string.green_line), ""),
            buildRoutesListItem(R.drawable.circle_orange, getString(R.string.orange_line), ""),
            buildRoutesListItem(R.drawable.circle_red, getString(R.string.red_line), "5"),
            buildRoutesListItem(R.drawable.circle_silver, getString(R.string.silver_line), "")
        ));
    }

    // Just for testing.
    public void increaseAlert(View view) {
        CustomListItem item0 = myRoutesAdapter.getItem(0);
        String item0info = Optional.ofNullable(item0.getInfo()).orElse("0");
        buildRoutesListItem(item0, item0.getIcon(), item0.getName(), (Integer.parseInt(item0info) + 1) + "");

        CustomListItem item1 = myRoutesAdapter.getItem(1);
        String item1info = Optional.ofNullable(item1.getInfo()).orElse("0");
        buildRoutesListItem(item1, item1.getIcon(), item1.getName(), (Integer.parseInt(item1info) + 1) + "");

        CustomListItem item3 = myRoutesAdapter.getItem(3);
        String item3Info = Optional.ofNullable(item3.getInfo()).orElse("0");
        buildRoutesListItem(item3, item3.getIcon(), item3.getName(), (Integer.parseInt(item3Info) - 1) + "");

        myRoutesAdapter.notifyDataSetChanged();
    }
}
