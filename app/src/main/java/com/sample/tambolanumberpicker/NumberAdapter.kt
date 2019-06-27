package com.sample.tambolanumberpicker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.sample.tambolanumberpicker.databinding.NumberItemBinding

class NumberAdapter(private val tambolaNumberList: List<TambolaNumber>) : Adapter<NumberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        val itemBinding: NumberItemBinding = DataBindingUtil
            .inflate(
                LayoutInflater.from(parent.context),
                R.layout.number_item,
                parent,
                false
            )
        return NumberViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return tambolaNumberList.size
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        holder.setData(tambolaNumberList[position])
    }

    fun remainingNumbers(): List<TambolaNumber> {
        return tambolaNumberList.filter {
            !it.isCalled
        }
    }

    fun setCalledFor(number: String) {
        tambolaNumberList.forEachIndexed { index, tambolaNumber ->
            if (tambolaNumber.number == number) {
                tambolaNumber.isCalled = true
                notifyItemChanged(index)
            }
        }
    }

    fun resetNumbers() {
        tambolaNumberList.forEach {
            it.isCalled = false
        }
        notifyDataSetChanged()
    }
}

fun List<TambolaNumber>.contains(number: String): Boolean {
    forEach {
        if (it.number == number) return true
    }
    return false
}

class NumberViewHolder(private val itemBinding: NumberItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
    fun setData(number: TambolaNumber) {
        itemBinding.tambolaNumber = number
    }
}

@BindingAdapter("isCalled")
fun setIsCalled(view: TextView, isCalled: Boolean) {
    view.isSelected = isCalled
}