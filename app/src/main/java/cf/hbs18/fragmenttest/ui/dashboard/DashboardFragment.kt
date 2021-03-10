package cf.hbs18.fragmenttest.ui.dashboard

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cf.hbs18.fragmenttest.R
import cf.hbs18.fragmenttest.jsonData
import cf.hbs18.fragmenttest.network.EthApi
import cf.hbs18.fragmenttest.network.MarsApi
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.math.RoundingMode


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    lateinit var root: View

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        return
    }

    override fun onResume() {
        super.onResume()
        root.findViewById<TextView>(R.id.portfolioValue).text="0"
        UpdateBTCPrice()
        UpdateETHPrice()
        updateCash()
        //hideETHstuff()
        //updateaj btc statsove, pa eth, pa onda cash (tvoj usd stats i portfolio value)
    }

    var attempts = 0
    var bitcoin_price = 0.0

    var attemptsEth = 0
    var eth_price = 0.0
    fun UpdateBTCPrice() {
        val walletPref = this.requireActivity().getSharedPreferences("wallet", Context.MODE_PRIVATE)
        root.findViewById<ProgressBar>(R.id.progressBarBTC).isVisible = false
        MarsApi.retrofitService.getProperties().enqueue(
                object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val jsonShit = response.body().toString()
                        val parsedJson = Gson().fromJson(jsonShit, jsonData::class.java)

                        if (isAdded) {

                            try {
                                root.findViewById<ProgressBar>(R.id.progressBarBTC).isVisible = false
                                //root.findViewById<TextView>(R.id.portfolioValue).text="0"
                                root.findViewById<TextView>(R.id.tvAmountBTC).text = BigDecimal(((BigDecimal(parsedJson[0].price.toDouble()).setScale(2, RoundingMode.HALF_EVEN)) * walletPref.getFloat("btc", 0F).toBigDecimal()).toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()          //fetchani price puta kolicina btc iz sharedpreferences i formatting
                                //root.findViewById<TextView>(R.id.tvAmountBTC).text = (parsedJson[0].price.toDouble() * walletPref.getFloat("btc", 0F)).toString()
                                bitcoin_price = (BigDecimal(parsedJson[0].price.toDouble()).setScale(2, RoundingMode.HALF_EVEN)).toDouble()
                                val totalPFValue = root.findViewById<TextView>(R.id.portfolioValue).text.toString().replace("$", "").toBigDecimal() + BigDecimal(((BigDecimal(parsedJson[0].price.toDouble()).setScale(2, RoundingMode.HALF_EVEN)) * walletPref.getFloat("btc", 0F).toBigDecimal()).toDouble()).setScale(2, RoundingMode.HALF_EVEN)
                                //val totalPFValue = (parsedJson[0].price.toDouble() * walletPref.getFloat("btc", 0F)) + walletPref.getFloat("usd", 0F)
                                root.findViewById<TextView>(R.id.portfolioValue).text = "$" + totalPFValue.toString()

                                root.findViewById<TextView>(R.id.tvDateBTC).text = walletPref.getFloat("btc", 0F).toString() + " BTC"      //gets current btc you have and places it where appropriate

                                root.findViewById<TextView>(R.id.tvNameBTC).text = "Bitcoin"
                                root.findViewById<TextView>(R.id.text_view_dollarUSD).text = "$"
                            } catch (e: NullPointerException) {
                                if (attempts < 6) {
                                    UpdateBTCPrice()
                                    attempts++
                                } else System.err.println("Null pointer exception");
                                attempts = 0
                            }


                        }
                    }
                })
        return
    }

    fun UpdateETHPrice() {
        val walletPref = this.requireActivity().getSharedPreferences("wallet", Context.MODE_PRIVATE)
        root.findViewById<ProgressBar>(R.id.progressBarEth).isVisible = false
        EthApi.retrofitService.getProperties().enqueue(
            object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val jsonShitEth = response.body().toString()
                    val parsedJsonEth = Gson().fromJson(jsonShitEth, jsonData::class.java)

                    if (isAdded) {

                        try {
                            root.findViewById<ProgressBar>(R.id.progressBarEth).isVisible = false
                            //root.findViewById<TextView>(R.id.portfolioValue).text="0"
                            root.findViewById<TextView>(R.id.tvAmountEth).text = BigDecimal(((BigDecimal(parsedJsonEth[0].price.toDouble()).setScale(2, RoundingMode.HALF_EVEN)) * walletPref.getFloat("eth", 0F).toBigDecimal()).toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()          //fetchani price puta kolicina eth iz sharedpreferences i formatting
                            //root.findViewById<TextView>(R.id.tvAmountBTC).text = (parsedJson[0].price.toDouble() * walletPref.getFloat("btc", 0F)).toString()
                            eth_price = (BigDecimal(parsedJsonEth[0].price.toDouble()).setScale(2, RoundingMode.HALF_EVEN)).toDouble()
                            val totalPFValue = root.findViewById<TextView>(R.id.portfolioValue).text.toString().replace("$", "").toBigDecimal() + BigDecimal(((BigDecimal(parsedJsonEth[0].price.toDouble()).setScale(2, RoundingMode.HALF_EVEN)) * walletPref.getFloat("eth", 0F).toBigDecimal()).toDouble()).setScale(2, RoundingMode.HALF_EVEN)
                            root.findViewById<TextView>(R.id.portfolioValue).text = "$" + totalPFValue.toString()

                            root.findViewById<TextView>(R.id.tvDateEth).text = walletPref.getFloat("eth", 0F).toString() + " ETH"      //gets current btc you have and places it where appropriate

                            root.findViewById<TextView>(R.id.tvNameEth).text = "Ethereum"
                            root.findViewById<TextView>(R.id.text_view_dollarEth).text = "$"
                        } catch (e: NullPointerException) {
                            if (attempts < 6) {
                                UpdateETHPrice()
                                attempts++
                            } else System.err.println("Null pointer exception");
                            attempts = 0
                        }


                    }
                }
            })
        return
    }

    fun updateCash(){
        val walletPref = this.requireActivity().getSharedPreferences("wallet", Context.MODE_PRIVATE)
        root.findViewById<ProgressBar>(R.id.progressBarUSD).isVisible=false
        root.findViewById<TextView>(R.id.tvNameUSD).text = "US Dollars"
        root.findViewById<TextView>(R.id.tvDateUSD).text = "Non-invested money"
        root.findViewById<TextView>(R.id.tvAmountUSD).text = walletPref.getFloat("usd", 0F).toString()
        root.findViewById<TextView>(R.id.text_view_dollarBTC).text="$"
        val totalPFValue = root.findViewById<TextView>(R.id.portfolioValue).text.toString().replace("$", "").toFloat() + BigDecimal(walletPref.getFloat("usd", 0F).toDouble()).setScale(2, RoundingMode.HALF_EVEN).toFloat()
        root.findViewById<TextView>(R.id.portfolioValue).text = "$"+totalPFValue.toString()
        return
    }

    fun refresh(item: MenuItem){
        root.findViewById<TextView>(R.id.portfolioValue).text="0"
        UpdateBTCPrice()
        updateCash()
        return
    }

    fun hideETHstuff(){
        root.findViewById<ConstraintLayout>(R.id.constraintlayoutETH).isVisible = false
    }
}
