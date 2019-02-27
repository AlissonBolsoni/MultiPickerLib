package br.com.alissontfb.multifilepicker.ui.fragment

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.alissontfb.multifilepicker.R
import br.com.alissontfb.multifilepicker.model.FilePickerParams
import br.com.alissontfb.multifilepicker.model.ObFileView
import br.com.alissontfb.multifilepicker.ui.adapter.BreadcrumbListAdapter
import br.com.alissontfb.multifilepicker.ui.adapter.FileListAdapter
import br.com.alissontfb.multifilepicker.ui.delegate.FilePickerItemsDelegate
import br.com.alissontfb.multifilepicker.utils.FindFiles
import kotlinx.android.synthetic.main.fragment_list_layout.*
import java.io.File

abstract class FilePickerFragment(val type: Int) : Fragment() {

    companion object {
        internal const val FILE_TYPE = 10
        internal const val IMAGE_TYPE = 20
        internal const val AUDIO_TYPE = 30
        internal const val VIDEO_TYPE = 40
    }

    internal lateinit var obFileView: ObFileView
    internal var adapter: FileListAdapter? = null
    internal lateinit var delegate: FilePickerItemsDelegate
    internal lateinit var params: FilePickerParams
    internal lateinit var root: File
    private val home = Environment.getExternalStorageDirectory()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        root = home
        delegate = activity!! as FilePickerItemsDelegate
        params = delegate.getParams()

        openDirectory()
        setAdapter()
    }

    abstract fun setAdapter()

    abstract fun initAdapter(): ObFileView

    private fun openDirectory() {
        obFileView = initAdapter()
        createAdapterFiles()

        if (type == FILE_TYPE)
            createAdapterBreadcrumb()
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

    private fun createAdapterFiles() {
        this.adapter = FileListAdapter(activity!!, obFileView, params.max, delegate.getSelectedItems(), {
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

    fun getAdapter() = adapter
}