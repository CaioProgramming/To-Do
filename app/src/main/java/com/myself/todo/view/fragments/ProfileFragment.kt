package com.myself.todo.view.fragments


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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        val fragmentProfileBinding: FragmentProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        ProfilePresenter(activity!!, fragmentProfileBinding)
        return fragmentProfileBinding.root
    }


}