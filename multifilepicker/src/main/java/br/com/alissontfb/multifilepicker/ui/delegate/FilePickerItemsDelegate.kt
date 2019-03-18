package br.com.alissontfb.multifilepicker.ui.delegate

import br.com.alissontfb.multifilepicker.model.FilePickerParams
import br.com.alissontfb.multifilepicker.ui.adapter.filesAdpter.BaseListAdapter
import java.io.File

interface FilePickerItemsDelegate {

    fun updateQuantity(min: Int, max: Int)
    fun getParams(): FilePickerParams
    fun setUpMenus(type: Int)
    fun addItemsSelected(item: File)
    fun removeItemsSelected(item: File)
    fun getSelectedItems():HashMap<String, String>
    fun setAdapter(adapter: BaseListAdapter, key: Int)
    fun setMicOf()

}