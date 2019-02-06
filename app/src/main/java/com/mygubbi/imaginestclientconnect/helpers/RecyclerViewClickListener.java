package com.mygubbi.imaginestclientconnect.helpers;

import android.view.View;

/**
 * @author Hemanth
 * @since 3/27/2018
 */
public interface RecyclerViewClickListener {

    void onClick(View view, int position);

    void onLongClick(View view, int position);

}
