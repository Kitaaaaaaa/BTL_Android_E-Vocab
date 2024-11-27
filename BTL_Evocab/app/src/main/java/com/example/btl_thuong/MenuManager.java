package com.example.btl_thuong;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

public class MenuManager {
    private static boolean isMenuVisible = false; // Biến lưu trạng thái của menu
    private static FrameLayout layoutMenu;
    private static LinearLayout dimBg;
    private static ImageButton btn_menu_menu;
    private static ImageButton btn_lstFolder_menu;
    private static ImageButton btn_lstVocab_menu;
    private static ImageButton btn_aboutUs_menu;

    public static void setUpMenuFolder(final Activity activity) {
        getViews(activity);

        btn_menu_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMenuVisible) {
                    hideMenu(layoutMenu, dimBg);
                } else {
                    showMenu(layoutMenu, dimBg);
                }
                isMenuVisible = !isMenuVisible;
            }
        });

        btn_lstFolder_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMenuVisible) {
                    hideMenu(layoutMenu, dimBg);
                } else {
                    showMenu(layoutMenu, dimBg);
                }
                isMenuVisible = !isMenuVisible;
            }
        });

        // Thiết lập sự kiện cho lớp phủ mờ (dim background)
        dimBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMenu(layoutMenu, dimBg); // Nếu click vào nền mờ, ẩn menu
                isMenuVisible = false; // Cập nhật trạng thái của menu
            }
        });

        setUpMenuButton(activity);
    }

    public static void setUpMenuVocab(final Activity activity) {
        getViews(activity);

        btn_menu_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMenuVisible) {
                    hideMenu(layoutMenu, dimBg);
                } else {
                    showMenu(layoutMenu, dimBg);
                }
                isMenuVisible = !isMenuVisible;
            }
        });

        btn_lstVocab_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMenuVisible) {
                    hideMenu(layoutMenu, dimBg);
                } else {
                    showMenu(layoutMenu, dimBg);
                }
                isMenuVisible = !isMenuVisible;
            }
        });

        // Thiết lập sự kiện cho lớp phủ mờ (dim background)
        dimBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMenu(layoutMenu, dimBg); // Nếu click vào nền mờ, ẩn menu
                isMenuVisible = false; // Cập nhật trạng thái của menu
            }
        });

        setUpMenuButton(activity);
    }

    public static void setUpMenuAboutUs(final Activity activity) {
        getViews(activity);

        btn_menu_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMenuVisible) {
                    hideMenu(layoutMenu, dimBg);
                } else {
                    showMenu(layoutMenu, dimBg);
                }
                isMenuVisible = !isMenuVisible;
            }
        });

        btn_aboutUs_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMenuVisible) {
                    hideMenu(layoutMenu, dimBg);
                } else {
                    showMenu(layoutMenu, dimBg);
                }
                isMenuVisible = !isMenuVisible;
            }
        });

        // Thiết lập sự kiện cho lớp phủ mờ (dim background)
        dimBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMenu(layoutMenu, dimBg); // Nếu click vào nền mờ, ẩn menu
                isMenuVisible = false; // Cập nhật trạng thái của menu
            }
        });

        setUpMenuButton(activity);
    }

    private static void getViews(final Activity activity) {
        layoutMenu = activity.findViewById(R.id.menu_layout);
        dimBg = activity.findViewById(R.id.dim_background);
        btn_menu_menu = activity.findViewById(R.id.btn_menu_menu);
        btn_lstFolder_menu = activity.findViewById(R.id.btn_lstFolder_menu);
        btn_lstVocab_menu = activity.findViewById(R.id.btnMenu_lstVocab);
        btn_aboutUs_menu = activity.findViewById(R.id.btn_aboutUs_menu);
    }

    // Hiển thị menu
    private static void showMenu(FrameLayout menu, LinearLayout bg) {
        menu.setVisibility(View.VISIBLE); // Hiển thị menu
        menu.animate().translationX(0).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // Khi animation kết thúc, hiển thị nền mờ
                bg.setVisibility(View.VISIBLE);
            }
        });
    }

    // Ẩn menu
    private static void hideMenu(FrameLayout menu, LinearLayout bg) {
        bg.setVisibility(View.GONE);
        menu.animate()
                .translationX(-menu.getWidth())  // Hiệu ứng trượt ra ngoài
                .setDuration(300)
                .withEndAction(new Runnable() {  // Khi animation kết thúc, ẩn menu và nền mờ
                    @Override
                    public void run() {
                        menu.setVisibility(View.GONE);  // Ẩn menu sau khi trượt xong
                        // Ẩn nền mờ
                    }
                });
    }

    // Thiết lập sự kiện cho các nút trong menu
    private static void setUpMenuButton(final Activity activity) {
        Button btnListFolder = activity.findViewById(R.id.btn_menu_listFolder);
        Button btnAboutUs = activity.findViewById(R.id.btn_menu_aboutUs);
        Button btnLogOut = activity.findViewById(R.id.btn_menu_logOut);

        btnListFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ListFolderActivity.class);
                activity.startActivity(i);
            }
        });

        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(activity, AboutUsActivity.class);
                activity.startActivity(i2);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = activity.getSharedPreferences("UserPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=prefs.edit();
                editor.clear();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();
                Intent i3 = new Intent(activity, LogInActivity.class);
                activity.startActivity(i3);
                activity.finish();
            }
        });
    }
}
