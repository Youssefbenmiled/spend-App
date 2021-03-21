package app.elite.spendapp.data

import androidx.room.*

@Dao
interface TransactionDao {

    @Insert
    suspend fun insertOne(entity: TransactionEntity)

    @Insert
    suspend fun insertAll(entities: List<TransactionEntity>)

    @Delete
    suspend fun deleteOne(entity: TransactionEntity)

    @Query("SELECT * FROM transaction_table")
    suspend fun getAll(): List<TransactionEntity>

    @Query("SELECT * FROM transaction_table WHERE id = :id")
    suspend fun getOneByID(id: Int): TransactionEntity

    @Update
    suspend fun updateOne(transactionEntity: TransactionEntity)

}