package com.myself.todo.Fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.github.mmin18.widget.RealtimeBlurView;
import com.myself.todo.Adapters.RecyclerFotoAdapter;
import com.myself.todo.Beans.Album;
import com.myself.todo.Database.AlbumRepository;
import com.myself.todo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FotosFragment extends Fragment {

    List<Album> lstalbum;
    AlbumRepository albRepository;
    Switch favorites;


    public FotosFragment() {
        // Required empty public constructor
    }

    RealtimeBlurView blur;

    public void setBlur(RealtimeBlurView blur) {
        this.blur = blur;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fotos, container, false);
        FrameLayout frameLayout = view.findViewById(R.id.fotos);
        lstalbum = new ArrayList<>();
        //blur = view.findViewById(R.id.blur);
        favorites = view.findViewById(R.id.favoriteswitch);
        final RecyclerView recycler = view.findViewById(R.id.recyclerviewfotos);
        final LinearLayout empty = view.findViewById(R.id.nofotos);
        albRepository = new AlbumRepository(getActivity());
        final ProgressBar pb = view.findViewById(R.id.progress);
        albRepository.abrir();
        final String usuario = getActivity().getIntent().getExtras().getString("usuario");
        CarregarFotos(recycler, empty, usuario);


        favorites.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    pb.setVisibility(View.VISIBLE);
                    pb.animate().start();
                    recycler.setVisibility(View.INVISIBLE);
                    lstalbum.clear();
                    recycler.destroyDrawingCache();
                    CountDownTimer countDownTimer = new CountDownTimer(2000, 300) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            pb.setVisibility(View.INVISIBLE);
                            recycler.setVisibility(View.VISIBLE);
                            CarregarFotosFavoritas(recycler, empty, usuario);
                        }
                    }.start();


                } else {
                    pb.setVisibility(View.VISIBLE);
                    pb.animate().start();
                    recycler.setVisibility(View.INVISIBLE);
                    lstalbum.clear();
                    recycler.destroyDrawingCache();
                    CountDownTimer countDownTimer = new CountDownTimer(2000, 300) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            pb.setVisibility(View.INVISIBLE);
                            recycler.setVisibility(View.VISIBLE);
                            CarregarFotos(recycler, empty, usuario);
                        }
                    }.start();
                }
            }
        });

        return view;


    }

    private void CarregarFotos(RecyclerView recycler, LinearLayout empty, String usuario) {
        albRepository.abrir();
        Cursor evento = albRepository.obterFotos(usuario);
        System.out.println(evento.getCount());
        evento.moveToFirst();

        if (evento.getCount() == 0) {

        } else {

            empty.setVisibility(View.INVISIBLE);
            recycler.setVisibility(View.VISIBLE);


        }
        for (int i = 0; i < evento.getCount(); i++) {

            lstalbum.add(albRepository.criafoto(evento));
            evento.moveToNext();

        }
        GridLayoutManager llm = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recycler.setHasFixedSize(true);
        System.out.println(lstalbum.size());
        albRepository.fecha();
        final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
        RecyclerFotoAdapter myadapter = new RecyclerFotoAdapter(getContext(), getActivity(), lstalbum, blur);
        recycler.setAdapter(myadapter);
        recycler.setLayoutManager(llm);
        recycler.startAnimation(myanim2);
        albRepository.fecha();
    }

    private void CarregarFotosFavoritas(RecyclerView recycler, LinearLayout empty, String usuario) {
        albRepository.abrir();
        Cursor evento = albRepository.obterFavoritos(usuario);
        System.out.println(evento.getCount());
        evento.moveToFirst();

        if (evento.getCount() == 0) {

        } else {

            empty.setVisibility(View.INVISIBLE);
            recycler.setVisibility(View.VISIBLE);


        }
        for (int i = 0; i < evento.getCount(); i++) {

            lstalbum.add(albRepository.criafoto(evento));
            evento.moveToNext();

        }
        GridLayoutManager llm = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recycler.setHasFixedSize(true);
        System.out.println(lstalbum.size());
        albRepository.fecha();
        final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
        RecyclerFotoAdapter myadapter = new RecyclerFotoAdapter(getContext(), getActivity(), lstalbum, blur);
        recycler.setAdapter(myadapter);
        recycler.setLayoutManager(llm);
        recycler.startAnimation(myanim2);
        albRepository.fecha();
    }


}
