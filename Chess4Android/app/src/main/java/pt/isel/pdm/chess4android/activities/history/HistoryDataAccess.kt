package pt.isel.pdm.chess4android.activities.history

import androidx.room.*
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*


@Entity(tableName = "history_puzzle")
data class PuzzleEntity(
    @PrimaryKey val id: String,
    val pgn: String,
    val solution: String,
    val originalSolution: String,
    val originalPgn: String,
    val timestamp: Date = Date.from(Instant.now().truncatedTo(ChronoUnit.DAYS)),
) {
    fun isTodayQuote(): Boolean =
        timestamp.toInstant().compareTo(Instant.now().truncatedTo(ChronoUnit.DAYS)) == 0
}

/**
 * Contains converters used by the ROOM ORM to map between Kotlin types and MySQL types
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long) = Date(value)

    @TypeConverter
    fun dateToTimestamp(date: Date) = date.time
}

@Dao
interface PuzzleHistoryDao {
    @Insert
    fun insert(puzzle: PuzzleEntity)

    @Delete
    fun delete(puzzle: PuzzleEntity)

    @Query("SELECT * FROM history_puzzle ORDER BY id DESC LIMIT 100")
    fun getAll(): List<PuzzleEntity>

    @Query("SELECT * FROM history_puzzle WHERE id = :id")
    fun getById(id: String): PuzzleEntity

    @Query("SELECT * FROM history_puzzle ORDER BY timestamp DESC LIMIT :count")
    fun getLast(count: Int): List<PuzzleEntity>

    @Query("UPDATE history_puzzle SET pgn =:pgn,solution=:solution WHERE id = :id")
    fun updatePuzzle(id: String, pgn: String, solution: String)
}

@Database(entities = [PuzzleEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class PuzzleHistoryDataBase : RoomDatabase() {
    abstract fun getHistoryDataBase(): PuzzleHistoryDao
}


