package com.example.csci4176_pmgroupproject

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.csci4176_pmgroupproject.ActivityModel.ActivityModelEnums
import com.example.csci4176_pmgroupproject.Model.ActivityModel
import com.example.csci4176_pmgroupproject.Model.CheckedActivityModel
import com.example.csci4176_pmgroupproject.Model.CountableActivityModel
import com.example.csci4176_pmgroupproject.Model.TimedActivityModel
import java.time.DayOfWeek

class ModifyActivity : Fragment(){
    private var activityModel: ActivityModel? = null

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
        private const val ARG_ACT = "activityModel"
        private const val ARG_TYPE = "activityModelType"

        fun newInstance(type: ActivityModelEnums, activityModel: ActivityModel?): ModifyActivity {
            val fragment = ModifyActivity()
            val args = Bundle().apply {
                putParcelable(ARG_ACT, activityModel)
            }
            fragment.arguments = args
            return fragment
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val type = it.getString(ARG_TYPE)
            when (type){
                ActivityModelEnums.CHECKED.toString() -> activityModel = it.getParcelable(ARG_ACT, CheckedActivityModel::class.java)
                ActivityModelEnums.COUNTABLE.toString() -> activityModel = it.getParcelable(ARG_ACT, CountableActivityModel::class.java)
                ActivityModelEnums.TIMED.toString() -> activityModel = it.getParcelable(ARG_ACT, TimedActivityModel::class.java)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activityTitleView = view.findViewById<EditText>(R.id.new_activity_title)
        val activityNoteSection = view.findViewById<EditText>(R.id.new_activity_note)
        val activitySaveButton = view.findViewById<Button>(R.id.save_activity_button)
        val activityDeleteButton = view.findViewById<Button>(R.id.delete_activity_button)

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
        for (i in DOWtoggles.indices){
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
        for (i in REPtoggles.indices){
            REPSwitchListener(REPtoggles[i], i)
        }

        /* Activity Type Selection */
        val radioGroup = view.findViewById<RadioGroup>(R.id.activity_type)
        /* RadioGroup checking functionality */

        // Fill fields:
        activityModel?.let { model ->
            activityTitleView.setText(model.title)
            activityNoteSection.setText(model.note)
            setDaysOfWeek(model.days)
            setActivityRep(model.frequency)
            when (model.type){
                com.example.csci4176_pmgroupproject.ActivityModelEnums.CHECKED ->  radioGroup.check(R.id.checkable_radio)
                com.example.csci4176_pmgroupproject.ActivityModelEnums.TIMED ->  radioGroup.check(R.id.timed_radio)
                com.example.csci4176_pmgroupproject.ActivityModelEnums.COUNTABLE ->  radioGroup.check(R.id.countable_radio)
            }
        }
    }

    private fun makeMsg(msg: String){
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun setDaysOfWeek(days: ArrayList<DayOfWeek>) {
        for (day in days){
            when (day){
                DayOfWeek.MONDAY -> DOWtoggles[0].isChecked = true
                DayOfWeek.TUESDAY -> DOWtoggles[1].isChecked = true
                DayOfWeek.WEDNESDAY -> DOWtoggles[2].isChecked = true
                DayOfWeek.THURSDAY -> DOWtoggles[3].isChecked = true
                DayOfWeek.FRIDAY -> DOWtoggles[4].isChecked = true
                DayOfWeek.SATURDAY -> DOWtoggles[5].isChecked = true
                DayOfWeek.SUNDAY -> DOWtoggles[6].isChecked = true
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

}