package edu.uco.dpham9.datprobertmtermproject

import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import edu.uco.dpham9.datprobertmtermproject.Model.Category
import edu.uco.dpham9.datprobertmtermproject.Model.Exercise
import kotlinx.android.synthetic.main.activity_find_exercise.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class FindExerciseActivity : AppCompatActivity()
{

    var exerciseList = ArrayList<Exercise>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_exercise)

        val adapter = ArrayAdapter<Exercise>(this,
            android.R.layout.simple_list_item_1, exerciseList )

        id_listView.adapter = adapter

        ExerciseGetTask().execute()
    }

    inner class  ExerciseGetTask: AsyncTask<String, Int, String>() {
        override fun doInBackground(vararg params: String?): String {
            val baseURL = "https://wger.de/api/v2/exerciseinfo/?format=json&language=2"
            val uri = Uri.parse(baseURL)//.buildUpon()
                //.build()

            //.appendQueryParameter("?language=", params[0])

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

            try
            {
                exerciseList.clear()

                for(i in 0..(resultArray.length() - 1))
                {
                    val exercise = resultArray.getJSONObject(i)
                    if(exercise.length() > 0)
                    {
                        val name = exercise.getString("name")
                        val category = exercise.getJSONObject("category").getString("name")
                        val description = exercise.getString("description")

                        val equipmentList: JSONArray = exercise.getJSONArray("equipment")
                        var equipment = ""
                        if (equipmentList.length() > 0)
                        {
                            for (e in 0..(equipmentList.length() - 1))
                            {
                                equipment = "$equipment${equipmentList.getJSONObject(e).getString("name")}, "
                            }
                        }

                        val muscleList = exercise.getJSONArray("muscles")
                        var muscles = ""
                        if (muscleList.length() > 0)
                        {
                            for (m in 0..(muscleList.length() - 1))
                            {
                                muscles = "$muscles${muscleList.getJSONObject(m).getString("name")}, "
                            }
                        }

                        val exerciseInfo = Exercise(name, category, description, equipment, muscles)
                        exerciseList.add(exerciseInfo)
                    }
                }
            }
            catch(ex: Exception)
            {
                Log.d(edu.uco.dpham9.datprobertmtermproject.Users.TAG, ex.toString())
            }

            val adapter = id_listView.adapter as ArrayAdapter<Exercise>
            adapter.notifyDataSetChanged()
            progressBar4.visibility = View.GONE
        }
    }
}
