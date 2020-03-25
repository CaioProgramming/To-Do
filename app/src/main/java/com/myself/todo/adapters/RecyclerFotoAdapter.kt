package com.myself.todo.adapters

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.mmin18.widget.RealtimeBlurView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.myself.todo.Beans.Album
import com.myself.todo.R
import com.myself.todo.Utils.Utilities
import de.mateware.snacky.Snacky
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

class RecyclerFotoAdapter(//AlbumRepository lst;
        private val mContext: Context?, private val mActivity: Activity?, private val mData: MutableList<Album?>?, private val mBlur: RealtimeBlurView?) : RecyclerView.Adapter<RecyclerFotoAdapter.MyViewHolder?>() {
    var i = 0
    var raiz: DatabaseReference? = null
    private var myDialog: Dialog? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder? {
        val view: View
        val mInflater = LayoutInflater.from(mContext)
        view = mInflater.inflate(R.layout.cardlayoutfotos, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        raiz = FirebaseDatabase.getInstance().getReference("album")
        Glide.with(mContext).load(mData.get(position).getFotouri()).into(holder.pic)
        val myanim2 = AnimationUtils.loadAnimation(mContext, R.anim.fade_in)
        val myanim = AnimationUtils.loadAnimation(mContext, R.anim.pop_out)
        if (mData.get(position).getStatus() == "F") {
            holder.fav.setChecked(true)
        }
        holder.fav.setOnClickListener(View.OnClickListener {
            if (holder.fav.isChecked()) { //user = FirebaseAuth.getInstance().getCurrentUser();
                Status(position, "F")
            } else {
                Status(position, "N")
            }
        })
        holder.card.startAnimation(myanim2)
        myDialog = Dialog(mContext)
        myDialog.setContentView(R.layout.popupfoto)
        if (holder.card.isPressed()) {
            Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            raiz = FirebaseDatabase.getInstance().getReference("album")
            raiz.keepSynced(true)
            mBlur.animate()
            mBlur.setBlurRadius(20f)
            val event: TextView?
            val desc: TextView?
            val pic: ImageView?
            val dltbtn: Button?
            val favbtn: CheckBox?
            pic = myDialog.findViewById<ImageView?>(R.id.albpic)
            event = myDialog.findViewById<TextView?>(R.id.descricaopic)
            desc = myDialog.findViewById<TextView?>(R.id.diapic)
            favbtn = myDialog.findViewById<CheckBox?>(R.id.dlgfavpic)
            dltbtn = myDialog.findViewById<Button?>(R.id.dlgexcluir)
            event.setText(mData.get(position).getDescription())
            desc.setText(mData.get(position).getDia())
            Glide.with(mContext).load(mData.get(position)).into(pic)
            if (mData.get(position).getStatus() == "F") {
                favbtn.setChecked(true)
            }
            favbtn.setOnClickListener(View.OnClickListener {
                if (favbtn.isChecked()) {
                    Status(position, "F")
                } else {
                    Status(position, "N")
                }
            })
            myDialog.setOnDismissListener(DialogInterface.OnDismissListener { mBlur.setBlurRadius(0f) })
            myDialog.show()
        } else {
            myDialog.dismiss()
        }
        holder.card.setOnLongClickListener(OnLongClickListener {
            if (holder.card.isPressed()) {
                myDialog = Dialog(mActivity, R.style.CustomDialog)
                Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mBlur.animate().alpha(2000f).start()
                mBlur.setBlurRadius(20f)
                myDialog = Dialog(mContext)
                myDialog.setContentView(R.layout.popupfoto)
                val event: TextView?
                val desc: TextView?
                val pic: ImageView?
                val dltbtn: Button?
                val favbtn: CheckBox?
                pic = myDialog.findViewById<ImageView?>(R.id.albpic)
                event = myDialog.findViewById<TextView?>(R.id.descricaopic)
                desc = myDialog.findViewById<TextView?>(R.id.diapic)
                favbtn = myDialog.findViewById<CheckBox?>(R.id.dlgfavpic)
                dltbtn = myDialog.findViewById<Button?>(R.id.dlgexcluir)
                event.setText(mData.get(position).getDescription())
                desc.setText(mData.get(position).getDia())
                try {
                    pic.setImageBitmap(Utilities.Companion.handleSamplingAndRotationBitmap(mContext, Uri.parse(mData.get(position).getFotouri())))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (mData.get(position).getStatus() == "F") {
                    favbtn.setChecked(true)
                }
                favbtn.setOnClickListener(View.OnClickListener {
                    if (favbtn.isChecked()) {
                        Status(position, "N")
                    } else {
                        Status(position, "F")
                    }
                })
                dltbtn.setOnClickListener(View.OnClickListener {
                    //favbtn.setDrawingCacheBackgroundColor(Color.YELLOW);
                    raiz.child(mData.get(position).getId()).removeValue().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Snacky.builder().setActivity(mActivity).success().setText("Foto apagada").show()
                        } else {
                            Snacky.builder().setActivity(mActivity).error().setText("Erro " + task.exception)
                        }
                    }
                })
                myDialog.show()
                myDialog.setOnDismissListener(DialogInterface.OnDismissListener {
                    mBlur.animate()
                    mBlur.setBlurRadius(0f)
                })
                true
            } else {
                myDialog.dismiss()
                false
            }
        })
    }

    private fun Status(position: Int, status: String?) {
        val a = Album()
        a.status = status
        a.id = mData.get(position).getId()
        a.fotouri = mData.get(position).getFotouri()
        a.description = mData.get(position).getDescription()
        a.dia = mData.get(position).getDia()
        a.userID = mData.get(position).getUserID()
        println(mData.get(position).getId())
        raiz.child(a.id).setValue(a).addOnCompleteListener {
            if (a.status == "F") {
                Snacky.builder()
                        .setText("Foto adicionada aos favoritos")
                        .setTextColor(Color.BLACK)
                        .setIcon(R.drawable.ic_favorite_black_24dp)
                        .setBackgroundColor(Color.WHITE)
                        .setActivity(mActivity)
                        .setDuration(Snacky.LENGTH_SHORT)
                        .build()
                        .show()
            } else {
                Snacky.builder()
                        .setText("Foto removida aos favoritos")
                        .setTextColor(Color.BLACK)
                        .setIcon(R.drawable.ic_favorite2_black_24dp)
                        .setBackgroundColor(Color.WHITE)
                        .setActivity(mActivity)
                        .setDuration(Snacky.LENGTH_SHORT)
                        .build()
                        .show()
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
        var pic: ImageView?
        var fav: CheckBox?
        var card: CardView?

        init {
            fav = itemView.findViewById<CheckBox?>(R.id.favcheck)
            pic = itemView.findViewById<ImageView?>(R.id.pic)
            card = itemView.findViewById(R.id.fotocard)
        }
    }

}