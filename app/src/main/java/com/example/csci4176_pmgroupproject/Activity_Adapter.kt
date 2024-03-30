package com.example.csci4176_pmgroupproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Activity_Adapter(private var dataSet: MutableList<String>) :
RecyclerView.Adapter<Activity_Adapter.ViewHolder>() {
        private var modifyItemListener: OnClickModifyItemListener? = null
        private var addNewActivityListener: OnClickAddItemListener? = null
        private var isAddingActivity = false

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder)
         */
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView
            val  modifyBtn: Button

            init {
                textView = view.findViewById(R.id.textView_habit_item_recyclerview_item)
                modifyBtn = view.findViewById(R.id.modifyBtn_habit_item_recyclerview_item)
            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view, which defines the UI of the list item
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.activity_item, viewGroup, false)

            return ViewHolder(view)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            viewHolder.textView.text = dataSet[position]

            // TODO: In "Adding activity" mode, the CreateActivity fragment is shown

        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size

        fun getIsAddingActivity(): Boolean {
            return this.isAddingActivity
        }

        // Set the "Adding activity" mode to true or false
        fun setIsAddingActivity(value: Boolean){
            this.isAddingActivity = value
        }

        interface OnClickAddItemListener {
            fun onClickItem(position: Int)
        }

        interface OnClickModifyItemListener {
            fun onClickModifyItem(activityName: String)
        }

        fun setOnClickAddItemListener(listener: OnClickAddItemListener) {
            this.addNewActivityListener = listener
        }

        fun setOnClickModifyItemListener(listener: OnClickModifyItemListener){
            this.modifyItemListener = listener
        }

}