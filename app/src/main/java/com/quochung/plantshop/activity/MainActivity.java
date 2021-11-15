package com.quochung.plantshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.quochung.plantshop.R;
import com.quochung.plantshop.fragment.Cart;
import com.quochung.plantshop.fragment.PlantOverview;


public class MainActivity extends AppCompatActivity {
    ImageView menu, logout;
    String currentfragment = "plantshop";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        initUI();
        togglemenu();
        logoutfunction();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,
                    new PlantOverview()).commit();
        }



    }


    protected  void initUI() {
        logout = findViewById(R.id.logout);
        menu = findViewById(R.id.menu);
    }

    protected  void togglemenu() {
        menu.setOnClickListener(view -> {
            if (currentfragment.equals("plantshop")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,
                        new Cart()).commit();
                menu.setImageResource(R.drawable.profile);
                currentfragment = "cart";
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,
                        new PlantOverview()).commit();
                currentfragment = "plantshop";
                menu.setImageResource(R.drawable.cart);
            }
        });
    }


    protected  void logoutfunction() {
        logout.setOnClickListener(view -> logout());
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}