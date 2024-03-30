package com.example.csci4176_pmgroupproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.csci4176_pmgroupproject.Model.ActivityModel

class ActivityAdapter(private var dataSet: ArrayList<ActivityModel>) :
RecyclerView.Adapter<ActivityAdapter.ViewHolder>() {
        private var modifyItemListener: OnClickModifyItemListener? = null

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder)
         */
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView
            val  modifyBtn: Button

            init {
                textView = view.findViewById(R.id.textView_activity_item_recyclerview_item)
                modifyBtn = view.findViewById(R.id.modifyBtn_activity_item_recyclerview_item)
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
            for(activity in dataSet){
                // TODO: Extract the titles of activities and display them here in textView.text
                viewHolder.textView.text = "Blending smoothie"
            }

            // Set a listener on the Modify button to modify the activity
            viewHolder.modifyBtn.setOnClickListener{
                if(modifyItemListener != null){
                    modifyItemListener!!.onClickModifyItem(true)
                }
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size

        interface OnClickModifyItemListener {
            fun onClickModifyItem(isClicked: Boolean)
        }

        fun setOnClickModifyItemListener(listener: OnClickModifyItemListener){
            this.modifyItemListener = listener
        }

}