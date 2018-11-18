package edu.uco.dpham9.datprobertmtermproject.Users

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import edu.uco.dpham9.datprobertmtermproject.MainActivity
import edu.uco.dpham9.datprobertmtermproject.R
import edu.uco.dpham9.datprobertmtermproject.Model.User
import kotlinx.android.synthetic.main.activity_user_sign_up.*
import java.lang.Exception

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
            var isTrainee = id_traineeSignUpRad.isChecked
            var isTrainer = id_trainerSignUpRad.isChecked

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
                    if(it.isSuccessful)
                    {
                        if(isTrainee)
                        {
                            db?.collection("Users")
                                ?.whereEqualTo("trainer", true)
                                ?.get()
                                ?.addOnSuccessListener {

                                    var trainers = ArrayList<User>()

                                    for (docSnapShot in it) {
                                        trainers.add(docSnapShot.toObject(User::class.java))
                                    }

                                    var trainerWithLeastTrainees = trainers[0]
                                    if(trainerWithLeastTrainees?.traineeIds != null)
                                    {
                                        var index = 1
                                        while (index < trainers.size) {
                                            if (trainers[index]?.traineeIds != null)
                                            {
                                                if (trainers[index].traineeIds!!.size <
                                                    trainerWithLeastTrainees.traineeIds!!.size
                                                )
                                                    trainerWithLeastTrainees = trainers[index]
                                            }
                                            else
                                            {
                                                trainerWithLeastTrainees = trainers[index]
                                                break
                                            }
                                            index++
                                        }
                                    }

                                    var userField = User(
                                        email, password, isTrainee, isTrainer,
                                        mAuth?.currentUser!!.uid
                                    )
                                    userField.trainerId = trainerWithLeastTrainees.id

                                db?.collection("Users")?.document("$email")
                                    ?.set(userField)
                                    ?.addOnCompleteListener {
                                        Toast.makeText(
                                            this,
                                            R.string.err_sign_up_successful, Toast.LENGTH_SHORT
                                        ).show()

                                        db?.collection("Users")
                                            ?.whereEqualTo("id", userField.trainerId)
                                            ?.get()
                                            ?.addOnSuccessListener {
                                                try {
                                                    var trainer = it.documents[0].toObject(User::class.java)
                                                    var traineeIds = ArrayList<String>()
                                                    if(trainer?.traineeIds != null)
                                                    {
                                                        traineeIds = ArrayList(trainer.traineeIds)
                                                    }
                                                    traineeIds!!.add(mAuth?.currentUser!!.uid)

                                                    db?.collection("Users")
                                                        ?.document(trainer!!.email)
                                                        ?.update("traineeIds", traineeIds)
                                                }
                                                catch(ex: Exception)
                                                {
                                                    Log.d(TAG, ex.toString())
                                                }
                                                val i = Intent(this, MainActivity::class.java)
                                                startActivity(i)
                                                finish()
                                            }

                                    }
                                    ?.addOnFailureListener {
                                        Toast.makeText(
                                            this, getString(
                                                R.string.err_sign_up_failed,
                                                it
                                            ), Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        }
                        else
                        {
                            var userField = User(email, password, isTrainee, isTrainer,
                                mAuth?.currentUser!!.uid)
                            db?.collection("Users")?.document("$email")
                                ?.set(userField)
                                ?.addOnCompleteListener {
                                    Toast.makeText(this,
                                        R.string.err_sign_up_successful, Toast.LENGTH_SHORT).show()
                                    val i = Intent(this, MainActivity::class.java)
                                    startActivity(i)
                                    finish()
                                }
                                ?.addOnFailureListener {
                                    Toast.makeText(this, getString(R.string.err_sign_up_failed, it),
                                        Toast.LENGTH_SHORT).show()
                                }
                        }

                    }
                    else{
                        val m = it.exception
                        Toast.makeText(this, getString(R.string.err_sign_up_failed, m),
                            Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
