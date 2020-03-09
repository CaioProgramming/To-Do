package com.myself.todo.Fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mmin18.widget.RealtimeBlurView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.myself.todo.Adapters.RecyclerFotoAdapter
import com.myself.todo.Beans.Album
import com.myself.todo.R
import de.mateware.snacky.Snacky
import org.junit.runner.RunWith
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class FotosFragment : Fragment() {
    var lstalbum: MutableList<Album?>? = null
    var favorites: Switch? = null
    var blur: RealtimeBlurView? = null
    var pb: ProgressBar? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fotos, container, false)
        val frameLayout = view.findViewById<FrameLayout?>(R.id.fotos)
        lstalbum = ArrayList()
        blur = activity.findViewById(R.id.rootblur)
        favorites = view.findViewById<Switch?>(R.id.favoriteswitch)
        val recycler: RecyclerView = view.findViewById(R.id.recyclerviewfotos)
        val empty = view.findViewById<LinearLayout?>(R.id.nofotos)
        val pb = view.findViewById<ProgressBar?>(R.id.progress)
        CarregarFotos(recycler, empty)
        favorites.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                pb.setVisibility(View.VISIBLE)
                pb.animate().start()
                recycler.visibility = View.INVISIBLE
                lstalbum.clear()
                recycler.destroyDrawingCache()
                val countDownTimer = object : CountDownTimer(2000, 300) {
                    override fun onTick(l: Long) {}
                    override fun onFinish() {
                        pb.setVisibility(View.INVISIBLE)
                        recycler.visibility = View.VISIBLE
                        CarregarFotosFavoritas(recycler, empty)
                    }
                }.start()
            } else {
                pb.setVisibility(View.VISIBLE)
                pb.animate().start()
                recycler.visibility = View.INVISIBLE
                lstalbum.clear()
                recycler.destroyDrawingCache()
                val countDownTimer = object : CountDownTimer(2000, 300) {
                    override fun onTick(l: Long) {}
                    override fun onFinish() {
                        pb.setVisibility(View.INVISIBLE)
                        recycler.visibility = View.VISIBLE
                        CarregarFotos(recycler, empty)
                    }
                }.start()
            }
        })
        return view
    }

    private fun CarregarFotos(recycler: RecyclerView?, empty: LinearLayout?) {
        val user = FirebaseAuth.getInstance().currentUser
        val raiz = FirebaseDatabase.getInstance().reference.child("album")
        raiz.keepSynced(true)
        if (user != null) {
            raiz.child("album").orderByChild("userID").equalTo(user.uid)
        }
        raiz.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.children) {
                    val a: Album = d.getValue<Album?>(Album::class.java)
                    val album = Album()
                    if (a != null) {
                        println(a.id)
                        println(d.key)
                        album.fotouri = a.fotouri
                        album.description = a.description
                        album.status = a.status
                        album.dia = a.dia
                        album.id = d.key
                        album.userID = a.userID
                        lstalbum.add(album)
                        println(album.id)
                        println(lstalbum.size)
                    }
                }
                recycler.setVisibility(View.VISIBLE)
                val llm = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
                recycler.setHasFixedSize(true)
                println(lstalbum.size)
                //final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
                val myadapter = RecyclerFotoAdapter(context, activity, lstalbum, blur)
                recycler.setAdapter(myadapter)
                recycler.setLayoutManager(llm)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Snacky.builder().setActivity(Objects.requireNonNull(activity)).error().setText("Erro $databaseError").show()
            }
        })
    }

    private fun CarregarFotosFavoritas(recycler: RecyclerView?, empty: LinearLayout?) {
        val user = FirebaseAuth.getInstance().currentUser
        val raiz = FirebaseDatabase.getInstance().reference.child("album")
        raiz.keepSynced(true)
        if (user != null) {
            raiz.child("album").orderByChild("userID").equalTo(user.uid)
        }
        raiz.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.children) {
                    val a: Album = d.getValue<Album?>(Album::class.java)
                    val album = Album()
                    if (a != null) {
                        album.fotouri = a.fotouri
                        album.description = a.description
                        album.status = a.status
                        album.dia = a.dia
                        album.id = a.id
                        album.userID = a.userID
                        println(a.status)
                        if (album.status == "F") {
                            lstalbum.clear()
                            lstalbum.add(a)
                            println(lstalbum.size)
                        }
                    }
                }
                recycler.setVisibility(View.VISIBLE)
                val llm = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
                recycler.setHasFixedSize(true)
                println(lstalbum.size)
                //final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
                val myadapter = RecyclerFotoAdapter(context, activity, lstalbum, blur)
                recycler.setAdapter(myadapter)
                recycler.setLayoutManager(llm)
                //recycler.startAnimation(myanim2);
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Snacky.builder().setActivity(Objects.requireNonNull(activity)).error().setText("Erro $databaseError").show()
            }
        })
    }
}