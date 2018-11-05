package edu.uco.dpham9.datprobertmtermproject.Users

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.uco.dpham9.datprobertmtermproject.MainActivity
import edu.uco.dpham9.datprobertmtermproject.R
import edu.uco.dpham9.datprobertmtermproject.Model.UserAuth
import kotlinx.android.synthetic.main.activity_user_sign_up.*

class UserSignUp : AppCompatActivity()
{

    var mAuth: FirebaseAuth? = null
    var db: FirebaseFirestore? = null
    private val pattern = """.+@.+\..+""".toRegex()

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

            if(email.isNullOrEmpty() || email.isNullOrBlank())
            {
                Toast.makeText(this, R.string.err_empty_email, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if(!pattern.matches(email))
            {
                Toast.makeText(this, R.string.err_invalid_email, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(password.length < 6){
                Toast.makeText(this, R.string.err_minimum_6_character, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mAuth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) {
                    if(it.isSuccessful){
                        var userField = UserAuth(email, password, trainee, trainer)
                        db?.collection("Users")?.document("$email")?.set(userField)
                            ?.addOnCompleteListener {
                                Toast.makeText(this,
                                    R.string.err_sign_up_successful, Toast.LENGTH_SHORT).show()
                                val i = Intent(this, MainActivity::class.java)
                                startActivity(i)
                                finish()
                            }
                            ?.addOnFailureListener {
                                Toast.makeText(this, getString(R.string.err_sign_up_failed, it), Toast.LENGTH_SHORT).show()
                            }
                    }
                    else{
                        val m = it.exception
                        Toast.makeText(this, getString(R.string.err_sign_up_failed, m), Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
