package com.geekbrains.moviesearcher22.model.room

import androidx.room.*

@Dao
interface HistoryDao {
    @Query("SELECT * FROM HistoryEntity")
    fun all(): List<HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: HistoryEntity)

    @Update
    fun update(entity: HistoryEntity)

    @Query("UPDATE HistoryEntity SET note = :note WHERE movieId = :movieID")
    fun updateNote(movieID: Int, note: String)

    @Query("UPDATE HistoryEntity SET viewTime = :viewTime WHERE movieId = :movieID")
    fun updateViewTime(movieID: Int, viewTime: Long)

    @Query("SELECT COUNT(*) FROM HistoryEntity WHERE movieId = :movieID")
    fun getRecordsCount(movieID: Int): Int

    @Delete
    fun delete(entity: HistoryEntity)

    @Query("SELECT note FROM HistoryEntity WHERE movieId = :movieID")
    fun getNote(movieID: Int): String?

    @Query("DELETE FROM HistoryEntity WHERE id = :id")
    fun deleteById(id: Long)

    @Query("DELETE FROM HistoryEntity")
    fun clearTable();
}