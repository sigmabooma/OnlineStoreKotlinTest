package com.example.onlinestorekotlin

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.ArrayAdapter
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_home_screen.*

class HomeScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        // reusable alert dialog function to display messages
        fun dialogBuilder(title: String, message: String?, context: Context){
            val dBuilder = AlertDialog.Builder(context)
            dBuilder.setTitle(title)
            dBuilder.setMessage(message)
            dBuilder.create().show()
        }

        val brandsURL = "http://192.168.43.25/OnlineStoreApp/fetch_brands.php"
        val brandsList = ArrayList<String>()

        val requestQ = Volley.newRequestQueue(this@HomeScreen)
        val jsonAR = JsonArrayRequest(Request.Method.GET, brandsURL, null, Response.Listener {
                response ->  for (jsonObject in 0.until(response.length())){

            brandsList.add(response.getJSONObject(jsonObject).getString("brand"))
        }

            val brandsListAdapter = ArrayAdapter(this@HomeScreen, R.layout.brand_item_text_view, brandsList)
            brandslistView.adapter = brandsListAdapter

        }, Response.ErrorListener {
            error -> dialogBuilder("Message", error.message, this)
        })

        requestQ.add(jsonAR)

        brandslistView.setOnItemClickListener { adapterView, view, index, id ->

            val tappedBrand = brandsList[index]
            val intent = Intent(this@HomeScreen, FetchEProductsActivity::class.java)
            intent.putExtra("BRAND", tappedBrand)
            startActivity(intent)
        }


    }
}
