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
import cf.hbs18.fragmenttest.format2
import cf.hbs18.fragmenttest.jsonData
import cf.hbs18.fragmenttest.network.EthApi
import cf.hbs18.fragmenttest.network.MarsApi
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*


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
        setBTC()
        setETH()
        setUSD()
        calculatePortfolioValue()
    }

    lateinit var format3: NumberFormat

    fun setBTC(){
        format2 = NumberFormat.getInstance()
        format2.setMinimumFractionDigits(2)
        format2.setMaximumFractionDigits(2)

        format3 = NumberFormat.getInstance()
        format3.setMaximumFractionDigits(8)

        val btcDataSharedPrefs: SharedPreferences = this.requireActivity().getSharedPreferences("btcData", AppCompatActivity.MODE_PRIVATE)
        val walletSharedPrefs = this.requireActivity().getSharedPreferences("wallet", Context.MODE_PRIVATE)
        val btcPrice = btcDataSharedPrefs.getString("currentPrice", "0")?.toFloat()
        val yourBtc = walletSharedPrefs.getFloat("btc", 0F)
        val yourbtcValueInUsd = yourBtc* btcPrice!!
        //now set views
        root.findViewById<ProgressBar>(R.id.progressBarBTC).isVisible = false
        root.findViewById<TextView>(R.id.tvAmountBTC).text = format2.format(yourbtcValueInUsd)
        root.findViewById<TextView>(R.id.tvDateBTC).text = format3.format(yourBtc).toString() + " BTC"
        root.findViewById<TextView>(R.id.tvNameBTC).text = "Bitcoin"
        root.findViewById<TextView>(R.id.text_view_dollarUSD).text = "$"
        return
    }

    fun setETH(){
        format2 = NumberFormat.getInstance()
        format2.setMinimumFractionDigits(2)
        format2.setMaximumFractionDigits(2)

        format3 = NumberFormat.getInstance()
        format3.setMaximumFractionDigits(8)

        val ethDataSharedPrefs: SharedPreferences = this.requireActivity().getSharedPreferences("ethData", AppCompatActivity.MODE_PRIVATE)
        val walletSharedPrefs = this.requireActivity().getSharedPreferences("wallet", Context.MODE_PRIVATE)
        val ethPrice = ethDataSharedPrefs.getString("currentPrice", "0")?.toFloat()
        val yourEth = walletSharedPrefs.getFloat("eth", 0F)
        val yourethValueInUsd = yourEth* ethPrice!!
        //now set views
        root.findViewById<ProgressBar>(R.id.progressBarEth).isVisible = false
        root.findViewById<TextView>(R.id.tvAmountEth).text = format2.format(yourethValueInUsd)
        root.findViewById<TextView>(R.id.tvDateEth).text = format3.format(yourEth).toString() + " ETH"
        root.findViewById<TextView>(R.id.tvNameEth).text = "Ethereum"
        root.findViewById<TextView>(R.id.text_view_dollarEth).text = "$"
        return

    }

    fun setUSD(){
        format2 = NumberFormat.getInstance()
        format2.setMinimumFractionDigits(2)
        format2.setMaximumFractionDigits(2)

        format3 = NumberFormat.getInstance()
        format3.setMaximumFractionDigits(8)

        val walletPref = this.requireActivity().getSharedPreferences("wallet", Context.MODE_PRIVATE)
        val yourMoney = walletPref.getFloat("usd", 0F)

        root.findViewById<ProgressBar>(R.id.progressBarUSD).isVisible=false
        root.findViewById<TextView>(R.id.tvNameUSD).text = "US Dollars"
        root.findViewById<TextView>(R.id.tvDateUSD).text = "Non-invested money"
        root.findViewById<TextView>(R.id.tvAmountUSD).text = format2.format(yourMoney)
        root.findViewById<TextView>(R.id.text_view_dollarBTC).text="$"
        return
    }

    fun calculatePortfolioValue(){
        format2 = NumberFormat.getCurrencyInstance()
        format2.setMinimumFractionDigits(2)
        format2.setMaximumFractionDigits(2)
        format2.setCurrency(Currency.getInstance("USD"))

        val walletSharedPrefs = this.requireActivity().getSharedPreferences("wallet", Context.MODE_PRIVATE)
        val ethDataSharedPrefs: SharedPreferences = this.requireActivity().getSharedPreferences("ethData", AppCompatActivity.MODE_PRIVATE)
        val btcDataSharedPrefs: SharedPreferences = this.requireActivity().getSharedPreferences("btcData", AppCompatActivity.MODE_PRIVATE)

        val btcPrice = btcDataSharedPrefs.getString("currentPrice", "0")?.toFloat()
        val yourBtc = walletSharedPrefs.getFloat("btc", 0F)
        val yourbtcValueInUsd = yourBtc* btcPrice!!

        val ethPrice = ethDataSharedPrefs.getString("currentPrice", "0")?.toFloat()
        val yourEth = walletSharedPrefs.getFloat("eth", 0F)
        val yourMoney = walletSharedPrefs.getFloat("usd", 0F)
        val yourethValueInUsd = yourEth* ethPrice!!

        val PFValue = yourbtcValueInUsd+yourethValueInUsd+yourMoney

        root.findViewById<TextView>(R.id.portfolioValue).text = format2.format(PFValue).toString()

        return
    }


    fun hideETHstuff(){
        root.findViewById<ConstraintLayout>(R.id.constraintlayoutETH).isVisible = false
    }
}
