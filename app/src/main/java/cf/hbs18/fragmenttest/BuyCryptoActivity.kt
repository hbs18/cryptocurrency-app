package cf.hbs18.fragmenttest

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import cf.hbs18.fragmenttest.network.EthApi
import cf.hbs18.fragmenttest.network.MarsApi
import cf.hbs18.fragmenttest.network.SparklineApi
import cf.hbs18.fragmenttest.network.sevenDaysAgo
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import io.material.rally_line_chart.DataPoint
import io.material.rally_line_chart.RallyLineGraphChart
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*

lateinit var message: String
var bitcoin_price: Double = 0.0
var jsonShit: String? = null
var attempts=0
var eth_price: Double = 0.0
var jsonShitEth: String? = null
class BuyCryptoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_crypto)
        val rallyLine = findViewById<RallyLineGraphChart>(R.id.rallyLine)
        rallyLine.setCurveBorderColor(R.color.rally_blue)
        //rallyLine.addDataPoints(getTestPoints())
        message = intent.getStringExtra(EXTRA_MESSAGE).toString()
        setTitle("Buy cryptocurrency")
        if (message == "btc"){
            findViewById<TextView>(R.id.textViewBuyTitle).text="How much $ of BTC do you want to buy?"
            setTitle("Buy Bitcoin")
            updateViewsBtc()
            sparklineBtc()
        }
        else if(message == "eth"){
            findViewById<TextView>(R.id.textViewBuyTitle).text="How much $ of ETH do you want to buy?"
            setTitle("Buy Ethereum")
            updateViewsEth()
            sparklineEth()
        }




    }



    fun updateViewsBtc() {
        findViewById<ProgressBar>(R.id.testProgressBar).isVisible = false
        val sharedPrefsData = applicationContext.getSharedPreferences("btcData", MODE_PRIVATE)
        if (message=="btc"){
            val sharedPrefsData = applicationContext.getSharedPreferences("btcData", MODE_PRIVATE)
        }
        if (message=="eth"){
            val sharedPrefsData = applicationContext.getSharedPreferences("ethData", MODE_PRIVATE)
        }
        val parsedJson = Gson().fromJson(jsonShit, jsonData::class.java)
        val priceU = sharedPrefsData.getString("currentPrice", "0")
        val tickerU = sharedPrefsData.getString("ticker", "0")
        val volumeChangePctU = sharedPrefsData.getString("volumeTicker", "0")
        val mktCapChangePctU = sharedPrefsData.getString("mktCapChangeTicker", "0")
        val nameU = sharedPrefsData.getString("name", "0")
        val volumeChangeU = sharedPrefsData.getString("volume", "0")
        val mktCapChangeU = sharedPrefsData.getString("mktCapChange", "0")

        //formatted values below
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.setMaximumFractionDigits(0)
        format.setCurrency(Currency.getInstance("USD"))

        val priceChangePct = tickerU?.toDouble()?.times(100)
        val volumeChangePct = volumeChangePctU?.toDouble()?.times(100)
        val mktCapChangePct = mktCapChangePctU?.toDouble()?.times(100)
        val cryptoPrice = format.format(priceU?.toDouble())
        val cryptoName = nameU

        //set crypto name
        findViewById<TextView>(R.id.tvName).text = cryptoName

        //set crypto price
        findViewById<TextView>(R.id.text_btcPrice).text = cryptoPrice

        //set btc ticker
        if (priceChangePct != null) {
            if (priceChangePct > 0) {
                findViewById<TextView>(R.id.text_tickerCard).text = "+" + (BigDecimal(priceChangePct).setScale(2, RoundingMode.HALF_EVEN)).toString() + "%"
                findViewById<TextView>(R.id.text_tickerCard).setTextColor(Color.parseColor("#21AF6C"))
            }
        }
        if (priceChangePct != null) {
            if (priceChangePct < 0) {
                findViewById<TextView>(R.id.text_tickerCard).text = (BigDecimal(priceChangePct).setScale(2, RoundingMode.HALF_EVEN)).toString() + "%"
                findViewById<TextView>(R.id.text_tickerCard).setTextColor(Color.parseColor("#FC4F47"))
            }
        }
        if (priceChangePct == 0.toDouble()) {
            findViewById<TextView>(R.id.text_tickerCard).text = (priceChangePct).toString() + "%"
        }

        //set volume ticker
        if (volumeChangePct != null) {
            if (volumeChangePct > 0) {
                findViewById<TextView>(R.id.card3Subtitle).text = "+" + (BigDecimal(volumeChangePct).setScale(2, RoundingMode.HALF_EVEN)).toString() + "%"
                findViewById<TextView>(R.id.card3Subtitle).setTextColor(Color.parseColor("#21AF6C"))
            }
        }
        if (volumeChangePct != null) {
            if (volumeChangePct < 0) {
                findViewById<TextView>(R.id.card3Subtitle).text = (BigDecimal(volumeChangePct).setScale(2, RoundingMode.HALF_EVEN)).toString() + "%"
                findViewById<TextView>(R.id.card3Subtitle).setTextColor(Color.parseColor("#FC4F47"))
            }
        }
        if (volumeChangePct == 0.toDouble()) {
            findViewById<TextView>(R.id.card3Subtitle).text = (volumeChangePct).toString() + "%"
        }


        //set mkt cap change ticker
        if (mktCapChangePct != null) {
            if (mktCapChangePct > 0) {
                findViewById<TextView>(R.id.card4Subtitle).text = "+" + (BigDecimal(mktCapChangePct).setScale(2, RoundingMode.HALF_EVEN)).toString() + "%"
                findViewById<TextView>(R.id.card4Subtitle).setTextColor(Color.parseColor("#21AF6C"))
            }
        }
        if (mktCapChangePct != null) {
            if (mktCapChangePct < 0) {
                findViewById<TextView>(R.id.card4Subtitle).text = (BigDecimal(mktCapChangePct).setScale(2, RoundingMode.HALF_EVEN)).toString() + "%"
                findViewById<TextView>(R.id.card4Subtitle).setTextColor(Color.parseColor("#FC4F47"))
            }
        }
        if (mktCapChangePct == 0.toDouble()) {
            findViewById<TextView>(R.id.card4Subtitle).text = (mktCapChangePct).toString() + "%"
        }

        //set volume

        findViewById<TextView>(R.id.card3Text).text = format.format(sharedPrefsData.getString("volume", "0")?.toDouble())

        //set mkt cap change
        if (mktCapChangeU != null) {
            findViewById<TextView>(R.id.card4Text).text = format.format(mktCapChangeU.toDouble())
        }
        return
    }

    fun updateViewsEth() {
        findViewById<ProgressBar>(R.id.testProgressBar).isVisible = false
        val sharedPrefsData = applicationContext.getSharedPreferences("ethData", MODE_PRIVATE)
        val parsedJson = Gson().fromJson(jsonShit, jsonData::class.java)
        val priceU = sharedPrefsData.getString("currentPrice", "0")
        val tickerU = sharedPrefsData.getString("ticker", "0")
        val volumeChangePctU = sharedPrefsData.getString("volumeTicker", "0")
        val mktCapChangePctU = sharedPrefsData.getString("mktCapChangeTicker", "0")
        val nameU = sharedPrefsData.getString("name", "0")
        val volumeChangeU = sharedPrefsData.getString("volume", "0")
        val mktCapChangeU = sharedPrefsData.getString("mktCapChange", "0")

        //formatted values below
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.setMaximumFractionDigits(0)
        format.setCurrency(Currency.getInstance("USD"))

        val priceChangePct = tickerU?.toDouble()?.times(100)
        val volumeChangePct = volumeChangePctU?.toDouble()?.times(100)
        val mktCapChangePct = mktCapChangePctU?.toDouble()?.times(100)
        val cryptoPrice = format.format(priceU?.toDouble())
        val cryptoName = nameU

        //set crypto name
        findViewById<TextView>(R.id.tvName).text = cryptoName

        //set crypto price
        findViewById<TextView>(R.id.text_btcPrice).text = cryptoPrice

        //set btc ticker
        if (priceChangePct != null) {
            if (priceChangePct > 0) {
                findViewById<TextView>(R.id.text_tickerCard).text = "+" + (BigDecimal(priceChangePct).setScale(2, RoundingMode.HALF_EVEN)).toString() + "%"
                findViewById<TextView>(R.id.text_tickerCard).setTextColor(Color.parseColor("#21AF6C"))
            }
        }
        if (priceChangePct != null) {
            if (priceChangePct < 0) {
                findViewById<TextView>(R.id.text_tickerCard).text = (BigDecimal(priceChangePct).setScale(2, RoundingMode.HALF_EVEN)).toString() + "%"
                findViewById<TextView>(R.id.text_tickerCard).setTextColor(Color.parseColor("#FC4F47"))
            }
        }
        if (priceChangePct == 0.toDouble()) {
            findViewById<TextView>(R.id.text_tickerCard).text = (priceChangePct).toString() + "%"
        }

        //set volume ticker
        if (volumeChangePct != null) {
            if (volumeChangePct > 0) {
                findViewById<TextView>(R.id.card3Subtitle).text = "+" + (BigDecimal(volumeChangePct).setScale(2, RoundingMode.HALF_EVEN)).toString() + "%"
                findViewById<TextView>(R.id.card3Subtitle).setTextColor(Color.parseColor("#21AF6C"))
            }
        }
        if (volumeChangePct != null) {
            if (volumeChangePct < 0) {
                findViewById<TextView>(R.id.card3Subtitle).text = (BigDecimal(volumeChangePct).setScale(2, RoundingMode.HALF_EVEN)).toString() + "%"
                findViewById<TextView>(R.id.card3Subtitle).setTextColor(Color.parseColor("#FC4F47"))
            }
        }
        if (volumeChangePct == 0.toDouble()) {
            findViewById<TextView>(R.id.card3Subtitle).text = (volumeChangePct).toString() + "%"
        }


        //set mkt cap change ticker
        if (mktCapChangePct != null) {
            if (mktCapChangePct > 0) {
                findViewById<TextView>(R.id.card4Subtitle).text = "+" + (BigDecimal(mktCapChangePct).setScale(2, RoundingMode.HALF_EVEN)).toString() + "%"
                findViewById<TextView>(R.id.card4Subtitle).setTextColor(Color.parseColor("#21AF6C"))
            }
        }
        if (mktCapChangePct != null) {
            if (mktCapChangePct < 0) {
                findViewById<TextView>(R.id.card4Subtitle).text = (BigDecimal(mktCapChangePct).setScale(2, RoundingMode.HALF_EVEN)).toString() + "%"
                findViewById<TextView>(R.id.card4Subtitle).setTextColor(Color.parseColor("#FC4F47"))
            }
        }
        if (mktCapChangePct == 0.toDouble()) {
            findViewById<TextView>(R.id.card4Subtitle).text = (mktCapChangePct).toString() + "%"
        }

        //set volume

        findViewById<TextView>(R.id.card3Text).text = format.format(sharedPrefsData.getString("volume", "0")?.toDouble())

        //set mkt cap change
        if (mktCapChangeU != null) {
            findViewById<TextView>(R.id.card4Text).text = format.format(mktCapChangeU.toDouble())
        }
        return
    }

    fun onClick(view: View){
        if (message=="btc"){
            buyBtc()
        }
        else if (message=="eth"){
            buyEth()
        }
    }

    fun buyBtc(){
        MarsApi.retrofitService.getProperties().enqueue(
            object: Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    //findViewById<TextView>(R.id.textView).text=response.body()
                    if(response.isSuccessful){
                        jsonShit = response.body().toString()
                        val parsedJson= Gson().fromJson(jsonShit, jsonData::class.java)
                        bitcoin_price = (BigDecimal(parsedJson[0].price.toDouble()).setScale(2, RoundingMode.HALF_EVEN)).toDouble()
                        val walletPref: SharedPreferences = getApplicationContext().getSharedPreferences("wallet", MODE_PRIVATE)
                        editor = walletPref.edit()

                        val purchaseValue_USD = findViewById<EditText>(R.id.editTextNumber).text.toString()
                        val purchaseValue_BTC = purchaseValue_USD.toDouble() / bitcoin_price

                        val current_BTC = walletPref.getFloat("btc", 0F)
                        val current_USD = walletPref.getFloat("usd", 0F)


                        editor.putFloat("btc", (purchaseValue_BTC+current_BTC).toFloat())
                        editor.putFloat("usd", (current_USD-purchaseValue_USD.toFloat()))
                        editor.apply()
                    }
                    else {
                        val toast = Toast.makeText(getApplicationContext(),
                                "Error code "+response.code().toString(),
                                Toast.LENGTH_SHORT);
                        toast.show()
                    }

                }
            })
        return
    }

    fun buyEth(){
        EthApi.retrofitService.getProperties().enqueue(
            object: Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    //findViewById<TextView>(R.id.textView).text=response.body()
                    if(response.isSuccessful){
                        jsonShitEth = response.body().toString()
                        val parsedJsonEth= Gson().fromJson(jsonShitEth, jsonData::class.java)
                        eth_price = (BigDecimal(parsedJsonEth[0].price.toDouble()).setScale(2, RoundingMode.HALF_EVEN)).toDouble()
                        val walletPref: SharedPreferences = getApplicationContext().getSharedPreferences("wallet", MODE_PRIVATE)
                        editor = walletPref.edit()

                        val purchaseValue_USD = findViewById<EditText>(R.id.editTextNumber).text.toString()
                        val purchaseValue_ETH = purchaseValue_USD.toDouble() / eth_price

                        val current_ETH = walletPref.getFloat("eth", 0F)
                        val current_USD = walletPref.getFloat("usd", 0F)


                        editor.putFloat("eth", (purchaseValue_ETH+current_ETH).toFloat())
                        editor.putFloat("usd", (current_USD-purchaseValue_USD.toFloat()))
                        editor.apply()
                    }
                    else{
                        val toast = Toast.makeText(getApplicationContext(),
                                "Error code "+response.code().toString(),
                                Toast.LENGTH_SHORT);
                        toast.show()
                    }
                }
            })
        return
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_purchase, menu)
        return true
    }

    fun openDebugScreen(item: MenuItem) {
        val intent = Intent(this, debug::class.java)
        startActivity(intent)
    }


    fun calculatePointForGraph(number: Double, max: Double, min: Double): Float{
        val percentage = (number - min) / (max - min)
        return percentage.toFloat()
    }

    val queryThing = sevenDaysAgo() +"T00:00:00Z"

    fun sparklineBtc(){
        SparklineApi.retrofitService.getProperties(queryThing).enqueue(
            object: Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    //findViewById<TextView>(R.id.textView).text=response.body()
                    if(response.isSuccessful){
                        val responseBody = response.body().toString()
                        val parsedResponseBody= Gson().fromJson(responseBody, sparklineData::class.java)
                        val pricesMax = parsedResponseBody[0].prices.max()
                        val pricesMin = parsedResponseBody[0].prices.min()
                        val list = mutableListOf<DataPoint>()
                        if (pricesMax != null) {
                            if (pricesMin != null) {
                                list.add(DataPoint(calculatePointForGraph(parsedResponseBody[0].prices[0].toDouble(), pricesMax.toDouble(), pricesMin.toDouble())))
                                list.add(DataPoint(calculatePointForGraph(parsedResponseBody[0].prices[1].toDouble(), pricesMax.toDouble(), pricesMin.toDouble())))
                                list.add(DataPoint(calculatePointForGraph(parsedResponseBody[0].prices[2].toDouble(), pricesMax.toDouble(), pricesMin.toDouble())))
                                list.add(DataPoint(calculatePointForGraph(parsedResponseBody[0].prices[3].toDouble(), pricesMax.toDouble(), pricesMin.toDouble())))
                                list.add(DataPoint(calculatePointForGraph(parsedResponseBody[0].prices[4].toDouble(), pricesMax.toDouble(), pricesMin.toDouble())))
                                list.add(DataPoint(calculatePointForGraph(parsedResponseBody[0].prices[5].toDouble(), pricesMax.toDouble(), pricesMin.toDouble())))
                                list.add(DataPoint(calculatePointForGraph(parsedResponseBody[0].prices[6].toDouble(), pricesMax.toDouble(), pricesMin.toDouble())))
                            }
                        }
                        val rallyLine = findViewById<RallyLineGraphChart>(R.id.rallyLine)
                        rallyLine.setCurveBorderColor(R.color.bitcoin_color)
                        rallyLine.addDataPoints(list)
                    }
                    else{
                        val toast = Toast.makeText(getApplicationContext(),
                            "Error code "+response.code().toString(),
                            Toast.LENGTH_SHORT);
                        toast.show()
                        findViewById<TextView>(R.id.sparklineDebugText).text = SparklineApi.retrofitService.getProperties((queryThing)).request().url().toString()
                    }
                }
            })
        return
    }

    fun sparklineEth(){
        SparklineApi.retrofitService.getProperties(queryThing).enqueue(
            object: Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    //findViewById<TextView>(R.id.textView).text=response.body()
                    if(response.isSuccessful){
                        val responseBody = response.body().toString()
                        val parsedResponseBody= Gson().fromJson(responseBody, sparklineData::class.java)
                        val pricesMax = parsedResponseBody[1].prices.max()
                        val pricesMin = parsedResponseBody[1].prices.min()
                        val list = mutableListOf<DataPoint>()
                        if (pricesMax != null) {
                            if (pricesMin != null) {
                                list.add(DataPoint(calculatePointForGraph(parsedResponseBody[1].prices[0].toDouble(), pricesMax.toDouble(), pricesMin.toDouble())))
                                list.add(DataPoint(calculatePointForGraph(parsedResponseBody[1].prices[1].toDouble(), pricesMax.toDouble(), pricesMin.toDouble())))
                                list.add(DataPoint(calculatePointForGraph(parsedResponseBody[1].prices[2].toDouble(), pricesMax.toDouble(), pricesMin.toDouble())))
                                list.add(DataPoint(calculatePointForGraph(parsedResponseBody[1].prices[3].toDouble(), pricesMax.toDouble(), pricesMin.toDouble())))
                                list.add(DataPoint(calculatePointForGraph(parsedResponseBody[1].prices[4].toDouble(), pricesMax.toDouble(), pricesMin.toDouble())))
                                list.add(DataPoint(calculatePointForGraph(parsedResponseBody[1].prices[5].toDouble(), pricesMax.toDouble(), pricesMin.toDouble())))
                                list.add(DataPoint(calculatePointForGraph(parsedResponseBody[1].prices[6].toDouble(), pricesMax.toDouble(), pricesMin.toDouble())))
                            }
                        }
                        val rallyLine = findViewById<RallyLineGraphChart>(R.id.rallyLine)
                        rallyLine.setCurveBorderColor(R.color.ethereum_color)
                        rallyLine.addDataPoints(list)
                    }
                    else{
                        val toast = Toast.makeText(getApplicationContext(),
                            "Error code "+response.code().toString(),
                            Toast.LENGTH_SHORT);
                        toast.show()
                        findViewById<TextView>(R.id.sparklineDebugText).text = SparklineApi.retrofitService.getProperties((queryThing)).request().url().toString()
                    }
                }
            })
        return
    }
}