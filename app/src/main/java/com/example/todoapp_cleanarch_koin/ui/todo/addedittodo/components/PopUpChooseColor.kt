package com.example.todoapp_cleanarch_koin.ui.todo.addedittodo.components

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import com.example.todoapp_cleanarch_koin.R
import com.example.todoapp_cleanarch_koin.databinding.DialogChooseColorTagBinding
import kotlin.math.ceil

class PopupChooseColor private constructor(
    private val currentColor: Int?,
    private val anchorView: View,
    private val binding: DialogChooseColorTagBinding,
    private val widthPopup: Int,
    private val heightPopup: Int,
) : PopupWindow(binding.root, widthPopup, heightPopup) {

    private constructor(builder: Builder) : this(
        builder.currentColor,
        builder.anchorView,
        builder.bindingDialogFragment,
        builder.widthPopup,
        builder.heightPopup,
    )

    class Builder(val context: Context) {
        private val itemColorSize by lazy {
            ceil(
                context.resources.getDimension(R.dimen.radio_btn_color_size) + context.resources.getDimension(
                    R.dimen.radio_btn_color_margin
                ) * 2
            ).toInt()
        }

        val bindingDialogFragment by lazy {
            DialogChooseColorTagBinding.inflate(
                LayoutInflater.from(
                    context
                )
            )
        }

        var currentColor: Int? = null
        lateinit var anchorView: View
        val widthPopup: Int by lazy { calculatePopUpChooseColorWeight() }
        val heightPopup: Int by lazy { calculatePopUpChooseColorHeight(widthPopup) }

        fun build(): PopupChooseColor {
            if (this::anchorView.isInitialized.not()) {
                throw IllegalArgumentException("anchorView must not be null")
            } else return PopupChooseColor(this)
        }

        fun setAnchorView(anchorView: View): Builder {
            this.anchorView = anchorView
            return this
        }

        fun setCurrentColor(currentColor: Int?): Builder {
            this.currentColor = currentColor
            Log.i("PopUpChooseColor", "setCurrentColor: $currentColor")
            bindingDialogFragment.apply {
                rbColorBlack.isChecked = currentColor == R.color.black
                rbColorBrown.isChecked = currentColor == R.color.brown
                rbColorBlue.isChecked = currentColor == R.color.blue
                rbColorGreen.isChecked = currentColor == R.color.green
                rbColorGrey.isChecked = currentColor == R.color.grey
                rbColorOrange.isChecked = currentColor == R.color.orange
//                rbColorPink.isChecked = currentColor == R.color.pink
                rbColorPurple.isChecked = currentColor == R.color.purple
                rbColorRed.isChecked = currentColor == R.color.red
                rbColorYellow.isChecked = currentColor == R.color.yellow


            }
            return this
        }

        private fun calculatePopUpChooseColorHeight(widthPopUp: Int): Int {
            // convert dp to px
            return (itemColorSize * ceil((9.0 / (widthPopUp / itemColorSize))).toInt())
        }

        private fun calculatePopUpChooseColorWeight(): Int {
            // get height and width of screen
            val displayMetrics = context.resources.displayMetrics
            if ((displayMetrics.widthPixels / 2) < itemColorSize * 9) return ((displayMetrics.widthPixels / 2) - (displayMetrics.widthPixels / 2) % itemColorSize)
            return itemColorSize * 9
        }
    }

    fun setOnColorSelectedListener(action: (Int?) -> Unit): PopupChooseColor {
        binding.apply {
            rbColorBlack.setOnClickListener {
                if (rbColorBlack.isChecked) action(R.color.black)
                else action(null)
                dismiss()

            }
            rbColorBrown.setOnClickListener {
                if (rbColorBrown.isChecked) action(R.color.brown)
                else action(null)
                dismiss()
            }

            rbColorBlue.setOnClickListener {
                if (rbColorBlue.isChecked) action(R.color.blue)
                else action(null)
                dismiss()
            }
            rbColorGreen.setOnClickListener {
                if (rbColorGreen.isChecked) action(R.color.green)
                else action(null)
                dismiss()
            }
            rbColorOrange.setOnClickListener {
                if (rbColorOrange.isChecked) action(R.color.orange)
                else action(null)
                dismiss()
            }

            rbColorPurple.setOnClickListener {
                if (rbColorPurple.isChecked) action(R.color.purple)
                else action(null)
                dismiss()
            }
            rbColorRed.setOnClickListener {
                if (rbColorRed.isChecked) action(R.color.red)
                else action(null)
                dismiss()
            }
            rbColorYellow.setOnClickListener {
                if (rbColorYellow.isChecked) action(R.color.yellow)
                else action(null)
                dismiss()
            }

            rbColorGrey.setOnClickListener {
                if (rbColorGrey.isChecked) action(R.color.grey)
                else action(null)
                dismiss()
            }
        }
        return this
    }

    fun setOutSideTouchable(isOutsideTouchable: Boolean): PopupChooseColor {
        this.isOutsideTouchable = isOutsideTouchable
        return this
    }

    fun show() {
        showAsDropDown(anchorView, 0, -(heightPopup + anchorView.height + 10))
    }
}
