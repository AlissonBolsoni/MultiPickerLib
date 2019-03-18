package br.com.alissontfb.multifilepicker.ui.adapter.filesAdpter

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

class FileListAdapter(obFileView: ObFileView,
                      maxSelect: Int,
                      selectedFiles: HashMap<String, String>,
                      onClick: (ObFileItemView) -> Unit,
                      open: (File) -> Unit) : BaseListAdapter(obFileView, maxSelect, selectedFiles, onClick, open) {


    override fun setTypeItem(holder: BaseListAdapter.ItemHolder, file: ObFileItemView) {
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

}