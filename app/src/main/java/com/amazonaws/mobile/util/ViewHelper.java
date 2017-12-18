//
// Copyright 2017 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.19
//
package com.amazonaws.mobile.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Utilities for Views.
 */
public final class ViewHelper {
    /**
     * Displays a modal dialog with an OK button.
     *
     * @param activity invoking activity
     * @param title title to display for the dialog
     * @param body content of the dialog
     */
    public static void showDialog(final Activity activity, final String title, final String body) {
        if (null == activity) {
            return;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(body);
        builder.setNeutralButton(android.R.string.ok, null);
        builder.show();
    }

    /**
     * Displays a modal dialog.
     *
     * @param activity invoking activity
     * @param title title to display for the dialog
     * @param body content of the dialog
     * @param positiveButton String for positive button
     * @param negativeButton String for negative button
     * @param negativeButtonListener  the listener which should be invoked when a negative button is pressed
     * @param positiveButtonListener  the listener which should be invoked when a positive button is pressed
     */
    public static void showDialog(final Activity activity, final String title, final String body, final String positiveButton, final DialogInterface.OnClickListener positiveButtonListener, final String negativeButton, final DialogInterface.OnClickListener negativeButtonListener){
        if (null == activity) {
            return;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(body);
        builder.setPositiveButton(positiveButton,positiveButtonListener);
        builder.setNegativeButton(negativeButton, negativeButtonListener);
        builder.show();
    }
}
