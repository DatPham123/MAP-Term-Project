package edu.uco.dpham9.datprobertmtermproject

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import edu.uco.dpham9.datprobertmtermproject.Model.User
import kotlinx.android.synthetic.main.activity_manage_account.*


class ManageAccount : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var db: FirebaseFirestore? = null
    private var storage: FirebaseStorage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_account)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        id_deleteBtn.setOnClickListener {
            val alertBuilder = AlertDialog.Builder(this@ManageAccount)

            alertBuilder.setTitle(R.string.label_delete_title)
            alertBuilder.setMessage(R.string.err_delete_warning)

            alertBuilder.setPositiveButton(R.string.err_yes){_, _ ->
            val me = mAuth?.currentUser
            val email = me?.email!!
            val path = "Videos/" + mAuth?.currentUser?.email

            db?.collection("Users")
                ?.whereEqualTo("id", me.uid)
                ?.get()
                ?.addOnSuccessListener {

                    val trainee = it.documents[0].toObject(User::class.java)
                    Log.d(edu.uco.dpham9.datprobertmtermproject.Users.TAG, trainee.toString())
                    db?.collection("Users")?.whereEqualTo("id", trainee!!.trainerId)
                        ?.get()
                        ?.addOnSuccessListener {

                            val trainer = it.documents[0].toObject(User::class.java)
                            val traineeIds = trainer?.traineeIds
                            traineeIds?.remove(mAuth?.currentUser?.uid)
                            db?.collection("Users")?.document(trainer!!.email)
                                ?.update("traineeIds", trainer.traineeIds)

                            //delete users database
                            db?.collection("Users")
                                ?.document(email)?.delete()

                            //delete account from auth
                            me.delete()
                        }
                }

            //delete user's exercises database and video
            db?.collection("TraineeExercises/$email/MyExercises")?.get()
                ?.addOnSuccessListener {
                    it.forEach {

                        try
                        {
                            //delete comment database
                            db?.collection("Comments")?.whereEqualTo("exerciseId", it["exerciseId"])
                                ?.get()?.addOnSuccessListener {
                                    it.forEach {
                                        it.reference.delete()
                                    }
                                }

                            if (!it["videoUrl"].toString().isNullOrEmpty()) {
                                storage!!.reference.child(it["videoUrl"].toString()).delete().addOnSuccessListener {
                                    //Toast.makeText(this, "deleted video", Toast.LENGTH_LONG).show()
                                }.addOnFailureListener {
                                    //Toast.makeText(this, "fail delete video\n$it", Toast.LENGTH_LONG).show()
                                }
                            }
                            it.reference.delete()
                        }
                        catch(ex: Exception)
                        {
                            Log.d(edu.uco.dpham9.datprobertmtermproject.Users.TAG, ex.toString())
                        }
                    }
                }
            db?.collection("TraineeExercises")
                ?.document(email)?.delete()


            //delete video
            storage!!.reference.child(path).delete().addOnSuccessListener {
                //Toast.makeText(this, "deleted video", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                //Toast.makeText(this, "fail delete video\n$it", Toast.LENGTH_LONG).show()
            }

            //go back to login page
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
            }

            alertBuilder.setNeutralButton(R.string.err_cancel){_,_->
            }

            val alert = alertBuilder.create()
            alert.show()
        }

    }

}
