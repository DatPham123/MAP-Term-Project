package edu.uco.dpham9.datprobertmtermproject.Model

import com.google.firebase.firestore.Exclude

class User(var name: String, var email: String, var password: String, val isTrainee: Boolean, val isTrainer: Boolean,
           val userId: String)
{
    constructor(): this ("" , "" , "", false, false, "")
    var timeStamp: Long = 0

    @get:Exclude
    var id: String = ""

    var trainerId: String? = null   //if isTrainee, your trainer's id
    var traineeIds: ArrayList<String>? = null   //if isTrainer, your trainees' ids
}