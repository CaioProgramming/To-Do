package com.myself.todo.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myself.todo.Beans.Events;
import com.myself.todo.R;

import java.util.List;

import de.mateware.snacky.Snacky;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    DatabaseReference raiz;
    private Context mContext;
    private Dialog myDialog;
    private Activity mActivity;
    private List<Events> mData;
    private RealtimeBlurView blurView;

    public RecyclerAdapter(Context mContext, Activity activity, List<Events> mData, RealtimeBlurView blurView) {
        this.mContext = mContext;
        this.mData = mData;
        this.blurView = blurView;
        this.mActivity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardlayout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerAdapter.MyViewHolder holder, final int position) {
        raiz = FirebaseDatabase.getInstance().getReference("events");
        holder.title.setText(mData.get(position).getEvento());
        holder.descricao.setText(mData.get(position).getDescricao());
        holder.data.setText(mData.get(position).getData());
        if (mData.get(position).getStatus().equals("F")) {
            holder.cardlayout.setBackgroundResource(R.drawable.gradalbum);
            holder.title.setTextColor(Color.WHITE);
            holder.data.setTextColor(Color.WHITE);
            holder.descricao.setTextColor(Color.WHITE);
        } else if (mData.get(position).getStatus().equals("C")) {
            holder.cardlayout.setBackgroundResource(R.drawable.gradmusic);
            holder.title.setTextColor(Color.WHITE);
            holder.data.setTextColor(Color.WHITE);
            holder.descricao.setTextColor(Color.WHITE);

        }


        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                blurView.setBlurRadius(20);

                myDialog = new Dialog(mContext);
                myDialog.setContentView(R.layout.popup);
                TextView event,desc;
                final Button favbtn,dltbtn,scsbtn;
                event = myDialog.findViewById(R.id.eventdlg);
                desc = myDialog.findViewById(R.id.descricaodlg);
                favbtn = myDialog.findViewById(R.id.dlgbtn);
                dltbtn = myDialog.findViewById(R.id.dlgexcluir);
                scsbtn = myDialog.findViewById(R.id.dlgcncl);

                event.setText(mData.get(position).getEvento());
                desc.setText(mData.get(position).getDescricao());

                scsbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Status(position, "C");
                    }
                });


                favbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mData.get(position).getStatus().equals("F")) {
                            Status(position, "C");
                        } else {
                            Status(position, "F");
                        }

                    }
                });

                dltbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        raiz.child(mData.get(position).getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Snacky.builder().setActivity(mActivity).success().setText("Evento apagado").show();
                                } else {
                                    Snacky.builder().setActivity(mActivity).error().setText("Erro " + task.getException()).show();
                                }
                            }
                        });
                    }
                });
                myDialog.show();
                myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        blurView.setBlurRadius(0);
                    }
                });
                return true;
            }
        });


    }

    private void Status(int position, String status) {
        final Events e = mData.get(position);
        e.setStatus(status);
        System.out.println(e.getId());
        raiz.child(e.getId()).setValue(e).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (e.getStatus().equals("F")) {
                        Snacky.builder().setActivity(mActivity).success().setText("Evento adicionado aos favoritos").show();
                    } else {
                        Snacky.builder().setActivity(mActivity).success().setText("Evento removido dos favoritos").show();

                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        if(mData.size() == 0){
            return 0;

        }else{
        return mData.size();}
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout cardlayout;
        TextView  title,descricao,data;
        CheckBox favorite, succescheck;
        CardView card;
        public MyViewHolder(View view) {
            super(view);
            cardlayout = itemView.findViewById(R.id.cardlayout);
            title = itemView.findViewById(R.id.titulo);
            descricao = itemView.findViewById(R.id.descricao);
            data = itemView.findViewById(R.id.data);
            card = itemView.findViewById(R.id.eventcard);


        }
    }
}
