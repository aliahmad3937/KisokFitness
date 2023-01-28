package com.codecoy.fitnesskiodkapp.adapters


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.codecoy.fitnesskiodkapp.MyApp
import com.codecoy.fitnesskiodkapp.R
import com.codecoy.fitnesskiodkapp.callbacks.UpdateClasses
import com.codecoy.fitnesskiodkapp.databinding.ItemClassCategoryBinding
import com.codecoy.fitnesskiodkapp.models.Data
import com.codecoy.fitnesskiodkapp.utils.Constants.BASE_URL
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_class_category.view.*


class ClassCategoryAdapter(
    var list: ArrayList<Data>, val context: Context, val updateClasses: UpdateClasses
) : RecyclerView.Adapter<ClassCategoryAdapter.ViewHolder>() {

    // create an inner class with name ViewHolder
    // It takes a view argument, in which pass the generated class of single_item.xml
    // ie SingleItemBinding and in the RecyclerView.ViewHolder(binding.root) pass it like this
    inner class ViewHolder(val binding: ItemClassCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)


    // inside the onCreateViewHolder inflate the view of SingleItemBinding
    // and return new ViewHolder object containing this layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemClassCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    // bind the items with each item
    // of the list languageList
    // which than will be
    // shown in recycler view
    // to keep it simple we are
    // not setting any image data to view
    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(list[position]) {

            holder.binding.tv1.text = this.category_title

            if (this.id == 0) {
                holder.binding.image!!.setImageResource(R.drawable.all)

            } else {
                Picasso.get()
                    .load("${BASE_URL}${this.icon_img}".trim())
                    .fit()
                    .into(holder.binding.image)
            }


            // // White Tint
            //   holder.binding.image!!.setColorFilter(Color.argb(255, 255, 255, 255));


            // if you want color tint then
            //    holder.binding.image!!.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

           //For Vector Drawable


            if(MyApp.selectedCategory == this.id){
                holder.binding.image!!.setColorFilter(
                    ContextCompat.getColor(context, R.color.selec),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                holder.binding.tv1.setTextColor(context.getColor(R.color.selec))
            }else{
                holder.binding.image!!.setColorFilter(
                    ContextCompat.getColor(context, R.color.un_selec),
                    android.graphics.PorterDuff.Mode.SRC_IN
                );
                holder.binding.tv1.setTextColor(context.getColor(R.color.un_selec))
            }


            holder.itemView.setOnClickListener {
                updateClasses.refreshCategory(this.id!!)
            }


        }
    }


    // return the size of languageList
    override fun getItemCount(): Int {
        return list.size
    }
}