package edu.uco.dpham9.datprobertmtermproject.Model

class Comment(var comment: String, val posterId: String, val exerciseId: String)
{
    //posterId refers to userId in User
    //exerciseId refers to exerciseId in TraineeExercise

    override fun toString(): String
    {
        return comment
    }
}