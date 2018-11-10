package edu.uco.dpham9.datprobertmtermproject

import android.app.AlertDialog
import android.content.Intent
import android.hardware.camera2.CaptureRequest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import edu.uco.dpham9.datprobertmtermproject.Model.TraineeExercise
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

            alertBuilder.setTitle("Delete Account")
            alertBuilder.setMessage("Are you sure you want to delete this account PERMANENTLY?")

            alertBuilder.setPositiveButton("YES"){dialog, which ->
            val me = mAuth?.currentUser
            val path = "Videos/" + mAuth?.currentUser?.email

            //delete account from auth
            me?.delete()

            //delete user's exercises database
            db?.collection("TraineeExercises/${me?.email.toString()}/MyExercises")?.get()
                ?.addOnSuccessListener {
                    it.forEach {
                        storage!!.reference.child(it["videoUrl"].toString()).delete().addOnSuccessListener {
                            //Toast.makeText(this, "deleted video", Toast.LENGTH_LONG).show()
                        }.addOnFailureListener {
                            //Toast.makeText(this, "fail delete video\n$it", Toast.LENGTH_LONG).show()
                        }
                        it.reference.delete() }
                }
            db?.collection("TraineeExercises")
                ?.document(me?.email.toString())?.delete()

            //delete users database
            db?.collection("Users")
                ?.document(me?.email.toString())?.delete()

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

            alertBuilder.setNeutralButton("CANCEL"){_,_->
            }

            val alert = alertBuilder.create()
            alert.show()
        }

    }

}
