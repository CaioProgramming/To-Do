package com.myself.todo.model

import android.app.Activity
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.myself.todo.Utils.Utilities
import de.mateware.snacky.Snacky

abstract class ModelBase(val activity: Activity) : ModelContract{
    val user = FirebaseAuth.getInstance().currentUser
    var succesmesage = "Salvo com sucesso."
    var errormessage = "Erro ao salvar."
    var confirmmessage = "Tem certeza que deseja remover?"
    var updatemessage = "Atualizado com sucesso!"
    var updateerrormessage = "Erro ao atualizar!"
    override var raiz = FirebaseDatabase.getInstance().reference.child(user!!.uid)

    override fun remover(id: String) {
        if (id.isNotBlank()) {
            reference.child(id).removeValue(removeListener)
        } else {
            MaterialAlertDialogBuilder(activity)
                    .setTitle("Atenção")
                    .setMessage(confirmmessage)
                    .setPositiveButton("Confirmar") { dialog, which ->
                        raiz.removeValue().addOnCompleteListener {
                            dialog.dismiss()
                            if (it.isSuccessful) {
                                taskComplete()
                            } else {
                                taskError()
                            }
                        }
                    }
                    .setNegativeButton("cancelar", null)
                    .show()
        }
    }



    override fun alterar(id: String, obj: Any) {
        reference.child(id).setValue(obj).addOnCompleteListener {
            if (it.isSuccessful) {
                Snacky.builder().setActivity(activity).success().setText(updatemessage + Utilities.randomhappymoji())
            } else {
                Snacky.builder().setActivity(activity).error().setText(updateerrormessage + Utilities.randomhappymoji())

            }
        }
    }

    override fun inserir(obj: Any) {
        reference.push().setValue(obj).addOnCompleteListener { task ->
            if (task.isSuccessful) {
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


