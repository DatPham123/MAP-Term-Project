package edu.uco.dpham9.datprobertmtermproject.Model

import android.os.Parcel
import android.os.Parcelable

class TraineeExercise(var name: String, var description: String, var videoUrl: String,
                      val traineeId: String,
                      val exerciseId: String = java.util.UUID.randomUUID().toString(),
                      var rating: Float =0F) : Parcelable
{
    constructor() : this("", "", "", "", "", 0F)

    //traineeId refers to User: userId
    //var exerciseId = java.util.UUID.randomUUID().toString() //randomly generated string
    //var rating: Float = 0.0f

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readFloat()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(videoUrl)
        parcel.writeString(traineeId)
        parcel.writeString(exerciseId)
        parcel.writeFloat(rating)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TraineeExercise> {
        override fun createFromParcel(parcel: Parcel): TraineeExercise {
            return TraineeExercise(parcel)
        }

        override fun newArray(size: Int): Array<TraineeExercise?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean
    {
        if(other !is TraineeExercise) return false
        return exerciseId == other.exerciseId
    }
}