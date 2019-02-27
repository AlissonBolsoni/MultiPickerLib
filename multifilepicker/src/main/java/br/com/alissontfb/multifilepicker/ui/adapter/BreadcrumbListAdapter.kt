package br.com.alissontfb.multifilepicker.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import br.com.alissontfb.multifilepicker.R
import br.com.alissontfb.multifilepicker.utils.UtilsFile
import java.io.File

class BreadcrumbListAdapter(private val context: Context,
                            private val list: ArrayList<File>,
                            private val open: (File) -> Unit) : RecyclerView.Adapter<BreadcrumbListAdapter.ItemHolder>() {


    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.bread_image)
        val path: TextView = itemView.findViewById(R.id.bread_directory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ItemHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_bread_crumb, parent, false)
        return ItemHolder(view)
    }

    override fun getItemCount() = list.size

    private fun getItem(position: Int) = list[position]

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val file = getItem(position)
        holder.path.text = UtilsFile.getFileName(file.path)

        if (position > 0)
            holder.image.visibility = View.GONE
        else{
            holder.image.visibility = View.VISIBLE
            holder.path.text = context.getString(R.string.home)
        }

        holder.itemView.setOnClickListener {
            open(file)
        }

    }
}