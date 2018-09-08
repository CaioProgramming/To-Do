package com.myself.todo.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.myself.todo.Beans.Events;
import com.myself.todo.Database.ObjRepository;
import com.myself.todo.R;

import java.util.List;

public class RecyclerAdapterFavorites extends RecyclerView.Adapter<RecyclerAdapterFavorites.MyViewHolder> {


    private Context mContext;
    private Dialog myDialog;
    private ObjRepository lst;

    private List<Events> mData;

    public RecyclerAdapterFavorites(Context mContext, List<Events> mData) {
        this.mContext = mContext;
        this.mData = mData;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardfavoritos,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapterFavorites.MyViewHolder holder, final int position) {
        holder.titlef.setText(mData.get(position).getEvento());
        holder.descricaof.setText(mData.get(position).getDescricao());
        holder.dataf.setText(mData.get(position).getData());

        holder.cardf.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

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
                        lst = new ObjRepository(mContext);
                        lst.abrir();
                        lst.concluir(mData.get(position).getId());
                        lst.fecha();
                    }
                });

                favbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lst = new ObjRepository(mContext);
                        lst.abrir();
                        lst.unfavoritar(mData.get(position).getId());
                        lst.fecha();
                    }
                });

                dltbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        favbtn.setDrawingCacheBackgroundColor(Color.YELLOW);
                        lst = new ObjRepository(mContext);
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
        if(mData.size() == 0){
            return 0;

        }else{
        return mData.size();}
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView  titlef,descricaof,dataf;

        CardView cardf;
        public MyViewHolder(View view) {
            super(view);
            titlef = itemView.findViewById(R.id.titulofavorites);
            descricaof = itemView.findViewById(R.id.descricaofavorites);
            dataf = itemView.findViewById(R.id.datafavorites);

            cardf = itemView.findViewById(R.id.favoritecard);


        }
    }
}
