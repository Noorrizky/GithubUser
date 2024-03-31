import com.example.submissiononevtwo.data.response.DetailUserResponse
import com.example.submissiononevtwo.data.response.GithubResponse
import com.example.submissiononevtwo.data.response.ItemsItem
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

    @GET("users/{username}/followers")
    @Headers("Authorization: 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")
    fun getFollowers(@Path("username") username: String): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: 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")
    fun getFollowing(@Path("username") username: String): Call<List<ItemsItem>>
}
