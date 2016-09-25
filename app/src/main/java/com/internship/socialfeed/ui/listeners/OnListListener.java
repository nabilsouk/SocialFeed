package com.internship.socialfeed.ui.listeners;

import android.view.View;

/**
 * Created by Nader on 16-Aug-16.
 */

public interface OnListListener {
    /**
     * Indicates the click of a row.
     */
    void onRowClick(View view, int position);

    /**
     * Indicates the click of a row's title.
     * @param position
     */
    void onTitleSelected(int position);
}
