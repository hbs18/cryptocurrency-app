package cf.hbs18.fragmenttest

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import io.material.rally_line_chart.DataPoint
import io.material.rally_line_chart.RallyLineGraphChart


class debug : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)
        setTitle("Debugging options")

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











}