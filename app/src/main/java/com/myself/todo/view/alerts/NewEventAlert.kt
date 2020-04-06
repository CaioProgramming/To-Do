package com.myself.todo.view.alerts

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Typeface
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.myself.todo.R
import com.myself.todo.databinding.NewEventPopupBinding
import com.myself.todo.model.EventsDB
import com.myself.todo.model.beans.Events
import de.mateware.snacky.Snacky
import java.util.*

class NewEventAlert(activity: Activity) : AlertBase(activity) {
    private var newEventPopupBinding: NewEventPopupBinding? = null
    override fun setupAlert() {
        newEventPopupBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.new_event_popup, null, false)
        changeToBottom()
        setTheme(R.style.Bottom_Dialog_No_Border)
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
        val datenow = Calendar.getInstance().time
        val calendar = GregorianCalendar()
        calendar.time = datenow
        DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val day = "$dayOfMonth/$month/$year"
            newEventPopupBinding!!.eventDay.text = day
            newEventPopupBinding!!.eventDay.setTypeface(newEventPopupBinding!!.eventDay.typeface, Typeface.BOLD)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun createTimePicker() {
        val datenow = Calendar.getInstance().time
        val calendar = GregorianCalendar()
        calendar.time = datenow
        TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val hour = "$hourOfDay:$minute"
            newEventPopupBinding!!.eventTime.text = hour
            newEventPopupBinding!!.eventTime.setTypeface(newEventPopupBinding!!.eventDay.typeface, Typeface.BOLD)
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
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
        val eventname = newEventPopupBinding!!.eventName.text.toString()
        val eventnote = newEventPopupBinding!!.eventNotes.text.toString()
        val eventday = newEventPopupBinding!!.eventDay.text.toString()
        val eventtime = newEventPopupBinding!!.eventTime.text.toString()
        val eventicon = newEventPopupBinding!!.eventEmoji.text.toString()
        return Events(eventname, eventicon, eventday, eventtime, eventnote)
    }
}