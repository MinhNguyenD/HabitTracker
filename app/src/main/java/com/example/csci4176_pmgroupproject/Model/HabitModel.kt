package com.example.csci4176_pmgroupproject.Model

// For default habit categories, the userID will be null
data class HabitModel(var habitId: String, val userId: String?, val habitName: String)

// Default habits that should always exist in the database for the user to select from
enum class DefaultHabits(val habitName: String){
    HABIT1("Health"),
    HABIT2("Fitness"),
    HABIT3("Study"),
    HABIT4("Lifestyle"),
    HABIT5("Art"),
    HABIT6("Financial"),
    HABIT7("Social")
}