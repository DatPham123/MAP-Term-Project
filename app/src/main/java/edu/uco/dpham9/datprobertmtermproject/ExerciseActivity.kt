package edu.uco.dpham9.datprobertmtermproject

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import edu.uco.dpham9.datprobertmtermproject.Model.Comment
import edu.uco.dpham9.datprobertmtermproject.Model.TraineeExercise
import edu.uco.dpham9.datprobertmtermproject.Model.User
import edu.uco.dpham9.datprobertmtermproject.Users.EXTRA_EXERCISE
import edu.uco.dpham9.datprobertmtermproject.Users.EXTRA_TRAINEE_EMAIL
import edu.uco.dpham9.datprobertmtermproject.Users.EXTRA_TRAINER_LOGGED_IN
import kotlinx.android.synthetic.main.activity_exercise.*
import java.lang.Exception
import java.sql.Timestamp

private var storage: FirebaseStorage? = null
private var mAuth: FirebaseAuth? = null
private var db: FirebaseFirestore? = null
private var idea = ArrayList<String>()

class ExerciseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val myExercise = intent.getParcelableExtra<TraineeExercise>(EXTRA_EXERCISE)

        val isTrainer = intent.getBooleanExtra(EXTRA_TRAINER_LOGGED_IN, false)

        val blockComment: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, idea)
        id_commentList.adapter = blockComment

        try {
            val path = intent.getParcelableExtra<TraineeExercise>(EXTRA_EXERCISE).videoUrl

            storage = FirebaseStorage.getInstance()
            storage!!.reference.child(path).downloadUrl.addOnSuccessListener {
                id_videoView.setVideoURI(Uri.parse(it.toString()))
            }.addOnFailureListener {
                id_noVideo.text = getString(R.string.label_noVideo)
                Toast.makeText(this, "$it", Toast.LENGTH_LONG).show()
            }

            id_playBtn.setOnClickListener {
                id_videoView.start()
            }

        }
        catch (exception: Exception){
            id_noVideo.text = getString(R.string.label_noVideo)
        }

        //update rating
        db?.collection("TraineeExercises/${mAuth?.currentUser?.email.toString()}/MyExercises")
                ?.document(myExercise.rating.toString())?.get()?.addOnSuccessListener {
                    id_ratingBar.rating = myExercise.rating
                }

        //updating comment
        db?.collection("Comments")
            ?.whereEqualTo("exerciseId", myExercise.exerciseId)?.get()
            ?.addOnSuccessListener {
                idea.clear()
                for(docSnapShot in it)
                {
                    val comment = docSnapShot.toObject(Comment::class.java)

                    var poster =
                    if(isTrainer)
                    {
                        if(comment.posterId == mAuth?.currentUser?.uid)
                            "trainer: "
                        else
                            "trainee: "
                    }
                    else
                    {
                        if(comment.posterId == mAuth?.currentUser?.uid)
                            "trainee: "
                        else
                            "trainer: "
                    }
                    idea.add(poster + comment)
                }
                blockComment.notifyDataSetChanged()
            }


        //disable rating for trainee
        if (!isTrainer) {
            id_ratingBar.isEnabled = false
        }
        else{
            //rating, CAN'T DO THIS BECAUSE NOT CURRENT USER BUT NEED TRAINER USER
            id_ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                db?.collection("TraineeExercises/" +
                        "${intent.getStringExtra(EXTRA_TRAINEE_EMAIL)}/MyExercises")
                    ?.document(myExercise.name)
                    ?.update("rating",id_ratingBar.rating)?.addOnSuccessListener {
                        Toast.makeText(this, R.string.err_rated, Toast.LENGTH_LONG).show()
                    }?.addOnFailureListener {
                        //Toast.makeText(this, "$it", Toast.LENGTH_LONG).show()
                    }
            }
        }

        //comment
        id_addCmtBtn.setOnClickListener {
            //if(myExercise.rating.isNullOrEmpty()) return@setOnClickListener
            //comment
            var commented = id_commentBlock.text.toString()
            if(commented.isNullOrEmpty() || commented.isNullOrBlank()){
                Toast.makeText(this, R.string.err_enter_your_comment, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            var commentField = Comment(commented, mAuth?.currentUser?.uid.toString(), myExercise.exerciseId)
                    db?.collection("Comments")
                        ?.document(Timestamp(System.currentTimeMillis()).toString())?.set(commentField)
                        ?.addOnCompleteListener {
                    Toast.makeText(this, R.string.err_commentAdded, Toast.LENGTH_LONG).show()
                            val data = Intent()
                            setResult(Activity.RESULT_OK, data)
                }?.addOnFailureListener{
                    Toast.makeText(this, R.string.err_commentFailed, Toast.LENGTH_LONG).show()
                }

            var poster =
            if(isTrainer)
            {
                if(commentField.posterId == mAuth?.currentUser?.uid)
                    "trainer: "
                else
                    "trainee: "
            }
            else
            {
                if(commentField.posterId == mAuth?.currentUser?.uid)
                    "trainee: "
                else
                    "trainer: "
            }
            idea.add(poster + commented)

            id_commentBlock.setText("")
            blockComment.notifyDataSetChanged()
        }
    }

    override fun onBackPressed() {
        val i = Intent()
        setResult(Activity.RESULT_OK, i)

        finish()
    }
}
