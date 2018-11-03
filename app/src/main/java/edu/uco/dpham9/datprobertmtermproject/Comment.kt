package edu.uco.dpham9.datprobertmtermproject

class Comment(var comment: String, val posterId: String, val exerciseId: String)
{
    //posterId refers to userId in User
    //exerciseId refers to exerciseId in TraineeExercise
    var timeStamp: Long = 0L
}