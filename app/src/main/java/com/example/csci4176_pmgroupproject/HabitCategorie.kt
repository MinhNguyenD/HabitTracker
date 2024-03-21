package com.example.csci4176_pmgroupproject

import android.R.attr.data
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class HabitCategorie : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.habit_item)

        val categories = arrayOf("Health", "Fitness", "Study", "LifeStyle", "Art", "Financial", "Social")
        val customAdapter = Habit_Categories_Adapter(categories)
        val recyclerView: RecyclerView = findViewById(R.id.categoryRecyclerView)
        connectAdapter(recyclerView, customAdapter)

        customAdapter.setOnClickListener(object : Habit_Categories_Adapter.OnClickListener {
            override fun onClick(position: Int) {
                //TODO: connect to database and add chosen categories
                Toast.makeText(
                    this@HabitCategorie, "You Selected: "+ categories[position],
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    fun connectAdapter(recyclerView : RecyclerView, customAdapter : Habit_Categories_Adapter){
        val llm = LinearLayoutManager(this)
        llm.setOrientation(LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = llm
        recyclerView.adapter = customAdapter
    }

}
