package com.example.csci4176_pmgroupproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ToggleButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [create_activity.newInstance] factory method to
 * create an instance of this fragment.
 */
class create_activity : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var daysOfWeek = arrayOf(false, false, false, false, false, false, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activityTitleView = view.findViewById<EditText>(R.id.new_activity_title)
        val activityNoteSection = view.findViewById<EditText>(R.id.new_activity_note)
        /* Selected Days */
        val DOWtoggles = arrayOf(view.findViewById<ToggleButton>(R.id.dow_sunday),
            view.findViewById<ToggleButton>(R.id.dow_monday),
            view.findViewById<ToggleButton>(R.id.dow_tuesday),
            view.findViewById<ToggleButton>(R.id.dow_wednesday),
            view.findViewById<ToggleButton>(R.id.dow_thursday),
            view.findViewById<ToggleButton>(R.id.dow_friday),
            view.findViewById<ToggleButton>(R.id.dow_saturday)
        )
        /* set listen for all */
        for (i in 0..6){
            DOWSwitchListener(DOWtoggles[i], i)
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment create_activity.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            create_activity().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    private fun DOWSwitchListener(toggle: ToggleButton, index:Int){
       toggle.setOnCheckedChangeListener { button, checked ->
           daysOfWeek[index] = checked
       }
    }
}