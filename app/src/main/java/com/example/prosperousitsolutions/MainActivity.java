package com.example.prosperousitsolutions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.example.prosperousitsolutions.user_session.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MainActivity extends AppCompatActivity {
//    RadioGroup radioGroup;
//    RadioButton search, offer;

    public boolean isFreelancer;
    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;
    private Menu collapsedMenu;
    private boolean appBarExpanded = true;
    protected void init(){
//        search = findViewById(R.id.search);
//        offer = findViewById(R.id.offer);
//        radioGroup = findViewById(R.id.toggle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
//        Window w = getWindow();
////        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        w.setStatusBarColor(Color.TRANSPARENT);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        isFreelancer= (boolean) task.getResult().get("freelancer");
                    }
                });

        Toast.makeText(this, isFreelancer+"", Toast.LENGTH_SHORT).show();


        //getSupportActionBar().hide();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null)
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setContentScrimColor(Color.parseColor("#64B5F6"));
        collapsingToolbar.setStatusBarScrimColor(Color.parseColor("#64B5F6"));
        collapsingToolbar.setTitle("Hey Mayank");


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                Log.d(AnimateToolbar.class.getSimpleName(), "onOffsetChanged: verticalOffset: " + verticalOffset);

                //  Vertical offset == 0 indicates appBar is fully expanded.
                if (Math.abs(verticalOffset) > 200) {
                    appBarExpanded = false;
                    invalidateOptionsMenu();
                } else {
                    appBarExpanded = true;
                    invalidateOptionsMenu();
                }
            }
        });


    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        if (collapsedMenu != null
//                && (!appBarExpanded || collapsedMenu.size() != 1)) {
//            //collapsed
//            collapsedMenu.add("Add")
//                    .setIcon(R.drawable.ic_action_add)
//                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//        } else {
//            //expanded
//        }
//        return super.onPrepareOptionsMenu(collapsedMenu);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                return true;
            case R.id.main_search:
                return true;
            case R.id.main_chat:
                return true;

        }
//        if (item.getTitle() == "Add") {
//            Toast.makeText(this, "clicked add", Toast.LENGTH_SHORT).show();
//        }
        return super.onOptionsItemSelected(item);
    }

}
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                if (i == R.id.search) {
//                    search.setTextColor(Color.parseColor("#FFFFFF"));
//                    offer.setTextColor(Color.parseColor("#4FC3F7"));
//                    search.setTextSize(20);
//                    offer.setTextSize(10);
//                }
//                else if (i == R.id.offer){
//                    offer.setTextColor(Color.parseColor("#FFFFFF"));
//                    search.setTextColor(Color.parseColor("#4FC3F7"));
//                    offer.setTextSize(20);
//                    search.setTextSize(10);
//                }
//            }
//        });