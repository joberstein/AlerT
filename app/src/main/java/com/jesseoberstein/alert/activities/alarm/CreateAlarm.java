package com.jesseoberstein.alert.activities.alarm;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.jesseoberstein.alert.listeners.inputs.HideKeyboardOnItemClick;
import com.jesseoberstein.alert.listeners.inputs.HideKeyboardOnNextAction;
import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.listeners.ToggleColorOnClick;
import com.jesseoberstein.alert.providers.EndpointsProvider;
import com.jesseoberstein.alert.providers.StationsProvider;
import com.jesseoberstein.alert.utils.AlertUtils;
import com.jesseoberstein.alert.utils.Tints;
import com.jesseoberstein.alert.validation.AbstractValidator;
import com.jesseoberstein.alert.validation.AutoCompleteValidator;
import com.jesseoberstein.alert.validation.TextValidator;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static com.jesseoberstein.alert.utils.AlertUtils.isEven;
import static com.jesseoberstein.alert.validation.CreateAlarmPredicates.isValidNickname;
import static com.jesseoberstein.alert.validation.CreateAlarmPredicates.isValidStation;

public class CreateAlarm extends AppCompatActivity {
    private static final int THEME_COLOR = R.color.orange_line;
    private static final int NICKNAME_KEY = 0;
    private static final int STATION_KEY = 1;
    private StationsProvider stationsProvider;
    private EndpointsProvider endpointsProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_create_alarm);
        Optional<ActionBar> supportActionBarOptional = Optional.ofNullable(getSupportActionBar());
        supportActionBarOptional.ifPresent(bar -> {
            bar.setTitle(R.string.create_alarm_page);
            bar.setBackgroundDrawable(getDrawable(THEME_COLOR));
            bar.setDisplayHomeAsUpEnabled(true);
        });

        stationsProvider = StationsProvider.init(getAssets());
        endpointsProvider = EndpointsProvider.init(getAssets());

        // Get the activity views.
        EditText nicknameText = (EditText) findViewById(R.id.nickname);
        AutoCompleteTextView stationAutoComplete = (AutoCompleteTextView) findViewById(R.id.station);
        LinearLayout directionsView = (LinearLayout) findViewById(R.id.directions);
        Button createAlarmButton = (Button) findViewById(R.id.create_alarm);

        // Set up the page elements.
        AlertUtils.showKeyboard(this);
        setUpNickname(nicknameText, createAlarmButton);
        setUpAutoComplete(stationAutoComplete, createAlarmButton);
        setUpDirectionButtons(directionsView, THEME_COLOR, R.color.white);
        setUpCreateAlarmButton(createAlarmButton, THEME_COLOR, R.color.white);
    }

    /**
     * Set up validation for the nickname input field.
     * @param nicknameText The view to use for the nickname EditText.
     * @param submit The button to update validation status with.
     */
    private void setUpNickname(EditText nicknameText, Button submit) {
        String error = "Must be 1 - 12 characters.";
        TextValidator validator = new TextValidator(nicknameText, isValidNickname(), error, submit, NICKNAME_KEY);
        nicknameText.addTextChangedListener(validator);
    }

    /**
     * Set up autocomplete for the station input field.
     * @param autoComplete The view to use for the station autocomplete.
     * @param submit The button to update validation status with.
     */
    private void setUpAutoComplete(AutoCompleteTextView autoComplete, Button submit) {
        String error = "Not a valid station.";
        String[] stations = stationsProvider.getStopNamesForRoute("Green Line").toArray(new String[0]);
        Predicate<String> isValidStation = isValidStation(stations);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, stations);
        TextValidator textValidator = new TextValidator(autoComplete, isValidStation, error, submit, STATION_KEY);
        AutoCompleteValidator validator = new AutoCompleteValidator(autoComplete, isValidStation, error, submit, STATION_KEY);
        View focusHolder = findViewById(R.id.hiddenFocus);

        autoComplete.setAdapter(adapter);
        autoComplete.setValidator(validator);
        autoComplete.addTextChangedListener(textValidator);
        autoComplete.setOnEditorActionListener(new HideKeyboardOnNextAction(focusHolder));
        autoComplete.setOnItemClickListener(new HideKeyboardOnItemClick(autoComplete, focusHolder));
    }

    /**
     * Create the direction buttons and add them to the view. Force the list of directions to be
     * an even number.
     * @param directionsView The view to add the direction buttons to.
     * @param buttonColor The color of am direction active button.
     * @param buttonTextColor The text color of an active direction button.
     */
    private void setUpDirectionButtons(LinearLayout directionsView, int buttonColor, int buttonTextColor) {
        List<String> directions = endpointsProvider.getEndpointsForRoute("Orange Line");
        directions.add(null);
        int size = directions.size();
        int evenSize = isEven(size) ? size : size - 1;
        IntStream.range(0, evenSize)
                .filter(AlertUtils::isEven)
                .mapToObj(i -> directions.subList(i, i + 2))
                .forEach(subList -> {
                    LinearLayout buttonRow = addButtonRow(directionsView);
                    subList.forEach(dir -> addDirectionButton(buttonRow, dir, buttonColor, buttonTextColor));
                });
    }

    /**
     * Add a button row to a given parent.
     * @param parent The parent this row should be added to.
     * @return The created row.
     */
    private LinearLayout addButtonRow(ViewGroup parent) {
        LinearLayout row = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.row_direction, parent, false);
        parent.addView(row);
        return row;
    }

    /**
     * Add a button containing a direction to the given parent.
     * @param parent The parent this button should be added to.
     * @param direction The direction to set for the button text.
     * @param buttonColor The color to set each direction button to when active.
     * @param buttonTextColor The color to set the text of each direction button when active.
     */
    private void addDirectionButton(ViewGroup parent, String direction, int buttonColor, int buttonTextColor) {
        ToggleColorOnClick toggleBtnColor = new ToggleColorOnClick(buttonColor, buttonTextColor, this);
        ToggleButton btn = (ToggleButton) LayoutInflater.from(this).inflate(R.layout.button_direction, parent, false);
        parent.addView(btn);

        Optional.ofNullable(direction)
                .map(validDirection -> {
                    btn.setOnCheckedChangeListener(toggleBtnColor);
                    btn.setText(validDirection);
                    btn.setTextOn(validDirection);
                    btn.setTextOff(validDirection);
                    btn.setBackgroundTintList(Tints.forColoredButton(this, getColor(R.color.white)));
                    return btn;
                })
                .orElseGet(() -> {
                    btn.setVisibility(View.INVISIBLE);
                    return btn;
        });
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
    }
}
