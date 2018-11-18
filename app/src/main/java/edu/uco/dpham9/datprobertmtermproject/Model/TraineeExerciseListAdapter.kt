package edu.uco.dpham9.datprobertmtermproject.Model

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import edu.uco.dpham9.datprobertmtermproject.EditTraineeExercise
import edu.uco.dpham9.datprobertmtermproject.ExerciseActivity
import edu.uco.dpham9.datprobertmtermproject.R
import edu.uco.dpham9.datprobertmtermproject.Users.EXTRA_EXERCISE
import edu.uco.dpham9.datprobertmtermproject.Users.REQ_CODE_COMMENT
import edu.uco.dpham9.datprobertmtermproject.Users.REQ_CODE_EDIT_EX

class TraineeExerciseListAdapter(val context: Context, var traineeExercises: ArrayList<TraineeExercise>)
    : RecyclerView.Adapter<TraineeExerciseListAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.card_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int
    {
        return traineeExercises.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bindItem(position)
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
    {
        fun bindItem(position: Int)
        {
            //references to text views
            val nameView = itemView.findViewById<TextView>(R.id.id_trainee_ex_name)
            val descView = itemView.findViewById<TextView>(R.id.id_trainee_ex_desc)
            val editBtn = itemView.findViewById<Button>(R.id.id_trainee_editBtn)

            nameView.text = traineeExercises[position].name
            descView.text = traineeExercises[position].description

            itemView.setOnClickListener {
                val i = Intent(context, ExerciseActivity::class.java)
                i.putExtra(EXTRA_EXERCISE, traineeExercises[position])
                if(context is Activity)
                    context.startActivityForResult(i, REQ_CODE_COMMENT)
            }

            //itemView represents 1 card entry
            editBtn.setOnClickListener {
                //Launch Edit Activity
                val i = Intent(context, EditTraineeExercise::class.java)
                i.putExtra(EXTRA_EXERCISE, traineeExercises[position])

                //must cast to Activity to use startActivityForResult outside of activity classes
                if(context is Activity)
                    context.startActivityForResult(i, REQ_CODE_EDIT_EX)
            }
        }

    }
}