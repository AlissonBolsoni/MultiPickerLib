package br.com.alissontfb.multifilepicker.ui.fragment

import android.support.v7.widget.GridLayoutManager
import br.com.alissontfb.multifilepicker.R
import br.com.alissontfb.multifilepicker.model.ObFileView
import br.com.alissontfb.multifilepicker.ui.adapter.BreadcrumbListAdapter
import br.com.alissontfb.multifilepicker.ui.adapter.filesAdpter.FileListAdapter
import br.com.alissontfb.multifilepicker.utils.FilesCache
import br.com.alissontfb.multifilepicker.utils.FindFiles
import kotlinx.android.synthetic.main.fragment_list_layout.*
import java.io.File


class FilesFragment : FilePickerFragment(FilePickerFragment.FILE_TYPE) {

    override fun initLoader(): ObFileView {
        return FilesCache.getFiles(root)
    }

    override fun setAdapter() {
        delegate.setAdapter(adapter!!, FindFiles.FILES)
    }

    override fun openDirectory() {
        obFileView = initLoader()
        createAdapterFiles()
        setAdapter()
        createAdapterBreadcrumb()
    }

    override fun createAdapterFiles() {
        this.adapter = FileListAdapter(obFileView, params.max, delegate.getSelectedItems(), {
            if (it.checked)
                delegate.addItemsSelected(it.item)
            else
                delegate.removeItemsSelected(it.item)
        }, {
            root = it
            val selectedFiles = adapter!!.getSelectedFiles()
            openDirectory()
            adapter!!.setSelectedFiles(selectedFiles)
        })
        pick_list_recycler.adapter = this.adapter
        val layoutManager = GridLayoutManager(context!!, context!!.resources.getInteger(R.integer.quantity_of_recycler_spam))
        pick_list_recycler.layoutManager = layoutManager
    }

    private fun createAdapterBreadcrumb() {
        val list = makeBreadcrumbList(root)
        val adapter = BreadcrumbListAdapter(context!!, list) {
            root = it
            val selectedFiles = adapter!!.getSelectedFiles()
            openDirectory()
            adapter!!.setSelectedFiles(selectedFiles)
        }
        pick_list_breadcrumb.adapter = adapter
    }

    private fun makeBreadcrumbList(file: File, list: ArrayList<File> = ArrayList()): ArrayList<File> {
        if (file.parent == home.parent) {
            list.add(file)
            val temp = ArrayList<File>()
            for (f in list.asReversed())
                temp.add(f)
            return temp
        }

        list.add(file)
        return makeBreadcrumbList(file.parentFile, list)
    }
}