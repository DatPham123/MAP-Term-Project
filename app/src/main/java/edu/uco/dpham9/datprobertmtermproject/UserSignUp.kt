package edu.uco.dpham9.datprobertmtermproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_user_sign_up.*

class UserSignUp : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var db: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_sign_up)


        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        //sign up button to create new Auth
        id_signupUserBtn.setOnClickListener {
            val email = id_emailSignup.text.toString().trim()
            val password = id_passwordSignUp.text.toString().trim()
            var trainee = id_traineeSignUpRad.isChecked
            var trainer = id_trainerSignUpRad.isChecked

            if(password.length < 6){
                Toast.makeText(this, "Minimum of 6 character for password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mAuth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) {
                    if(it.isSuccessful){
                        var userField = UserAuth(email, password,trainee, trainer)
                        db?.collection("Users")?.document()?.set(userField)
                            ?.addOnCompleteListener {
                                Toast.makeText(this, "sign up Success", Toast.LENGTH_SHORT).show()
                                val i = Intent(this, MainActivity::class.java)
                                startActivity(i)
                                finish()
                            }
                            ?.addOnFailureListener {
                                Toast.makeText(this, "sign up Failed\n$it", Toast.LENGTH_SHORT).show()
                            }
                    }
                    else{
                        val m = it.exception
                        Toast.makeText(this, "sign up Failed\n$m", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
