package com.myself.todo.view.fragments

import android.animation.ValueAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asksira.bsimagepicker.BSImagePicker
import com.asksira.bsimagepicker.BSImagePicker.OnSingleImageSelectedListener
import com.asksira.bsimagepicker.Utils
import com.bumptech.glide.Glide
import com.github.mmin18.widget.RealtimeBlurView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.myself.todo.adapters.RecyclerAdapter
import com.myself.todo.adapters.RecyclerFotoAdapter
import com.myself.todo.Beans.Album
import com.myself.todo.Beans.Events
import com.myself.todo.view.activities.ActivityProfileEdit
import com.myself.todo.R
import de.hdodenhof.circleimageview.CircleImageView
import de.mateware.snacky.Snacky
import org.junit.runner.RunWith
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment(), OnSingleImageSelectedListener {
    var lstevents: MutableList<Events?>? = null
    var lstalbum: MutableList<Album?>? = null
    var user = FirebaseAuth.getInstance().currentUser
    var blur: RealtimeBlurView? = null
    var picsnumber: TextView? = null
    var objectivesnumber: TextView? = null
    var musics: TextView? = null
    var album: DatabaseReference? = FirebaseDatabase.getInstance().reference.child("album")
    var events: DatabaseReference? = FirebaseDatabase.getInstance().reference.child("events")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val userdata = view.findViewById<RelativeLayout?>(R.id.userdata)
        val profile = view.findViewById<Button?>(R.id.profile)
        val data = view.findViewById<LinearLayout?>(R.id.data)
        blur = view.findViewById(R.id.blur)
        val objectiverecyler: RecyclerView = view.findViewById(R.id.objectiverecyler)
        val fotorecycler: RecyclerView = view.findViewById(R.id.fotorecycler)
        val progress = view.findViewById<ProgressBar?>(R.id.progress)
        val recently = view.findViewById<ScrollView?>(R.id.recently)
        musics = view.findViewById<TextView?>(R.id.musics)
        objectivesnumber = view.findViewById<TextView?>(R.id.objectivesnumber)
        picsnumber = view.findViewById<TextView?>(R.id.picsnumber)
        val username = view.findViewById<TextView?>(R.id.username)
        val profilepic: CircleImageView = view.findViewById(R.id.profilepic)
        profilepic.setOnClickListener { Picalert() }
        InitDB()
        profile.setOnClickListener(View.OnClickListener {
            val i = Intent(activity, ActivityProfileEdit::class.java)
            startActivity(i)
        })
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            username.setText(user.displayName)
        }
        assert(user != null)
        if (user.getPhotoUrl() == null) {
        } else {
            Glide.with(this).load(user.getPhotoUrl()).into(profilepic)
        }
        //ContarItems(musics, objectivesnumber, picsnumber, username, usuario, usuarioB);
        CarregarMusicas()
        CarregarFotos(fotorecycler)
        CarregarObjetivos(objectiverecyler)
        val countDownTimer = object : CountDownTimer(3000, 100) {
            override fun onTick(l: Long) {}
            override fun onFinish() {
                progress.setVisibility(View.INVISIBLE)
                recently.setVisibility(View.VISIBLE)
            }
        }.start()
        return view
    }

    private fun Picalert() {
        val singleSelectionPicker = BSImagePicker.Builder("com.myself.fileprovider") //Default: Integer.MAX_VALUE. Don't worry about performance :)
                .hideGalleryTile()
                .setSpanCount(3) //Default: 3. This is the number of columns
                .setGridSpacing(Utils.dp2px(2)) //Default: 2dp. Remember to pass in a value in pixel.
                .setPeekHeight(Utils.dp2px(360)) //Default: 360dp. This is the initial height of the dialog.
                .setOverSelectTextColor(R.color.black)
                .setMultiSelectDoneTextColor(R.color.blue_300)
                .build()
        singleSelectionPicker.show(childFragmentManager, "picker")
    }

    private fun InitDB() {
        lstevents = ArrayList()
        lstalbum = ArrayList()
    }

    private fun CarregarFotos(recycler: RecyclerView?) {
        lstalbum.clear()
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
                val llm = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
                recycler.setHasFixedSize(true)
                println(lstalbum.size)
                //final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
                val myadapter = RecyclerFotoAdapter(context, activity, lstalbum, blur)
                recycler.setAdapter(myadapter)
                recycler.setLayoutManager(llm)
                val animator = ValueAnimator.ofInt(0, lstalbum.size)
                animator.duration = 3000
                animator.addUpdateListener { picsnumber.setText(animator.animatedValue.toString()) }
                animator.start()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun CarregarObjetivos(recycler: RecyclerView?) {
        lstevents.clear()
        val user = FirebaseAuth.getInstance().currentUser
        val raiz = FirebaseDatabase.getInstance().reference.child("events")
        raiz.keepSynced(true)
        if (user != null) {
            raiz.child("events").orderByChild("userID").equalTo(user.uid)
        }
        raiz.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.children) {
                    val e: Events = d.getValue<Events?>(Events::class.java)
                    val events = Events()
                    if (e != null) {
                        println(e.id)
                        println(d.key)
                        events.evento = e.evento
                        events.descricao = e.descricao
                        events.status = e.status
                        events.data = e.data
                        events.id = d.key
                        events.userID = e.userID
                        lstevents.add(events)
                        println(events.id)
                        println(lstevents.size)
                    }
                }
                recycler.setVisibility(View.VISIBLE)
                val llm = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
                recycler.setHasFixedSize(true)
                println(lstevents.size)
                val myadapter = RecyclerAdapter(context, activity, lstevents, blur)
                recycler.setAdapter(myadapter)
                recycler.setLayoutManager(llm)
                val animator = ValueAnimator.ofInt(0, lstevents.size)
                animator.duration = 3000
                animator.addUpdateListener { objectivesnumber.setText(animator.animatedValue.toString()) }
                animator.start()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun CarregarMusicas() {
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        val cursor = Objects.requireNonNull(activity).getContentResolver().query(uri, null, selection, null, MediaStore.Audio.Media.DATE_MODIFIED)!!
        musics.setText(cursor.count.toString())
        val valueAnimator = ValueAnimator.ofInt(0, cursor.count)
        valueAnimator.duration = 1000
        valueAnimator.addUpdateListener { valueAnimator -> musics.setText(valueAnimator.animatedValue.toString()) }
        valueAnimator.start()
    }

    override fun onSingleImageSelected(uri: Uri?) {
        val user = FirebaseAuth.getInstance().currentUser
        val profileChangeRequest = UserProfileChangeRequest.Builder().setPhotoUri(uri).build()
        user?.updateProfile(profileChangeRequest)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Snacky.builder().setActivity(Objects.requireNonNull(activity)).success().setText("Foto de perfil alterada").show()
                //CircleImageView profilepic = getActivity().findViewById(R.id.profile_pic);
//Glide.with(getActivity()).load(user.getPhotoUrl()).into(profilepic);
            } else {
                Snacky.builder().setActivity(Objects.requireNonNull(activity)).success().setText("Erro " + task.exception).show()
            }
        }
    }
}