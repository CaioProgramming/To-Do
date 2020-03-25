package com.myself.todo.adapters

import android.app.Activity
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.myself.todo.Beans.Events
import com.myself.todo.Beans.Tarefas
import com.myself.todo.R
import com.myself.todo.databinding.TasksLayoutBinding
import com.myself.todo.model.EventsDB
import kotlinx.android.synthetic.main.tasks_layout.view.*

class RecyclerTarefasAdapter(val activity: Activity,val events: Events) : RecyclerView.Adapter<RecyclerTarefasAdapter.TarefasViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarefasViewHolder {
        val tasksLayoutBinding: TasksLayoutBinding= DataBindingUtil.inflate(LayoutInflater.from(activity),R.layout.tasks_layout,parent,false)
        return TarefasViewHolder(tasksLayoutBinding)
    }

    override fun onBindViewHolder(holder: TarefasViewHolder, position: Int) {
            val task = events.tasks!![position]
            holder.tasksLayoutBinding.taskname.taskname.text = task.tarefa
            holder.tasksLayoutBinding.date.text = task.data
            holder.tasksLayoutBinding.taskCheck.setOnCheckedChangeListener { buttonView, isChecked ->
              complete(position,isChecked)
            }
            if (task.completed){
                 paintflag(holder.tasksLayoutBinding.taskname)
                 paintflag(holder.tasksLayoutBinding.date)
            }
    }

    private fun paintflag(tv:TextView){
        tv.paintFlags = tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    private fun complete(position: Int,boolean: Boolean){
        val task = events.tasks!![position]
        events.tasks!![position] = task
        EventsDB(activity).alterar(events.id!!,events)
    }



    override fun getItemCount(): Int {
      return events.tasks!!.size
    }

    class TarefasViewHolder(val tasksLayoutBinding: TasksLayoutBinding) : RecyclerView.ViewHolder(tasksLayoutBinding.root)




}