package com.example.csci4176_pmgroupproject

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
class ModifyActivity : Fragment(){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activityTitleView = view.findViewById<EditText>(R.id.new_activity_title)
        val activityNoteSection = view.findViewById<EditText>(R.id.new_activity_note)
        val activitySaveButton = view.findViewById<Button>(R.id.save_activity_button)
        val activityDeleteButton = view.findViewById<Button>(R.id.delete_activity_button)
    }

}