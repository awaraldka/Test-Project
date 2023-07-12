package com.swipetest.extension

import android.app.AlertDialog
import android.content.Context
import com.swipetest.R
import com.swipetest.interfaces.FinishActivity

object androidExtension {

    fun alertBox(message: String, context: Context) {
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setTitle("Swipe Test")
        builder.setMessage(message)

        builder.setPositiveButton("ok") { dialogInterface, which ->
            alertDialog!!.dismiss()
        }
        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog.show()
    }

    fun alertBoxFinish(message: String, context: Context,click:FinishActivity) {
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setTitle("Swipe Test")
        builder.setMessage(message)

        builder.setPositiveButton("ok") { dialogInterface, which ->
            click.finishActivity()
            alertDialog!!.dismiss()
        }
        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog.show()
    }

}