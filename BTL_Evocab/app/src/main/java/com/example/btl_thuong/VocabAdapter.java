package com.example.btl_thuong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class VocabAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<VocabItem> vocabList;

    public VocabAdapter(Context context, ArrayList<VocabItem> vocabList) {
        this.context = context;
        this.vocabList = vocabList;
    }

    @Override
    public int getCount() {
        return vocabList.size();
    }

    @Override
    public Object getItem(int position) {
        return vocabList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.item_vocab, parent, false);
        }

        //Lay doi tuong tu vung o vi tri hien tai
        VocabItem vocab = vocabList.get(position);

        TextView tvTerm = convertView.findViewById(R.id.tvTerminology);
        TextView tvType = convertView.findViewById(R.id.tvType);
        TextView tvDef = convertView.findViewById(R.id.tvDefinition);

        tvTerm.setText(vocab.getTerminology());
        tvType.setText(vocab.getType());
        tvDef.setText(vocab.getDefinition());
        return convertView;
    }
}











