package com.myself.todo.Utils

import android.annotation.SuppressLint
import android.view.View
import androidx.core.view.ViewCompat
import com.myself.todo.R
import com.myself.todo.model.beans.OnBoard
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject
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

        fun fadeOut(view: View): Completable {
            val animationSubject = CompletableSubject.create()
            return animationSubject.doOnSubscribe {
                ViewCompat.animate(view)
                        .setDuration(800)
                        .alpha(0f)
                        .withEndAction {
                            animationSubject.onComplete()
                        }
            }


        }

        fun fadeIn(view: View): Completable {
            val animationSubject = CompletableSubject.create()
            return animationSubject.doOnSubscribe {
                ViewCompat.animate(view)
                        .setDuration(800)
                        .alpha(1f)
                        .withEndAction {
                            animationSubject.onComplete()
                        }
            }


        }

        const val RC_SIGN_IN = 1

        val backgrounds = arrayOf(R.drawable.ic_wave,
                R.drawable.ic_fluid,
                R.drawable.ic_valle)

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

        val sadmojis = arrayOf("😓", "😔", "😕", "😥", "🤒", "🤕", "😭", "😦", "😧")
        val happymojis = arrayOf("😀", "😁", "😂", "🤣", "😃", "😄", "😅", "😆", "😉", "😊", "😋", "😎", "😍", "😘", "🥰", "😗", "😙", "😚", "☺", "\uD83D\uDE42", "\uD83E\uDD17", "\uD83E\uDD29", "🥳")


        fun randomhappymoji(): String {
            val random = Random()


            return happymojis[random.nextInt(happymojis.size)]
        }

        fun randomsadmoji(): String {
            val random = Random()


            return sadmojis[random.nextInt(sadmojis.size)]
        }

        fun randombackground(): Int {
            val random = Random()


            return backgrounds[random.nextInt(backgrounds.size)]
        }

       val imagegif = "https://media.giphy.com/media/kaCR7oCmtOn7KpOQdQ/giphy.gif"



    }

}