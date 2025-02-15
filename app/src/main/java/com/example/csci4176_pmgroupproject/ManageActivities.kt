package com.example.csci4176_pmgroupproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csci4176_pmgroupproject.Activities.CreateActivity
import com.example.csci4176_pmgroupproject.Activities.ManageActivity
import com.example.csci4176_pmgroupproject.Activities.ModifyActivity
import com.example.csci4176_pmgroupproject.Adapters.ActivityAdapter
import com.example.csci4176_pmgroupproject.Database.DatabaseAPI
import com.example.csci4176_pmgroupproject.Model.ActivityModel

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [Modify.newInstance] factory method to
 * create an instance of this fragment.
 */
class ManageActivities : Fragment() {
    private var selectedHabitId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedHabitId = it.getString(com.example.csci4176_pmgroupproject.ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Make sure the navigation bar is visible here because other fragments may hide it
        (activity as ManageActivity).showNavigationBar()

        return inflater.inflate(R.layout.fragment_manage_activities, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView: TextView = view.findViewById(R.id.textView)
        val recyclerView: RecyclerView = view.findViewById(R.id.activityRecyclerView)
        val addBtn: ImageButton = view.findViewById(R.id.addActivityButton)

        selectedHabitId?.let {
            DatabaseAPI.getHabitById(it) { habit ->
                // The title of this page is the name of the selected category
                textView.text = habit.habitName

                // Display all the activities in a RecycleView
                DatabaseAPI.getAllActivityByHabitId(it) { activityList: ArrayList<ActivityModel> ->
                    // If the activity list for this habit is empty, display a message
                    // Assure that the it matches habitId, so the correct activities are displayed
                    if (activityList.size <= 0) {
                        displayNoActivity(view)
                    } else {
                        val customAdapter = ActivityAdapter(activityList)
                        connectAdapter(recyclerView, customAdapter)

                        // Set up a listener to listen for clicks on the Modify button
                        customAdapter.setOnClickModifyItemListener(object :
                            ActivityAdapter.OnClickModifyItemListener {
                            override fun onClickModifyItem(
                                isClicked: Boolean,
                                activityModel: ActivityModel
                            ) {
                                if (isClicked) {
                                    // The ModifyActivity fragment is shown
                                    val modifyActivityFrag: Fragment =
                                        ModifyActivity.newInstance(activityModel.taskId)
                                    val manageActivity = activity as ManageActivity
                                    manageActivity.replaceFragment(modifyActivityFrag)
                                }
                            }

                        })
                    }
                }

            }
        }

        // Set up a listener to listen for clicks on the 'plus' button
        addBtn.setOnClickListener{
            // The CreateActivity fragment is shown
            val createActivityFrag: Fragment = CreateActivity.newInstance(selectedHabitId!!)
            val manageActivity = activity as ManageActivity
            manageActivity.replaceFragment(createActivityFrag)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment ModifyActivity.
         */
        @JvmStatic
        fun newInstance(param1: String) =
            ManageActivities().apply {
                arguments = Bundle().apply {
                    putString(com.example.csci4176_pmgroupproject.ARG_PARAM1, param1)
                }
            }
    }

    private fun connectAdapter(recyclerView: RecyclerView, customAdapter: ActivityAdapter){
        val llm = LinearLayoutManager(requireContext())
        llm.setOrientation(LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = llm
        recyclerView.adapter = customAdapter
    }

    private fun displayNoActivity(view: View){
        val noActivityView: TextView = view.findViewById(R.id.noActivity)
        val recyclerView: RecyclerView = view.findViewById(R.id.activityRecyclerView)

        recyclerView.visibility = View.GONE
        noActivityView.visibility = View.VISIBLE
        noActivityView.text = "You have no activities at the moment"
    }
}