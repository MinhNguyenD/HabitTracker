package com.example.csci4176_pmgroupproject

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csci4176_pmgroupproject.Activities.ManageActivity
import com.example.csci4176_pmgroupproject.Adapters.HabitCategoriesAdapter
import com.example.csci4176_pmgroupproject.Database.DatabaseAPI
import com.example.csci4176_pmgroupproject.Model.HabitModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateHabit.newInstance] factory method to
 * create an instance of this fragment.
 */
class ManageHabits : Fragment() {
    private var categories: ArrayList<String> = ArrayList()
    private val habitsRef = FirebaseDatabase.getInstance().getReference("habits")
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categories = it.getStringArrayList(ARG_PARAM1)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Check if default categories exist
        DatabaseAPI.checkForDefaultCategories { check ->
            if(!check){
                Toast.makeText(requireContext(), "Something is wrong, default habits cannot be " +
                        "added to database.", Toast.LENGTH_LONG).show()
            }
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_habits, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val customAdapter = HabitCategoriesAdapter(categories)
        val recyclerView: RecyclerView = view.findViewById(R.id.categoryRecyclerView)
        val addCategoryBtn: ImageButton = view.findViewById(R.id.addCategoryButton)
        connectAdapter(recyclerView, customAdapter)

        // Set a listener to listen for clicks on each item in the list of the categories
        customAdapter.setOnClickItemListener(object : HabitCategoriesAdapter.OnClickItemListener {
                // Display ModifyActivity screen on this activity
                override fun onClickItem(position: Int) {
                    val selectedCategory = categories[position]

                    DatabaseAPI.getHabitIdByName(selectedCategory) { habitId ->
                        // The ManageActivities fragment is shown
                        val manageActivitiesFrag = ManageActivities.newInstance(habitId)
                        val manageActivity = activity as ManageActivity
                        manageActivity.replaceFragment(manageActivitiesFrag)
                    }

                }
            })

        // Set a listener to listen for clicks on the save button
        customAdapter.setOnClickSaveItemListener(object :
            HabitCategoriesAdapter.OnClickSaveItemListener {
            override fun onClickSaveItem(newCategoryName: String) {
                addCustomCategory(newCategoryName)
                categories[categories.size - 1] = newCategoryName

                // Notify the adapter to leave "Adding category" mode
                customAdapter.setIsAddingCategory(false)

                // Refresh the page with the newly added category
                customAdapter.notifyDataSetChanged()

                // Display a pop-up message to the user
                Toast.makeText(
                    requireContext(), "The new category $newCategoryName " +
                            "has been added!", Toast.LENGTH_LONG
                ).show()
            }

        })

        // Set a listener to listen for clicks on the top right "Add category" button
        addCategoryBtn.setOnClickListener {
            // Notify the adapter to go to "Adding category" mode
            customAdapter.setIsAddingCategory(true)

            categories.add("")
            customAdapter.setNewItemPosition(categories.size - 1)
            recyclerView.smoothScrollToPosition(categories.size - 1)

            // Refresh with the latest item updated
            customAdapter.notifyItemInserted(categories.size)
        }

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment CreateHabit.
         */
        @JvmStatic
        fun newInstance(param1: MutableList<String>) =
            ManageHabits().apply {
                arguments = Bundle().apply {
                    putStringArrayList(ARG_PARAM1, ArrayList(param1))
                }
            }
    }
    private fun connectAdapter(recyclerView : RecyclerView, customAdapter : HabitCategoriesAdapter){
        val llm = LinearLayoutManager(requireContext())
        llm.setOrientation(LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = llm
        recyclerView.adapter = customAdapter
    }

    /**
     * This function adds a custom category for the currently logged in user to the database.
     * @param the name of the category to add to the database
     */
    fun addCustomCategory(habitName: String){
        // Generate a unique key for the new habit
        val habitId = habitsRef.push().key
        val habit = habitId?.let { HabitModel(it, userId, habitName) }

            // Add the habit to the generated habitId
            habitId?.let{
                habitsRef.child(it).setValue(habit)
                    .addOnSuccessListener {
                        Log.d(TAG, "The new category has been created in firebase")
                    }
                    .addOnFailureListener{ e ->
                        Toast.makeText(requireContext(), "Could not create a new category. Please try again later.",
                            Toast.LENGTH_LONG).show()
                        // Print to log debug messages
                        Log.e(TAG, "Error adding custom habit", e)
                    }
            }
        }
}