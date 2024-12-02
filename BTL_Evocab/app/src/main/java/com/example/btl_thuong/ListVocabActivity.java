package com.example.btl_thuong;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ListVocabActivity extends AppCompatActivity {
    Button btnMix;
    ImageButton btnMenu_lstVocab, btnBack_lstVocab, btnAddVocab;
    TextView tvFolderName_lstVocab, tvNumber_vocab;
    ListView lvVocab;
    ArrayList<VocabItem> myListVocab = new ArrayList<>();
    VocabAdapter myAdapter;
    DBManager dbManager;
    FolderItem currentFolder;

    View  vDetailVocab;
    TextView tvTerm, tvType, tvDef, tvEx;
    EditText edtAddTerm, edtAddDef, edtAddEx;
    EditText edtEditTerm, edtEditDef, edtEditEx;
    Button btn_detail_edit, btn_detail_delete, btn_edit_save_vocab, btn_edit_cancel_vocab,
            btn_add_add_vocab, btn_add_Cancel_vocab;
    ImageView btn_speak;
    TextToSpeech textToSpeech;
    ImageView img_detail_close;
    Spinner sp_Type;

    MenuManager menuManager;
    ViewPager2 vpFlashCard;
    List<VocabItem> myListFlashCard;
    FlashCardAdapter flashCardAdapter;
    boolean isMixEnabled = false;
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
        SharedPreferences prefs = getSharedPreferences("UserPref", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        if(!isLoggedIn){
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        getView();

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    //Set default language
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);
                    if (result == textToSpeech.LANG_MISSING_DATA || result == textToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported!");
                    }
                } else {
                    Log.e("TTS", "Initialization failed!");
                }
            }
        });

//menu
        menuManager=new MenuManager();
        menuManager.setUpMenuVocab(this);

//Khoi tao CSDL
        dbManager = new DBManager(this);
        dbManager.open();
//Receive data from Intent
        receiceIntent();
        btnBack_lstVocab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//Add vocab
        btnAddVocab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addButtonClick();
            }
        });
        setFlashCard();

//Tron the
        btnMix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMixEnabled=!isMixEnabled;
                if(myListFlashCard!=null){
                    if (isMixEnabled) {
                        btnMix.setText("Mixed");
                        Collections.shuffle(myListFlashCard);
                        flashCardAdapter.notifyDataSetChanged();
                    }else {
                        btnMix.setText("Mix");
                            myListFlashCard.sort((v1, v2)->Integer.compare(v1.getVocabID(), v2.getVocabID()));
                            flashCardAdapter.notifyDataSetChanged();
                            setFlashCard();
                    }
                }
            }
        });
    }

    public void getView() {
        btnMenu_lstVocab = findViewById(R.id.btnMenu_lstVocab);
        btnBack_lstVocab = findViewById(R.id.btnBack_lstVocab);
        btnAddVocab = findViewById(R.id.btnAddVocab);
        tvFolderName_lstVocab = findViewById(R.id.tvFolderName_lstVocab);
        tvNumber_vocab = findViewById(R.id.tv_number_vocab);
        lvVocab = findViewById(R.id.lvVocab);
        vDetailVocab = findViewById(R.id.lo_detail_vocab);
        vDetailVocab.setVisibility(View.GONE);
        vpFlashCard = findViewById(R.id.vpFlashCard);
        btnMix=findViewById(R.id.btnMix);
    }

    public void getView_add(View dialogView) {
        edtAddTerm = dialogView.findViewById(R.id.edt_add_Terminology);
        sp_Type = dialogView.findViewById(R.id.sp_add_type);
        edtAddDef = dialogView.findViewById(R.id.edt_add_Definition);
        edtAddEx = dialogView.findViewById(R.id.edt_add_example);
        btn_add_add_vocab = dialogView.findViewById(R.id.btn_add_add_vocab);
        btn_add_Cancel_vocab = dialogView.findViewById(R.id.btn_add_Cancel_vocab);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                ListVocabActivity.this,
                R.array.vocab_type,
                R.layout.item_spinner
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Type.setAdapter(adapter);
    }

    public void getView_detail() {
        tvTerm = findViewById(R.id.tv_detail_terminology);
        tvType = findViewById(R.id.tv_detail_type);
        tvDef = findViewById(R.id.tv_detail_definition);
        tvEx = findViewById(R.id.tv_detail_example);
        btn_detail_edit = findViewById(R.id.btn_detail_edit);
        btn_detail_delete = findViewById(R.id.btn_detail_delete);
        img_detail_close = findViewById(R.id.img_detail_close);
        btn_speak = findViewById(R.id.btn_speak);

    }

    public void getView_edit(View dialogView) {
        edtEditTerm = dialogView.findViewById(R.id.edt_edit_terminology);
        edtEditDef = dialogView.findViewById(R.id.edt_edit_definition);
        edtEditEx = dialogView.findViewById(R.id.edt_edit_example);
        btn_edit_save_vocab = dialogView.findViewById(R.id.btn_edit_save_vocab);
        btn_edit_cancel_vocab = dialogView.findViewById(R.id.btn_edit_cancel_vocab);
        sp_Type = dialogView.findViewById(R.id.sp_edit_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                ListVocabActivity.this,
                R.array.vocab_type,
                R.layout.item_spinner
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Type.setAdapter(adapter);
    }

    public void setFlashCard(){
        //chuyen tu myListVocab thanh myListFlashCard
        myListFlashCard = new ArrayList<>();
        for (VocabItem vocab : myListVocab) {
            myListFlashCard.add(new VocabItem(vocab.getTerminology(), vocab.getType(), vocab.getDefinition()));
        }

        //set adapter cho Viewpage 2
        flashCardAdapter = new FlashCardAdapter(myListFlashCard);
        vpFlashCard.setAdapter(flashCardAdapter);
    }
    //Receive data from Intent
    public void receiceIntent() {
        Intent i = getIntent();
        if (i != null && i.hasExtra("folder")) {
            currentFolder = (FolderItem) i.getSerializableExtra("folder");
            if (currentFolder != null) {
                tvFolderName_lstVocab.setText(currentFolder.getFolderName());
                myListVocab = dbManager.getAllVocab(currentFolder.getId());
                myAdapter = new VocabAdapter(ListVocabActivity.this, myListVocab);
                lvVocab.setAdapter(myAdapter);
                tvNumber_vocab.setText(String.valueOf(myListVocab.size()));
            } else {
                Toast.makeText(this, "No information found for this folder!", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "No data found for this folder!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //call this function at VocabAdapter
    public void clickItemVocab(VocabItem vocab) {
        runOnUiThread(() -> {
            lvVocab.setVisibility(View.GONE);
            vDetailVocab.setVisibility(View.VISIBLE);
            getView_detail();
            tvTerm.setText(vocab.getTerminology());
            tvType.setText(vocab.getType());
            tvDef.setText(vocab.getDefinition());
            tvEx.setText(vocab.getExample());
        });
        img_detail_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vDetailVocab.setVisibility(View.GONE);
                lvVocab.setVisibility(View.VISIBLE);
            }
        });
        btn_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = tvTerm.getText().toString().trim();
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
        btn_detail_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editButtonClick(vocab);
            }
        });
        btn_detail_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButtonDelete(vocab.getVocabID());
            }
        });
    }

    public void clickButtonDelete(int id) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.delete_dialog, null);
        Button btnYes = dialogView.findViewById(R.id.btn_delete_yes_vocab);
        Button btnNo = dialogView.findViewById(R.id.btn_delete_no_vocab);
        AlertDialog.Builder builder = new AlertDialog.Builder(ListVocabActivity.this);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // Đặt nền của dialog có độ trong suốt bao phủ toàn bộ màn hình
        dialog.getWindow().setDimAmount(0f); // Làm mờ toàn bộ nền nếu cần
        dialog.setCanceledOnTouchOutside(false);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = dbManager.deleteVocab(id) > 0;
                if (result) {
                    myListVocab.clear();
                    myListVocab.addAll(dbManager.getAllVocab(currentFolder.getId()));
                    myAdapter.notifyDataSetChanged();
                    Toast.makeText(ListVocabActivity.this, "Delete vocab successfully!", Toast.LENGTH_SHORT).show();
                    setFlashCard();
                    tvNumber_vocab.setText(String.valueOf(myListVocab.size()));
                    isMixEnabled = false;
                    btnMix.setText("Mix");
                } else {
                    Toast.makeText(ListVocabActivity.this, "Delete vocab failed!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                vDetailVocab.setVisibility(View.GONE);
                lvVocab.setVisibility(View.VISIBLE);
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void addButtonClick(){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_vocab, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(ListVocabActivity.this);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // Đặt nền của dialog có độ trong suốt bao phủ toàn bộ màn hình
        dialog.getWindow().setDimAmount(0f); // Làm mờ toàn bộ nền nếu cần
        dialog.setCanceledOnTouchOutside(false);
        getView_add(dialogView);
        btn_add_add_vocab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String term = edtAddTerm.getText().toString().trim();
                String type = sp_Type.getSelectedItem().toString().trim();
                String def = edtAddDef.getText().toString().trim();
                String ex = edtAddEx.getText().toString().trim();

                if (!term.isEmpty() && !def.isEmpty()) {
                    if (currentFolder != null) {
                        VocabItem newVocab = new VocabItem(0, term, type, def, ex, currentFolder.getId());
                        long result = dbManager.insertVocab(newVocab);
                        if (result != -1) {
                            myListVocab.clear();
                            myListVocab.addAll(dbManager.getAllVocab(currentFolder.getId()));
                            myAdapter.notifyDataSetChanged();
                            tvNumber_vocab.setText(String.valueOf(myListVocab.size()));
                            Toast.makeText(ListVocabActivity.this, "Add new vocab successfully!", Toast.LENGTH_SHORT).show();
                            edtAddTerm.setText("");
                            sp_Type.setSelection(0);
                            edtAddDef.setText("");
                            edtAddEx.setText("");
                            setFlashCard();
                            isMixEnabled = false;
                            btnMix.setText("Mix");
                            dialog.dismiss();
                        } else {
                            Toast.makeText(ListVocabActivity.this, "Add new vocab failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(ListVocabActivity.this, "Please enter Terminology and Definition!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_add_Cancel_vocab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtAddTerm.setText("");
                sp_Type.setSelection(0);
                edtAddDef.setText("");
                edtAddEx.setText("");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void editButtonClick(VocabItem Vocab){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_vocab, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(ListVocabActivity.this);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // Đặt nền của dialog có độ trong suốt bao phủ toàn bộ màn hình
        dialog.getWindow().setDimAmount(0f); // Làm mờ toàn bộ nền nếu cần
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        getView_edit(dialogView);
        edtEditTerm.setText(Vocab.getTerminology());
        edtEditDef.setText(Vocab.getDefinition());
        edtEditEx.setText(Vocab.getExample());
        String[] vocabTypes = getResources().getStringArray(R.array.vocab_type);
        int position = -1;
        for (int i = 0; i < vocabTypes.length; i++) {
            if (vocabTypes[i].equals(Vocab.getType().toString().trim())) {
                position = i;
                break;
            }
        }
        sp_Type.setSelection(position);
        btn_edit_save_vocab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String term = edtEditTerm.getText().toString().trim();
                String type = sp_Type.getSelectedItem().toString().trim();
                String def = edtEditDef.getText().toString().trim();
                String ex = edtEditEx.getText().toString().trim();
                int f = currentFolder.getId();

                if (!term.isEmpty() && !def.isEmpty()) {
                    VocabItem vocab = new VocabItem(term, type, def, ex, f);
                    long i = dbManager.updateVocab(vocab, Vocab.getVocabID());
                    if (i > 0) {
                        myListVocab.clear();
                        myListVocab.addAll(dbManager.getAllVocab(currentFolder.getId()));
                        myAdapter.notifyDataSetChanged();
                        Toast.makeText(ListVocabActivity.this, "Edit vocab successfully!", Toast.LENGTH_SHORT).show();
                        setFlashCard();
                        isMixEnabled = false;
                        btnMix.setText("Mix");
                        tvTerm.setText(vocab.getTerminology());
                        tvType.setText(vocab.getType());
                        tvDef.setText(vocab.getDefinition());
                        tvEx.setText(vocab.getExample());
                        dialog.dismiss();
                    } else {
                        Toast.makeText(ListVocabActivity.this, "Edit vocab failed!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ListVocabActivity.this, "Please enter Terminology and Definition!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_edit_cancel_vocab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}

















