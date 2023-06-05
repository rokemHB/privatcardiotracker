import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.privatecardiowatch.db.HeartRateContract

class HeartRateDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "HeartRate.db"
        const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${HeartRateContract.HeartRateEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${HeartRateContract.HeartRateEntry.COLUMN_BPM} INTEGER," +
                    "${HeartRateContract.HeartRateEntry.COLUMN_SENSOR_TIMESTAMP} INTEGER," +
                    "${HeartRateContract.HeartRateEntry.COLUMN_UNIX_TIMESTAMP} INTEGER)"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${HeartRateContract.HeartRateEntry.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
}
