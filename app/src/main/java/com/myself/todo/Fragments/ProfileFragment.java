package com.myself.todo.Fragments;


import android.animation.ValueAnimator;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.asksira.bsimagepicker.BSImagePicker;
import com.asksira.bsimagepicker.Utils;
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
import com.myself.todo.Profile;
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
        RelativeLayout userdata = view.findViewById(R.id.userdata);
        final Button profile = view.findViewById(R.id.profile);
        LinearLayout data = view.findViewById(R.id.data);
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
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picalert();
            }
        });
        InitDB();
        String usuario = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getString("usuario");
        //int cod_usuario = getActivity().getIntent().getExtras().getInt("codigo");
        String senha = getActivity().getIntent().getExtras().getString("senha");

        System.out.println(usuario);
        criarConexao();
        userRepository = new UserRepository(conexao);
        final User usuarioB = userRepository.findByLogin(usuario, senha);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Profile.class);
                i.putExtra("usuario", usuarioB.getUser());
                i.putExtra("senha", usuarioB.getPassword());
                startActivity(i);
            }
        });
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

    private void Picalert() {
        BSImagePicker singleSelectionPicker = new BSImagePicker.Builder("com.myself.fileprovider")
                //Default: Integer.MAX_VALUE. Don't worry about performance :)
                .hideGalleryTile()

                .setSpanCount(3) //Default: 3. This is the number of columns
                .setGridSpacing(Utils.dp2px(2)) //Default: 2dp. Remember to pass in a value in pixel.
                .setPeekHeight(Utils.dp2px(360))//Default: 360dp. This is the initial height of the dialog.
                .setOverSelectTextColor(R.color.black)
                .setMultiSelectDoneTextColor(R.color.blue_300)
                .build();

        singleSelectionPicker.show(getChildFragmentManager(), "picker");
    }

    private void ContarItems(final TextView musics, TextView objectivesnumber, TextView picsnumber, TextView username, String usuario, User usuarioB) {
        username.setText(usuario);
        picsnumber.setText(String.valueOf(albumRepository.contar(usuario)));
        objectivesnumber.setText(String.valueOf(objRepository.contar(usuarioB.getUser())));
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        Cursor cursor = Objects.requireNonNull(getActivity()).getContentResolver().query(uri, null, selection, null, MediaStore.Audio.Media.DATE_MODIFIED);
        assert cursor != null;
        musics.setText(String.valueOf(cursor.getCount()));
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, cursor.getCount());
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                musics.setText(valueAnimator.getAnimatedValue().toString());

            }
        });
        valueAnimator.start();
    }

    private void InitDB() {
        albumRepository = new AlbumRepository(getContext());
        objRepository = new ObjRepository(getContext());
        lstevents = new ArrayList<>();
        lstalbum = new ArrayList<>();
    }

    private void CarregarFotos(RealtimeBlurView blur, RecyclerView fotorecycler, User usuarioB, final TextView picnumber) {
        albumRepository.abrir();
        Cursor evento = albumRepository.obterFotosRecentes(usuarioB.getUser());
        System.out.println(evento.getCount());
        evento.moveToLast();

        if (evento.getCount() == 0) {

        } else {

            fotorecycler.setVisibility(View.VISIBLE);


        }
        for (int i = 0; i < evento.getCount(); i++) {

            lstalbum.add(albumRepository.criafoto(evento));
            evento.moveToPrevious();

        }
        GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
        fotorecycler.setHasFixedSize(true);
        System.out.println(lstalbum.size());
        albumRepository.fecha();
        final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        RecyclerFotoAdapter myadapter = new RecyclerFotoAdapter(getContext(), getActivity(), lstalbum, blur);
        fotorecycler.setAdapter(myadapter);
        fotorecycler.setLayoutManager(llm);
        fotorecycler.startAnimation(myanim2);
        albumRepository.fecha();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, lstalbum.size());
        valueAnimator.setDuration(1500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                picnumber.setText(valueAnimator.getAnimatedValue().toString());

            }
        });
        valueAnimator.start();
    }

    private void CarregarObjetivos(RecyclerView objectiverecyler, User usuarioB, final TextView objectivesnumber) {
        objRepository = new ObjRepository(getActivity());
        objRepository.abrir();


        Cursor evento = objRepository.obterEventos(usuarioB.getUser());
        System.out.println(evento.getCount());
        evento.moveToLast();
        if (evento.getCount() == 0) {

        } else {

            objectiverecyler.setVisibility(View.VISIBLE);

        }
        for (int i = 0; i < evento.getCount(); i++) {

            lstevents.add(objRepository.criaevento(evento));
            evento.moveToPrevious();

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
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, lstevents.size());
        valueAnimator.setDuration(1500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                objectivesnumber.setText(valueAnimator.getAnimatedValue().toString());

            }
        });
        valueAnimator.start();

    }

    private void criarConexao() {
        DadosOpenHelper dadosOpenHelper = new DadosOpenHelper(getContext());

        conexao = dadosOpenHelper.getWritableDatabase();


        userRepository = new UserRepository(conexao);

        //Toast.makeText(this,"CONEXÃO CRIADA COM SUCESSO!", Toast.LENGTH_SHORT).show();


    }


}
