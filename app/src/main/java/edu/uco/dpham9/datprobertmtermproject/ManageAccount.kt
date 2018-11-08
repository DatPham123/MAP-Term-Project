package edu.uco.dpham9.datprobertmtermproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_manage_account.*

class ManageAccount : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var db: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_account)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        id_deleteBtn.setOnClickListener {
            val me = mAuth?.currentUser

            //delete account
            //me?.delete()

            //delete user's exercises
            db?.collection("TraineeExercises")
                ?.document(me?.email.toString())?.delete()?.addOnSuccessListener {
                    Toast.makeText(this, "deleted", Toast.LENGTH_LONG).show()
                }?.addOnFailureListener {
                    Toast.makeText(this, "Fail deleted", Toast.LENGTH_LONG).show()
                }

            //delete users database
            db?.collection("Users")
                ?.document(me?.email.toString())?.delete()

            //val i = Intent(this, MainActivity::class.java)
            //startActivity(i)
            finish()
        }
    }


}
