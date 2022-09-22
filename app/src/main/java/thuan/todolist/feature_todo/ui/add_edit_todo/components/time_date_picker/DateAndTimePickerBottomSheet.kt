package thuan.todolist.feature_todo.ui.add_edit_todo.components.time_date_picker

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import thuan.todolist.databinding.BottomsheetDateAndTimeBinding
import java.util.*

class DateAndTimePickerBottomSheet(private val callback: (String) -> Unit) :
    BottomSheetDialogFragment() {
    private lateinit var binding: BottomsheetDateAndTimeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetDateAndTimeBinding.inflate(inflater, container, false)
        binding.timePicker.setIs24HourView(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvDetailTime.text = getCurrentTimeAndDate()
        setOnClick()
    }

    private fun getCurrentTimeAndDate(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        return String.format("%02d:%02d %02d/%02d/%d", hour, minute, day, month, year)
    }

    private fun setOnClick() {
        binding.apply {
            btnCancel.setOnClickListener {
                cancelButtonClicked()
            }

            btnOK.setOnClickListener {
                okButtonClicked()
            }
        }
    }


    private fun cancelButtonClicked() = this.dismiss()

    private fun okButtonClicked() {
        callback(getDate())
        this.dismiss()
    }


    private fun getDate(): String {
        val hour = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.timePicker.hour
        } else {
            "Error"
        }
        val minute = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.timePicker.minute
        } else {
            "Error"
        }

        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month + 1
        val year = binding.datePicker.year

        return String.format("%02d:%02d %02d/%02d/%d", hour, minute, day, month, year)
    }
}