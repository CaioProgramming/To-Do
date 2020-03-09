package com.myself.todo.Adapters

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.github.mmin18.widget.RealtimeBlurView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.myself.todo.Beans.Events
import com.myself.todo.R
import de.mateware.snacky.Snacky
import org.junit.runner.RunWith

class RecyclerAdapter(private val mContext: Context?, private val mActivity: Activity?, private val mData: MutableList<Events?>?, private val blurView: RealtimeBlurView?) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder?>() {
    var raiz: DatabaseReference? = null
    private var myDialog: Dialog? = null
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder? {
        val view: View
        val mInflater = LayoutInflater.from(mContext)
        view = mInflater.inflate(R.layout.cardlayout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        raiz = FirebaseDatabase.getInstance().getReference("events")
        holder.title.setText(mData.get(position).getEvento())
        holder.descricao.setText(mData.get(position).getDescricao())
        holder.data.setText(mData.get(position).getData())
        if (mData.get(position).getStatus() == "F") {
            holder.cardlayout.setBackgroundResource(R.drawable.gradalbum)
            holder.title.setTextColor(Color.WHITE)
            holder.data.setTextColor(Color.WHITE)
            holder.descricao.setTextColor(Color.WHITE)
        } else if (mData.get(position).getStatus() == "C") {
            holder.cardlayout.setBackgroundResource(R.drawable.gradmusic)
            holder.title.setTextColor(Color.WHITE)
            holder.data.setTextColor(Color.WHITE)
            holder.descricao.setTextColor(Color.WHITE)
        }
        holder.card.setOnLongClickListener(OnLongClickListener {
            blurView.setBlurRadius(20f)
            myDialog = Dialog(mContext)
            myDialog.setContentView(R.layout.popup)
            val event: TextView?
            val desc: TextView?
            val favbtn: Button?
            val dltbtn: Button?
            val scsbtn: Button?
            event = myDialog.findViewById<TextView?>(R.id.eventdlg)
            desc = myDialog.findViewById<TextView?>(R.id.descricaodlg)
            favbtn = myDialog.findViewById<Button?>(R.id.dlgbtn)
            dltbtn = myDialog.findViewById<Button?>(R.id.dlgexcluir)
            scsbtn = myDialog.findViewById<Button?>(R.id.dlgcncl)
            event.setText(mData.get(position).getEvento())
            desc.setText(mData.get(position).getDescricao())
            scsbtn.setOnClickListener(View.OnClickListener { Status(position, "C") })
            favbtn.setOnClickListener(View.OnClickListener {
                if (mData.get(position).getStatus() == "F") {
                    Status(position, "C")
                } else {
                    Status(position, "F")
                }
            })
            dltbtn.setOnClickListener(View.OnClickListener {
                raiz.child(mData.get(position).getId()).removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Snacky.builder().setActivity(mActivity).success().setText("Evento apagado").show()
                    } else {
                        Snacky.builder().setActivity(mActivity).error().setText("Erro " + task.exception).show()
                    }
                }
            })
            myDialog.show()
            myDialog.setOnDismissListener(DialogInterface.OnDismissListener { blurView.setBlurRadius(0f) })
            true
        })
    }

    private fun Status(position: Int, status: String?) {
        val e = mData.get(position)
        e.setStatus(status)
        println(e.getId())
        raiz.child(e.getId()).setValue(e).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (e.getStatus() == "F") {
                    Snacky.builder().setActivity(mActivity).success().setText("Evento adicionado aos favoritos").show()
                } else {
                    Snacky.builder().setActivity(mActivity).success().setText("Evento removido dos favoritos").show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (mData.size == 0) {
            0
        } else {
            mData.size
        }
    }

    class MyViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
        var cardlayout: LinearLayout?
        var title: TextView?
        var descricao: TextView?
        var data: TextView?
        var favorite: CheckBox? = null
        var succescheck: CheckBox? = null
        var card: CardView?

        init {
            cardlayout = itemView.findViewById<LinearLayout?>(R.id.cardlayout)
            title = itemView.findViewById<TextView?>(R.id.titulo)
            descricao = itemView.findViewById<TextView?>(R.id.descricao)
            data = itemView.findViewById<TextView?>(R.id.data)
            card = itemView.findViewById(R.id.eventcard)
        }
    }

}