package com.myself.todo.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mmin18.widget.RealtimeBlurView;
import com.myself.todo.Beans.Music;
import com.myself.todo.Database.AlbumRepository;
import com.myself.todo.Database.MusicRepository;
import com.myself.todo.R;

import java.io.IOException;
import java.util.List;

public class RecyclerMusicAdapter extends RecyclerView.Adapter<RecyclerMusicAdapter.MyViewHolder> {


    int i = 0;
    AlbumRepository lst;
    private Context mContext;
    private Activity mActivity;
    private Dialog myDialog;
    MediaPlayer mp = new MediaPlayer();
    private List<Music> mData;


    public RecyclerMusicAdapter(Context mContext, Activity mActivity, List<Music> mData) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mData = mData;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.musiccard, parent, false);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final RecyclerMusicAdapter.MyViewHolder holder, final int position) {
        Glide.with(mContext).load(mData.get(position).getMusicuri()).into(holder.pic);
        final Animation myanim2 = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_bottom);
        final Animation fade = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        final Animation pulse = AnimationUtils.loadAnimation(mContext, R.anim.pulse);
        if (mData.get(position).getStatus() == null) {
            return;
        } else {
            if (mData.get(position).getStatus().equals("F")) {
                holder.fav.setChecked(true);
            }
        }
        holder.card.startAnimation(myanim2);
        holder.pic.startAnimation(fade);
        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.fav.isChecked()) {
                    Music music = new Music();
                    music.setMusicuri(mData.get(position).getMusicuri());
                    music.setMusic(mData.get(position).getMusic());
                    MusicRepository musicRepository = new MusicRepository(mContext);
                    musicRepository.abrir();
                    musicRepository.inserir(music);

                } else {

                    Music music = new Music();
                    music.setMusicuri(mData.get(position).getMusicuri());
                    music.setMusic(mData.get(position).getMusic());
                    MusicRepository musicRepository = new MusicRepository(mContext);
                    musicRepository.abrir();
                    musicRepository.unfavoritar(mData.get(position).getMusicuri());
                }
            }
        });
        final Uri path;
        System.out.println(mData.get(position).getMusicuri());
        path = Uri.parse(mData.get(position).getMusicuri());
        MediaMetadataRetriever myRetriever = new MediaMetadataRetriever();

        myRetriever.setDataSource(mContext, path); // the URI of audio file
        holder.music.setText(myRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        byte[] artwork;
        artwork = myRetriever.getEmbeddedPicture();
        if (artwork != null) {
            Bitmap bMap = BitmapFactory.decodeByteArray(artwork, 0, artwork.length);
            Glide.with(mContext).load(bMap).into(holder.pic);
        } else {
            Glide.with(mContext).load(R.drawable.album).into(holder.pic);
        }

        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.play.isChecked()) {
                    mp = new MediaPlayer();
                    try {
                        holder.blurView.setBlurRadius(10);
                        System.out.println(mData.get(position).getMusicuri());
                        mp.setDataSource(mData.get(position).getMusicuri());
                        mp.prepareAsync();
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mp.start();

                                holder.play.setChecked(true);
                                holder.play.startAnimation(pulse);
                            }
                        });


                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else {
                    mp.release();
                    holder.play.setChecked(false);
                    holder.play.clearAnimation();
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        if (mData.size() == 0) {
            return 0;

        } else {
            return mData.size();
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        RealtimeBlurView blurView;
        TextView music;
        ImageView pic;
        CheckBox play, fav;
        CardView card;

        public MyViewHolder(View view) {
            super(view);
            fav = itemView.findViewById(R.id.favbtn);
            play = itemView.findViewById(R.id.play);
            music = itemView.findViewById(R.id.musicname);
            blurView = itemView.findViewById(R.id.blur);
            pic = itemView.findViewById(R.id.albumcover);
            card = itemView.findViewById(R.id.musicacard);


        }
    }
}
