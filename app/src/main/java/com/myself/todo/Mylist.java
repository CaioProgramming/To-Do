package com.myself.todo;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.myself.todo.Database.ListRepository;

public class Mylist extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            BottomNavigationView navigation2 = findViewById(R.id.navigation);

            Activity activity = new Mylist();

            switch (item.getItemId()) {
                case R.id.navigation_newev:
                    //navigation2.setBackground(getDrawable(R.drawable.gradnewevent));

                    Semevento();
                    getSupportFragmentManager()
                                .beginTransaction()
                            .replace(R.id.fragment, new NewEvent())
                                .commit();
                     return true;
                case R.id.navigation_home:
                    //navigation2.setBackground(getDrawable(R.drawable.gradhome));
                    Semevento();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, new BlankFragment())
                            .commit();

                    return true;
                case R.id.navigation_favorites:
                    //navigation2.setBackground(getDrawable(R.drawable.gradfavorites));
                    Semevento();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, new NextEvents())
                            .commit();

                    return true;

                case R.id.navigation_succes:
                    //navigation2.setBackground(getDrawable(R.drawable.gradnewconcl));
                    Semevento();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, new SuccesEvents())
                            .commit();

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylist);


        if (savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment,new NewEvent())
                    .commit();


        }


        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        Semevento();



        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private BottomNavigationView Semevento() {
        BottomNavigationView navigation2 = findViewById(R.id.navigation);
        ListRepository listRepository = new ListRepository(this);
        listRepository.abrir();
        Cursor evento = listRepository.obterEventosconcluidos();
        Cursor evento1 = listRepository.obterEventos();
        Cursor evento2 = listRepository.obterFavoritos();

        if (evento.getCount() == 0 || evento1.getCount() == 0|| evento2.getCount() == 0){
            navigation2.setItemTextColor(ColorStateList.valueOf(Color.RED));
            navigation2.setItemIconTintList(ColorStateList.valueOf(Color.RED));

        }else{

            navigation2.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
            navigation2.setItemIconTintList(ColorStateList.valueOf(Color.BLACK));
        }

        return navigation2;
    }

}
