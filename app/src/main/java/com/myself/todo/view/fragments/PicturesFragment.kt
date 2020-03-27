package com.myself.todo.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.myself.todo.adapters.RecyclerFotoAdapter
import com.myself.todo.Beans.Album
import com.myself.todo.R
import com.myself.todo.databinding.FragmentFotosBinding
import com.myself.todo.presenter.FotosPresenter
import de.mateware.snacky.Snacky
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class PicturesFragment : Fragment() {
    var fotosBinding:FragmentFotosBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fotosBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_fotos,container,false)
        fotosBinding?.let { FotosPresenter(this) }
        return  fotosBinding!!.root
    }



}