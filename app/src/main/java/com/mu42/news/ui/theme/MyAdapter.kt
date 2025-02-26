package com.mu42.news.ui.theme

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mu42.news.R

class MyAdapter( val listener:Ionclicking):RecyclerView.Adapter<MyViewHolderClass>() {

   private val newsItems : ArrayList<DataNewsClass> = ArrayList<DataNewsClass>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderClass {

        var viewInflate=LayoutInflater.from(parent.context).inflate(R.layout.list_items,parent,false)
        val viewHolderObj=MyViewHolderClass(viewInflate)
       viewInflate.setOnClickListener{
           listener.onClickitem(newsItems[viewHolderObj.bindingAdapterPosition])
       }
        return viewHolderObj
    }

    override fun getItemCount(): Int {
     return  newsItems.size
    }

    override fun onBindViewHolder(holder: MyViewHolderClass, position: Int) {

        if(newsItems.isNotEmpty()) {
            val currentItem = newsItems[position]

            holder.nTitle.text = currentItem.Title
            holder.nAuther.text = currentItem.Author
            Glide.with(holder.itemView.context).load(currentItem.image).into(holder.Nimage)

        }

        else {
            Log.d("binding", " newsItems is empty")
        }



    }

    fun updateData(updatedData:ArrayList<DataNewsClass>)
    {
        newsItems.clear()
        newsItems.addAll(updatedData)

        notifyDataSetChanged()
    }
}

class MyViewHolderClass(viewVar : View) : RecyclerView.ViewHolder(viewVar)
{
  val nTitle:TextView=viewVar.findViewById(R.id.NewsTitle)
  val nAuther:TextView=viewVar.findViewById(R.id.Nauther)
    val Nimage:ImageView=viewVar.findViewById(R.id.Nimage) as ImageView
}

interface  Ionclicking {
    fun onClickitem(itemI:DataNewsClass)

}