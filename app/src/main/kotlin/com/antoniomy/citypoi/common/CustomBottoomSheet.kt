package com.antoniomy.citypoi.common

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.antoniomy.citypoi.R
import com.antoniomy.citypoi.databinding.PopUpErrorWarningBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


class CustomBottomSheet(context: Context) {

    private var popUpErrorWarningBinding: PopUpErrorWarningBinding
    private var view: View
    private var dialog: CustomSheet

    init {
        dialog = CustomSheet(context)
        view = (context as AppCompatActivity).layoutInflater.inflate(
            R.layout.pop_up_error_warning,
            null
        )
        popUpErrorWarningBinding = DataBindingUtil.inflate(
            context.layoutInflater,
            R.layout.pop_up_error_warning,
            view as ViewGroup,
            false
        )
        popUpErrorWarningBinding.root.rootView?.let { dialog.setContentView(it) }
    }

    fun setValues(title: String = "", message: String = "", imageResource: Int? = null) {
        imageResource?.let { popUpErrorWarningBinding.warning.setBackgroundResource(it) }
        popUpErrorWarningBinding.title.text = title
        popUpErrorWarningBinding.message.text = message
    }

    fun acceptBtn() = popUpErrorWarningBinding.accept
    fun cancelBtn() = popUpErrorWarningBinding.cancel
    fun closeBottomSheet() = dialog.dismiss()
    fun showBottomSheet() = dialog.show()

    class CustomSheet(context: Context) : BottomSheetDialog(context, R.style.NoBackgroundDialogTheme)
}