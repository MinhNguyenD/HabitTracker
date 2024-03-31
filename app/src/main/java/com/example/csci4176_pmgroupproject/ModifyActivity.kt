package com.example.csci4176_pmgroupproject

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.csci4176_pmgroupproject.ActivityModel.ActivityModelEnums
import com.example.csci4176_pmgroupproject.Model.ActivityModel
import com.example.csci4176_pmgroupproject.Model.CheckedActivityModel
import com.example.csci4176_pmgroupproject.Model.CountableActivityModel
import com.example.csci4176_pmgroupproject.Model.TimedActivityModel

class ModifyActivity : Fragment(){
    private var activityModel: ActivityModel? = null

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
            var type = it.getString(ARG_TYPE)
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
    }

}