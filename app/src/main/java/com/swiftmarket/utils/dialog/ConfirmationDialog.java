package com.swiftmarket.utils.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ConfirmationDialog {

    public interface ConfirmationDialogListener {
        void onConfirm();
        default void onCancel(){}
    }

    public static void showConfirmationDialog(Context context, String title, String message, final ConfirmationDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (listener != null) {
                        listener.onConfirm();
                    }
                })
                .setNegativeButton("No", (dialog, which) -> {
                    if (listener != null) {
                        listener.onCancel();
                    }
                    dialog.dismiss();
                })
                .show();
    }
}
