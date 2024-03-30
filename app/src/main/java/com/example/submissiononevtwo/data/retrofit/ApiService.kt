import com.example.submissiononevtwo.data.response.DetailUserResponse
import com.example.submissiononevtwo.data.response.GithubResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: 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")
    fun getUsers(
        @Query("q") username: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    @Headers("Authorization: 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")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>
}
