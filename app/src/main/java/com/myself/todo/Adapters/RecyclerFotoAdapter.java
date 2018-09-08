package com.myself.todo.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.myself.todo.Beans.Album;
import com.myself.todo.Database.AlbumRepository;
import com.myself.todo.R;
import com.myself.todo.Utils.Utilities;

import java.io.IOException;
import java.util.List;

import de.mateware.snacky.Snacky;

public class RecyclerFotoAdapter extends RecyclerView.Adapter<RecyclerFotoAdapter.MyViewHolder> {


    int i = 0;
    AlbumRepository lst;
    private Context mContext;
    private Activity mActivity;
    private Dialog myDialog;
    private List<Album> mData;

    public RecyclerFotoAdapter(Context mContext, Activity mActivity, List<Album> mData) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mData = mData;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardlayoutfotos, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerFotoAdapter.MyViewHolder holder, final int position) {

        try {
            holder.pic.setImageBitmap(Utilities.handleSamplingAndRotationBitmap(mContext, Uri.parse(mData.get(position).getFotouri())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Animation myanim2 = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_bottom);

        holder.card.startAnimation(myanim2);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (i == 2) {

                    AlbumRepository albumRepository = new AlbumRepository(mContext);
                    albumRepository.favoritar(mData.get(position).getId());
                    Snacky.builder()
                            .setText("Foto adcionada aos favoritos")
                            .setIcon(R.drawable.ic_favorite2_black_24dp)
                            .setBackgroundColor(Color.WHITE)
                            .setActivity(mActivity)
                            .setDuration(Snacky.LENGTH_SHORT)
                            .info()
                            .show();

                    i = 0;
                } else {

                    Snacky.builder()
                            .setText("Clique novamente para adcionar aos favoritos")
                            .setActivity(mActivity)
                            .setDuration(Snacky.LENGTH_SHORT)
                            .info()
                            .show();
                    i += 1;

                }


            }
        });
        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                myDialog = new Dialog(mContext);
                myDialog.setContentView(R.layout.popupfoto);
                TextView event, desc;
                ImageView pic;
                final Button favbtn, dltbtn;
                pic = myDialog.findViewById(R.id.albpic);
                event = myDialog.findViewById(R.id.descricaopic);
                desc = myDialog.findViewById(R.id.diapic);
                favbtn = myDialog.findViewById(R.id.dlgfavpic);
                dltbtn = myDialog.findViewById(R.id.dlgexcluir);

                event.setText(mData.get(position).getDescription());
                desc.setText(mData.get(position).getDia());
                pic.setImageURI(Uri.parse(mData.get(position).getFotouri()));


                favbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lst = new AlbumRepository(mContext);
                        lst.abrir();
                        lst.favoritar(mData.get(position).getId());
                        lst.fecha();
                    }
                });

                dltbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        favbtn.setDrawingCacheBackgroundColor(Color.YELLOW);
                        lst = new AlbumRepository(mContext);
                        lst.abrir();
                        lst.apagar(mData.get(position).getId());
                        lst.fecha();
                    }
                });
                myDialog.show();
                return true;
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

        TextView data;
        ImageView pic;
        CheckBox fav;

        CardView card;

        public MyViewHolder(View view) {
            super(view);


            pic = itemView.findViewById(R.id.pic);
            card = itemView.findViewById(R.id.fotocard);


        }
    }
}
