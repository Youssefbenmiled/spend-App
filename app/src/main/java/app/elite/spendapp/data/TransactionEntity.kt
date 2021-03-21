package app.elite.spendapp.data

import androidx.room.*

// 1 step mark the class as entity
@Entity(tableName = "transaction_table")
/*@Relation(
    parentColumn = "ownerId",
    entityColumn = "amount",
    entity = TransactionEntity::class
)*/
data class TransactionEntity(
    // 2 step mark field as primary key
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "transaction_title")
    val title: String,
    val amount: Int,
    val type: String,
    val tag: String,
    val date: String,
    val note: String
)

