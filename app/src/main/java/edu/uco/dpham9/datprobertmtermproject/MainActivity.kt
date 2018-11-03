package edu.uco.dpham9.datprobertmtermproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        //sign in button
        id_signinBtn.setOnClickListener {

            //val email = id_email.text.toString().trim()
            //val password = id_password.text.toString().trim()
            val email = "dpham9@uco.edu"
            val password = "password"

            //trainee is selected
                mAuth?.signInWithEmailAndPassword(email, password)
                    ?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, R.string.err_login, Toast.LENGTH_SHORT).show()
                            val traineeIntent = Intent(this, TraineeHomeActivity::class.java)
                            startActivity(traineeIntent)
                        } else {

                            Toast.makeText(this, R.string.err_loginFail, Toast.LENGTH_SHORT).show()
                        }
                    }
        }

        //sign up button moved to new activity
        id_signupBtn.setOnClickListener {
            val createUserPage = Intent(this, UserSignUp::class.java)
            startActivity(createUserPage)
        }

        //next function here
    }
}
