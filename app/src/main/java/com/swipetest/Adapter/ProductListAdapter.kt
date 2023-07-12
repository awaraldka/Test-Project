package com.swipetest.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.swipetest.R
import com.swipetest.api.response.Products
import com.swipetest.databinding.ProductListBinding

class ProductListAdapter(private val context:Context, private var data:List<Products>)
    :RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       try{
           data[position].apply {

               if (image.isNotBlank()){
                   Glide.with(context).load(image).into(holder.binding.itemImage)
               }else{

                   if (image.isEmpty() && (product_name == "laptop" || product_name == "macbook" || product_name == "pepsi")) {
                       when (product_name) {
                           "laptop" -> Glide.with(context).load(R.drawable.laptop).into(holder.binding.itemImage)
                           "macbook" -> Glide.with(context).load(R.drawable.mac).into(holder.binding.itemImage)
                           "pepsi" -> Glide.with(context).load( R.drawable.pepsi).into(holder.binding.itemImage)
                       }
                   }else{
                       Glide.with(context).load(R.drawable.dummy).into(holder.binding.itemImage)
                   }
               }


               holder.binding.productName.text =  product_name
               holder.binding.productType.text =  product_type
               holder.binding.tax.text =  "₹${String.format(" % .2f", tax.toDouble())}"
               holder.binding.price.text =   "₹${String.format(" % .2f", price.toDouble())}"




           }

       }catch (e:Exception){
           e.printStackTrace()
       }



    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(val binding: ProductListBinding) :
        RecyclerView.ViewHolder(binding.root)





    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: List<Products>) {
        data = filteredList
        notifyDataSetChanged()

    }


}