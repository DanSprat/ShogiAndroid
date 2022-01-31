package ru.popov.shogi.classes

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.popov.shogi.R

class TestAdapter(var arrayList: ArrayList<String>):
    RecyclerView.Adapter<TestAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text1:TextView = itemView.findViewById(R.id.text1)
        var text2:TextView = itemView.findViewById(R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v:View = LayoutInflater.from(parent.context).inflate(R.layout.test,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text1.text = "text1"
        holder.text2.text = "text2"
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


}