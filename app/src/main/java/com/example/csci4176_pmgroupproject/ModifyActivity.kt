package com.example.csci4176_pmgroupproject

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import com.example.csci4176_pmgroupproject.Model.ActivityModel
import com.example.csci4176_pmgroupproject.Model.CheckedActivityModel
import com.example.csci4176_pmgroupproject.Model.CountableActivityModel
import com.example.csci4176_pmgroupproject.Model.TimedActivityModel
import java.time.DayOfWeek

private const val ARG_PARAM1 = "param1"
class ModifyActivity : Fragment(){
    private var taskId: String? = null;

    private val daysOfWeek = arrayListOf(false, false, false, false, false, false ,false)
    private var REPEnums = arrayListOf(
        ActivityModelFrequency.DAILY,
        ActivityModelFrequency.WEEKLY,
        ActivityModelFrequency.BIWEEKLY,
        ActivityModelFrequency.TRI_WEEKLY,
        ActivityModelFrequency.MONTHLY
    )

    private lateinit var DOWtoggles: ArrayList<ToggleButton>
    private lateinit var REPtoggles: ArrayList<ToggleButton>;

    private lateinit var repeatFrequency: ActivityModelFrequency;

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            ModifyActivity().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            taskId = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_modify_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activityTitleView = view.findViewById<EditText>(R.id.activity_title)
        val activityNoteSection = view.findViewById<EditText>(R.id.activity_note)
        val activitySaveButton = view.findViewById<Button>(R.id.save_activity_button)
        val activityDeleteButton = view.findViewById<Button>(R.id.delete_activity_button)

        /* Selected Days get Toggles */
        DOWtoggles = arrayListOf(
            view.findViewById<ToggleButton>(R.id.dow_sunday),
            view.findViewById<ToggleButton>(R.id.dow_monday),
            view.findViewById<ToggleButton>(R.id.dow_tuesday),
            view.findViewById<ToggleButton>(R.id.dow_wednesday),
            view.findViewById<ToggleButton>(R.id.dow_thursday),
            view.findViewById<ToggleButton>(R.id.dow_friday),
            view.findViewById<ToggleButton>(R.id.dow_saturday)
        )
        /* set listen for all day toggles */
        for (i in DOWtoggles.indices) {
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
        for (i in REPtoggles.indices) {
            REPSwitchListener(REPtoggles[i], i)
        }

        /* Activity Type Selection */
        val radioGroup = view.findViewById<RadioGroup>(R.id.activity_type)
        /* RadioGroup checking functionality */

        if (taskId != null) {
            DatabaseAPI.getActivityById(taskId!!) { model ->
                activityTitleView.setText(model.title)
                activityNoteSection.setText(model.note)
                setDaysOfWeek(model.days)
                setActivityRep(model.frequency)
                when (model.type) {
                    ActivityModelEnums.CHECKED -> radioGroup.check(R.id.checkable_radio)
                    ActivityModelEnums.TIMED -> radioGroup.check(R.id.timed_radio)
                    ActivityModelEnums.COUNTABLE -> radioGroup.check(R.id.countable_radio)
                }

                /* Setup Buttons: */
                activitySaveButton.setOnClickListener {
                    activityDeleteButton.isClickable = false;
                    activitySaveButton.isClickable = false

                    val title = activityTitleView.text.toString()
                    val note = activityNoteSection.text.toString()
                    if (repeatFrequency == ActivityModelFrequency.DAILY){
                        model.frequency = repeatFrequency
                        model.days = arrayListOf()
                        model.note = note
                        if (title.isNotEmpty() && title != model.title){ model.title = title }
                        updateActivityModel(model)
                    }else{
                        val days = getDaysOfWeek()
                        model.frequency = repeatFrequency
                        model.days = days
                        model.note = note
                        if (title.isNotEmpty() && title != model.title){ model.title = title }
                        if (days.isNotEmpty()){
                            updateActivityModel(model)
                        }else{
                            makeMsg("Must have at least 1 day selected!")
                            activityDeleteButton.isClickable = true;
                            activitySaveButton.isClickable = true
                        }
                    }
                }

                activityDeleteButton.setOnClickListener {
                    activityDeleteButton.isClickable = false;
                    activitySaveButton.isClickable = false
                    deleteActivityModel(model.taskId)
                }
            }
        }

    }

    private fun makeMsg(msg: String){
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun setDaysOfWeek(days: ArrayList<DayOfWeek>) {
        for (day in days){
            when (day){
                DayOfWeek.SUNDAY -> DOWtoggles[0].isChecked = true
                DayOfWeek.MONDAY -> DOWtoggles[1].isChecked = true
                DayOfWeek.TUESDAY -> DOWtoggles[2].isChecked = true
                DayOfWeek.WEDNESDAY -> DOWtoggles[3].isChecked = true
                DayOfWeek.THURSDAY -> DOWtoggles[4].isChecked = true
                DayOfWeek.FRIDAY -> DOWtoggles[5].isChecked = true
                DayOfWeek.SATURDAY -> DOWtoggles[6].isChecked = true
            }
        }
    }

    private fun setActivityRep(frequency: ActivityModelFrequency){
        for (i in REPEnums.indices){
            if (frequency == REPEnums[i]){
                REPtoggles[i].isChecked = true
            }
        }
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

    private fun updateActivityModel(activityModel: ActivityModel){
        DatabaseAPI.updateActivity(activityModel)
        // Move back to the previous fragment
        parentFragmentManager.popBackStack()
    }

    private fun deleteActivityModel(id: String) {
        DatabaseAPI.deleteActivity(id)
        parentFragmentManager.popBackStack()
    }
}