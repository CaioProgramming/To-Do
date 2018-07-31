package com.myself.todo;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.myself.todo.Database.ListRepository;

public class Mylist extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Activity activity = new Mylist();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Semevento();
                    getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.fragment,new NewEvent())
                                .commit();
                     return true;
                case R.id.navigation_dashboard:
                    Semevento();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.fragment,new BlankFragment())
                            .commit();

                    return true;
                case R.id.navigation_notifications:
                    Semevento();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.fragment,new NextEvents())
                            .commit();

                    return true;

                case R.id.navigation_succes:
                    Semevento();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.fragment,new SuccesEvents())
                            .commit();

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylist);

        if (savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment,new NewEvent())
                    .commit();


        }


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        Semevento();



        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView Semevento() {
        BottomNavigationView navigation2 = (BottomNavigationView) findViewById(R.id.navigation);
        ListRepository listRepository = new ListRepository(this);
        listRepository.abrir();
        Cursor evento = listRepository.obterEventosconcluidos();
        Cursor evento1 = listRepository.obterEventos();
        Cursor evento2 = listRepository.obterFavoritos();

        if (evento.getCount() == 0 || evento1.getCount() == 0|| evento2.getCount() == 0){
            navigation2.setBackgroundColor(Color.RED);
        }else{

            navigation2.setBackground(getDrawable(R.color.transparent));

        }

        return navigation2;
    }

}
