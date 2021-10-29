package pt.isel.pdm.chess4android.Connection

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

interface ChessService {
        @GET("api/puzzle/daily")
        fun listRepos(@Path("game") user: String?): Call<String>
}