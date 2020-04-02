package com.myself.todo.Utils

import android.annotation.SuppressLint
import com.myself.todo.R
import com.myself.todo.model.beans.OnBoard
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
        const val RC_SIGN_IN = 1

        val pagers = arrayOf(Pagersettings(R.drawable.page1, R.color.colorPrimary, "Hora de criar o seu evento!", "Escreva o nome do seu evento...", false),
                Pagersettings(R.drawable.page2, R.color.colorPrimaryDark, "Adicione algumas tarefas ao seu evento", "Adicione tarefas ao seu evento", true),
                Pagersettings(R.drawable.page3, R.color.colorAccent, "Esta é seu evento... Clique em salvar finalizar.", "", false))

        val onBoardScreens = arrayOf(OnBoard("Bem-Vindo ao You", "O seu espaço, para guardar tudo que é mais importante!", R.drawable.ic_box),
                OnBoard("Seus momentos!", "Guarde suas fotos mais importantes aqui, chega de se perder na galeria, aqui é apenas o que você não quer esquecer!", R.drawable.ic_camera_shadowed),
                OnBoard("Suas tarefas", "o You é o seu espaço para se lembrar de tudo que quer fazer(não precisa nem fazer é só para que você possa se lembrar de seus objetivos \uD83E\uDD2A)", R.drawable.ic_basketball),
                OnBoard("Está pronto", "Isso é tudo que você precisa saber sobre o You, aproveite o quanto quiser sem se preocupar!", R.drawable.ic_startup)
        )

        fun convertDate(dia: String?): String? {
            var dia = dia
            val df: DateFormat = SimpleDateFormat("yyy-MM-dd")
            try {
                val result = df.parse(dia)
                val format = SimpleDateFormat("dd/MM/yyy")
                dia = format.format(result)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            println(dia)
            return dia
        }

        fun actualday(): String {
            val datenow = Calendar.getInstance().time
            @SuppressLint("SimpleDateFormat") val df = SimpleDateFormat("dd/MM/yyyy")
            val dia = df.format(datenow)
            println(dia)
            return dia
        }

        val sadmojis = arrayOf("\uD83D\uDE25", "\uD83D\uDE14", "\uD83D\uDE1F", "\uD83D\uDE13", "\uD83D\uDE22", "\uD83D\uDE3F", "\uD83D\uDE2D", "\uD83D\uDE1E", "\uD83E\uDD7A")
        val happymojis = arrayOf("\uD83E\uDD17", "\uD83D\uDE03", "☺️", "\uD83D\uDE42", "\uD83D\uDE0A", "\uD83D\uDE09", "\uD83D\uDE17", "\uD83D\uDE1E")


        fun randomhappymoji(): String {
            var random = Random()


            return happymojis[random.nextInt(happymojis.size)]
        }

        fun randomsadmoji(): String {
            var random = Random()


            return sadmojis[random.nextInt(sadmojis.size)]
        }



    }

    class Pagersettings(val image: Int?, val color: Int, val title: String, val hint: String, val allowchipgroup: Boolean)
}