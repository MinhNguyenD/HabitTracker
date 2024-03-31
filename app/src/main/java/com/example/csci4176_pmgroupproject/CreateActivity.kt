package com.example.csci4176_pmgroupproject

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import android.widget.ToggleButton
import com.example.csci4176_pmgroupproject.ActivityModel.ActivityModelEnums
import com.example.csci4176_pmgroupproject.Model.ActivityModel
import com.example.csci4176_pmgroupproject.Model.CheckedActivityModel
import com.example.csci4176_pmgroupproject.Model.CountableActivityModel
import com.example.csci4176_pmgroupproject.Model.TimedActivityModel
import java.time.DayOfWeek

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateActivity.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateActivity : Fragment() {
    // Habit Id passed from ManageActivities fragment
    private var habitId: String? = null
    private var daysOfWeek = arrayListOf(false, false, false, false, false, false, false)
    private var REPEnums = arrayListOf(
        ActivityModelFrequency.DAILY,
        ActivityModelFrequency.WEEKLY,
        ActivityModelFrequency.BIWEEKLY,
        ActivityModelFrequency.TRI_WEEKLY,
        ActivityModelFrequency.MONTHLY
        )
    private lateinit var DOWtoggles: ArrayList<ToggleButton>;
    private lateinit var REPtoggles: ArrayList<ToggleButton>;

    private lateinit var repeatFrequency: ActivityModelFrequency;
    private lateinit var activityType: ActivityModelEnums;

    private lateinit var countContainer: EditText;
    private var count: Int = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            habitId = it.getString(ARG_PARAM1)
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
        repeatFrequency = ActivityModelFrequency.WEEKLY
        val activityTitleView = view.findViewById<EditText>(R.id.new_activity_title)
        val activityNoteSection = view.findViewById<EditText>(R.id.new_activity_note)
        val activityCreateButton = view.findViewById<Button>(R.id.create_activity_button)
        countContainer = view.findViewById(R.id.new_activity_count)

        /* Selected Days get Toggles */
        DOWtoggles = arrayListOf(view.findViewById<ToggleButton>(R.id.dow_sunday),
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
        REPtoggles = arrayListOf(
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
        /* RadioGroup checking functionality */
        radioGroupListener(radioGroup)
        radioGroup.check(R.id.checkable_radio)

        activityCreateButton.setOnClickListener {
            activityCreateButton.isActivated = false
            val title = activityTitleView.text.toString()
            val note = activityNoteSection.text.toString()

            if (title.isNotEmpty()){
                if (repeatFrequency == ActivityModelFrequency.DAILY){
                    // No need to store days of the week.
                    if (activityType == ActivityModelEnums.COUNTABLE){
                        val countText = countContainer.text.toString()
                        if (countText.isNotEmpty() && countText.toInt() > 0){
                            count = countText.toInt()
                            createActivity(title, note, arrayListOf())
                        }else {
                            makeMsg("Please input a value greater than 0")
                            activityCreateButton.isActivated = true
                        }
                    }else {
                        createActivity(title, note, arrayListOf())
                    }
                }else {
                    val days:ArrayList<DayOfWeek> = getDaysOfWeek()
                    if (days.isNotEmpty()){
                        if (activityType == ActivityModelEnums.COUNTABLE){
                            val countText = countContainer.text.toString()
                            if (countText.isNotEmpty() && countText.toInt() > 0){
                                count = countText.toInt()
                                createActivity(title, note, days)
                            }else {
                                makeMsg("Please input a value greater than 0")
                                activityCreateButton.isActivated = true
                            }
                        }else {
                            createActivity(title, note, days)
                        }
                    } else{
                        makeMsg("Please select at least 1 day!")
                        activityCreateButton.isActivated = true
                    }
                }
            } else {
                makeMsg("Please give the activity a title!")
                activityCreateButton.isActivated = true
            }

        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment create_activity.
         */
        @JvmStatic
        fun newInstance(param1: String) =
            CreateActivity().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
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
                    for (dayToggle in DOWtoggles){
                        dayToggle.isChecked = true;
                    }
                }
                for (dayToggle in REPtoggles){
                    if (dayToggle != button){
                        dayToggle.isChecked = false;
                        dayToggle.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.toggle_unselected))
                    }
                }
                repeatFrequency = REPEnums[index]
            } else if (index == 0){
                button.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.toggle_unselected))
                REPtoggles[1].isChecked = true
                REPtoggles[1].backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.toggle_selected))
                repeatFrequency = REPEnums[1]
                for (dayToggle in DOWtoggles){
                    dayToggle.isChecked = false;
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

    private fun radioGroupListener(radioGroup: RadioGroup){
        radioGroup.setOnCheckedChangeListener { group, i ->
            when (group.checkedRadioButtonId){
                R.id.checkable_radio -> {
                    activityType = ActivityModelEnums.CHECKED
                    countContainer.visibility = android.view.View.GONE
                }
                R.id.timed_radio -> {
                    activityType = ActivityModelEnums.TIMED
                    countContainer.visibility = android.view.View.GONE
                }
                R.id.countable_radio -> {
                    activityType = ActivityModelEnums.COUNTABLE
                    countContainer.visibility = android.view.View.VISIBLE
                }
            }
        }
    }

    private fun createActivity(title: String, note:String, days:ArrayList<DayOfWeek>){
        // TODO: Connect to database
        var model: ActivityModel
        when (activityType){
            ActivityModelEnums.CHECKED -> {
                model = CheckedActivityModel(habitId!!, title, repeatFrequency, days)
            }
            ActivityModelEnums.TIMED -> {
                model = TimedActivityModel(habitId!!,title,repeatFrequency, days)
            }

            ActivityModelEnums.COUNTABLE -> {
                model = CountableActivityModel(habitId!!, title, repeatFrequency, days, count)
            }
        }
        model.makeNote(note)
        makeMsg("Success!")
//        Log.w("Create Activity", "Model: ${model.title} | ${model.type}" +
//                "| ${model.days.toString()} | ${model.frequency} |")
        DatabaseAPI.createActivity(model)

        // Move back to the previous fragment
        parentFragmentManager.popBackStack()
    }

    private fun getDaysOfWeek(): ArrayList<DayOfWeek>{
        val days = arrayListOf(
            DayOfWeek.SUNDAY,
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY
        )
        var submit = ArrayList<DayOfWeek>()
        for (i in daysOfWeek.indices){
            if (daysOfWeek[i]){
                submit.add(days[i])
            }
        }
        return submit
    }

    private fun makeMsg(msg: String){
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}