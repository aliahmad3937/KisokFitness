package com.codecoy.fitnesskiodkapp.adapters


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.codecoy.fitnesskiodkapp.callbacks.ReplaceFragment
import com.codecoy.fitnesskiodkapp.databinding.ItemEquipmentBinding
import com.codecoy.fitnesskiodkapp.models.Data
import com.codecoy.fitnesskiodkapp.utils.Constants
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation


class EquipmentAdapter(
    var list: ArrayList<Data>, val context: Context  , val replaceFragment: ReplaceFragment
) : RecyclerView.Adapter<EquipmentAdapter.ViewHolder>() {

    // create an inner class with name ViewHolder
    // It takes a view argument, in which pass the generated class of single_item.xml
    // ie SingleItemBinding and in the RecyclerView.ViewHolder(binding.root) pass it like this
    inner class ViewHolder(val binding: ItemEquipmentBinding) : RecyclerView.ViewHolder(binding.root)

    val transformation: Transformation
    init {
        transformation = RoundedTransformationBuilder()
            .borderColor(Color.BLACK)
            .borderWidthDp(0f)
            .cornerRadiusDp(6f)
            .oval(false)
            .build()
    }

    // inside the onCreateViewHolder inflate the view of SingleItemBinding
    // and return new ViewHolder object containing this layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEquipmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }



    // bind the items with each item
    // of the list languageList
    // which than will be
    // shown in recycler view
    // to keep it simple we are
    // not setting any image data to view
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            with(list[position]) {
                holder.binding.title.text = this.eqpName


                Picasso.get()
                    .load("${Constants.BASE_URL}${this.eqpImg}".trim())
                    .fit()
                    .transform(transformation)
                    .into(holder.binding.image)


                Log.v("Tag", "$this")

                holder.itemView.setOnClickListener{
                    replaceFragment.showEquipmentDisplayFragment(
                        id = this.id!!,
                        clasVideoPath = this.clasVideoPath,
                        eqpVideoPath = this.eqpVideoPath,
                        name = this.eqpName!!,
                        desc = this.eqpDesc!!
                    )
                }
            }

    }



    // return the size of languageList
    override fun getItemCount(): Int {
        return list.size
    }
}