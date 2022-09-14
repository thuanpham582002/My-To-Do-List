package thuan.todolist.feature_todo.ui.time_date_picker

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import thuan.todolist.MainActivity
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
        val dayOfWeekString: String = when (calendar.firstDayOfWeek) {
            1 -> "Sunday"
            2 -> "Monday"
            3 -> "Tuesday"
            4 -> "Wednesday"
            5 -> "Thursday"
            6 -> "Friday"
            7 -> "Saturday"
            else -> "Error"
        }

        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        return "$hour:$minute | $dayOfWeekString | $day/$month/$year"
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
//            TODO("VERSION.SDK_INT < M")
        }
        val minute = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.timePicker.minute
        } else {
            "Error"
//            TODO("VERSION.SDK_INT < M")
        }

        val day = binding.datePicker.dayOfMonth

        val dayOfWeekString: String = when (binding.datePicker.firstDayOfWeek) {
            1 -> "Sunday"
            2 -> "Monday"
            3 -> "Tuesday"
            4 -> "Wednesday"
            5 -> "Thursday"
            6 -> "Friday"
            7 -> "Saturday"
            else -> "Error"
        }

        val month = binding.datePicker.month

        val year = binding.datePicker.year

        return "$hour:$minute | $dayOfWeekString | $day/$month/$year"
    }
}