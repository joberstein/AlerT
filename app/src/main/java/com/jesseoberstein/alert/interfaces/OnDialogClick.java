package com.jesseoberstein.alert.interfaces;

import android.os.Bundle;

public interface OnDialogClick {
    void onAddSelected(Bundle selected);
    void onRemoveSelected(Bundle selected);
    void onCancelSelected(Bundle selected);
}
