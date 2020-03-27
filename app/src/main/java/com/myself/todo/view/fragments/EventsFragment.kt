package com.myself.todo.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.myself.todo.R
import com.myself.todo.databinding.FragmentEventsBinding
import com.myself.todo.presenter.EventsPresenter

/**
 * A simple [Fragment] subclass.
 */
class EventsFragment : Fragment() {
    var eventsBinding: FragmentEventsBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        eventsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_events,container,false)
        EventsPresenter(this)
        return eventsBinding?.root
    }

}