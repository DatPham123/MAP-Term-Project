package edu.uco.dpham9.datprobertmtermproject

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.uco.dpham9.datprobertmtermproject.Users.UserHomeActivity
import edu.uco.dpham9.datprobertmtermproject.Users.UserSignUp
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{

    private var mAuth: FirebaseAuth? = null
    private var db: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        progressBar.visibility = View.GONE

        //sign in button
        id_signinBtn.setOnClickListener {
            val email = id_email.text.toString().trim()
            val password = id_password.text.toString().trim()

            if(email.isNullOrEmpty() || email.isNullOrBlank())
            {
                Toast.makeText(this, R.string.err_empty_email, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(password.isNullOrEmpty() || password.isNullOrBlank())
            {
                Toast.makeText(this, R.string.err_empty_password, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE
            //sign in
                mAuth?.signInWithEmailAndPassword(email, password)
                    ?.addOnCompleteListener {
                        if (it.isSuccessful)
                        {
                            Toast.makeText(this, R.string.err_signIn, Toast.LENGTH_SHORT).show()
                            val i = Intent(this, UserHomeActivity::class.java)
                            startActivity(i)
                            finish()
                        }
                        else
                        {
                            Toast.makeText(this, R.string.err_signInFail, Toast.LENGTH_SHORT).show()
                            progressBar.visibility = View.GONE
                        }
                    }
        }

        //sign up button moved to new activity
        id_signupBtn.setOnClickListener {
            val createUserPage = Intent(this, UserSignUp::class.java)
            startActivity(createUserPage)
        }
    }
}
