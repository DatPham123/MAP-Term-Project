package edu.uco.dpham9.datprobertmtermproject

const val NO_EMAIL = "No Email"
const val NO_PASS = "No PASSWORD"
class UserAuth(var email: String, var password: String, var trainee: Boolean, var trainer: Boolean){
    init {

        if(email.isEmpty() || email.isNullOrBlank()){
            email = NO_EMAIL
        }
        if(password.isEmpty() || password.isNullOrBlank()){
            password = NO_PASS
        }
        if(trainee){
            this.trainee = trainee
        }
        if(trainer){
            this.trainer = trainer
        }
    }
}