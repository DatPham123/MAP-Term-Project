package edu.uco.dpham9.datprobertmtermproject

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_exercise.*

private var storage: FirebaseStorage? = null
private var mAuth: FirebaseAuth? = null

class ExerciseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        mAuth = FirebaseAuth.getInstance()

        val path = "Videos/" + mAuth?.currentUser?.email
        storage = FirebaseStorage.getInstance()
        val r = storage!!.reference.child(path).downloadUrl.addOnSuccessListener {
            id_videoView.setVideoURI(Uri.parse(it.toString()))
        }.addOnFailureListener {
            Toast.makeText(this, "$it", Toast.LENGTH_LONG).show()
        }

        id_playBtn.setOnClickListener {
            id_videoView.start()
        }
    }
}
