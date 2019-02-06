package com.mygubbi.imaginestclientconnect.clientIssues.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mygubbi.imaginestclientconnect.helpers.TouchImageView;

import java.util.ArrayList;

/**
 * @author Hemanth
 * @since 4/5/2018
 */
public class TouchImageAdapter extends PagerAdapter {

//    private static final String TAG = "TouchImageAdapter";

    private Context context;
    private ArrayList<String> imagesList;
    private ImageLoadListener listener;

    public TouchImageAdapter(Context context, ArrayList<String> imagesList, @Nullable ImageLoadListener listener) {
        this.context = context;
        this.imagesList = imagesList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    @NonNull
    @Override
    public View instantiateItem(@NonNull ViewGroup container, int position) {
        final TouchImageView imageView = new TouchImageView(container.getContext());

        if (imagesList != null && !imagesList.isEmpty()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagesList.get(position), options);

            if (listener != null) {
                new Handler().postDelayed(() -> {
                    imageView.setImageBitmap(bitmap);
                    listener.onImageLoaded();
                }, 2000);
            } else {
                imageView.setImageBitmap(bitmap);
            }
        }

        container.addView(imageView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public interface ImageLoadListener {

        void onImageLoaded();
    }
}