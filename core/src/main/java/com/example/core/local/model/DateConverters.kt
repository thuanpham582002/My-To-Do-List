package com.example.core.data.source.local.model

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.util.*

@ProvidedTypeConverter
class DateConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

