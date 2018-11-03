package edu.uco.dpham9.datprobertmtermproject

class User(var name: String, var email: String, var password: String, val isTrainee: Boolean, val isTrainer: Boolean,
           val userId: String)
{
    var trainerId: String? = null   //if isTrainee, your trainer's id
    var traineeIds: ArrayList<String>? = null   //if isTrainer, your trainees' ids
}