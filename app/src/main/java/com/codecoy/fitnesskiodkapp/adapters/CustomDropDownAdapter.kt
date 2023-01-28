package com.codecoy.fitnesskiodkapp.adapters

import android.content.Context

import android.view.LayoutInflater

import android.view.View

import android.view.ViewGroup

import android.widget.BaseAdapter

import android.widget.ImageView

import android.widget.TextView
import com.codecoy.fitnesskiodkapp.MyApp
import com.codecoy.fitnesskiodkapp.R
import com.codecoy.fitnesskiodkapp.models.Data
import com.codecoy.fitnesskiodkapp.models.EquipmentResponse


class CustomDropDownAdapter(val context: Context, var dataSource: ArrayList<Data>) : BaseAdapter() {



    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater



    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {



        val view: View

        val vh: ItemHolder

        if (convertView == null) {

            view = inflater.inflate(R.layout.custom_spinner_item, parent, false)

            vh = ItemHolder(view)

            view?.tag = vh

        } else {

            view = convertView

            vh = view.tag as ItemHolder

        }
        MyApp.categoryType = dataSource.get(position).category_title!!
        vh.label.text = MyApp.categoryType

//        val id = context.resources.getIdentifier(dataSource.get(position).url, "drawable", context.packageName)
//
//        vh.img.setBackgroundResource(id)



        return view

    }



    override fun getItem(position: Int): Any? {

        return dataSource[position];

    }



    override fun getCount(): Int {

        return dataSource.size;

    }



    override fun getItemId(position: Int): Long {

        return position.toLong();

    }



    private class ItemHolder(row: View?) {

        val label: TextView

        init {

            label = row?.findViewById(R.id.text) as TextView


        }

    }



}