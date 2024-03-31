package com.example.csci4176_pmgroupproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [ModifyActivity.newInstance] factory method to
 * create an instance of this fragment.
 */
class ManageActivities : Fragment() {
    private var selectedCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedCategory = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_manage_activities, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView: TextView = view.findViewById(R.id.textView)
        val recyclerView: RecyclerView = view.findViewById(R.id.activityRecyclerView)
        val addBtn: ImageButton = view.findViewById(R.id.addActivityButton)

        // The title of this page is the name of the selected category
        textView.text = selectedCategory

        // Display all the activities in a RecycleView
        DatabaseAPI.getAllActivity { activityList ->
            // If the activity list for this habit is empty, display a message
            if(activityList.size <= 0) {
                displayNoActivity(view)
            }
            else {
                val customAdapter = ActivityAdapter(activityList)
                connectAdapter(recyclerView, customAdapter)

                // Set up a listener to listen for clicks on the Modify button
                customAdapter.setOnClickModifyItemListener(object :
                    ActivityAdapter.OnClickModifyItemListener {
                    override fun onClickModifyItem(isClicked: Boolean) {
                        if (isClicked) {
                            // TODO: The ModifyActivity fragment is shown

                        }
                    }

                })
            }
        }

        // Set up a listener to listen for clicks on the 'plus' button
        addBtn.setOnClickListener{
            // The CreateActivity fragment is shown
            val createActivityFrag: Fragment = CreateActivity.newInstance("", "")
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
                    putString(ARG_PARAM1, param1)
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