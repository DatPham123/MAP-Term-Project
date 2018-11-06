package edu.uco.dpham9.datprobertmtermproject.Model

class TraineeExercise(var name: String, var description: String, var videoUrl: String, val traineeId: String)
{
    constructor() : this("", "", "", "")

    //traineeId refers to User: userId
    val exerciseId = java.util.UUID.randomUUID().toString() //randomly generated string
    var rating: Int = 0
}