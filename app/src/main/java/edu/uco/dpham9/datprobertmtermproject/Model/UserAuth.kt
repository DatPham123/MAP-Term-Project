package edu.uco.dpham9.datprobertmtermproject.Model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.Exclude

const val NO_EMAIL = "No Email"
const val NO_PASS = "No PASSWORD"
class UserAuth(var email: String, var password: String, var trainee: Boolean, var trainer: Boolean):Parcelable{
    constructor(): this("" , "" , false, false)
    var timeStamp: Long = 0

    @get:Exclude
    var id: String = ""

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )


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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeByte(if (trainee) 1 else 0)
        parcel.writeByte(if (trainer) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserAuth> {
        override fun createFromParcel(parcel: Parcel): UserAuth {
            return UserAuth(parcel)
        }

        override fun newArray(size: Int): Array<UserAuth?> {
            return arrayOfNulls(size)
        }
    }
}