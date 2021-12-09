package pt.isel.pdm.chess4android.activities.history

import androidx.room.*
import com.google.gson.Gson
import pt.isel.pdm.chess4android.model.board.Board

@Entity(tableName = "history_puzzle")
data class PuzzleEntity(
    @PrimaryKey val id: String,
    val pgn : String,
    val board: Board,
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
}
@Database(entities = [PuzzleEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class PuzzleHistoryDataBase: RoomDatabase(){
    abstract fun getHistoryDataBase() :PuzzleHistoryDao
}
class Converters {

    @TypeConverter
    fun boardToJson(value: Board) = Gson().toJson(value)

    @TypeConverter
    fun jsonToBoard(value: String) = Gson().fromJson(value, Board::class.java)
}

