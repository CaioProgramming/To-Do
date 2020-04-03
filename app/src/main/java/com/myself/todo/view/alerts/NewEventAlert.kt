package com.myself.todo.view.alerts

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Typeface
import android.view.LayoutInflater
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.myself.todo.R
import com.myself.todo.databinding.NewEventPopupBinding
import com.myself.todo.model.EventsDB
import com.myself.todo.model.beans.Events
import de.mateware.snacky.Snacky

class NewEventAlert(activity: Activity) : BottomAlertBase(activity) {
    var newEventPopupBinding: NewEventPopupBinding? = null
    override fun setupAlert() {
        newEventPopupBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.new_event_popup, null, false)
        setView(newEventPopupBinding!!.root)
        newEventPopupBinding?.let { configureview() }

    }

    override fun configureview() {
        newEventPopupBinding!!.calendarmoji.text = "📅"
        newEventPopupBinding!!.notemoji.text = "⏰"
        newEventPopupBinding!!.timemoji.text = "\uD83D\uDCDD"
        newEventPopupBinding!!.datepicker.setOnClickListener { createDatePicker() }
        newEventPopupBinding!!.timepicker.setOnClickListener { createTimePicker() }
        newEventPopupBinding!!.saveEvent.setOnClickListener { save() }
        show()

    }

    private fun createDatePicker() {
        val datePicker = DatePickerDialog(activity, object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                val day = "$dayOfMonth/$month/$year"
                newEventPopupBinding!!.eventDay.text = day
                newEventPopupBinding!!.eventDay.setTypeface(newEventPopupBinding!!.eventDay.typeface, Typeface.BOLD)
            }

        }, 0, 0, 0)
        datePicker.show()
    }

    private fun createTimePicker() {
        val timePickerDialog = TimePickerDialog(activity, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                val hour = "$hourOfDay:$minute"
                newEventPopupBinding!!.eventTime.text = hour
                newEventPopupBinding!!.eventTime.setTypeface(newEventPopupBinding!!.eventDay.typeface, Typeface.BOLD)
            }

        }, 0, 0, true).show()
    }

    private fun save() {
        val eventsDB = EventsDB(activity)
        val event = createEvent()
        if (!event.evento.isNullOrBlank()) {
            if (event.icon.isNullOrBlank() || event.data_dia.isNullOrBlank() || event.data_hora.isNullOrBlank()) {
                Snacky.builder().setActivity(activity).warning().setText("Salvando sem ícone")
            }
            eventsDB.inserir(event)
        } else {
            newEventPopupBinding!!.eventName.error = "Ei você precisa colocar todos os detalhes do evento!"
            newEventPopupBinding!!.eventName.requestFocus()
        }
    }


    private fun createEvent(): Events {
        val user = FirebaseAuth.getInstance().currentUser
        val eventname = newEventPopupBinding!!.eventName.text.toString()
        val eventnote = newEventPopupBinding!!.eventNotes.text.toString()
        val eventday = newEventPopupBinding!!.eventDay.text.toString()
        val eventtime = newEventPopupBinding!!.eventTime.text.toString()
        return Events(eventname, eventnote, user!!.uid, eventday, eventtime, null, false)
    }
}