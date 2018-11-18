package edu.uco.dpham9.datprobertmtermproject.Model

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import edu.uco.dpham9.datprobertmtermproject.R

class UserListAdapter(var context: Context, var users: ArrayList<User>) :
    RecyclerView.Adapter<UserListAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.user_card_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int
    {
        return users.size
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
            val emailView = itemView.findViewById<TextView>(R.id.id_trainee_ex_name)

            emailView.text = users[position].email

            itemView.setOnClickListener {
//                val i = Intent(context, ExerciseActivity::class.java)
//                i.putExtra(EXTRA_EXERCISE, traineeExercises[position])
//                if(context is Activity)
//                    context.startActivityForResult(i, REQ_CODE_RATING)
            }
        }

    }
}