package cf.hbs18.fragmenttest.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

private const val BASE_URL = "https://api.nomics.com/v1/currencies/"

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

/**
 * A public interface that exposes the [getProperties] method
 */
interface EthApiService {
    /**
     * Returns a Coroutine [List] of [MarsProperty] which can be fetched with await() if
     * in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("ticker?key=512830e2c32e1feb7c3887c7c76ef35a&ids=ETH&interval=1d&convert=USD&per-page=100&page=1")
    fun getProperties(): Call<String>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object EthApi {
    val retrofitService : EthApiService by lazy { retrofit.create(EthApiService::class.java) }
}

