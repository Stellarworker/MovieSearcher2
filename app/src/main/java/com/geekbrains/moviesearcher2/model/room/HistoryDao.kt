package com.geekbrains.moviesearcher2.model.room

import androidx.room.*

@Dao
interface HistoryDao {
    @Query("SELECT * FROM HistoryEntity")
    fun all(): List<HistoryEntity>

    @Query("SELECT * FROM HistoryEntity WHERE title LIKE :title")
    fun getDataByWord(title: String): List<HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: HistoryEntity)

    @Update
    fun update(entity: HistoryEntity)

    @Query("UPDATE HistoryEntity SET note = :note WHERE id = (SELECT max(id) FROM HistoryEntity)")
    fun updateLastNote(note: String)

    @Delete
    fun delete(entity: HistoryEntity)

    @Query("DELETE FROM HistoryEntity WHERE id = :id")
    fun deleteById(id: Long)

    @Query("DELETE FROM HistoryEntity")
    fun clearTable();
}