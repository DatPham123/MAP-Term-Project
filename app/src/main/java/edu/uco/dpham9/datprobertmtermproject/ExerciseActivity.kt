package edu.uco.dpham9.datprobertmtermproject

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import edu.uco.dpham9.datprobertmtermproject.Model.TraineeExercise
import edu.uco.dpham9.datprobertmtermproject.Users.EXTRA_EXERCISE
import kotlinx.android.synthetic.main.activity_exercise.*
import java.lang.Exception

private var storage: FirebaseStorage? = null
private var mAuth: FirebaseAuth? = null

class ExerciseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        mAuth = FirebaseAuth.getInstance()

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
    }
}
