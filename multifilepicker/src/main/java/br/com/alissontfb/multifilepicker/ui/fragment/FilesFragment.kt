package br.com.alissontfb.multifilepicker.ui.fragment

import br.com.alissontfb.multifilepicker.model.ObFileView
import br.com.alissontfb.multifilepicker.utils.FilesCache
import br.com.alissontfb.multifilepicker.utils.FindFiles


class FilesFragment: FilePickerFragment(FilePickerFragment.FILE_TYPE) {

    override fun initAdapter(): ObFileView {
        return FilesCache.getFiles(root)
    }

    override fun setAdapter() {
        delegate.setAdapter(adapter!!, FindFiles.FILES)
    }
}