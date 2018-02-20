package com.jesseoberstein.alert.activities.routes;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarms.ViewAlarms;
import com.jesseoberstein.alert.adapters.CustomItemsAdapter;
import com.jesseoberstein.alert.data.UserRouteDao;
import com.jesseoberstein.alert.interfaces.OnDialogClick;
import com.jesseoberstein.alert.listeners.StartActivityOnClick;
import com.jesseoberstein.alert.listeners.routes.RemoveRouteOnLongClick;
import com.jesseoberstein.alert.models.CustomListItem;
import com.jesseoberstein.alert.models.RouteListItem;
import com.jesseoberstein.alert.models.UserRoute;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.jesseoberstein.alert.listeners.routes.SelectRouteOnClick.SELECTED_ROUTE;
import static com.jesseoberstein.alert.models.RouteListItem.buildRoutesListItem;
import static com.jesseoberstein.alert.utils.Constants.ROUTE;

public class ViewRoutes extends AppCompatActivity implements OnDialogClick {
    private CustomItemsAdapter myRoutesAdapter;
    RuntimeExceptionDao<UserRoute, String> userRouteDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserRouteDao userRouteDao = new UserRouteDao(getApplicationContext());
        this.userRouteDao = userRouteDao.getDao();
        ArrayList<CustomListItem> userRoutes = this.userRouteDao.queryForAll().stream()
                .map(RouteListItem::buildRoutesListItem)
                .collect(Collectors.toCollection(ArrayList::new));

        setContentView(R.layout.activities_view_routes);
        Optional<ActionBar> actionBarOptional = Optional.ofNullable(getSupportActionBar());
        actionBarOptional.ifPresent(bar -> bar.setTitle(R.string.routes_page));
        myRoutesAdapter = new CustomItemsAdapter(this, R.layout.list_routes, userRoutes);

        ListView listView = (ListView) findViewById(R.id.route_list);
        listView.setAdapter(myRoutesAdapter);
        listView.setEmptyView(findViewById(R.id.route_list_empty));
        listView.setOnItemClickListener(new StartActivityOnClick(this, ViewAlarms.class));
        listView.setOnItemLongClickListener(new RemoveRouteOnLongClick(this));

        updateAddRouteListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (SearchRoutes.REQUEST_CODE == requestCode)  {
            if (RESULT_OK == resultCode) {
                Bundle selected = data.getBundleExtra(SELECTED_ROUTE);
                String routeId = selected.getString(ROUTE);
                UserRoute userRoute = new UserRoute(routeId);
                userRouteDao.create(userRoute);
                myRoutesAdapter.addItem(buildRoutesListItem(userRoute));
                updateAddRouteListener();
            }
        }
    }

    /**
     * Set a new listener on the add route button so that SearchRoutes filters out an up-to-date
     * list of routes.  Called in 'onCreate' and every time a new route is added.
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
        bundle.putStringArrayList(SearchManager.QUERY, myRoutesAdapter.getItems().stream()
                .map(userRoute -> ((String) userRoute.getId()))
                .collect(Collectors.toCollection(ArrayList::new)));
        return bundle;
    }

    @Override
    public void onRemoveSelected(Bundle selected) {
        String routeName = selected.getString(ROUTE);
        userRouteDao.deleteById(routeName);
        myRoutesAdapter.removeItemByPrimaryText(routeName);
        updateAddRouteListener();
    }

    @Override
    public void onAddSelected(Bundle selected) {}

    @Override
    public void onCancelSelected(Bundle selected) {}
}
