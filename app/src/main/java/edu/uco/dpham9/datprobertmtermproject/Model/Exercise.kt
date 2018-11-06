package edu.uco.dpham9.datprobertmtermproject.Model

//for general exercise database
enum class Category {
    ABS, ARMS, BACK, CALVES, CHEST, LEGS, SHOULDERS
}

class Exercise(var name: String, var category: Category, var description: String)
{
    constructor(name: String, category: Category, description: String, muscles: String,
                imageUrl: String) : this(name, category, description)
}