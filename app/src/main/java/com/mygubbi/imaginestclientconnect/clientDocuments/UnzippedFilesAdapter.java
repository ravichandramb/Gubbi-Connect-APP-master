package com.mygubbi.imaginestclientconnect.clientDocuments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mygubbi.imaginestclientconnect.R;
import com.mygubbi.imaginestclientconnect.models.UnzippedFiles;

import java.util.ArrayList;

public class UnzippedFilesAdapter extends BaseAdapter {

    private ArrayList<UnzippedFiles> listData;
    private LayoutInflater layoutInflater;

    public UnzippedFilesAdapter(Context context, ArrayList<UnzippedFiles> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_unzipped_file, parent, false);
            holder = new ViewHolder();
            holder.textFileName = convertView.findViewById(R.id.textFileName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textFileName.setText(listData.get(position).getFileName());

        return convertView;
    }

    static class ViewHolder {
        TextView textFileName;
    }

}