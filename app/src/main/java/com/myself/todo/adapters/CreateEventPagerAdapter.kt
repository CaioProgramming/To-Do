package com.myself.todo.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.myself.todo.Beans.Events
import com.myself.todo.Beans.Tarefas
import com.myself.todo.R
import com.myself.todo.Utils.Utilities
import com.myself.todo.databinding.CreateEventPagerBinding
import com.myself.todo.databinding.EventResumeLayoutBinding

class CreateEventPagerAdapter(val activity:Activity, val background:CardView, val myevent:Events, val doneListener: TextView.OnEditorActionListener): PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }
    private fun addChipToGroup(nome: String,chipGroup: ChipGroup) {
        val chip = Chip(activity)
        chip.text = nome
        chip.isCloseIconVisible = true
        chipGroup.addView(chip)
        val tarefa = Tarefas(nome,Utilities.actualday(),false)
        myevent.tasks.add(tarefa)
        chip.setOnCloseIconClickListener {
            chipGroup.removeView(chip)
            val removewhere = myevent.tasks.indexOfLast{ tarefas -> tarefas.tarefa!!.contains(nome) }
            myevent.tasks.remove(myevent.tasks[removewhere])
        }
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val  createEventPagerBinding: CreateEventPagerBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.create_event_pager, container,false)
        val pagersetting = Utilities.pagers[position]
        background.setCardBackgroundColor(pagersetting.color)
        createEventPagerBinding.title.text = pagersetting.title
        pagersetting.image?.let { Glide.with(activity).load(it).into(createEventPagerBinding.image)}
        when (position) {
            1 -> {
                createEventPagerBinding.pagerEditText.setOnEditorActionListener { v, actionId, event ->
                    addChipToGroup(v!!.text.toString(),createEventPagerBinding.chipGroup)
                    false
                }
            }
            0 -> {
                createEventPagerBinding.pagerEditText.setOnEditorActionListener { v, actionId, event ->
                    myevent.evento = createEventPagerBinding.pagerEditText.text.toString()
                    false
                }
            }
            else -> {
                createEventPagerBinding.pagerEditText.setOnEditorActionListener(doneListener)
            }
        }
        createEventPagerBinding.pagerEditText.visibility =  if (position == count - 1){ GONE }else{ VISIBLE }
        createEventPagerBinding.title.visibility =  if (position == count - 1){ GONE }else{ VISIBLE }
        createEventPagerBinding.chipGroup.visibility =  if (position == count - 1){ GONE }else{ VISIBLE }
        createEventPagerBinding.eventResume.resumeCard.visibility =  if (position != count - 1){ GONE }else{ VISIBLE }
       if (createEventPagerBinding.eventResume.resumeCard.visibility == VISIBLE){
           createEventResume(createEventPagerBinding.eventResume)
       }

        container.addView(createEventPagerBinding.root)
        return createEventPagerBinding.root
    }
    override fun getCount(): Int {
        return 3
    }

    private fun createEventResume(eventResumeLayoutBinding: EventResumeLayoutBinding){
        eventResumeLayoutBinding.eventTitle.text = myevent.evento
        var tasks = ""
        for (task in myevent.tasks){
            tasks += "${task.tarefa}\n"
        }
        eventResumeLayoutBinding.eventTasks.text = tasks
    }

}