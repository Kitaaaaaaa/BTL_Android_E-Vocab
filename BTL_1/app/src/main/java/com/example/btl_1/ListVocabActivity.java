package com.example.btl_1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class ListVocabActivity extends AppCompatActivity {
    Button btnMix;
    ImageButton btnMenu_lstVocab, btnBack_lstVocab, btnAddVocab;
    TextView tvFolderName_lstVocab, tvNumber_vocab, tvNo_Vocab;
    ListView lvVocab;
    ArrayList<MyVocab> myListVocab;
    VocabAdapter myAdapter;
    DBManager dbManager;
    private FolderItem folderItem;

    ViewPager2 vpFlashCard;
    List<FlashCard> myListFlashCard;
    FlashCardAdapter flashCardAdapter;
    ConstraintLayout layout_add_vocab, layout_edit_vocab, layout_detail_vocab;

    EditText etTerminology, etDefinity, etEx;
    Spinner spType;
    Button btnSave, btnCancel;

    Button bt_detail_cancel, bt_detail_edit, bt_detail_del;
    TextView tv_edit_terminology, tv_edit_Definity, tv_edit_ex, tv_edit_type;

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
        folderItem = (FolderItem) getIntent().getSerializableExtra("folder");
        if (folderItem == null) {
            Toast.makeText(this, "Không nhận được dữ liệu từ FolderActivity!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        tvFolderName_lstVocab.setText(folderItem.getName().toString());
        myListVocab = new ArrayList<>();
        myAdapter = new VocabAdapter(ListVocabActivity.this, R.layout.item_vocab, myListVocab);
        lvVocab.setAdapter(myAdapter);

        dbManager = new DBManager(ListVocabActivity.this);
        dbManager.open();
        myListVocab.addAll(dbManager.getVocabByFolderId(folderItem.getId()));
        myAdapter.notifyDataSetChanged();

        //kiem tra myListVocab rong hay khong
        if(myListVocab.isEmpty())
        {
            tvNo_Vocab.setVisibility(View.VISIBLE);
            vpFlashCard.setVisibility(View.GONE);
        }
        else
        {
            tvNo_Vocab.setVisibility(View.GONE);
            vpFlashCard.setVisibility(View.VISIBLE);
            //chuyen tu myListVocab thanh myListFlashCard
            myListFlashCard = new ArrayList<>();
            for (MyVocab vocab : myListVocab) {
                myListFlashCard.add(new FlashCard(vocab.getTerminology(), vocab.getType(), vocab.getDefinition()));
            }

            flashCardAdapter = new FlashCardAdapter(myListFlashCard);
            vpFlashCard.setAdapter(flashCardAdapter);
        }

        //Dem so luong vocab
        int vocabCount = myListVocab.size();
        tvNumber_vocab.setText(String.valueOf(vocabCount));

        btnAddVocab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvVocab.setVisibility(View.GONE);
                layout_add_vocab.setVisibility(View.VISIBLE);
                getView_add();
                onClickBT_add();
            }
        });
        lvVocab.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lvVocab.setVisibility(View.GONE);
                layout_detail_vocab.setVisibility(View.VISIBLE);
                getView_detail();
                MyVocab selectedVocab = myListVocab.get(i);
                tv_edit_terminology.setText(selectedVocab.getTerminology().toString());
                tv_edit_Definity.setText(selectedVocab.getDefinition().toString());
                tv_edit_type.setText(selectedVocab.getType().toString());
                tv_edit_ex.setText(selectedVocab.getExample().toString());
                onClickBT_detail(selectedVocab);
            }
        });
    }

    public void getView(){
        btnMenu_lstVocab = findViewById(R.id.btnMenu_lstVocab);
        btnAddVocab = findViewById(R.id.btnAddVocab);
        tvFolderName_lstVocab = findViewById(R.id.tvFolderName_lstVocab);
        tvNumber_vocab = findViewById(R.id.tv_number_vocab);
        lvVocab = findViewById(R.id.lvVocab);
        tvNo_Vocab = findViewById(R.id.tv_NoVocab);
        layout_add_vocab = findViewById(R.id.lo_add);
        layout_edit_vocab = findViewById(R.id.lo_edit);
        layout_detail_vocab = findViewById(R.id.lo_detail);
    }
    public  void getView_add(){
        etTerminology = findViewById(R.id.et_add_Terminology);
        etDefinity =  findViewById(R.id.et_add_Definity);
        etEx = findViewById(R.id.et_add_example);
        spType = findViewById(R.id.sp_add_type);
        btnSave = findViewById(R.id.bt_add_save);
        btnCancel = findViewById(R.id.bt_add_Cancel);
        //tạo adapter danh  sách loại từ lưu trong string.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                ListVocabActivity.this,
                R.array.vocab_type,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(adapter);
    }
    public  void getView_edit(){
        etTerminology = findViewById(R.id.et_edit_Terminology);
        etDefinity =  findViewById(R.id.et_edit_Definity);
        etEx = findViewById(R.id.et_edit_example);
        spType = findViewById(R.id.sp_edit_type);
        btnSave = findViewById(R.id.bt_edit_save);
        btnCancel = findViewById(R.id.bt_edit_Cancel);
        //tạo adapter danh  sách loại từ lưu trong string.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                ListVocabActivity.this,
                R.array.vocab_type,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(adapter);
    }
    public void getView_detail(){
        tv_edit_terminology = findViewById(R.id.tv_detail_Terminology);
        tv_edit_Definity = findViewById(R.id.tv_detail_Definity);
        tv_edit_type = findViewById(R.id.tv_detail_type);
        tv_edit_ex = findViewById(R.id.tv_detail_example);
        bt_detail_cancel = findViewById(R.id.bt_edit_Cancel);
        bt_detail_edit = findViewById(R.id.bt_detail_edit);
        bt_detail_del = findViewById(R.id.bt_detail_Delete);
    }
    public void onClickBT_add() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Terminology =  etTerminology.getText().toString().trim();
                String Definity = etDefinity.getText().toString().trim();
                String ex = etEx.getText().toString().trim();
                String type = spType.getSelectedItem().toString();
                MyVocab myVocab = new MyVocab(Terminology, type, Definity, ex);
                long result = dbManager.insVocab(myVocab, folderItem.getId());


                if(result > 0)
                {
                    Toast.makeText(ListVocabActivity.this, "Thêm dữ liệu thành công!", Toast.LENGTH_SHORT).show();
                    myListVocab.clear();
                    myListVocab.addAll(dbManager.getVocabByFolderId(folderItem.getId()));
                    myAdapter.notifyDataSetChanged();
                    etTerminology.setText("");
                    etDefinity.setText("");
                    etEx.setText("");
                    spType.setSelection(0);
                    layout_add_vocab.setVisibility(View.GONE);
                    lvVocab.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(ListVocabActivity.this, "Lỗi mẹ r!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etTerminology.setText("");
                etDefinity.setText("");
                etEx.setText("");
                spType.setSelection(0);
                layout_add_vocab.setVisibility(View.GONE);
                lvVocab.setVisibility(View.VISIBLE);
            }
        });
    }
    public void onClickBT_detail(MyVocab vocab){
        bt_detail_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_edit_terminology.setText("");
                tv_edit_Definity.setText("");
                tv_edit_type.setText("");
                tv_edit_ex.setText("");
                layout_detail_vocab.setVisibility(View.GONE);
                lvVocab.setVisibility(View.VISIBLE);
            }
        });
        bt_detail_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_detail_vocab.setVisibility(View.GONE);
                layout_edit_vocab.setVisibility(View.VISIBLE);
                getView_edit();
                etTerminology.setText(vocab.getTerminology().toString());
                etDefinity.setText(vocab.getDefinition().toString());
                etEx.setText(vocab.getExample().toString());
                String[] vocabTypes = getResources().getStringArray(R.array.vocab_type);
                int position = -1;
                for (int i = 0; i < vocabTypes.length; i++) {
                    if (vocabTypes[i].equals(vocab.getType().toString())) {
                        position = i;
                        break;
                    }
                }
                spType.setSelection(position);
                onClickBT_edit(vocab.getId());

            }
        });
        bt_detail_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDelete(vocab.getId());
            }
        });
    }
    public void onClickBT_edit(int idvocab){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Terminology =  etTerminology.getText().toString().trim();
                String Definity = etDefinity.getText().toString().trim();
                String ex = etEx.getText().toString().trim();
                String type = spType.getSelectedItem().toString();
                MyVocab myVocab = new MyVocab(Terminology, type, Definity, ex);
                long result = dbManager.updateVocab(myVocab, idvocab);


                if(result > 0)
                {
                    Toast.makeText(ListVocabActivity.this, "Sửa dữ liệu thành công!", Toast.LENGTH_SHORT).show();
                    myListVocab.clear();
                    myListVocab.addAll(dbManager.getVocabByFolderId(folderItem.getId()));
                    myAdapter.notifyDataSetChanged();
                    etTerminology.setText("");
                    etDefinity.setText("");
                    etEx.setText("");
                    spType.setSelection(0);
                    layout_edit_vocab.setVisibility(View.GONE);
                    lvVocab.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(ListVocabActivity.this, "Lỗi mẹ r!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etTerminology.setText("");
                etDefinity.setText("");
                etEx.setText("");
                spType.setSelection(0);
                layout_add_vocab.setVisibility(View.GONE);
                lvVocab.setVisibility(View.VISIBLE);
            }
        });
    }
    private void showDelete(int idvocab){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.delete_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(ListVocabActivity.this);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        Button btnYes = dialogView.findViewById(R.id.bt_yes);
        Button btnNo = dialogView.findViewById(R.id.bt_no);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = dbManager.deleteVocab(idvocab) > 0;
                if (result) {
                    Toast.makeText(ListVocabActivity.this, "Item deleted!", Toast.LENGTH_SHORT).show();
                    myListVocab.clear();
                    myListVocab.addAll(dbManager.getVocabByFolderId(folderItem.getId()));
                    myAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ListVocabActivity.this, "Lỗi khi xóa!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                layout_detail_vocab.setVisibility(View.GONE);
                lvVocab.setVisibility(View.VISIBLE);
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}