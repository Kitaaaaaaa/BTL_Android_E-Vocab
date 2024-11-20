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

public class VocabAdapter extends ArrayAdapter<MyVocab> {
    Context context;
    int layout;
    ArrayList<MyVocab> myListVocab;


    public VocabAdapter(@NonNull Context context, int resource, ArrayList<MyVocab> lstVocab) {
        super(context, resource, lstVocab);
        this.context = context;
        this.layout = resource;
        this.myListVocab = lstVocab;
    }

    @Nullable
    @Override
    public MyVocab getItem(int position) {
        return myListVocab.get(position);
    }

    @Override
    public int getCount() {
        return myListVocab.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            //load layout item len de hien thi du lieu
            convertView= LayoutInflater.from(context).inflate(layout, parent, false);
        }
        //Lay doi tuong vocab tai vi tri position
        MyVocab v = getItem(position);
        TextView tvTerminilogy = convertView.findViewById(R.id.tvTerminology);
        TextView tvType = convertView.findViewById(R.id.tvType);
        TextView tvDefinition=convertView.findViewById(R.id.tvDefinition);

        //data bind
        tvTerminilogy.setText(v.getTerminology());
        tvType.setText(v.getType());
        tvDefinition.setText(v.getDefinition());
        return convertView;
    }
}
