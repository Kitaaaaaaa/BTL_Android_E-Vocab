package com.example.btl_1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ListVocabActivity extends AppCompatActivity {
    Button btnMix;
    ImageButton btnMenu_lstVocab, btnBack_lstVocab, btnAddVocab;
    TextView tvFolderName_lstVocab, tvNumber_vocab;
    ListView lvVocab;
    ArrayList<MyVocab> myListVocab;
    VocabAdapter myAdapter;
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_vocab);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getView();
        myListVocab = new ArrayList<>();
        myAdapter = new VocabAdapter(ListVocabActivity.this, R.layout.item_vocab, myListVocab);
        lvVocab.setAdapter(myAdapter);

        dbManager = new DBManager(ListVocabActivity.this);
        dbManager.open();
        myListVocab.addAll(dbManager.getAllVocab());
        myAdapter.notifyDataSetChanged();

        //Dem so luong vocab
        int vocabCount=dbManager.getNumberOfVocab();
        tvNumber_vocab.setText(String.valueOf(vocabCount));

        btnAddVocab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void getView(){
        btnMenu_lstVocab = findViewById(R.id.btnMenu_lstVocab);
        btnBack_lstVocab=findViewById(R.id.btnBack_lstVocab);
        btnAddVocab=findViewById(R.id.btnAddVocab);
        tvFolderName_lstVocab=findViewById(R.id.tvFolderName_lstVocab);
        tvNumber_vocab=findViewById(R.id.tv_number_vocab);
        lvVocab=findViewById(R.id.lvVocab);

    }
}