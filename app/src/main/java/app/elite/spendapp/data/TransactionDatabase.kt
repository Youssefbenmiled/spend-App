package app.elite.spendapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [TransactionEntity::class],
    version = 1
)
abstract class TransactionDatabase : RoomDatabase() {

    abstract fun getTranscationDao(): TransactionDao

    companion object {

        private var instance: TransactionDatabase? = null

        fun getInstance(context: Context): TransactionDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    TransactionDatabase::class.java,
                    "transaction_db"
                ).build()
            }
            return instance!!
        }
    }
}