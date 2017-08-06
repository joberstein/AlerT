package com.jesseoberstein.alert.interfaces;

import android.os.Bundle;

public interface OnRouteDialogClick {
    void onAddSelectedRoute(Bundle selectedRoute);
    void onRemoveSelectedRoute(Bundle selectedRoute);
    void onCancelSelectedRoute(Bundle selectedRoute);
}
