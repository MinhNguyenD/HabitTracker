package com.example.csci4176_pmgroupproject.Model

// For default habit categories, the userID will be null
data class HabitModel(var habitId: String, val userId: String?, val habitName: String)