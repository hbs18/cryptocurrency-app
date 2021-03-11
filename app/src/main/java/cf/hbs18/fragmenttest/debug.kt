package cf.hbs18.fragmenttest

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import cf.hbs18.fragmenttest.network.EthApi
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


class debug : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)
        setTitle("Debugging options")
        sparklineTest()

    }
    fun removeMoney(view: View){
        val walletPref: SharedPreferences = getApplicationContext().getSharedPreferences("wallet", MODE_PRIVATE)
        editor = walletPref.edit()
        editor.putFloat("usd", 0F)
        editor.apply()
        Snackbar.make(view, "Money (USD) set to 0", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
        return
    }

    fun removeBTC(view: View){
        val walletPref: SharedPreferences = getApplicationContext().getSharedPreferences("wallet", MODE_PRIVATE)
        editor = walletPref.edit()
        editor.putFloat("btc", 0F)
        editor.apply()
        Snackbar.make(view, "All BTC removed", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
        return
    }

    fun removeETH(view: View){
        val walletPref: SharedPreferences = getApplicationContext().getSharedPreferences("wallet", MODE_PRIVATE)
        editor = walletPref.edit()
        editor.putFloat("eth", 0F)
        editor.apply()
        Snackbar.make(view, "All BTC removed", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
        return
    }

    fun setMoneyTo100(view: View){
        val walletPref: SharedPreferences = getApplicationContext().getSharedPreferences("wallet", MODE_PRIVATE)
        editor = walletPref.edit()
        editor.putFloat("usd", 100F)
        editor.apply()
        Snackbar.make(view, "USD set to 100", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
        return
    }

    val queryThing = sevenDaysAgo()+"T00:00:00Z"

    fun sparklineTest(){
        SparklineApi.retrofitService.getProperties(queryThing).enqueue(
            object: Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    //findViewById<TextView>(R.id.textView).text=response.body()
                    if(response.isSuccessful){
                        val responseBody = response.body().toString()
                        findViewById<TextView>(R.id.sparklineDebugText).text = responseBody
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