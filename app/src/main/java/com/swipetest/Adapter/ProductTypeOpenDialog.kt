package com.swipetest.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swipetest.databinding.ListsBinding
import com.swipetest.interfaces.PopupItemClickListener
import kotlin.Exception


class ProductTypeOpenDialog(
    var context: Context,
    var data: List<String>,
    var click: PopupItemClickListener
) :
    RecyclerView.Adapter<ProductTypeOpenDialog.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            data[position].apply {
                holder.binding.contentTextView.text =  this


                holder.binding.click.setOnClickListener {
                    click.getProductTypeData(this)
                }



            }
        }catch (e:Exception){
            e.printStackTrace()
        }





    }

    override fun getItemCount(): Int {
        return data.size
    }


    inner class ViewHolder(val binding: ListsBinding) : RecyclerView.ViewHolder(binding.root)


    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: List<String>) {
        data = filteredList
        notifyDataSetChanged()

    }


}