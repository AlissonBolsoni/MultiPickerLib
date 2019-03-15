package br.com.alissontfb.multifilepicker.loader

import android.content.AsyncTaskLoader
import android.content.Context
import br.com.alissontfb.multifilepicker.model.ObFileView
import br.com.alissontfb.multifilepicker.utils.FilesCache
import java.io.File

abstract class BaseViewLoader(context: Context) :
        AsyncTaskLoader<ObFileView>(context) {

    internal var obFileView: ObFileView? = null

    override fun onStartLoading() {
        if (obFileView != null) {
            deliverResult(obFileView)
        }

        if (obFileView == null || takeContentChanged()) {
            forceLoad()
        }
    }


    override fun deliverResult(response: ObFileView?) {

        if (isReset) {
            return
        }

        obFileView = response

        if (isStarted) {
            super.deliverResult(obFileView)
        }
    }

    override fun onReset() {
        onStopLoading()

        if (obFileView != null) {
            obFileView = null
        }
    }

}