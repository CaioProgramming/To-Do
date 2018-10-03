package com.myself.todo.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myself.todo.Beans.Album;
import com.myself.todo.R;
import com.myself.todo.Utils.Utilities;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import de.mateware.snacky.Snacky;

public class RecyclerFotoAdapter extends RecyclerView.Adapter<RecyclerFotoAdapter.MyViewHolder> {


    int i = 0;
    DatabaseReference raiz;
    private Activity mActivity;
    private Dialog myDialog;
    private List<Album> mData;
    private RealtimeBlurView mBlur;
    //AlbumRepository lst;
    private Context mContext;

    public RecyclerFotoAdapter(Context mContext, Activity mActivity, List<Album> mData, RealtimeBlurView blur) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mData = mData;
        this.mBlur = blur;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardlayoutfotos, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerFotoAdapter.MyViewHolder holder, final int position) {

        raiz = FirebaseDatabase.getInstance().getReference("album");
        Glide.with(mContext).load(mData.get(position).getFotouri()).into(holder.pic);
        final Animation myanim2 = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        final Animation myanim = AnimationUtils.loadAnimation(mContext, R.anim.pop_out);

        if (mData.get(position).getStatus().equals("F")) {
            holder.fav.setChecked(true);
        }

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.fav.isChecked()) {
                    //user = FirebaseAuth.getInstance().getCurrentUser();
                    Status(position, "F");

                } else {
                    Status(position, "N");
                }
            }
        });
        holder.card.startAnimation(myanim2);
        myDialog = new Dialog(mContext);

        myDialog.setContentView(R.layout.popupfoto);
        if (holder.card.isPressed()) {
            Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            raiz = FirebaseDatabase.getInstance().getReference("album");
            raiz.keepSynced(true);
            mBlur.animate();
            mBlur.setBlurRadius(20);

            TextView event, desc;
            ImageView pic;
            final Button dltbtn;
            final CheckBox favbtn;
            pic = myDialog.findViewById(R.id.albpic);
            event = myDialog.findViewById(R.id.descricaopic);
            desc = myDialog.findViewById(R.id.diapic);
            favbtn = myDialog.findViewById(R.id.dlgfavpic);
            dltbtn = myDialog.findViewById(R.id.dlgexcluir);

            event.setText(mData.get(position).getDescription());
            desc.setText(mData.get(position).getDia());
            Glide.with(mContext).load(mData.get(position)).into(pic);
            if (mData.get(position).getStatus().equals("F")) {
                favbtn.setChecked(true);
            }
            favbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (favbtn.isChecked()) {
                        Status(position, "F");
                    } else {
                        Status(position, "N");
                    }
                }
            });
            myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    mBlur.setBlurRadius(0);
                }
            });
            myDialog.show();


        } else {
            myDialog.dismiss();
        }
        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (holder.card.isPressed()) {
                    myDialog = new Dialog(mActivity, R.style.CustomDialog);
                    Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    mBlur.animate().alpha(2000).start();

                    mBlur.setBlurRadius(20);
                    myDialog = new Dialog(mContext);

                    myDialog.setContentView(R.layout.popupfoto);
                    TextView event, desc;
                    ImageView pic;
                    final Button dltbtn;
                    final CheckBox favbtn;
                    pic = myDialog.findViewById(R.id.albpic);
                    event = myDialog.findViewById(R.id.descricaopic);
                    desc = myDialog.findViewById(R.id.diapic);
                    favbtn = myDialog.findViewById(R.id.dlgfavpic);
                    dltbtn = myDialog.findViewById(R.id.dlgexcluir);

                    event.setText(mData.get(position).getDescription());
                    desc.setText(mData.get(position).getDia());
                    try {
                        pic.setImageBitmap(Utilities.handleSamplingAndRotationBitmap(mContext, Uri.parse(mData.get(position).getFotouri())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (mData.get(position).getStatus().equals("F")) {
                        favbtn.setChecked(true);
                    }


                    favbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (favbtn.isChecked()) {
                                Status(position, "N");
                            } else {
                                Status(position, "F");
                            }
                        }
                    });

                    dltbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //favbtn.setDrawingCacheBackgroundColor(Color.YELLOW);
                            raiz.child(mData.get(position).getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Snacky.builder().setActivity(mActivity).success().setText("Foto apagada").show();
                                    } else {
                                        Snacky.builder().setActivity(mActivity).error().setText("Erro " + task.getException());
                                    }
                                }
                            });
                        }
                    });

                    myDialog.show();
                    myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            mBlur.animate();
                            mBlur.setBlurRadius(0);
                        }
                    });
                    return true;
                } else {


                    myDialog.dismiss();
                    return false;
                }

            }
        });
    }


    private void Status(int position, final String status) {
        final Album a = new Album();
        a.setStatus(status);
        a.setId(mData.get(position).getId());
        a.setFotouri(mData.get(position).getFotouri());
        a.setDescription(mData.get(position).getDescription());
        a.setDia(mData.get(position).getDia());
        a.setUserID(mData.get(position).getUserID());
        System.out.println(mData.get(position).getId());
        raiz.child(a.getId()).setValue(a).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (a.getStatus().equals("F")) {
                    Snacky.builder()
                            .setText("Foto adicionada aos favoritos")
                            .setTextColor(Color.BLACK)
                            .setIcon(R.drawable.ic_favorite_black_24dp)
                            .setBackgroundColor(Color.WHITE)
                            .setActivity(mActivity)
                            .setDuration(Snacky.LENGTH_SHORT)
                            .build()
                            .show();
                } else {
                    Snacky.builder()
                            .setText("Foto removida aos favoritos")
                            .setTextColor(Color.BLACK)
                            .setIcon(R.drawable.ic_favorite2_black_24dp)
                            .setBackgroundColor(Color.WHITE)
                            .setActivity(mActivity)
                            .setDuration(Snacky.LENGTH_SHORT)
                            .build()
                            .show();
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


        ImageView pic;
        CheckBox fav;
        CardView card;

        public MyViewHolder(View view) {
            super(view);

            fav = itemView.findViewById(R.id.favcheck);
            pic = itemView.findViewById(R.id.pic);
            card = itemView.findViewById(R.id.fotocard);


        }
    }
}
