package br.com.alissontfb.multifilepicker.ui.fragment

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
import br.com.alissontfb.multifilepicker.ui.adapter.filesAdpter.BaseListAdapter
import br.com.alissontfb.multifilepicker.ui.adapter.filesAdpter.FileListAdapter
import br.com.alissontfb.multifilepicker.ui.delegate.FilePickerItemsDelegate
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
    internal var adapter: BaseListAdapter? = null
    internal lateinit var delegate: FilePickerItemsDelegate
    internal lateinit var params: FilePickerParams
    internal lateinit var root: File
    internal val home = Environment.getExternalStorageDirectory()

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
    }

    abstract fun setAdapter()

    abstract fun initLoader(): ObFileView

    abstract fun openDirectory()

    abstract fun createAdapterFiles()
}