package com.myself.todo.view.fragments

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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fotosBinding: FragmentFotosBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_fotos, container, false)
        FotosPresenter(activity!!, fotosBinding)
        return fotosBinding.root
    }
}