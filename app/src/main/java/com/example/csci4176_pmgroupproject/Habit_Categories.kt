package com.example.csci4176_pmgroupproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Habit_Categories : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.habit_item)

        val categories = arrayOf("cat1", "cat2", "cat3")
        val customAdapter = Habit_Categories_Adapter(categories)
        val recyclerView: RecyclerView = findViewById(R.id.categoryRecyclerView)
        connectAdapter(recyclerView, customAdapter)


    }

    fun connectAdapter(recyclerView : RecyclerView, customAdapter : Habit_Categories_Adapter){
        val llm = LinearLayoutManager(this)
        llm.setOrientation(LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = llm
        recyclerView.adapter = customAdapter
    }

}
