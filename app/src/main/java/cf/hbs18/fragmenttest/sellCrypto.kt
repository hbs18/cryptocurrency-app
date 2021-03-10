package cf.hbs18.fragmenttest

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cf.hbs18.fragmenttest.network.EthApi
import cf.hbs18.fragmenttest.network.MarsApi
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.math.RoundingMode

lateinit var messageSell: String
var bitcoin_priceSell: Double = 0.0
var jsonShitSell: String? = null

var eth_priceSell: Double = 0.0
var jsonShitEthSell: String? = null

class sellCrypto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_crypto)
        messageSell = intent.getStringExtra(EXTRA_MESSAGE).toString()
        setTitle("Sell cryptocurrency")
        if (messageSell == "btc"){
            findViewById<TextView>(R.id.textViewSellTitle).text="How much $ of BTC do you want to sell?"
            setTitle("Sell Bitcoin")
        }
        else if(messageSell == "eth"){
            findViewById<TextView>(R.id.textViewSellTitle).text="How much $ of ETH do you want to sell?"
            setTitle("Sell Ethereum")
        }
    }

    fun onClickSell(view: View){
        if (messageSell=="btc"){
            sellBtc()
        }
        else if (messageSell=="eth"){
            sellEth()
        }
    }

    fun setAllCrypto(view: View){
        if (messageSell=="btc"){
            val walletPref: SharedPreferences = getApplicationContext().getSharedPreferences("wallet", MODE_PRIVATE)
            val allBtc = walletPref.getFloat("btc", 0F)
            findViewById<EditText>(R.id.editTextNumberSell).setText(allBtc.toString())
        }
        if (messageSell=="eth"){
            val walletPref: SharedPreferences = getApplicationContext().getSharedPreferences("wallet", MODE_PRIVATE)
            val allEth = walletPref.getFloat("eth", 0F)
            findViewById<EditText>(R.id.editTextNumberSell).setText(allEth.toString())
        }
        return
    }

    fun Double.roundTo2DecimalPlaces() =
        BigDecimal(this).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()

    fun sellBtc(){
        MarsApi.retrofitService.getProperties().enqueue(
            object: Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    //findViewById<TextView>(R.id.textView).text=response.body()
                    jsonShitSell = response.body().toString()
                    val parsedJson= Gson().fromJson(jsonShitSell, jsonData::class.java)
                    bitcoin_priceSell = (BigDecimal(parsedJson[0].price.toDouble()).setScale(2, RoundingMode.HALF_EVEN)).toDouble()
                    val walletPref: SharedPreferences = getApplicationContext().getSharedPreferences("wallet", MODE_PRIVATE)
                    editor = walletPref.edit()

                    val sellValue_BTC = findViewById<EditText>(R.id.editTextNumberSell).text.toString()                 //amount of BTC you're selling
                    val sellValue_USD = sellValue_BTC.toDouble() * bitcoin_priceSell                                //amount of money you're earning
                    val sellValue_USDTwoDecimal = BigDecimal(sellValue_USD).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()

                    val current_BTC = walletPref.getFloat("btc", 0F)
                    val current_USD = walletPref.getFloat("usd", 0F)


                    editor.putFloat("btc", (current_BTC-sellValue_BTC.toFloat()).toFloat())                         //reduce the BTC in your wallet
                    editor.putFloat("usd", (current_USD+sellValue_USDTwoDecimal.toFloat()))                                   //increase your money
                    editor.apply()
                }
            })
        return
    }

    fun sellEth(){
        EthApi.retrofitService.getProperties().enqueue(
            object: Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    //findViewById<TextView>(R.id.textView).text=response.body()
                    jsonShitEthSell = response.body().toString()
                    val parsedJsonEth= Gson().fromJson(jsonShitEthSell, jsonData::class.java)
                    eth_priceSell = (BigDecimal(parsedJsonEth[0].price.toDouble()).setScale(2, RoundingMode.HALF_EVEN)).toDouble()
                    val walletPref: SharedPreferences = getApplicationContext().getSharedPreferences("wallet", MODE_PRIVATE)
                    editor = walletPref.edit()

                    val sellValue_ETH = findViewById<EditText>(R.id.editTextNumberSell).text.toString()                 //amount of eth you're selling
                    //val sellValue_USD = sellValue_ETH.toDouble() * eth_priceSell                                //amount of money you're earning
                    val sellValue_USDTwoDecimal = (BigDecimal(sellValue_ETH.toDouble() * eth_priceSell ).setScale(2, RoundingMode.HALF_EVEN)).toDouble()        //usd value to 2 decimals

                    val current_ETH = walletPref.getFloat("eth", 0F)
                    val current_USD = walletPref.getFloat("usd", 0F)


                    editor.putFloat("eth", (current_ETH-sellValue_ETH.toFloat()).toFloat())                         //reduce the BTC in your wallet
                    editor.putFloat("usd", (current_USD+sellValue_USDTwoDecimal.toFloat()))                                   //increase your money
                    editor.apply()
                }
            })
        return
    }
}