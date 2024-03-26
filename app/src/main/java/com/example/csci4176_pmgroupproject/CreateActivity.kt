package com.example.csci4176_pmgroupproject

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.ToggleButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateActivity.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateActivity : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var daysOfWeek = arrayOf(false, false, false, false, false, false, false)
    private var REPEnums = arrayOf(
        ActivityModelRepeat.DAILY,
        ActivityModelRepeat.WEEKLY,
        ActivityModelRepeat.BI_WEEKLY,
        ActivityModelRepeat.TRI_WEEKLY,
        ActivityModelRepeat.MONTHLY
        )
    private lateinit var DOWtoggles: Array<ToggleButton>;
    private lateinit var REPtoggles: Array<ToggleButton>;

    private lateinit var repeatFrequency: ActivityModelRepeat;
    private lateinit var activityType: ActivityModelEnums;

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
        repeatFrequency = ActivityModelRepeat.WEEKLY
        val activityTitleView = view.findViewById<EditText>(R.id.new_activity_title)
        val activityNoteSection = view.findViewById<EditText>(R.id.new_activity_note)
        val activityCreateButton = view.findViewById<Button>(R.id.create_activity_button)

        /* Selected Days get Toggles */
        DOWtoggles = arrayOf(view.findViewById<ToggleButton>(R.id.dow_sunday),
            view.findViewById<ToggleButton>(R.id.dow_monday),
            view.findViewById<ToggleButton>(R.id.dow_tuesday),
            view.findViewById<ToggleButton>(R.id.dow_wednesday),
            view.findViewById<ToggleButton>(R.id.dow_thursday),
            view.findViewById<ToggleButton>(R.id.dow_friday),
            view.findViewById<ToggleButton>(R.id.dow_saturday)
        )
        /* set listen for all day toggles */
        for (i in 0..<DOWtoggles.size){
            DOWSwitchListener(DOWtoggles[i], i)
        }

        /* Repeat Frequency Toggles */
        REPtoggles = arrayOf(
            view.findViewById<ToggleButton>(R.id.repeat_daily),
            view.findViewById<ToggleButton>(R.id.repeat_weekly),
            view.findViewById<ToggleButton>(R.id.repeat_biweekly),
            view.findViewById<ToggleButton>(R.id.repeat_triweekly),
            view.findViewById<ToggleButton>(R.id.repeat_monthly)
        )
        for (i in 0..<REPtoggles.size){
            REPSwitchListener(REPtoggles[i], i)
        }

        /* Activity Type Selection */
        val radioGroup = view.findViewById<RadioGroup>(R.id.activity_type)
        radioGroup.setOnCheckedChangeListener { group, i ->
            when (group.checkedRadioButtonId){
                R.id.checkable_radio -> {
                    activityType = ActivityModelEnums.CHECKED
                }
                R.id.timed_radio -> {
                    activityType = ActivityModelEnums.TIMED
                }
                R.id.countable_radio -> {
                    activityType = ActivityModelEnums.COUNTABLE
                }
            }
        }

        activityCreateButton.setOnClickListener {
            val title = activityTitleView.text.toString()
            val note = activityNoteSection.text.toString()
            var days = "["
            for (i in 0..<daysOfWeek.size){
                if (i != daysOfWeek.size - 1) {
                    days += daysOfWeek[i].toString() + ", "
                }else {
                    days += daysOfWeek[i].toString() + "]"
                }
            }
            Log.w("Activity Create", "All Values: Title: ${title}, Days of the week: ${days}," +
                    " Repeat Frequency: ${repeatFrequency.toString()}, Activity Type: ${activityType.toString()}," +
                    "Note: $note")
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
            CreateActivity().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    private fun REPSwitchListener(toggle: ToggleButton, index: Int){
        if (index == 1){
            // Default on:
            REPtoggles[index].isChecked = true
            REPtoggles[index].backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.toggle_selected))
            repeatFrequency = REPEnums[index]
        }
        toggle.setOnCheckedChangeListener { button, checked ->
            if (checked){
                button.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.toggle_selected))
                if (index == 0){
                    for (toggle in DOWtoggles){
                        toggle.isChecked = true;
                    }
                }
                for (toggle in REPtoggles){
                    if (toggle != button){
                        toggle.isChecked = false;
                        toggle.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.toggle_unselected))
                    }
                }
                repeatFrequency = REPEnums[index]
            } else if (index == 0){
                button.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.toggle_unselected))
                REPtoggles[1].isChecked = true
                REPtoggles[1].backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.toggle_selected))
                repeatFrequency = REPEnums[1]
                for (toggle in DOWtoggles){
                    toggle.isChecked = false;
                }
            }
        }
    }

    private fun DOWSwitchListener(toggle: ToggleButton, index:Int){
       toggle.setOnCheckedChangeListener { button, checked ->
           daysOfWeek[index] = checked
           if (checked){
               button.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.toggle_selected))
           }else {
               button.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.toggle_unselected))
           }
       }
    }
}