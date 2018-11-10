package edu.uco.dpham9.datprobertmtermproject

import android.app.Activity
import android.app.AlertDialog
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
import edu.uco.dpham9.datprobertmtermproject.Users.EXTRA_EXERCISE
import edu.uco.dpham9.datprobertmtermproject.Users.REQ_CODE_VIDEO
import kotlinx.android.synthetic.main.activity_edit_trainee_exercise.*
import java.sql.Timestamp

class EditTraineeExercise : AppCompatActivity()
{
    private var videoUri: Uri? = null
    private var mAuth: FirebaseAuth? = null
    private var db: FirebaseFirestore? = null
    private var storage: FirebaseStorage? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_trainee_exercise)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        //get trainee exercise to edit
        val myExercise = intent.getParcelableExtra<TraineeExercise>(EXTRA_EXERCISE)

        //update fields
        id_trainee_ex_title.setText(myExercise.name)
        id_trainee_ex_desc.setText(myExercise.description)
        id_videoUrl.text = myExercise.videoUrl

        id_trainee_find_vidBtn.setOnClickListener {
            val openVid = Intent(Intent.ACTION_PICK)

            val externalStorageDirectory = Environment.
                getExternalStorageDirectory()

            val pathName = externalStorageDirectory.path

            val data = Uri.parse(pathName)

            openVid.setDataAndType(data, "video/*")
            startActivityForResult(openVid, REQ_CODE_VIDEO)
        }

        id_trainee_update_exBtn.setOnClickListener {

            val name = id_trainee_ex_title.text.toString().trim()
            val desc = id_trainee_ex_desc.text.toString().trim()
            var path = id_videoUrl.text.toString().trim()

            if(name.isNullOrBlank() || name.isNullOrEmpty())
            {
                Toast.makeText(this, R.string.err_exNameEmpty, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(videoUri != null)
            {
                if(!myExercise.videoUrl.isNullOrEmpty() || !myExercise.videoUrl.isNullOrBlank()){
                    storage!!.reference.child(myExercise.videoUrl).delete().addOnSuccessListener {
                        //Toast.makeText(this, "deleted video", Toast.LENGTH_LONG).show()
                    }.addOnFailureListener {
                        //Toast.makeText(this, "fail delete video\n$it", Toast.LENGTH_LONG).show()
                    }
                }
                //add video to storage
                //url = videoUri.toString()
                 path = "Videos/" + "${mAuth?.currentUser?.email}/" + Timestamp(System.currentTimeMillis())
                val video = storage?.getReference(path)

                video?.putFile(videoUri!!)?.addOnSuccessListener {
                    Toast.makeText(this, R.string.err_success, Toast.LENGTH_LONG).show()
                }?.addOnFailureListener {
                    Toast.makeText(this, "$it", Toast.LENGTH_LONG).show()
                }
            }


            db?.collection("TraineeExercises/${mAuth?.currentUser?.email}/MyExercises")
                ?.document(myExercise.name)?.delete()
                ?.addOnSuccessListener {
                    db?.collection("TraineeExercises/${mAuth?.currentUser?.email}/MyExercises")
                        ?.document(name)
                        ?.set(TraineeExercise(name, desc, path, mAuth?.currentUser?.uid.toString()))
                        ?.addOnSuccessListener {
                            Toast.makeText(this, R.string.err_exUpdated, Toast.LENGTH_SHORT).show()

                            val i = Intent()
                            setResult(Activity.RESULT_OK, i)

                            finish()
                        }
                        ?.addOnFailureListener { ex: Exception ->
                            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show()
                        }
                }
                ?.addOnFailureListener { ex: Exception ->
                    Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show()
                }


        }

        id_delete_ex.setOnClickListener {
            val alertBuilder = AlertDialog.Builder(this@EditTraineeExercise)

            alertBuilder.setTitle("Delete Exercise")
            alertBuilder.setMessage("Are you sure you want to delete this exercise?")

            alertBuilder.setPositiveButton("YES"){dialog, which ->
                db?.collection("TraineeExercises/${mAuth?.currentUser?.email}/MyExercises")
                    ?.document(myExercise.name)?.delete()
                    ?.addOnSuccessListener {
                        Toast.makeText(this, R.string.err_deleteExSuccess, Toast.LENGTH_SHORT).show()

                        val i = Intent()
                        setResult(Activity.RESULT_OK, i)

                        finish()
                    }
                    ?.addOnFailureListener{ ex: Exception ->
                        Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show()
                    }

                    //**DELETE VIDEO FROM STORAGE?**//
                //delete video
                if(!myExercise.videoUrl.isNullOrEmpty() || !myExercise.videoUrl.isNullOrBlank()){
                    storage!!.reference.child(myExercise.videoUrl).delete().addOnSuccessListener {
                        //Toast.makeText(this, "deleted video", Toast.LENGTH_LONG).show()
                    }.addOnFailureListener {
                        //Toast.makeText(this, "fail delete video\n$it", Toast.LENGTH_LONG).show()
                    }
                }

            }

            alertBuilder.setNeutralButton("CANCEL"){_,_->
            }

            val alert = alertBuilder.create()
            alert.show()
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
