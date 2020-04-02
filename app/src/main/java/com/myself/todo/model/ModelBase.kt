package com.myself.todo.model

import android.app.Activity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.myself.todo.Utils.Utilities
import com.myself.todo.view.alerts.AlertContract
import com.myself.todo.view.alerts.MessageAlert
import de.mateware.snacky.Snacky

abstract class ModelBase(val activity: Activity) : ModelContract{
    var path = ""
    val raiz =  FirebaseDatabase.getInstance().reference.child(path)
    val user = FirebaseAuth.getInstance().currentUser
    var succesmesage = "Salvo com sucesso."
    var errormessage = "Erro ao salvar."
    var confirmmessage = "Tem certeza que deseja remover?"
    var updatemessage = "Atualizado com sucesso!"
    var updateerrormessage = "Erro ao atualizar!"
    val deleteAllListener: ValueEventListener = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {
            errormessage = "Erro ao encontrar dados"
            taskError()
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            for (d in dataSnapshot.children) {
                d.ref.removeValue()
            }
        }
    }


    override fun remover(id: String) {
        if (id.isNotBlank()) {
            raiz.child(id).removeValue(removeListener)
        } else {
            val messageAlert = MessageAlert(activity, confirmmessage)
            messageAlert.actionsListener = object : AlertContract.ActionsListener {
                override fun primaryAction() {
                    raiz.orderByChild("userID").equalTo(user!!.uid).addValueEventListener(deleteAllListener)
                }

                override fun secondaryAction() {
                    messageAlert.dimiss()
                }
            }
        }
    }



    override fun alterar(id: String, obj: Any) {
        raiz.child(id).setValue(obj).addOnCompleteListener {
            if (it.isSuccessful) {
                Snacky.builder().setActivity(activity).success().setText(updatemessage + Utilities.randomhappymoji())
            } else {
                Snacky.builder().setActivity(activity).error().setText(updateerrormessage + Utilities.randomhappymoji())

            }
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
        Snacky.builder().setActivity(activity).error().setText(errormessage + Utilities.randomsadmoji()).show()

    }
    fun taskComplete(){
        Snacky.builder().setActivity(activity).success().setText(succesmesage + Utilities.randomhappymoji()).show()
    }

    val removeListener = DatabaseReference.CompletionListener { error, _ ->
        if (error == null){
            Toast.makeText(activity,"Removido com sucesso",Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(activity,"Erro ao remover",Toast.LENGTH_LONG).show()

        }
    }
}


