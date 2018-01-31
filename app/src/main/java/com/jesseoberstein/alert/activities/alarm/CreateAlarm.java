package com.jesseoberstein.alert.activities.alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.adapters.ButtonListAdapter;
import com.jesseoberstein.alert.listeners.StartActivityOnClick;
import com.jesseoberstein.alert.listeners.endpoints.SelectDirectionOnClick;
import com.jesseoberstein.alert.listeners.inputs.HideKeyboardOnItemClick;
import com.jesseoberstein.alert.listeners.inputs.HideKeyboardOnNextAction;
import com.jesseoberstein.alert.providers.EndpointsProvider;
import com.jesseoberstein.alert.providers.StopsProvider;
import com.jesseoberstein.alert.utils.AlertUtils;
import com.jesseoberstein.alert.utils.Tints;
import com.jesseoberstein.alert.validation.AbstractValidator;
import com.jesseoberstein.alert.validation.AutoCompleteValidator;
import com.jesseoberstein.alert.validation.CreateAlarmTextValidator;
import com.jesseoberstein.mbta.model.Endpoint;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static com.jesseoberstein.alert.utils.Constants.COLOR;
import static com.jesseoberstein.alert.utils.Constants.ROUTE;
import static com.jesseoberstein.alert.validation.CreateAlarmPredicates.isValidNickname;
import static com.jesseoberstein.alert.validation.CreateAlarmPredicates.isValidStation;

public class CreateAlarm extends AppCompatActivity {
    public static final int REQUEST_CODE = 2;
    public static final int NICKNAME_KEY = 0;
    public static final int STATION_KEY = 1;

    private int themeColor;

    private Bundle receivedBundle;
    private StopsProvider stopsProvider;
    private EndpointsProvider endpointsProvider;

    private EditText nicknameText;
    private AutoCompleteTextView stationAutoComplete;
    private LinearLayout directionsView;
    private TabLayout directionTabs;
    private LinearLayout endpointsView;
    private GridView endpointButtons;
    private View focusHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_create_alarm);
        receivedBundle = getIntent().getExtras();
        themeColor = receivedBundle.getInt(COLOR);
        String selectedRoute = receivedBundle.getString(ROUTE);
        stopsProvider = new StopsProvider(getAssets(), selectedRoute);
        endpointsProvider = new EndpointsProvider(getAssets(), selectedRoute);

        Optional<ActionBar> supportActionBarOptional = Optional.ofNullable(getSupportActionBar());
        supportActionBarOptional.ifPresent(bar -> {
            bar.setTitle(R.string.create_alarm_page);
            bar.setBackgroundDrawable(getDrawable(themeColor));
            bar.setDisplayHomeAsUpEnabled(true);
        });

        // Get the activity views.
        nicknameText = (EditText) findViewById(R.id.nickname);
        stationAutoComplete = (AutoCompleteTextView) findViewById(R.id.station);
        directionsView = (LinearLayout) findViewById(R.id.directions_view);
        directionTabs = (TabLayout) findViewById(R.id.direction_tabs);
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
        String[] stations = stopsProvider.getStopNames().toArray(new String[]{});
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

    public LinearLayout getEndpointsView() {
        return endpointsView;
    }

    public GridView getEndpointButtons() {
        return endpointButtons;
    }

    public View getFocusHolder() {
        return focusHolder;
    }

    public void setUpDirectionTabs() {
        directionsView.setVisibility(View.VISIBLE);
        directionTabs.removeAllTabs();
        directionTabs.setSelectedTabIndicatorColor(getColor(themeColor));
        directionTabs.addOnTabSelectedListener(new SelectDirectionOnClick(this));

        // Add a tab for each stop direction.
        endpointsProvider.getEndpointDirections().forEach(name ->
                directionTabs.addTab(directionTabs.newTab().setText(name)));

        int selectedTabIdx = directionTabs.getSelectedTabPosition();
        String defaultDirectionName = Optional.ofNullable(directionTabs.getTabAt(selectedTabIdx))
                .flatMap(tab -> Optional.ofNullable(tab.getText()))
                .map(CharSequence::toString).orElse("");
        setUpEndpointButtons(defaultDirectionName);
    }

    /**
     * Create the endpoint and line buttons for the given stop, and add them to the view.
     */
    public void setUpEndpointButtons(String defaultDirectionName) {
        List<Endpoint> endpoints = endpointsProvider.getEndpointsForDirection(defaultDirectionName);
        List<String> endpointNames = endpointsProvider.getEndpointNames(endpoints);
        ButtonListAdapter endpointsAdapter = new ButtonListAdapter(this, R.layout.button_direction, themeColor, endpointNames);
        endpointButtons.setAdapter(endpointsAdapter);
        endpointsView.setVisibility(View.VISIBLE);
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
