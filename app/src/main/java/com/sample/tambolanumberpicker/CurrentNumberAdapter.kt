package com.sample.tambolanumberpicker

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CurrentNumberAdapter : RecyclerView.Adapter<TextViewHolder>() {
    private val list = arrayListOf<String>()

    var previousHolder: TextViewHolder? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.current_number_item, parent, false)
        return TextViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        if (position == itemCount - 1) {
            ObjectAnimator.ofFloat(previousHolder?.currentNumber, "textSize", 100f, 50f).apply {
                duration = 1000
                start()
            }
//            previousHolder?.currentNumber?.textSize = 60f
            previousHolder = holder
            holder.currentNumber.textSize = 100f
        }
        holder.currentNumber.text = list[position]
    }

    fun addNumber(number: String) {
        list.add(number)
        notifyItemInserted(list.size - 1)
    }

    fun reset() {
        list.clear()
        previousHolder = null
        notifyDataSetChanged()
    }
}

class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val currentNumber: TextView = view.findViewById(R.id.currentNumber)
}