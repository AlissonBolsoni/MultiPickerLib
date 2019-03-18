package br.com.alissontfb.multifilepicker.ui.fragment

import android.support.v7.widget.GridLayoutManager
import br.com.alissontfb.multifilepicker.R
import br.com.alissontfb.multifilepicker.model.ObFileView
import br.com.alissontfb.multifilepicker.ui.adapter.filesAdpter.AudioListAdapter
import br.com.alissontfb.multifilepicker.ui.adapter.filesAdpter.VideoListAdapter
import br.com.alissontfb.multifilepicker.utils.FilesCache
import br.com.alissontfb.multifilepicker.utils.FindFiles
import kotlinx.android.synthetic.main.fragment_list_layout.*

class VideoFragment: FilePickerFragment(FilePickerFragment.VIDEO_TYPE) {

    override fun initLoader(): ObFileView {
        return FilesCache.getVideos()
    }

    override fun setAdapter() {
        delegate.setAdapter(adapter!!, FindFiles.VIDEO)
    }

    override fun openDirectory() {
        obFileView = initLoader()
        createAdapterFiles()
        setAdapter()
    }

    override fun createAdapterFiles() {
        this.adapter = VideoListAdapter(obFileView, params.max, delegate.getSelectedItems(), {
            if (it.checked)
                delegate.addItemsSelected(it.item)
            else
                delegate.removeItemsSelected(it.item)
        }, {
            root = it
        })
        pick_list_recycler.adapter = this.adapter
        val layoutManager = GridLayoutManager(context!!, context!!.resources.getInteger(R.integer.quantity_of_recycler_spam))
        pick_list_recycler.layoutManager = layoutManager
    }
}