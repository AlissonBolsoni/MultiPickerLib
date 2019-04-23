package br.com.alissontfb.multifilepicker.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.alissontfb.multifilepicker.R
import br.com.alissontfb.multifilepicker.config.CheckPermissions
import br.com.alissontfb.multifilepicker.model.FilePickerParams
import br.com.alissontfb.multifilepicker.utils.*

class PermissionsActivity : AppCompatActivity() {

    private lateinit var permission: CheckPermissions
    private lateinit var parameters: FilePickerParams

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions)

        permission = CheckPermissions(this, null)
        val permissions = permission.verifyPermissions(
            PERMISSION_REQUEST_STORAGE,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        )
        permission.necessaryPermissions()

        parameters = FilePickerParams()
        if (intent.hasExtra(INTENT_TO_ACTIVITY_PARAM)) {
            parameters = intent.getSerializableExtra(INTENT_TO_ACTIVITY_PARAM) as FilePickerParams
            if (permissions)
                gotoFilePicker()
        }else finish()
    }

    private fun gotoFilePicker() {
        val intent = Intent(this, FilePickerTabActivity::class.java)
        intent.putExtra(INTENT_TO_ACTIVITY_PARAM, parameters)
        startActivityForResult(intent, REQUEST_CODE_TO_RESULT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (REQUEST_CODE_TO_RESULT == requestCode && Activity.RESULT_OK == resultCode && data != null) {
            val paths = data.getSerializableExtra(PARAM_RESULT_ITEMS_PATHS) as ArrayList<String>
            val intent = Intent()
            intent.putExtra(PARAM_RESULT_ITEMS_PATHS, paths)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }else
            finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (PERMISSION_REQUEST == requestCode) {
            for (result in grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
//                    permission.alertPermissionValidation()
                }
            }
        } else if(PERMISSION_REQUEST_STORAGE == requestCode){
            for (result in grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    permission.alertPermissionValidation()
                    break
                }
            }
            gotoFilePicker()
        }
    }
}
