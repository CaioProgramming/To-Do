package com.myself.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.myself.todo.Beans.Music;


public class NewMusicActivity extends AppCompatActivity {
    Music mb;
    ImageView albumart, albumartback;
    EditText descriptiontext;
    TextView artist, music, album;
    Button save;
    CheckBox play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mb = new Music();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_music);
        albumart = findViewById(R.id.muicalbum);
        albumartback = findViewById(R.id.background);
        artist = findViewById(R.id.musicartist);
        album = findViewById(R.id.musicalbum);
        music = findViewById(R.id.musicname);
        save = findViewById(R.id.savebtn);
        play = findViewById(R.id.play);


        PickMusic();
    }

    private void PickMusic() {
        Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent selectmusic) {


    }

}
