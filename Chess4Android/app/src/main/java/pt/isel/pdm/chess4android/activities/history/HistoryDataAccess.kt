package pt.isel.pdm.chess4android.activities.history

import androidx.room.*


@Entity(tableName = "history_puzzle")
data class PuzzleEntity(
    @PrimaryKey val id: String,
    val pgn : String,
    val solution : String,
)

@Dao
interface PuzzleHistoryDao{
    @Insert
    fun insert(puzzle: PuzzleEntity)

    @Delete
    fun delete(puzzle: PuzzleEntity)

    @Query("SELECT * FROM history_puzzle ORDER BY id DESC LIMIT 100")
    fun getAll() : List<PuzzleEntity>

    @Query("SELECT * FROM history_puzzle WHERE id = :id")
    fun getById(id: String): PuzzleEntity
}
@Database(entities = [PuzzleEntity::class], version = 1)
abstract class PuzzleHistoryDataBase: RoomDatabase(){
    abstract fun getHistoryDataBase() :PuzzleHistoryDao
}


