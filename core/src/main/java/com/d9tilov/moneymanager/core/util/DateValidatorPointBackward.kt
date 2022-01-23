package com.d9tilov.moneymanager.core.util

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CalendarConstraints.DateValidator

class DateValidatorPointBackward private constructor(private val point: Long) : DateValidator {
    override fun isValid(date: Long): Boolean {
        return date <= point
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(@NonNull dest: Parcel, flags: Int) {
        dest.writeLong(point)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is DateValidatorPointBackward) {
            return false
        }
        return point == other.point
    }

    override fun hashCode(): Int {
        val hashedFields = arrayOf<Any>(point)
        return hashedFields.contentHashCode()
    }

    companion object {
        /**
         * Returns a [CalendarConstraints.DateValidator] which enables only days before `point`, in UTC milliseconds.
         */
        @NonNull
        fun before(point: Long): DateValidatorPointBackward {
            return DateValidatorPointBackward(point)
        }

        /**
         * Returns a [CalendarConstraints.DateValidator] enabled from the current moment in device
         * time backwards.
         */
        @NonNull
        fun now(): DateValidatorPointBackward {
            return before(currentDateTime().toMillis())
        }

        /** Part of [android.os.Parcelable] requirements. Do not use.  */
        @JvmField
        val CREATOR: Parcelable.Creator<DateValidatorPointBackward> =
            object : Parcelable.Creator<DateValidatorPointBackward> {
                @NonNull
                override fun createFromParcel(@NonNull source: Parcel): DateValidatorPointBackward {
                    return DateValidatorPointBackward(source.readLong())
                }

                @NonNull
                override fun newArray(size: Int): Array<DateValidatorPointBackward?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
