package com.jesseoberstein.alert.listeners.endpoints;

import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import com.jesseoberstein.alert.listeners.ToggleColorOnClick;

import java.util.HashMap;
import java.util.List;

import static android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * Toggle visibility of endpoints when selecting lines during creating an alarm.
 */
public class ToggleEndpointsOnClick implements OnCheckedChangeListener {
    private ToggleColorOnClick toggleColorOnClick;
    private List<String> routeEndpoints;
    private ArrayAdapter<String> endpointsAdapter;
    private HashMap<String, Integer> activeEndpoints;

    public ToggleEndpointsOnClick(ToggleColorOnClick toggleColorOnClick,
                                  List<String> routeEndpoints,
                                  ArrayAdapter<String> endpointsAdapter,
                                  HashMap<String, Integer> activeEndpoints) {
        this.toggleColorOnClick = toggleColorOnClick;
        this.routeEndpoints = routeEndpoints;
        this.endpointsAdapter = endpointsAdapter;
        this.activeEndpoints = activeEndpoints;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        this.toggleColorOnClick.onCheckedChanged(compoundButton, isChecked);

        routeEndpoints.forEach(endpoint -> {
            int modifier = isChecked ? 1 : -1;
            this.activeEndpoints.put(endpoint, this.activeEndpoints.get(endpoint) + modifier);
            int numTimesEndpointIncluded = this.activeEndpoints.get(endpoint);

            if (numTimesEndpointIncluded == 0) {
                this.endpointsAdapter.remove(endpoint);
            }
            else if (numTimesEndpointIncluded == 1 && isChecked) {
                this.endpointsAdapter.add(endpoint);
            }
        });

        this.endpointsAdapter.notifyDataSetChanged();
    }
}
