package pt.isel.pdm.chess4android.common

import pt.isel.pdm.chess4android.activities.history.*
import pt.isel.pdm.chess4android.model.game.DailyPuzzle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DailyPuzzleRepository(
    private val dailyPuzzleService: DailyPuzzleService,
    private val historyDao: PuzzleHistoryDao
) {

    /**
     * Asynchronously gets the daily quote from the local DB, if available.
     * @param callback the function to be called to signal the completion of the
     * asynchronous operation, which is called in the MAIN THREAD.
     */
    private fun asyncMaybeGetTodayPuzzleFromDB(
        callback: (Result<PuzzleEntity?>) -> Unit
    ) {
        callbackAfterAsync(callback) {
            val puzzleEntity = historyDao.getLast(1).firstOrNull()
            puzzleEntity
        }
    }
    fun asyncGetPuzzleByIdFromDB(id: String,
        callback: (Result<PuzzleEntity?>) -> Unit
    ) {
        callbackAfterAsync(callback) {
            val puzzleEntity = historyDao.getById(id)
            puzzleEntity
        }
    }

    /**
     * Asynchronously gets the daily puzzle from the remote API.
     * @param callback the function to be called to signal the completion of the
     * asynchronous operation, which is called in the MAIN THREAD.
     */
    private fun asyncGetTodayPuzzleFromAPI(callback: (Result<DailyPuzzle>) -> Unit) {

        dailyPuzzleService.getPuzzle()
            .enqueue(object : Callback<ResultOfDailyPuzzle> {
                override fun onResponse(
                    call: Call<ResultOfDailyPuzzle>,
                    response: Response<ResultOfDailyPuzzle>
                ) {
                    val resultOfDailyPuzzle: ResultOfDailyPuzzle? = response.body()
                    val result =
                        if (resultOfDailyPuzzle != null && response.isSuccessful) {
                            val dailyPuzzle = DailyPuzzle(
                                resultOfDailyPuzzle.game.id,
                                resultOfDailyPuzzle.game.pgn,
                                resultOfDailyPuzzle.puzzle.solution
                            )
                            Result.success(dailyPuzzle)
                        } else Result.failure(ServiceUnavailable())
                    callback(result)
                }

                override fun onFailure(call: Call<ResultOfDailyPuzzle>, t: Throwable) {

                }
            })
    }

    /**
     * Asynchronously saves the daily puzzle to the local DB.
     * @param callback the function to be called to signal the completion of the
     * asynchronous operation, which is called in the MAIN THREAD.
     */
    private fun asyncSaveToDB(puzzle: DailyPuzzle, callback: (Result<Unit>) -> Unit = { }) {
        callbackAfterAsync(callback) {
            historyDao.insert(
                PuzzleEntity(
                    id = puzzle.getPuzzleId(),
                    pgn = puzzle.getPgn(),
                    solution = puzzle.getPuzzleSolution().joinToString(separator = ","),
                    originalSolution = puzzle.getPuzzleSolution().joinToString(separator = ","),
                    originalPgn = puzzle.getPgn()
                )
            )
        }
    }

    fun asyncUpdateToDB(puzzle: DailyPuzzle, callback: (Result<Unit>) -> Unit = { }) {
        callbackAfterAsync(callback) {
            historyDao.updatePuzzle(
                puzzle.getPuzzleId(),
                puzzle.getMovesPlayedAsPgn(),
                puzzle.getPuzzleSolution().joinToString(separator = ",")
            )
        }
    }

    /**
     * Asynchronously gets the daily puzzle, either from the local DB, if available, or from
     * the remote API.
     *
     * @param mustSaveToDB  indicates if the operation is only considered successful if all its
     * steps, including saving to the local DB, succeed. If false, the operation is considered
     * successful regardless of the success of saving the puzzle in the local DB (the last step).
     * @param callback the function to be called to signal the completion of the
     * asynchronous operation, which is called in the MAIN THREAD
     *
     * Using a boolean to distinguish between both options is a questionable design decision.
     */
    fun fetchDailyPuzzle(
        mustSaveToDB: Boolean = false,
        callback: (Result<DailyPuzzle>) -> Unit
    ) {
        asyncMaybeGetTodayPuzzleFromDB { maybeEntity ->
            maybeEntity.onSuccess { puzzleEntity ->
                if (puzzleEntity?.isTodayPuzzle() == true) {
                    callback(
                        Result.success(
                            DailyPuzzle(
                                puzzleId = puzzleEntity.id,
                                puzzlePgn = puzzleEntity.pgn,
                                puzzleSolution = puzzleEntity.solution.split(",").toMutableList()
                            )
                        )
                    )
                } else {
                    asyncGetTodayPuzzleFromAPI { apiResult ->
                        apiResult.onSuccess { dailyPuzzle ->
                            asyncSaveToDB(dailyPuzzle) { saveToDbResult ->
                                saveToDbResult.onSuccess {
                                    callback(Result.success(dailyPuzzle))
                                }.onFailure {
                                    callback(
                                        if (mustSaveToDB) Result.failure(it) else Result.success(
                                            dailyPuzzle
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
