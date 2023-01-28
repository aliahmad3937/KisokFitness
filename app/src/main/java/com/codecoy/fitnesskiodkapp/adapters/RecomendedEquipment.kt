package com.codecoy.fitnesskiodkapp.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codecoy.fitnesskiodkapp.MyApp
import com.codecoy.fitnesskiodkapp.callbacks.ReplaceFragment

import com.codecoy.fitnesskiodkapp.databinding.ItemRecomendBinding
import com.codecoy.fitnesskiodkapp.models.Data
import com.codecoy.fitnesskiodkapp.utils.Constants.BASE_URL
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation

class RecomendedEquipment(
    var list: ArrayList<Data>, val context: Context  , val replaceFragment: ReplaceFragment
) : RecyclerView.Adapter<RecomendedEquipment.ViewHolder>() {

    // create an inner class with name ViewHolder
    // It takes a view argument, in which pass the generated class of single_item.xml
    // ie SingleItemBinding and in the RecyclerView.ViewHolder(binding.root) pass it like this
    inner class ViewHolder(val binding: ItemRecomendBinding) : RecyclerView.ViewHolder(binding.root)

    // inside the onCreateViewHolder inflate the view of SingleItemBinding
    // and return new ViewHolder object containing this layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecomendBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    val transformation: Transformation
    init {
        transformation = RoundedTransformationBuilder()
            .borderColor(Color.BLACK)
            .borderWidthDp(0f)
            .cornerRadiusDp(6f)
            .oval(false)
            .build()
    }



    // bind the items with each item
    // of the list languageList
    // which than will be
    // shown in recycler view
    // to keep it simple we are
    // not setting any image data to view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            with(list[position]) {

                holder.binding.title!!.text  =  this.clasName


                Picasso.get()
                    .load("${BASE_URL}${this.clasImg}".trim())
                    .fit()
                    .transform(transformation)
                    .into(holder.binding.image)

                holder.itemView.setOnClickListener{
                    replaceFragment.showClassDisplayFragment(
                      this
                    )
                }
            }

    }



    // return the size of languageList
    override fun getItemCount(): Int {
        return list.size
    }
}