package com.mygubbi.imaginestclientconnect.clientIssues.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mygubbi.imaginestclientconnect.R;
import com.mygubbi.imaginestclientconnect.helpers.GlideApp;

import java.util.ArrayList;

/**
 * @author Hemanth
 * @since 4/19/2018
 */
public class IssuesPhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "IssuesPhotoAdapter";

    private Context context;
    private ArrayList<String> photosList;

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    public IssuesPhotoAdapter(Context context, ArrayList<String> photosList) {
        this.context = context;
        this.photosList = photosList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            //Inflating recycle view item layout
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photos, parent, false);
            return new PhotosViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            //Inflating footer view
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_footer, parent, false);
            return new FooterViewHolder(view);
        }

        //Some error occurs then exception occurs
        throw new RuntimeException("There is no view that matches the type " + viewType + " + make sure your using types correctly");
    }

    @SuppressLint("LogNotTimber")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();

        if (holder instanceof PhotosViewHolder) {
            PhotosViewHolder photosViewHolder = (PhotosViewHolder) holder;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(photosList.get(position), options);

            GlideApp.with(context)
                    .load(bitmap)
                    .centerCrop()
                    .into(photosViewHolder.imageView);
        } else if (holder instanceof FooterViewHolder) {
            // No implementation needed as click action is handled in hosting activity

            if (getItemCount() > 1) {
                ((FooterViewHolder) holder).textAddImage.setText(context.getString(R.string.change_photo));
            } else {
                ((FooterViewHolder) holder).textAddImage.setText(context.getString(R.string.add_photo));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        // Add +1 to array size to allow extra view for footer.
        return photosList.size() + 1;
    }

    class PhotosViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        PhotosViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        private TextView textAddImage;

        FooterViewHolder(View itemView) {
            super(itemView);

            textAddImage = itemView.findViewById(R.id.text_add_image);
        }
    }

}
