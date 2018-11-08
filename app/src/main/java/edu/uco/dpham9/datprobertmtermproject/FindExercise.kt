package edu.uco.dpham9.datprobertmtermproject

import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_find_exercise.*
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class FindExercise : AppCompatActivity() {

    var exerciseList = ArrayList<ExerciseList>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_exercise)

        val adapter = ArrayAdapter<ExerciseList>(this,
            android.R.layout.simple_list_item_1, exerciseList )

        id_listView.adapter = adapter

        id_searchBtn.setOnClickListener {
            val workout = id_workout.text.toString().trim()
            ExerciseGetTask().execute(workout, workout, 1.toString())
        }
    }

    inner class  ExerciseGetTask: AsyncTask<String, Int, String>() {
        override fun doInBackground(vararg params: String?): String {
            val baseURL = "https://wger.de/api/v2/exercise"
            val uri = Uri.parse(baseURL).buildUpon()
                .appendQueryParameter("i", params[0])
                .appendQueryParameter("q", params[1])
                .appendQueryParameter("p", params[2])
                .build()

            val httpConnection = URL(uri.toString()).openConnection()
            val bStream = BufferedInputStream(httpConnection.getInputStream())
            val reader = BufferedReader(InputStreamReader(bStream))
            val data = StringBuffer()

            while(true){
                val line = reader.readLine()
                if(line == null) break
                data.append(line)
            }
            return data.toString()
        }

        override fun onPostExecute(result: String?) {
            val jsonObject = JSONObject(result)
            val resultArray = jsonObject.getJSONArray("results")

            exerciseList.clear()

            for(i in 0..(resultArray.length()-1)){
                val obj = resultArray.getJSONObject(i)
                val exercise = obj.getString("exercise")
                val equipment = obj.getString("equipment")
                val muscle = obj.getString("muscle")
                val exerciseInfo = ExerciseList(exercise, equipment, muscle)
                exerciseList.add(exerciseInfo)
            }

            val adapter = id_listView.adapter as ArrayAdapter<ExerciseList>
            adapter.notifyDataSetChanged()
        }
    }
}
