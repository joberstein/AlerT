package com.jesseoberstein.alert.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.jesseoberstein.alert.activities.alarms.ViewAlarms;

import javax.inject.Inject;

import static android.content.pm.PackageManager.*;

public class ViewAlarmsWithPermissions extends ViewAlarms {
    private static final int REQUEST_PERMISSION_SET_TIME = 1;
    private static final String SET_TIME_PERMISSION = Manifest.permission.SET_TIME;

    @Inject
    AlarmManager alarmManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean hasSetTimePermission = checkSelfPermission(SET_TIME_PERMISSION) == PERMISSION_GRANTED;
        boolean shouldShowSetTimeRationale = shouldShowRequestPermissionRationale(SET_TIME_PERMISSION);

        if (!hasSetTimePermission && !shouldShowSetTimeRationale) {
            requestPermissions(new String[]{SET_TIME_PERMISSION}, REQUEST_PERMISSION_SET_TIME);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_SET_TIME: {
                if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    alarmManager.setTime(1L);
                }
            }
        }
    }
}
