package com.myself.todo.Fragments;


import android.animation.ValueAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myself.todo.Adapters.RecyclerAdapter;
import com.myself.todo.Adapters.RecyclerFotoAdapter;
import com.myself.todo.Beans.Album;
import com.myself.todo.Beans.Events;
import com.myself.todo.Profile;
import com.myself.todo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import de.mateware.snacky.Snacky;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements BSImagePicker.OnSingleImageSelectedListener {


    List<Events> lstevents;
    List<Album> lstalbum;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    RealtimeBlurView blur;
    TextView picsnumber;
    TextView objectivesnumber;
    TextView musics;


    DatabaseReference album = FirebaseDatabase.getInstance().getReference().child("album");
    DatabaseReference events = FirebaseDatabase.getInstance().getReference().child("events");

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
        blur = view.findViewById(R.id.blur);
        RecyclerView objectiverecyler = view.findViewById(R.id.objectiverecyler);
        RecyclerView fotorecycler = view.findViewById(R.id.fotorecycler);
        final ProgressBar progress = view.findViewById(R.id.progress);
        final ScrollView recently = view.findViewById(R.id.recently);
        musics = view.findViewById(R.id.musics);
        objectivesnumber = view.findViewById(R.id.objectivesnumber);
        picsnumber = view.findViewById(R.id.picsnumber);
        TextView username = view.findViewById(R.id.username);
        CircleImageView profilepic = view.findViewById(R.id.profilepic);
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picalert();
            }
        });
        InitDB();
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Profile.class);

                startActivity(i);
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            username.setText(user.getDisplayName());
        }
        assert user != null;
        if (user.getPhotoUrl() == null) {

        } else {
            Glide.with(this).load(user.getPhotoUrl()).into(profilepic);
        }


        //ContarItems(musics, objectivesnumber, picsnumber, username, usuario, usuarioB);

        CarregarMusicas();
        CarregarFotos(fotorecycler);
        CarregarObjetivos(objectiverecyler);
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


    private void InitDB() {

        lstevents = new ArrayList<>();
        lstalbum = new ArrayList<>();
    }

    private void CarregarFotos(final RecyclerView recycler) {
        lstalbum.clear();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference raiz = FirebaseDatabase.getInstance().getReference().child("album");
        raiz.keepSynced(true);
        if (user != null) {
            raiz.child("album").orderByChild("userID").equalTo(user.getUid());
        }
        raiz.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Album a = d.getValue(Album.class);
                    Album album = new Album();
                    if (a != null) {
                        System.out.println(a.getId());
                        System.out.println(d.getKey());
                        album.setFotouri(a.getFotouri());
                        album.setDescription(a.getDescription());
                        album.setStatus(a.getStatus());
                        album.setDia(a.getDia());
                        album.setId(d.getKey());
                        album.setUserID(a.getUserID());
                        lstalbum.add(album);
                        System.out.println(album.getId());
                        System.out.println(lstalbum.size());

                    }
                }
                recycler.setVisibility(View.VISIBLE);
                GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
                recycler.setHasFixedSize(true);
                System.out.println(lstalbum.size());
                //final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
                RecyclerFotoAdapter myadapter = new RecyclerFotoAdapter(getContext(), getActivity(), lstalbum, blur);
                recycler.setAdapter(myadapter);
                recycler.setLayoutManager(llm);
                final ValueAnimator animator = ValueAnimator.ofInt(0, lstalbum.size());
                animator.setDuration(3000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        picsnumber.setText(animator.getAnimatedValue().toString());
                    }
                });
                animator.start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void CarregarObjetivos(final RecyclerView recycler) {
        lstevents.clear();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference raiz = FirebaseDatabase.getInstance().getReference().child("events");
        raiz.keepSynced(true);
        if (user != null) {
            raiz.child("events").orderByChild("userID").equalTo(user.getUid());
        }
        raiz.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Events e = d.getValue(Events.class);
                    Events events = new Events();
                    if (e != null) {
                        System.out.println(e.getId());
                        System.out.println(d.getKey());
                        events.setEvento(e.getEvento());
                        events.setDescricao(e.getDescricao());
                        events.setStatus(e.getStatus());
                        events.setData(e.getData());
                        events.setId(d.getKey());
                        events.setUserID(e.getUserID());
                        lstevents.add(events);
                        System.out.println(events.getId());
                        System.out.println(lstevents.size());

                    }
                }
                recycler.setVisibility(View.VISIBLE);
                GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
                recycler.setHasFixedSize(true);
                System.out.println(lstevents.size());
                RecyclerAdapter myadapter = new RecyclerAdapter(getContext(), getActivity(), lstevents, blur);
                recycler.setAdapter(myadapter);
                recycler.setLayoutManager(llm);
                final ValueAnimator animator = ValueAnimator.ofInt(0, lstevents.size());
                animator.setDuration(3000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        objectivesnumber.setText(animator.getAnimatedValue().toString());
                    }
                });
                animator.start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void CarregarMusicas() {
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


    @Override
    public void onSingleImageSelected(Uri uri) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
        if (user != null) {
            user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Snacky.builder().setActivity(Objects.requireNonNull(getActivity())).success().setText("Foto de perfil alterada").show();
                        //CircleImageView profilepic = getActivity().findViewById(R.id.profile_pic);
                        //Glide.with(getActivity()).load(user.getPhotoUrl()).into(profilepic);
                    } else {
                        Snacky.builder().setActivity(Objects.requireNonNull(getActivity())).success().setText("Erro " + task.getException()).show();
                    }


                }
            });
        }

    }
}
