package com.example.btl_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FDAdapter  extends ArrayAdapter<FolderItem> {
     Context context;
     int layout;
     ArrayList<FolderItem> myFDItem;

    public FDAdapter(@NonNull Context context, int resource, ArrayList<FolderItem> myFDItem) {
        super(context, resource,myFDItem);
        this.context = context;
        this.layout = resource;
        this.myFDItem = myFDItem;
    }

    @Override
    public int getCount() {
        return myFDItem.size();
    }

    @Nullable
    @Override
    public FolderItem getItem(int position) {
        return myFDItem.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentView = convertView;
        if (currentView == null) {
            currentView = LayoutInflater.from(context).inflate(layout, parent, false);
        }

        FolderItem contact = getItem(position);
        TextView tvName = currentView.findViewById(R.id.namefd);

        // Kiểm tra contact có khác null trước khi truy cập
        if (contact != null) {
            tvName.setText(contact.getName());
        } else {
            tvName.setText(""); // Hoặc giá trị mặc định nếu contact là null
        }

        return currentView;
    }
}
