package com.example.privatecardiowatch.db

import android.provider.BaseColumns

object HeartRateContract {
    object HeartRateEntry : BaseColumns {
        const val TABLE_NAME = "heartrate"
        const val COLUMN_BPM = "bpm"
        const val COLUMN_SENSOR_TIMESTAMP = "sensor_timestamp"
        const val COLUMN_UNIX_TIMESTAMP = "unix_timestamp"
    }
}
