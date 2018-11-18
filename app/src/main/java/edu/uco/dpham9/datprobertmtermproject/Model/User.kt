package edu.uco.dpham9.datprobertmtermproject.Model

import android.os.Parcel
import android.os.Parcelable

const val NO_EMAIL = "No Email"
const val NO_PASS = "No PASSWORD"
class User(var email: String, var password: String, var isTrainee: Boolean, var isTrainer: Boolean,
           var id: String) : Parcelable
{
    constructor(): this("" , "" , false, false, "")

    var trainerId: String? = null   //if isTrainee, your trainer's id
    var traineeIds: ArrayList<String>? = null   //if isTrainer, your trainees' ids

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString()
    )
    {
        trainerId = parcel.readString()
    }

    init {

        if(email.isEmpty() || email.isNullOrBlank()){
            email = NO_EMAIL
        }
        if(password.isEmpty() || password.isNullOrBlank()){
            password = NO_PASS
        }
        if(isTrainee){
            this.isTrainee = isTrainee
        }
        if(isTrainer){
            this.isTrainer = isTrainer
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeByte(if (isTrainee) 1 else 0)
        parcel.writeByte(if (isTrainer) 1 else 0)
        parcel.writeString(id)
        parcel.writeString(trainerId)
        parcel.writeStringList(traineeIds)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String = email
}