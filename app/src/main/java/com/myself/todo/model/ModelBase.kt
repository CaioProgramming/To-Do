package com.myself.todo.model

import android.app.Activity
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.myself.todo.presenter.EventsPresenter
import de.mateware.snacky.Snacky

abstract class ModelBase(val activity: Activity) : ModelContract{
    var path = ""
    val raiz =  FirebaseDatabase.getInstance().reference.child(path)
    val user = FirebaseAuth.getInstance().currentUser
    var succesmesage = "Salvo com sucesso."
    var errormessage = "Erro ao salvar."

    override fun remover(id: String) {
        raiz.child(id).removeValue(removeListener)
    }



    override fun alterar(id: String, obj: Any) {
        raiz.child(id).setValue(obj).addOnCompleteListener {
            Toast.makeText(activity,"Evento atualizado com sucesso",Toast.LENGTH_LONG).show()
        }
    }

    override fun inserir(obj: Any) {
        raiz.push().setValue(obj).addOnCompleteListener { task -> if (task.isSuccessful){
            taskComplete()
        }else{
            taskError()
        }
        }
    }


    fun taskError(){
        Snacky.builder().setActivity(activity).success().setText(errormessage).show()

    }
    fun taskComplete(){
        Snacky.builder().setActivity(activity).success().setText(succesmesage).show()
    }

    val removeListener = DatabaseReference.CompletionListener { error, _ ->
        if (error == null){
            Toast.makeText(activity,"Removido com sucesso",Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(activity,"Erro ao remover",Toast.LENGTH_LONG).show()

        }
    }
}


