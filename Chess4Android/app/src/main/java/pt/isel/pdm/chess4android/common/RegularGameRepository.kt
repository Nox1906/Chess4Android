package pt.isel.pdm.chess4android.common

import pt.isel.pdm.chess4android.model.game.RegularGame

class RegularGameRepository(
    private val getHistoryDataBase: PuzzleHistoryDao
) {

    /**
     * Asynchronously gets regular game from the local DB, if available.
     * @param callback the function to be called to signal the completion of the
     * asynchronous operation, which is called in the MAIN THREAD.
     */
    fun asyncGetRegularGameFromDb(
        callback: (Result<RegularGameEntity?>) -> Unit
    ) {
        callbackAfterAsync(callback) {
            val gameEntity = getHistoryDataBase.getGame()
            gameEntity
        }
    }
    /**
     * Asynchronously gets regular game from the local DB, if available.
     * @param callback the function to be called to signal the completion of the
     * asynchronous operation, which is called in the MAIN THREAD.
     */



    /**
     * Asynchronously saves a new regular game to the local DB.
     * @param callback the function to be called to signal the completion of the
     * asynchronous operation, which is called in the MAIN THREAD.
     */
    fun asyncSaveToDB(game: RegularGame, callback: (Result<Unit>) -> Unit = { }) {
        callbackAfterAsync(callback) {
            getHistoryDataBase.insert(
                RegularGameEntity(1, game.getMovesPlayedAsPgn())
            )

        }
    }

    fun asyncUpdateToDB(game: RegularGame, callback: (Result<Unit>) -> Unit = { }) {
        callbackAfterAsync(callback) {
            getHistoryDataBase.updateGame(
                game.getMovesPlayedAsPgn()
            )
        }
    }
}
