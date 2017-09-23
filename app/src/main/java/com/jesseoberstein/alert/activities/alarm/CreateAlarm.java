package com.jesseoberstein.alert.activities.alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.adapters.ButtonListAdapter;
import com.jesseoberstein.alert.listeners.StartActivityOnClick;
import com.jesseoberstein.alert.listeners.ToggleColorOnClick;
import com.jesseoberstein.alert.listeners.endpoints.SelectDirectionOnClick;
import com.jesseoberstein.alert.listeners.endpoints.ToggleEndpointsOnClick;
import com.jesseoberstein.alert.listeners.inputs.HideKeyboardOnItemClick;
import com.jesseoberstein.alert.listeners.inputs.HideKeyboardOnNextAction;
import com.jesseoberstein.alert.providers.EndpointsProvider;
import com.jesseoberstein.alert.providers.RoutesProvider;
import com.jesseoberstein.alert.providers.StationsProvider;
import com.jesseoberstein.alert.utils.AlertUtils;
import com.jesseoberstein.alert.utils.Tints;
import com.jesseoberstein.alert.validation.AbstractValidator;
import com.jesseoberstein.alert.validation.AutoCompleteValidator;
import com.jesseoberstein.alert.validation.CreateAlarmTextValidator;
import com.jesseoberstein.mbta.model.Direction;
import com.jesseoberstein.mbta.model.Route;
import com.jesseoberstein.mbta.utils.RouteLine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static com.jesseoberstein.alert.utils.Constants.COLOR;
import static com.jesseoberstein.alert.utils.Constants.ROUTE;
import static com.jesseoberstein.alert.validation.CreateAlarmPredicates.isValidNickname;
import static com.jesseoberstein.alert.validation.CreateAlarmPredicates.isValidStation;
import static com.jesseoberstein.mbta.utils.RouteName.RED;

public class CreateAlarm extends AppCompatActivity {
    public static final int REQUEST_CODE = 2;
    public static final int NICKNAME_KEY = 0;
    public static final int STATION_KEY = 1;

    private String selectedRoute;
    private String selectedStop;
    private int themeColor;
    private HashMap<String, Integer> activeEndpoints = new HashMap<>();

    private Bundle receivedBundle;
    private RoutesProvider routesProvider;
    private StationsProvider stationsProvider;
    private EndpointsProvider endpointsProvider;

    private EditText nicknameText;
    private AutoCompleteTextView stationAutoComplete;
    private LinearLayout directionsView;
    private TabLayout directionTabs;
    private LinearLayout linesView;
    private LinearLayout lineButtons;
    private LinearLayout endpointsView;
    private GridView endpointButtons;
    private View focusHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_create_alarm);
        receivedBundle = getIntent().getExtras();
        selectedRoute = receivedBundle.getString(ROUTE);
        themeColor = receivedBundle.getInt(COLOR);

        Optional<ActionBar> supportActionBarOptional = Optional.ofNullable(getSupportActionBar());
        supportActionBarOptional.ifPresent(bar -> {
            bar.setTitle(R.string.create_alarm_page);
            bar.setBackgroundDrawable(getDrawable(themeColor));
            bar.setDisplayHomeAsUpEnabled(true);
        });

        routesProvider = RoutesProvider.init(getAssets());
        stationsProvider = StationsProvider.init(getAssets());
        endpointsProvider = EndpointsProvider.init(getAssets());

        // Get the activity views.
        nicknameText = (EditText) findViewById(R.id.nickname);
        stationAutoComplete = (AutoCompleteTextView) findViewById(R.id.station);
        directionsView = (LinearLayout) findViewById(R.id.directions_view);
        directionTabs = (TabLayout) findViewById(R.id.direction_tabs);
        linesView = (LinearLayout) findViewById(R.id.lines_view);
        lineButtons = (LinearLayout) linesView.findViewById(R.id.line_buttons);
        endpointsView = (LinearLayout) findViewById(R.id.endpoints_section);
        endpointButtons = (GridView) findViewById(R.id.endpoint_buttons);
        focusHolder = findViewById(R.id.hiddenFocus);
        Button createAlarmButton = (Button) findViewById(R.id.create_alarm);

        // Set up the page elements.
        AlertUtils.showKeyboard(this);
        setUpNickname(nicknameText, createAlarmButton);
        setUpAutoComplete(createAlarmButton);
        setUpCreateAlarmButton(createAlarmButton, themeColor, R.color.white);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (EditAlarm.REQUEST_CODE == requestCode)  {
            if (RESULT_OK == resultCode) {
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        }
    }

    /**
     * Set up validation for the nickname input field.
     * @param nicknameText The view to use for the nickname EditText.
     * @param submit The button to update validation status with.
     */
    private void setUpNickname(EditText nicknameText, Button submit) {
        String error = "Must be 1 - 12 characters.";
        CreateAlarmTextValidator validator = new CreateAlarmTextValidator(this, isValidNickname(), error, submit, NICKNAME_KEY);
        nicknameText.addTextChangedListener(validator);
    }

    /**
     * Set up autocomplete for the station input field.
     * @param submit The button to update validation status with.
     */
    private void setUpAutoComplete(Button submit) {
        String error = "Not a valid station.";
        String[] stations = stationsProvider.getStopNamesForRoute(selectedRoute).toArray(new String[0]);
        Predicate<String> isValidStation = isValidStation(stations);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, stations);
        CreateAlarmTextValidator createAlarmTextValidator = new CreateAlarmTextValidator(this, isValidStation, error, submit, STATION_KEY);
        AutoCompleteValidator validator = new AutoCompleteValidator(stationAutoComplete, isValidStation, error, submit, STATION_KEY);

        stationAutoComplete.setAdapter(adapter);
        stationAutoComplete.setValidator(validator);
        stationAutoComplete.addTextChangedListener(createAlarmTextValidator);
        stationAutoComplete.setOnEditorActionListener(new HideKeyboardOnNextAction(focusHolder));
        stationAutoComplete.setOnItemClickListener(new HideKeyboardOnItemClick(this));
    }

    public EditText getNicknameText() {
        return nicknameText;
    }

    public AutoCompleteTextView getStationAutoComplete() {
        return stationAutoComplete;
    }

    public LinearLayout getDirectionsView() {
        return directionsView;
    }

    public TabLayout getDirectionTabs() {
        return directionTabs;
    }

    public LinearLayout getLinesView() {
        return linesView;
    }

    public LinearLayout getEndpointsView() {
        return endpointsView;
    }

    public GridView getEndpointButtons() {
        return endpointButtons;
    }

    public View getFocusHolder() {
        return focusHolder;
    }

    public void setUpDirectionTabs(String stopName) {
        selectedStop = stopName;

        directionsView.setVisibility(View.VISIBLE);
        directionTabs.removeAllTabs();
        directionTabs.setSelectedTabIndicatorColor(getColor(themeColor));
        directionTabs.addOnTabSelectedListener(new SelectDirectionOnClick(this));

        // Add a tab for each stop direction.
        stationsProvider.getDirectionsForStop(selectedStop, selectedRoute).stream()
                .map(Direction::getDirectionName)
                .distinct()
                .forEach(dirName -> directionTabs.addTab(directionTabs.newTab().setText(dirName)));

        int selectedTabIdx = directionTabs.getSelectedTabPosition();
        String defaultDirectionName = Optional.ofNullable(directionTabs.getTabAt(selectedTabIdx))
                .flatMap(tab -> Optional.ofNullable(tab.getText()))
                .map(CharSequence::toString).orElse("");
        setUpLineAndEndpointButtons(defaultDirectionName);
    }

    /**
     * Create the endpoint and line buttons for the given stop, and add them to the view.
     */
    public void setUpLineAndEndpointButtons(String directionName) {
        List<String> selectedStopIds = stationsProvider.getStopIdForStopAndDirection(selectedStop, directionName, selectedRoute);
        ButtonListAdapter endpointsAdapter = new ButtonListAdapter(this, R.layout.button_direction, themeColor, new ArrayList<>());
        endpointButtons.setAdapter(endpointsAdapter);

        List<String> endpoints = endpointsProvider.getEndpointsForStop(selectedStopIds, selectedRoute);
        List<Route> routes = endpointsProvider.getRoutesForStop(selectedStopIds, routesProvider.getSpecificRouteNames(selectedRoute));
        boolean isRedLine = selectedRoute.equals(RED.toString());
        routes = isRedLine ? RoutesProvider.RED_LINE_ROUTES : routes;

        if (routes.size() > 1 && endpoints.size() > 1) {
            linesView.setVisibility(View.VISIBLE);
            lineButtons.removeAllViews();
            routes.forEach(route -> {
                RouteLine line = RouteLine.getLineFromRoute(route.getRouteId());
                String lineName = line.equals(RouteLine.OTHER) ? route.getRouteName() : line.name();
                List<String> routeEndpoints = isRedLine ?
                        endpointsProvider.getRedLineEndpoints(line, endpoints) :
                        endpointsProvider.getEndpointsForRouteStop(selectedStopIds, route.getRouteId());
                routeEndpoints.forEach(endpoint -> activeEndpoints.put(endpoint, 0));
                addLineButton(lineName, routeEndpoints, endpointsAdapter);
            });
        }
        else {
            endpointsView.setVisibility(View.VISIBLE);
            endpointsAdapter.addAll(endpoints);
        }
    }

    /**
     * Add a button containing a line name.
     * @param line The direction to set for the button text.
     * @param routeEndpoints The endpoints for the given line's route.
     * @param endpointsAdapter The endpoints adapter for the given line's route.
     */
    private void addLineButton(String line, List<String> routeEndpoints, ButtonListAdapter endpointsAdapter) {
        ToggleButton btn = (ToggleButton) LayoutInflater.from(this).inflate(R.layout.button_direction, lineButtons, false);
        ToggleColorOnClick toggleBtnColor = new ToggleColorOnClick(
                android.R.color.transparent, themeColor,
                android.R.color.transparent, android.R.color.black, this);

        btn.setOnCheckedChangeListener(toggleBtnColor);
        btn.setText(line);
        btn.setTextOn(line);
        btn.setTextOff(line);
        btn.setBackgroundColor(getColor(android.R.color.transparent));
        btn.setOnCheckedChangeListener(new ToggleEndpointsOnClick(
                toggleBtnColor, routeEndpoints, endpointsAdapter, activeEndpoints, this));

        lineButtons.addView(btn);
    }

    /**
     * Set the text color, background tint list, and validation for the create alarm button.
     * @param btn The button used to create the alarm.
     * @param buttonColor The color to set the create alarm button to.
     * @param buttonTextColor The color to set the text of the create alarm button.
     */
    private void setUpCreateAlarmButton(Button btn, int buttonColor, int buttonTextColor) {
        AbstractValidator.initializeValidationAwareView(btn, 2);
        btn.setBackgroundTintList(Tints.forColoredButton(this, getColor(buttonColor)));
        btn.setTextColor(Tints.forColoredButtonText(this, getColor(buttonTextColor)));
        btn.setEnabled(false);
        StartActivityOnClick editAlarm = new StartActivityOnClick(this, EditAlarm.class);
        btn.setOnClickListener(editAlarm.withBundle(receivedBundle).withRequestCode(EditAlarm.REQUEST_CODE));
    }
}
