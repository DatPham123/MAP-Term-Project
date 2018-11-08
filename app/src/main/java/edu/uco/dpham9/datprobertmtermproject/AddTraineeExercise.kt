package edu.uco.dpham9.datprobertmtermproject

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

import edu.uco.dpham9.datprobertmtermproject.Model.TraineeExercise
import edu.uco.dpham9.datprobertmtermproject.Users.REQ_CODE_VIDEO
import kotlinx.android.synthetic.main.activity_add_trainee_exercise.*


class AddTraineeExercise : AppCompatActivity()
{
    private var videoUri: Uri? = null
    private var mAuth: FirebaseAuth? = null
    private var db: FirebaseFirestore? = null
    private var storage: FirebaseStorage? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_trainee_exercise)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        id_trainee_find_vidBtn.setOnClickListener {
            val openVid = Intent(Intent.ACTION_PICK)

            val externalStorageDirectory = Environment.
                getExternalStorageDirectory()

            val pathName = externalStorageDirectory.path

            val data = Uri.parse(pathName)

            openVid.setDataAndType(data, "video/*")
            startActivityForResult(openVid, REQ_CODE_VIDEO)
        }

        id_trainee_add_exBtn.setOnClickListener {
            //add exercise to database/recyclerview
            val name = id_trainee_ex_title.text.toString().trim()
            val desc = id_trainee_ex_desc.text.toString().trim()
            var url = ""

            if(name.isNullOrBlank() || name.isNullOrEmpty())
            {
                Toast.makeText(this, R.string.err_exNameEmpty, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //add video to storage
            if(videoUri != null)
            {
                url = videoUri.toString()
                //add video to storage
                val path = "Videos/" + mAuth?.currentUser?.email
                val video = storage?.getReference(path)

                video?.putFile(videoUri!!)?.addOnSuccessListener {
                    Toast.makeText(this, R.string.err_success, Toast.LENGTH_LONG).show()
                }?.addOnFailureListener {
                    Toast.makeText(this, "$it", Toast.LENGTH_LONG).show()
                }
            }

            //add trainee exercise to database
            db?.collection("TraineeExercises/${mAuth?.currentUser?.email.toString()}/MyExercises")
                ?.document(name)
                ?.set(TraineeExercise(name, desc, url, mAuth?.currentUser?.uid.toString()))
                ?.addOnSuccessListener {
                    Toast.makeText(this, R.string.err_newExAdded, Toast.LENGTH_SHORT).show()
                    finish()
                }
                ?.addOnFailureListener { ex: Exception ->
                    Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?)
    {
        if (requestCode == REQ_CODE_VIDEO && resultCode == RESULT_OK)
        {
            videoUri = intent?.data
            //Toast.makeText(this, intent?.data?.encodedPath, Toast.LENGTH_LONG).show()
            //var fileName = videoUri.toString().substring(videoUri.toString()
                //.lastIndexOf("/")+1)
            id_videoUrl.text = "${getString(R.string.label_filename)} $videoUri"
        }
    }
}
