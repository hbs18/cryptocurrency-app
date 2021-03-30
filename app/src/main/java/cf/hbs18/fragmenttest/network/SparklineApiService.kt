package cf.hbs18.fragmenttest.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
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
//val oldDate = sevenDaysAgo()
//val thingToGet = "sparkline?key=512830e2c32e1feb7c3887c7c76ef35a&ids=BTC,ETH&start="+oldDate+"T00%3A00%3A00Z"
interface SparklineApiService {
    /**
     * Returns a Coroutine [List] of [MarsProperty] which can be fetched with await() if
     * in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("sparkline?key=512830e2c32e1feb7c3887c7c76ef35a&ids=BTC,ETH")
    fun getProperties(@Query("start") queryString: String): Call<String>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object SparklineApi {
    val retrofitService : SparklineApiService by lazy { retrofit.create(SparklineApiService::class.java) }
}

fun sevenDaysAgo(): String? {
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    val cal: Calendar = Calendar.getInstance()
    cal.add(Calendar.DATE, -7)
    return dateFormat.format(cal.getTime())
}

fun thirtyDaysAgo(): String? {
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    val cal: Calendar = Calendar.getInstance()
    cal.add(Calendar.DATE, -30)
    return dateFormat.format(cal.getTime())
}

fun HundredAndTwentyDaysAgo(): String? {
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    val cal: Calendar = Calendar.getInstance()
    cal.add(Calendar.DATE, -365)
    return dateFormat.format(cal.getTime())
}

