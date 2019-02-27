package br.com.alissontfb.multifilepicker.config

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import br.com.alissontfb.multifilepicker.R
import br.com.alissontfb.multifilepicker.ui.delegate.FilePickerItemsDelegate
import java.util.ArrayList

class CheckPermissions(private val activity: Activity, private val callback: FilePickerItemsDelegate? = null) {

    fun necessaryPermissions() =
            arrayOf(Manifest.permission.RECORD_AUDIO)

    fun verifyPermissions(requestCode: Int, permissions: Array<String>): Boolean {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissionsList = ArrayList<String>()
            for (permission in permissions) {
                val permissionsValidated = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
                if (!permissionsValidated) permissionsList.add(permission)
            }

            if (permissionsList.isEmpty()) return true

            val permissionsArray = arrayOfNulls<String>(permissionsList.size)
            permissionsList.forEachIndexed { index, s ->

                permissionsArray[index] = s

            }


            ActivityCompat.requestPermissions(activity, permissionsArray, requestCode)
        }

        return true
    }

    fun alertPermissionValidation() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(activity.getString(R.string.app_name))
        builder.setMessage("É necessário aceitar as permissões")
        builder.setPositiveButton(activity.getString(android.R.string.ok)) { _, _ ->
            callback?.setMicOf()
        }

        val dialog = builder.create()
        dialog.show()
    }

}