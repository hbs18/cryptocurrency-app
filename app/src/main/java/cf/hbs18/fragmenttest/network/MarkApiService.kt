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
interface MarsApiService {
    /**
     * Returns a Coroutine [List] of [MarsProperty] which can be fetched with await() if
     * in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("ticker?key=512830e2c32e1feb7c3887c7c76ef35a&ids=BTC,ETH&interval=1d&convert=USD&per-page=100&page=1")
    fun getProperties(): Call<String>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object MarsApi {
    val retrofitService : MarsApiService by lazy { retrofit.create(MarsApiService::class.java) }
}

fun yesterday(): String? {
    val dateFormat: DateFormat = SimpleDateFormat("yyyy MM dd")
    val cal: Calendar = Calendar.getInstance()
    cal.add(Calendar.DATE, -1)
    return dateFormat.format(cal.getTime())
}
