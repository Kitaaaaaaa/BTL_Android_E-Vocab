package com.example.btl_thuong;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FlashCardAdapter extends RecyclerView.Adapter<FlashCardAdapter.FlashViewHolder> {
    private List<VocabItem> lstFlashCard;

    public FlashCardAdapter(List<VocabItem> lstFlashCard) {
        this.lstFlashCard = lstFlashCard;
    }


    @NonNull
    @Override
    public FlashCardAdapter.FlashViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flashcard, parent, false);
        return new FlashViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashCardAdapter.FlashViewHolder holder, int position) {
        VocabItem fc = lstFlashCard.get(position);

        //Hien thi thuat ngu o mat truoc
        holder.tvTerm.setText(fc.getTerminology());

        //Hien thi loai va dinh nghia o mat sau
        holder.tvType.setText(fc.getType());
        holder.tvDefinition.setText(fc.getDefinition());

    }


    @Override
    public int getItemCount() {
        return lstFlashCard.size();
    }

    public static class FlashViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTerm;
        private TextView tvDefinition, tvType;
        private View frontCard, backCard;


        public FlashViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTerm = itemView.findViewById(R.id.tvFlashCard_term);
            tvDefinition = itemView.findViewById(R.id.tvFlashCard_definition);
            tvType = itemView.findViewById(R.id.tvFlashCard_type);

            frontCard = itemView.findViewById(R.id.frontCard);
            backCard = itemView.findViewById(R.id.backCard);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (frontCard.getVisibility() == View.VISIBLE) {
                        flipCard(frontCard, backCard);
                    } else {
                        flipCard(backCard, frontCard);
                    }
                }
            });
        }

        private void flipCard(View front, View back) {
            front.animate().rotationX(90).setDuration(300).withEndAction(() -> {
                front.setVisibility(View.GONE);
                back.setRotationX(-90);
                back.setVisibility(View.VISIBLE);
                back.animate().rotationX(0).setDuration(200).start();
            }).start();
        }
    }
}
