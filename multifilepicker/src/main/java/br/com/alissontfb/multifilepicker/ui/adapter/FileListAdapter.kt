package br.com.alissontfb.multifilepicker.ui.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import br.com.alissontfb.multifilepicker.R
import br.com.alissontfb.multifilepicker.model.ObFileItemView
import br.com.alissontfb.multifilepicker.model.ObFileView
import br.com.alissontfb.multifilepicker.utils.UtilsFile
import java.io.File

class FileListAdapter(private val context: Context,
                      private val obFileView: ObFileView,
                      private val maxSelect: Int,
                      private val selectedFiles: HashMap<String, String>,
                      private val onClick: (ObFileItemView) -> Unit,
                      private val open: (File) -> Unit) : RecyclerView.Adapter<FileListAdapter.ItemHolder>() {

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.item_picker_image)
        val path: TextView = itemView.findViewById(R.id.item_picker_path)
        val checked: ImageView = itemView.findViewById(R.id.item_picker_image_select)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ItemHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_file_picker, parent, false)
        return ItemHolder(view)
    }

    fun putFile(pic: File){
        val file = ObFileItemView(pic)
        file.checked = true
        selectedFiles[file.item.path] = file.item.path
        obFileView.itemsInPath.add(0, file)
        notifyDataSetChanged()
    }

    override fun getItemCount() = obFileView.itemsInPath.size

    private fun getItem(position: Int) = obFileView.itemsInPath[position]

    fun getSelectedFiles() = selectedFiles

    fun setSelectedFiles(selectedFiles: HashMap<String, String>) {
        this.selectedFiles.clear()
        this.selectedFiles.putAll(selectedFiles)
    }

    private fun setChecked(item: ObFileItemView) {
        if (item.checked) {
            selectedFiles.getOrPut(item.item.path) {
                item.item.path
            }
        }
        else
            selectedFiles.remove(item.item.path)
    }

    private fun setTypeItem(holder: ItemHolder, file: ObFileItemView) {
        if (file.item.isDirectory) {
            holder.image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_folder))
            holder.image.setOnClickListener {
                open(file.item)
            }
        } else {
            setFileIcon(holder, file)
            holder.image.setOnClickListener {
                file.checked = !file.checked
                if (selectedFiles.values.size < maxSelect) {
                    setChecked(file)
                    markAsSelected(file, holder.checked)
                    onClick(file)
                }
            }
        }
    }

    private fun setFileIcon(holder: ItemHolder, file: ObFileItemView) {
        UtilsFile.setImageFromExtension(context, file.item.path, holder.image)
    }

    private fun markAsSelected(item: ObFileItemView, imageView: ImageView) {
        if (item.checked || isAlreadyChecked(item))
            imageView.visibility = View.VISIBLE
        else
            imageView.visibility = View.GONE
    }

    private fun isAlreadyChecked(item: ObFileItemView) = selectedFiles[item.item.path] != null

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val file = getItem(position)

        markAsSelected(file, holder.checked)

        setTypeItem(holder, file)
        holder.path.text = UtilsFile.getFileName(file.item.path)
    }

}