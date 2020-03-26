package com.myself.todo.Utils

import com.myself.todo.R
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utilities {
    //Convert Date to Calendar
    private fun dateToCalendar(date: Date?): Calendar? {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar
    }





    companion object {
        val RC_SIGN_IN = 1

        val pagerimages = arrayOf(R.drawable.il_idea,R.drawable.il_tasks)
        val pagercolors = arrayOf(R.color.colorPrimary,R.color.colorPrimaryDark)
        val pagers = arrayOf(Pagersettings(R.drawable.il_idea,R.color.colorPrimary,"Escreva o nome do seu evento",false),
                Pagersettings(R.drawable.il_tasks,R.color.colorPrimaryDark,"Adicione tarefas ao seu evento",true),
                Pagersettings(null,R.color.colorAccent,"Esta é seu evento... Clique em salvar finalizar.",false))
        fun convertDate(dia: String?): String? { //2. Test - Convert Date to Calendar
//3. Test - Convert Calendar to Date
            var dia = dia
            val df: DateFormat = SimpleDateFormat("yyy-MM-dd")
            try {
                val result = df.parse(dia)
                val format = SimpleDateFormat("dd MMM yyyy EE")
                dia = format.format(result)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            println(dia)
            return dia
        }

    }

    class Pagersettings(val image: Int?, val color:Int, val title:String,val allowchipgroup:Boolean)
}