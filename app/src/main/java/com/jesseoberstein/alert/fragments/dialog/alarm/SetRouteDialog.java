package com.jesseoberstein.alert.fragments.dialog.alarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.databinding.AlarmRouteBinding;
import com.jesseoberstein.alert.models.AutoComplete;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.utils.ActivityUtils;
import com.jesseoberstein.alert.utils.LiveDataUtils;

import java.util.List;

import lombok.AllArgsConstructor;

import static android.widget.AdapterView.OnItemClickListener;
import static com.jesseoberstein.alert.utils.Constants.DELAY_DIALOG_DISMISS;

/**
 * A dialog fragment that shows a dialog for selecting the alarm route.
 */
@AllArgsConstructor
public class SetRouteDialog extends AlarmModifierDialog {

    private final List<Route> routes;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlarmRouteBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_alarm_dialog_route, null, false);
        binding.setLifecycleOwner(requireActivity());
        binding.setViewModel(this.viewModel);

        AutoComplete<Route> autoComplete = new AutoComplete<>(routes, this::onAutoCompleteItemSelected);
        autoComplete.attachAdapter(requireActivity());
        binding.setAutocomplete(autoComplete);

        AutoCompleteTextView editText = binding.getRoot().findViewById(R.id.alarm_route);
        LiveDataUtils.observeOnce(requireActivity(), this.viewModel.getRoute(), route -> {
            // Set the text first before setting the cursor - autocompleteview does not propagate the databinding yet
            editText.setText(route.toString());
            editText.setSelection(route.toString().length());
        });

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.route_dialog_title)
                .setView(binding.getRoot())
                .setOnKeyListener(this::onKeyPressed)
                .create();

        ActivityUtils.showKeyboard(dialog.getWindow());
        return dialog;
    }

    void onAutoCompleteItemSelected(AdapterView adapterView, View view, int i, long l) {
        Route selectedRoute = (Route) adapterView.getItemAtPosition(i);
        this.viewModel.getRoute().setValue(selectedRoute);

        new android.os.Handler().postDelayed(this::dismiss, DELAY_DIALOG_DISMISS);
    }

    boolean onKeyPressed(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            dialogInterface.dismiss();
        }

        return true;
    }

    @BindingAdapter({"adapter", "validator", "onItemClick"})
    public static <T> void setupAutocomplete(AutoCompleteTextView autoComplete,
                                             ArrayAdapter<T> adapter,
                                             AutoCompleteTextView.Validator validator,
                                             OnItemClickListener onItemClick) {
        autoComplete.setAdapter(adapter);
        autoComplete.setValidator(validator);
        autoComplete.setOnItemClickListener(onItemClick);
    }
}
