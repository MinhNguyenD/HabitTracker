package com.example.csci4176_pmgroupproject

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Habit_Categories_Adapter(private var dataSet: MutableList<String>) :
    RecyclerView.Adapter<Habit_Categories_Adapter.ViewHolder>() {
    private var itemOnClickListener: OnClickItemListener? = null
    private var addNewCategoryListener: OnClickSaveItemListener? = null

    private var newItemPosition: Int? = null
    private var isAddingCategory = false
    private var newCategoryName = ""

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val editText: EditText
        val saveBtn: Button

        init {
            // Define click listener for the ViewHolder's View
            textView = view.findViewById(R.id.textView_habit_item_recyclerview_item)
            editText = view.findViewById(R.id.editText_habit_item_recyclerview_item)
            saveBtn = view.findViewById(R.id.saveBtn_habit_item_recyclerview_item)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.habit_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = dataSet[position]

        // In "Adding category" mode, the EditText is available for the user to enter text
        if(isAddingCategory && position == newItemPosition){
            Log.d(ContentValues.TAG, "we're innn we are $dataSet")
            viewHolder.textView.visibility = View.GONE
            viewHolder.editText.visibility = View.VISIBLE
            viewHolder.saveBtn.visibility = View.VISIBLE
        }
        // Outside of the "Adding category" mode, just display all items
        else{
            viewHolder.editText.visibility = View.GONE
            viewHolder.saveBtn.visibility = View.GONE
            viewHolder.textView.visibility = View.VISIBLE
        }

        // Set a listener on each item and save their position
        viewHolder.itemView.setOnClickListener {
            if (itemOnClickListener != null) {
                itemOnClickListener!!.onClickItem(position)
            }
        }

        // Set a listener on the Save button to save the new category name entered by the user
        viewHolder.saveBtn.setOnClickListener{
            if(addNewCategoryListener != null){
                // Get the text from EditText
                newCategoryName = viewHolder.editText.text.toString()

                // Pass the new category name through the OnClickListener
                addNewCategoryListener!!.onClickSaveItem(newCategoryName)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    fun getIsAddingCategory(): Boolean {
        return this.isAddingCategory
    }

    // Set the "Adding category" mode to true or false
    fun setIsAddingCategory(value: Boolean){
        this.isAddingCategory = value
    }

    // Set the position for the new item
    fun setNewItemPosition(value: Int){
        this.newItemPosition = value
    }

    interface OnClickItemListener {
        fun onClickItem(position: Int)
    }

    interface OnClickSaveItemListener {
        fun onClickSaveItem(newCategoryName: String)
    }

    fun setOnClickItemListener(listener: OnClickItemListener) {
        this.itemOnClickListener = listener
    }

    fun setOnClickSaveItemListener(listener: OnClickSaveItemListener){
        this.addNewCategoryListener = listener
    }
}