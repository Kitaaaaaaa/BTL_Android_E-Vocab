package com.example.btl_thuong;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ListVocabActivity extends AppCompatActivity {
    //Chua xu ly menu, flashcard
    Button btnMix;
    ImageButton btnMenu_lstVocab, btnBack_lstVocab, btnAddVocab;
    TextView tvFolderName_lstVocab, tvNumber_vocab;
    ListView lvVocab;
    ArrayList<VocabItem> myListVocab = new ArrayList<>();
    VocabAdapter myAdapter;
    DBManager dbManager;
    FolderItem currentFolder;

    View vAddVocab, vEditVocab, vDeleteVocab, vDetailVocab;
    TextView tvTerm, tvType, tvDef, tvEx;
    EditText edtAddTerm, edtAddType, edtAddDef, edtAddEx;
    EditText edtEditTerm, edtEditType, edtEditDef, edtEditEx;
    Button btn_detail_edit, btn_detail_delete, btn_edit_save_vocab, btn_edit_cancel_vocab,
            btn_add_add_vocab, btn_add_Cancel_vocab;
    ImageView img_detail_close, img_edit_close;

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
                Intent i = new Intent(ListVocabActivity.this, ListFolderActivity.class);
                startActivity(i);
            }
        });

//Add vocab
        btnAddVocab.setOnClickListener(v -> {
            // Kiểm tra nếu vDetailVocab đang hiển thị thì ẩn nó
            if (vDetailVocab.getVisibility() == View.VISIBLE) {
                vDetailVocab.setVisibility(View.GONE);
            }

            // Kiểm tra nếu vEditVocab đang hiển thị thì ẩn nó
            if (vEditVocab.getVisibility() == View.VISIBLE) {
                vEditVocab.setVisibility(View.GONE);
            }
            vAddVocab.setVisibility(View.VISIBLE);
            lvVocab.setVisibility(View.GONE);
            getView_add();
            addNewVocab();
        });

        lvVocab.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                VocabItem vocab = myListVocab.get(position);
                clickItemVocab(vocab);
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
        vAddVocab = findViewById(R.id.lo_add_vocab);
        vEditVocab = findViewById(R.id.lo_edit_vocab);
        vDetailVocab = findViewById(R.id.lo_detail_vocab);
        vAddVocab.setVisibility(View.GONE);
        vEditVocab.setVisibility(View.GONE);
        vDetailVocab.setVisibility(View.GONE);
        vpFlashCard = findViewById(R.id.vpFlashCard);
        btnMix=findViewById(R.id.btnMix);
    }

    public void getView_add() {
        edtAddTerm = findViewById(R.id.edt_add_Terminology);
        edtAddType = findViewById(R.id.edt_add_type);
        edtAddDef = findViewById(R.id.edt_add_Definition);
        edtAddEx = findViewById(R.id.edt_add_example);
        btn_add_add_vocab = findViewById(R.id.btn_add_add_vocab);
        btn_add_Cancel_vocab = findViewById(R.id.btn_add_Cancel_vocab);
    }

    public void getView_detail() {
        tvTerm = findViewById(R.id.tv_detail_terminology);
        tvType = findViewById(R.id.tv_detail_type);
        tvDef = findViewById(R.id.tv_detail_definition);
        tvEx = findViewById(R.id.tv_detail_example);
        btn_detail_edit = findViewById(R.id.btn_detail_edit);
        btn_detail_delete = findViewById(R.id.btn_detail_delete);
        img_detail_close = findViewById(R.id.img_detail_close);
    }

    public void getView_edit() {
        edtEditTerm = findViewById(R.id.edt_edit_terminology);
        edtEditType = findViewById(R.id.edt_edit_type);
        edtEditDef = findViewById(R.id.edt_edit_definition);
        edtEditEx = findViewById(R.id.edt_edit_example);
        btn_edit_save_vocab = findViewById(R.id.btn_edit_save_vocab);
        btn_edit_cancel_vocab = findViewById(R.id.btn_edit_cancel_vocab);
        img_edit_close = findViewById(R.id.img_edit_close);
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

    public void addNewVocab() {
        btn_add_add_vocab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String term = edtAddTerm.getText().toString().trim();
                String type = edtAddType.getText().toString().trim();
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
                            edtAddType.setText("");
                            edtAddDef.setText("");
                            edtAddEx.setText("");
                            vAddVocab.setVisibility(View.GONE);
                            lvVocab.setVisibility(View.VISIBLE);
                            setFlashCard();
                            isMixEnabled = false;
                            btnMix.setText("Mix");
                        } else {
                            Toast.makeText(ListVocabActivity.this, "Add new vocab failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(ListVocabActivity.this, "Please enter Terminology and Definition!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_add_Cancel_vocab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtAddTerm.setText("");
                edtAddType.setText("");
                edtAddDef.setText("");
                edtAddEx.setText("");
                vAddVocab.setVisibility(View.GONE);
                lvVocab.setVisibility(View.VISIBLE);
            }
        });
    }

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

        btn_detail_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vDetailVocab.setVisibility(View.GONE);
                vEditVocab.setVisibility(View.VISIBLE);
                getView_edit();

                edtEditTerm.setText(vocab.getTerminology());
                edtEditType.setText(vocab.getType());
                edtEditDef.setText(vocab.getDefinition());
                edtEditEx.setText(vocab.getExample());

                clickButtonEdit(vocab.getVocabID());
            }
        });

        btn_detail_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButtonDelete(vocab.getVocabID());
            }
        });
    }

    public void clickButtonEdit(int id) {
        img_edit_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vEditVocab.setVisibility(View.GONE);
                lvVocab.setVisibility(View.VISIBLE);
            }
        });
        btn_edit_save_vocab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String term = edtEditTerm.getText().toString().trim();
                String type = edtEditType.getText().toString().trim();
                String def = edtEditDef.getText().toString().trim();
                String ex = edtEditEx.getText().toString().trim();
                int f = currentFolder.getId();

                if (!term.isEmpty() && !def.isEmpty()) {
                    VocabItem vocab = new VocabItem(term, type, def, ex, f);
                    long i = dbManager.updateVocab(vocab, id);
                    if (i > 0) {
                        myListVocab.clear();
                        myListVocab.addAll(dbManager.getAllVocab(currentFolder.getId()));
                        myAdapter.notifyDataSetChanged();
                        Toast.makeText(ListVocabActivity.this, "Edit vocab successfully!", Toast.LENGTH_SHORT).show();
                        vEditVocab.setVisibility(View.GONE);
                        lvVocab.setVisibility(View.VISIBLE);
                        setFlashCard();
                        isMixEnabled = false;
                        btnMix.setText("Mix");
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
                vEditVocab.setVisibility(View.GONE);
                vDetailVocab.setVisibility(View.VISIBLE);
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
//        dialog.setCancelable(true);
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


}

















