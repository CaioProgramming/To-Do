package com.myself.todo.Fragments;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mmin18.widget.RealtimeBlurView;
import com.myself.todo.Adapters.RecyclerAdapter;
import com.myself.todo.Adapters.RecyclerFotoAdapter;
import com.myself.todo.Beans.Album;
import com.myself.todo.Beans.Events;
import com.myself.todo.Beans.User;
import com.myself.todo.Database.AlbumRepository;
import com.myself.todo.Database.DadosOpenHelper;
import com.myself.todo.Database.ObjRepository;
import com.myself.todo.Database.UserRepository;
import com.myself.todo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    AlbumRepository albumRepository;
    UserRepository userRepository;
    ObjRepository objRepository;
    List<Events> lstevents;
    List<Album> lstalbum;
    private SQLiteDatabase conexao;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        RealtimeBlurView blur = view.findViewById(R.id.blur);
        RecyclerView objectiverecyler = view.findViewById(R.id.objectiverecyler);
        RecyclerView fotorecycler = view.findViewById(R.id.fotorecycler);
        final ProgressBar progress = view.findViewById(R.id.progress);
        final ScrollView recently = view.findViewById(R.id.recently);
        TextView musics = view.findViewById(R.id.musics);
        TextView objectivesnumber = view.findViewById(R.id.objectivesnumber);
        TextView picsnumber = view.findViewById(R.id.picsnumber);
        TextView username = view.findViewById(R.id.username);
        CircleImageView profilepic = view.findViewById(R.id.profilepic);
        profilepic = view.findViewById(R.id.profilepic);
        InitDB();
        String usuario = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getString("usuario");
        //int cod_usuario = getActivity().getIntent().getExtras().getInt("codigo");
        String senha = getActivity().getIntent().getExtras().getString("senha");

        System.out.println(usuario);
        criarConexao();
        userRepository = new UserRepository(conexao);
        User usuarioB = userRepository.findByLogin(usuario, senha);
        System.out.println(usuarioB.getProfilepic());
        System.out.println(usuario);
        username.setText(usuarioB.getUser());
        if (usuarioB.getProfilepic() == null) {

        } else {
            Glide.with(this).load(usuarioB.getProfilepic()).into(profilepic);
        }


        ContarItems(musics, objectivesnumber, picsnumber, username, usuario, usuarioB);


        CountDownTimer countDownTimer = new CountDownTimer(3000, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                progress.setVisibility(View.INVISIBLE);
                recently.setVisibility(View.VISIBLE);

            }
        }.start();


        CarregarObjetivos(objectiverecyler, usuarioB, objectivesnumber);


        CarregarFotos(blur, fotorecycler, usuarioB, picsnumber);

        return view;


    }

    private void ContarItems(TextView musics, TextView objectivesnumber, TextView picsnumber, TextView username, String usuario, User usuarioB) {
        username.setText(usuario);
        picsnumber.setText(String.valueOf(albumRepository.contar(usuario)));
        objectivesnumber.setText(String.valueOf(objRepository.contar(usuarioB.getUser())));
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        Cursor cursor = Objects.requireNonNull(getActivity()).getContentResolver().query(uri, null, selection, null, MediaStore.Audio.Media.DATE_MODIFIED);
        assert cursor != null;
        musics.setText(String.valueOf(cursor.getCount()));
    }

    private void InitDB() {
        albumRepository = new AlbumRepository(getContext());
        objRepository = new ObjRepository(getContext());
        lstevents = new ArrayList<>();
        lstalbum = new ArrayList<>();
    }

    private void CarregarFotos(RealtimeBlurView blur, RecyclerView fotorecycler, User usuarioB, TextView picnumber) {
        albumRepository.abrir();
        Cursor evento = albumRepository.obterFotosRecentes(usuarioB.getUser());
        System.out.println(evento.getCount());
        evento.moveToFirst();

        if (evento.getCount() == 0) {

        } else {

            fotorecycler.setVisibility(View.VISIBLE);


        }
        for (int i = 0; i < evento.getCount(); i++) {

            lstalbum.add(albumRepository.criafoto(evento));
            evento.moveToNext();

        }
        GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
        fotorecycler.setHasFixedSize(true);
        System.out.println(lstalbum.size());
        picnumber.setText(String.valueOf(lstalbum.size()));
        albumRepository.fecha();
        final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        RecyclerFotoAdapter myadapter = new RecyclerFotoAdapter(getContext(), getActivity(), lstalbum, blur);
        fotorecycler.setAdapter(myadapter);
        fotorecycler.setLayoutManager(llm);
        fotorecycler.startAnimation(myanim2);
        albumRepository.fecha();
    }

    private void CarregarObjetivos(RecyclerView objectiverecyler, User usuarioB, TextView objectivesnumber) {
        objRepository = new ObjRepository(getActivity());
        objRepository.abrir();


        Cursor evento = objRepository.obterEventos(usuarioB.getUser());
        System.out.println(evento.getCount());
        objectivesnumber.setText(String.valueOf(evento.getCount()));
        evento.moveToFirst();
        if (evento.getCount() == 0) {

        } else {

            objectiverecyler.setVisibility(View.VISIBLE);

        }
        for (int i = 0; i < evento.getCount(); i++) {

            lstevents.add(objRepository.criaevento(evento));
            evento.moveToNext();

        }
        GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
        objectiverecyler.setHasFixedSize(true);
        System.out.println(lstevents.size());

        objRepository.fecha();

        RecyclerAdapter myadapter = new RecyclerAdapter(getActivity(), lstevents);
        objectiverecyler.setAdapter(myadapter);
        objectiverecyler.setLayoutManager(llm);
        final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        objectiverecyler.startAnimation(myanim2);
    }

    private void criarConexao() {
        DadosOpenHelper dadosOpenHelper = new DadosOpenHelper(getContext());

        conexao = dadosOpenHelper.getWritableDatabase();


        userRepository = new UserRepository(conexao);

        //Toast.makeText(this,"CONEXÃO CRIADA COM SUCESSO!", Toast.LENGTH_SHORT).show();


    }


}
