package cf.hbs18.fragmenttest

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.widget.ContentLoadingProgressBar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cf.hbs18.fragmenttest.network.MarsApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


const val EXTRA_MESSAGE = "cf.hbs18.studybuddy.MESSAGE"
lateinit var editor: SharedPreferences.Editor
lateinit var editorEth: SharedPreferences.Editor

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //btc shit
        MarsApi.retrofitService.getProperties().enqueue(
                object: Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                    }
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if(response.isSuccessful){
                            val jsonShit = response.body().toString()
                            val parsedJson= Gson().fromJson(jsonShit, jsonData::class.java)
                            val btcDataSharedPrefs: SharedPreferences = applicationContext.getSharedPreferences("btcData", MODE_PRIVATE)
                            editor = btcDataSharedPrefs.edit()
                            editor.putString("currentPrice", parsedJson[0].price)
                            editor.putString("ticker", parsedJson[0].`1d`.price_change_pct)
                            editor.putString("name", parsedJson[0].name)
                            editor.putString("volume", parsedJson[0].`1d`.volume)
                            editor.putString("volumeTicker", parsedJson[0].`1d`.volume_change_pct)
                            editor.putString("mktCapChange", parsedJson[0].`1d`.market_cap_change)
                            editor.putString("mktCapChangeTicker", parsedJson[0].`1d`.market_cap_change_pct)
                            editor.apply()

                            val ethDataSharedPrefs: SharedPreferences = applicationContext.getSharedPreferences("ethData", MODE_PRIVATE)
                            editor = ethDataSharedPrefs.edit()
                            editor.putString("currentPrice", parsedJson[1].price)
                            editor.putString("ticker", parsedJson[1].`1d`.price_change_pct)
                            editor.putString("name", parsedJson[1].name)
                            editor.putString("volume", parsedJson[1].`1d`.volume)
                            editor.putString("volumeTicker", parsedJson[1].`1d`.volume_change_pct)
                            editor.putString("mktCapChange", parsedJson[1].`1d`.market_cap_change)
                            editor.putString("mktCapChangeTicker", parsedJson[1].`1d`.market_cap_change_pct)
                            editor.apply()
                            val toast = Toast.makeText(getApplicationContext(),
                                    "Error code "+response.code().toString(),
                                    Toast.LENGTH_SHORT);
                            toast.show()
                            //now create UI shit
                            setContentView(R.layout.activity_main)
                            val navView: BottomNavigationView = findViewById(R.id.nav_view)

                            val navController = findNavController(R.id.nav_host_fragment)
                            // Passing each menu ID as a set of Ids because each
                            // menu should be considered as top level destinations.
                            val appBarConfiguration = AppBarConfiguration(setOf(
                                    R.id.navigation_home, R.id.navigation_dashboard))
                            setupActionBarWithNavController(navController, appBarConfiguration)
                            navView.setupWithNavController(navController)

                        }
                        else {
                            val toast = Toast.makeText(getApplicationContext(),
                                    "Error code "+response.code().toString(),
                                    Toast.LENGTH_SHORT);
                            toast.show()
                        }

                    }
                })
    }

    fun openBuyCryptoScreen(view: View) {
        val intent = Intent(this, BuyCryptoActivity::class.java).apply{
            putExtra(EXTRA_MESSAGE, "btc")
        }
        startActivity(intent)
    }

    fun openBuyCryptoScreenEth(view: View) {
        val intent = Intent(this, BuyCryptoActivity::class.java).apply{
            putExtra(EXTRA_MESSAGE, "eth")
        }
        startActivity(intent)
    }

    fun openSellBtcScreen(view: View) {
        val intent = Intent(this, sellCrypto::class.java).apply{
            putExtra(EXTRA_MESSAGE, "btc")
        }
        startActivity(intent)
    }

    fun openSellEthScreen(view: View) {
        val intent = Intent(this, sellCrypto::class.java).apply{
            putExtra(EXTRA_MESSAGE, "eth")
        }
        startActivity(intent)
    }

}