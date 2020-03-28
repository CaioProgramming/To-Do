package com.myself.todo.view.fragments


import android.content.Context
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.myself.todo.R
import com.myself.todo.databinding.FragmentProfileBinding
import com.myself.todo.presenter.ProfilePresenter

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment(){
    var fragmentProfileBinding: FragmentProfileBinding? = null
    private var profilePresenter: ProfilePresenter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
       fragmentProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile,container,false)
        return fragmentProfileBinding?.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentProfileBinding?.let {
            profilePresenter = ProfilePresenter(this)
            profilePresenter!!.initview()
        }

    }

}