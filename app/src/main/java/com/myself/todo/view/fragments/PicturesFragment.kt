package com.myself.todo.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.myself.todo.R
import com.myself.todo.databinding.FragmentFotosBinding
import com.myself.todo.presenter.FotosPresenter

/**
 * A simple [Fragment] subclass.
 */
class PicturesFragment : Fragment() {
    var fotosBinding:FragmentFotosBinding? = null
    var fotosPresenter: FotosPresenter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fotosBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_fotos,container,false)

        return  fotosBinding!!.root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        fotosBinding?.let {
            fotosPresenter = FotosPresenter(this)
            fotosPresenter!!.initview()
        }
    }


}