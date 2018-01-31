package com.jesseoberstein.alert.listeners.alarm;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.jesseoberstein.alert.fragments.dialog.alarm.RemoveAlarmDialog;
import com.jesseoberstein.alert.fragments.dialog.route.RemoveRouteDialog;
import com.jesseoberstein.alert.models.CustomListItem;

import static android.widget.AdapterView.OnItemLongClickListener;
import static com.jesseoberstein.alert.utils.Constants.ALARM_ID;
import static com.jesseoberstein.alert.utils.Constants.NICKNAME;
import static com.jesseoberstein.alert.utils.Constants.ROUTE;

public class RemoveAlarmOnLongClick implements OnItemLongClickListener {
    private Activity activity;

    public RemoveAlarmOnLongClick(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        CustomListItem item = (CustomListItem) parent.getItemAtPosition(position);
        Bundle bundle = new Bundle();
        bundle.putInt(ALARM_ID, (int) item.getId());
        bundle.putString(NICKNAME, item.getPrimaryText());

        DialogFragment dialog = new RemoveAlarmDialog();
        dialog.setArguments(bundle);
        dialog.show(this.activity.getFragmentManager(), "RemoveAlarmDialog");
        return true;
    }
}
