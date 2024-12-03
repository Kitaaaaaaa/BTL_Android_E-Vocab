package com.example.btl_thuong;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FolderAdapter extends ArrayAdapter<FolderItem> {
    Context context;
    int layout;
    ArrayList<FolderItem> myListFolder;
    DBManager dbManager;

    public FolderAdapter(@NonNull Context context, int resource, ArrayList<FolderItem> myListFolder) {
        super(context, resource, myListFolder);
        this.context = context;
        this.layout = resource;
        this.myListFolder = myListFolder;
        this.dbManager=new DBManager(context);
    }

    @Override
    public int getCount() {
        return myListFolder.size();
    }

    @Nullable
    @Override
    public FolderItem getItem(int position) {
        return myListFolder.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView= LayoutInflater.from(context).inflate(layout, parent, false);
        }

        FolderItem f = getItem(position);
        TextView tvName = convertView.findViewById(R.id.tvFolderName);
        ImageView btnMenu = convertView.findViewById(R.id.imgMenuFolder);

        //Set ten thu muc
        if (tvName != null && f != null) {

            tvName.setText(f.getFolderName());
        }

        //Kiem tra neu Menu Button khong null
        if (btnMenu != null) {
            //Xu ly su kien nhan menu
            btnMenu.setOnClickListener(view ->{
                //Tao popup menu
                PopupMenu popupMenu = new PopupMenu(context, btnMenu);

                //Inflate menu tu folder_menu.xml
                popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

                //Dat listener cho cac muc menu
                popupMenu.setOnMenuItemClickListener(item->{
                    if (item.getItemId() == R.id.edit_folder) {
                        editFolder(position);
                        return true;
                    } else if (item.getItemId() == R.id.delete_folder) {
                        delFolder(position);
                        return true;
                    } else if (item.getItemId() == R.id.share_folder) {
                        shareFolder(position);
                        return true;
                    } else {
                        return false;
                    }
                });
                popupMenu.show();
            });
        }
        return convertView;
    }

    private void editFolder(int position){
        FolderItem f =  myListFolder.get(position);
        //Display new Dialog to edit Folder Name
        EditText edtName = new EditText(context);
        edtName.setText(f.getFolderName());
        new AlertDialog.Builder(context).setTitle("Edit folder name")
                .setView(edtName).setPositiveButton("Save", ((dialog, which) -> {
                    String newName = edtName.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        f.setFolderName(newName);
                        int result = dbManager.updateFolder(f, f.getId());
                        if (result>0){
                            Toast.makeText(context, "Updated successfully!", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }else {
                            Toast.makeText(context, "Update failed!", Toast.LENGTH_SHORT).show();
                        }
                    }

                })).setNegativeButton("Cancel", null).show();
    }

    private void delFolder(int position){
        FolderItem f = myListFolder.get(position);

        new AlertDialog.Builder(context).setTitle("Delete folder")
                .setMessage("Are you sure you want to delete this folder?")
                .setPositiveButton("Yes", ((dialog, which) -> {
                    dbManager.open();
                    int result = dbManager.deleteFolder(f.getId());
                    if (result > 0) {
                        myListFolder.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, "Delete failed!", Toast.LENGTH_SHORT).show();
                    }
                    dbManager.close();
                })).setNegativeButton("No", null).show();
    }
    private void shareFolder(int pos) {
        FolderItem f = myListFolder.get(pos);
        EditText edtuserName = new EditText(context);
        new AlertDialog.Builder(context)
                .setTitle("Shared username")
                .setView(edtuserName)
                .setPositiveButton("Share", (dialog, which) -> {
                    dbManager.open();
                    String userName = edtuserName.getText().toString().trim();
                    if (!userName.isEmpty()) {
                        if (dbManager.isUserNameExists(userName)) {
                            User user = dbManager.getUser(userName);
                            SharedPreferences prefs = getContext().getSharedPreferences("UserPref", MODE_PRIVATE);
                            int userID = prefs.getInt("userID", -1);
                            if(user.getUserID() != userID){
                                ArrayList<VocabItem> listVocab = dbManager.getAllVocab(f.getId());

                                FolderItem f1 = new FolderItem(f.getFolderName(), user.getUserID());
                                long newIdFolder = dbManager.insFolder(f1);

                                if (newIdFolder != -1) {
                                    int successCount = 0;

                                    for (VocabItem vocab : listVocab) {
                                        VocabItem newVocab = new VocabItem(
                                                vocab.getTerminology(),
                                                vocab.getType(),
                                                vocab.getDefinition(),
                                                vocab.getExample(),
                                                (int) newIdFolder
                                        );
                                        long result = dbManager.insertVocab(newVocab);
                                        if (result > 0) {
                                            successCount++;
                                        }
                                    }
                                    if (successCount != listVocab.size()) {
                                        Toast.makeText(context, "Sharing process failed! Some vocab were not copied.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Share success!", Toast.LENGTH_SHORT).show();
                                        notifyDataSetChanged();
                                    }
                                } else {
                                    Toast.makeText(context, "Failed to create shared folder!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, "The shared username matches the current username!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Username not found!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Username cannot be left blank!", Toast.LENGTH_SHORT).show();
                    }
                    dbManager.close();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


}





