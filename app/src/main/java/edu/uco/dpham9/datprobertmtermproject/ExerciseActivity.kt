package edu.uco.dpham9.datprobertmtermproject

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import edu.uco.dpham9.datprobertmtermproject.Model.Comment
import edu.uco.dpham9.datprobertmtermproject.Model.TraineeExercise
import edu.uco.dpham9.datprobertmtermproject.Users.EXTRA_EXERCISE
import kotlinx.android.synthetic.main.activity_exercise.*
import java.lang.Exception
import java.sql.Timestamp

private var storage: FirebaseStorage? = null
private var mAuth: FirebaseAuth? = null
private var db: FirebaseFirestore? = null
private var idea = ArrayList<Comment>()

class ExerciseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        var myExercise = intent.getParcelableExtra<TraineeExercise>(EXTRA_EXERCISE)

        val blockComment: ArrayAdapter<Comment> =
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
        db?.collection("Comments/${mAuth?.currentUser?.email.toString()}/MyComments")
            ?.whereEqualTo("exerciseID", myExercise.exerciseId)?.get()
            ?.addOnSuccessListener {

                for(docSnapShot in it)
                {
                    idea.add(docSnapShot.toObject(Comment::class.java))
                }

                blockComment.notifyDataSetChanged()
            }


        //rating
        id_ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            db?.collection("TraineeExercises/${mAuth?.currentUser?.email.toString()}/MyExercises")
                ?.document(myExercise.name)
                ?.update("rating",id_ratingBar.rating)?.addOnSuccessListener {
                    //Toast.makeText(this, R.string.err_rated, Toast.LENGTH_LONG).show()
                }?.addOnFailureListener {
                    //Toast.makeText(this, "$it", Toast.LENGTH_LONG).show()
                }
        }
        //comment and rating
        id_addCmtBtn.setOnClickListener {
            //if(myExercise.rating.isNullOrEmpty()) return@setOnClickListener


            //comment
            var commented = id_commentBlock.text.toString()
            if(commented.isNullOrEmpty() || commented.isNullOrBlank()){
                Toast.makeText(this, "Please Enter a comment", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            var commentField = Comment(commented, mAuth?.currentUser?.uid.toString(), myExercise.exerciseId)
                    db?.collection("Comments/${mAuth?.currentUser?.email.toString()}/MyComments")
                        ?.document(Timestamp(System.currentTimeMillis()).toString())?.set(commentField)
                        ?.addOnCompleteListener {
                    Toast.makeText(this, R.string.err_commentAdded, Toast.LENGTH_LONG).show()
                            val data = Intent()
                            setResult(Activity.RESULT_OK, data)
                }?.addOnFailureListener{
                    Toast.makeText(this, R.string.err_commentFailed, Toast.LENGTH_LONG).show()
                }

            idea.add(Comment(commented, mAuth?.currentUser?.uid.toString(),myExercise.exerciseId))
            val blockComment: ArrayAdapter<Comment> =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, idea)
            id_commentList.adapter = blockComment
            blockComment.notifyDataSetChanged()
        }
    }

    override fun onBackPressed() {
        val i = Intent()
        setResult(Activity.RESULT_OK, i)

        finish()
    }
}
