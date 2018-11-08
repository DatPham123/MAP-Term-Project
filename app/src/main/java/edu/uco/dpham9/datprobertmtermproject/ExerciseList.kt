package edu.uco.dpham9.datprobertmtermproject

class ExerciseList(var exercise: String, var equipment: String, var muscle: String) {
    override fun toString(): String =
        "[Title]$exercise\n[Equipment]$equipment\n[Muscle]$muscle"

}