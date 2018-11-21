package edu.uco.dpham9.datprobertmtermproject.Model

//for general exercise database
enum class Category
{
    ABS, ARMS, BACK, CALVES, CHEST, LEGS, SHOULDERS
}

class Exercise(var name: String, var category: String, var description: String)
{
    var equipment: String = ""
    var muscles: String = ""
    constructor(name: String, category: String, description: String,
                equipment: String, muscles: String) : this(name, category, description)
    {
        this.equipment = equipment
        this.muscles = muscles
    }

    override fun toString(): String =
        "[Name]:$name\n[Category]:$category\n[Description]:$description\n[Equipment]:$equipment\n[Muscles]:$muscles"
}