package edu.uco.dpham9.datprobertmtermproject

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_trainee_exercise.*

class AddTraineeExercise : AppCompatActivity()
{
    var videoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_trainee_exercise)

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
            //add video to storage
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?)
    {
        if (requestCode == REQ_CODE_VIDEO && resultCode == RESULT_OK)
        {
            videoUri = intent?.data
            var fileName = videoUri.toString().substring(videoUri.toString()
                .lastIndexOf("/")+1)
            id_videoUrl.text = "${getString(R.string.label_filename)} $fileName"
        }
    }
}
