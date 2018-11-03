package edu.uco.dpham9.datprobertmtermproject

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class TraineeExerciseListAdapter(val context: Context, var traineeExercises: ArrayList<TraineeExercise>)
    : RecyclerView.Adapter<TraineeExerciseListAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TraineeExerciseListAdapter.ViewHolder
    {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.card_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int
    {
        return traineeExercises.size
    }

    override fun onBindViewHolder(holder: TraineeExerciseListAdapter.ViewHolder, position: Int)
    {
        holder.bindItem(position)
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
    {
        fun bindItem(position: Int)
        {
//            //references to text and image views
//            val nameView = itemView.findViewById<TextView>(R.id.txt_name)
//            val ageView = itemView.findViewById<TextView>(R.id.txt_age)
//            val gpaView = itemView.findViewById<TextView>(R.id.txt_gpa)
//            val iconView = itemView.findViewById<ImageView>(R.id.img_cardIcon)
//
//            nameView.text = studentList[position].name
//            ageView.text = studentList[position].age.toString()
//            gpaView.text = studentList[position].gpa.toString()
//
//            val icon = when(studentList[position].year)
//            {
//                0 -> R.drawable.ic_year_one
//                1 -> R.drawable.ic_year_two
//                2 -> R.drawable.ic_year_three
//                else -> R.drawable.ic_year_four
//            }
//
//            iconView.setImageResource(icon)
//
//            //itemView represents 1 card entry
//            itemView.setOnClickListener {
//                Snackbar.make(it, "position = $position name = ${studentList[position].name}",
//                    Snackbar.LENGTH_LONG).show()
//            }
        }
    }
}