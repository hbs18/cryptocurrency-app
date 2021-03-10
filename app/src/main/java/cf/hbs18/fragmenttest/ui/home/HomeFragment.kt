package cf.hbs18.fragmenttest.ui.home

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cf.hbs18.fragmenttest.MainActivity
import cf.hbs18.fragmenttest.R
import cf.hbs18.fragmenttest.editor
import cf.hbs18.fragmenttest.jsonData
import cf.hbs18.fragmenttest.network.MarsApi
import cf.hbs18.fragmenttest.network.EthApi
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.math.RoundingMode


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    lateinit var root: View

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_home, container, false)
        val activity: MainActivity? = activity as MainActivity?

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun onResume() {
        super.onResume()
        UpdateHomeScreen()
        UpdateHomeScreenEth()
    }

    override fun onStop() {
        super.onStop()
        if(isAdded){
            root.findViewById<TextView>(R.id.text_btcPrice).text="..."
            root.findViewById<TextView>(R.id.text_btcTicker).text= "..."
        }
    }



    var attempts=0
    fun UpdateHomeScreen(){                                                     //update home screen with btc info. this will be changed to just load in relevant shit from sharedprefs and display it
        root.findViewById<ProgressBar>(R.id.progressBar).isVisible=false
        //root.findViewById<ProgressBar>(R.id.progressBarEth).isVisible=true
        try{
            val btcDataSharedPrefs: SharedPreferences = this.requireActivity().getSharedPreferences("btcData", AppCompatActivity.MODE_PRIVATE)
            val btcPrice =  btcDataSharedPrefs.getString("currentPrice", "0")
            val btcTicker =  btcDataSharedPrefs.getString("ticker", "0")
            root.findViewById<ProgressBar>(R.id.progressBar).isVisible=false
            if (btcPrice != null) {
                root.findViewById<TextView>(R.id.text_btcPrice).text="$"+(BigDecimal(btcPrice.toDouble()).setScale(2, RoundingMode.HALF_EVEN)).toString()
            }

            if (btcTicker != null) {
                if (btcTicker.toDouble()*100 >0){
                    if (btcTicker != null) {
                        root.findViewById<TextView>(R.id.text_btcTicker).text="+"+(BigDecimal(btcTicker.toDouble()*100).setScale(2, RoundingMode.HALF_EVEN)).toString()+"%"
                    }
                    root.findViewById<TextView>(R.id.text_btcTicker).setTextColor(Color.parseColor("#21AF6C"))
                }
            }
            if (btcTicker != null) {
                if (btcTicker.toDouble()*100 <0){
                    root.findViewById<TextView>(R.id.text_btcTicker).text=(BigDecimal(btcTicker.toDouble()*100).setScale(2, RoundingMode.HALF_EVEN)).toString()+"%"
                    root.findViewById<TextView>(R.id.text_btcTicker).setTextColor(Color.parseColor("#FC4F47"))
                }
            }
            if (btcTicker != null) {
                if (btcTicker.toDouble()*100 == 0.toDouble()){
                    root.findViewById<TextView>(R.id.text_btcTicker).text=(btcTicker.toDouble()*100).toString()+"%"
                }
            }
        }
        catch (e: NullPointerException){
            if (attempts<6){
                UpdateHomeScreen()
                attempts++
            }
            else System.err.println("Null pointer exception");
            attempts=0
        }



        //now eth...

        return
    }
    fun UpdateHomeScreenEth(){                                                              //update home screen with eth info. this will be changed to just load in relevant shit from sharedprefs and display it
        //root.findViewById<ProgressBar>(R.id.progressBar).isVisible=true
        val ethDataSharedPrefs: SharedPreferences = this.requireActivity().getSharedPreferences("ethData", AppCompatActivity.MODE_PRIVATE)
        val ethPrice =  ethDataSharedPrefs.getString("currentPrice", "0")
        val ethTicker =  ethDataSharedPrefs.getString("ticker", "0")
        root.findViewById<ProgressBar>(R.id.ethProgressBar).isVisible=false
        try{
            root.findViewById<ProgressBar>(R.id.ethProgressBar).isVisible=false
            if (ethPrice != null) {
                root.findViewById<TextView>(R.id.text_ethPrice).text="$"+(BigDecimal(ethPrice.toDouble()).setScale(2, RoundingMode.HALF_EVEN)).toString()
            }

            if (ethTicker != null) {
                if (ethTicker.toDouble()*100 >0){
                    root.findViewById<TextView>(R.id.text_ethTicker).text="+"+(BigDecimal(ethTicker.toDouble()*100).setScale(2, RoundingMode.HALF_EVEN)).toString()+"%"
                    root.findViewById<TextView>(R.id.text_ethTicker).setTextColor(Color.parseColor("#21AF6C"))
                }
            }
            if (ethTicker != null) {
                if (ethTicker.toDouble()*100 <0){
                    root.findViewById<TextView>(R.id.text_ethTicker).text=(BigDecimal(ethTicker.toDouble()*100).setScale(2, RoundingMode.HALF_EVEN)).toString()+"%"
                    root.findViewById<TextView>(R.id.text_ethTicker).setTextColor(Color.parseColor("#FC4F47"))
                }
            }
            if (ethTicker != null) {
                if (ethTicker.toDouble()*100 == 0.toDouble()){
                    root.findViewById<TextView>(R.id.text_ethTicker).text=(ethTicker.toDouble()*100).toString()+"%"
                }
            }
        }
        catch (e: NullPointerException){
            if (attempts<6){
                UpdateHomeScreenEth()
                attempts++
            }
            else System.err.println("Null pointer exception");
            attempts=0
        }



        //now eth...

        return
    }










}