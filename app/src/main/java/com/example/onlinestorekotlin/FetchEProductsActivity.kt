package com.example.onlinestorekotlin

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_fetch_eproducts.*

class FetchEProductsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch_eproducts)

        // reusable alert dialog function to display messages
        fun dialogBuilder(title: String, message: String?, context: Context){
            val dBuilder = AlertDialog.Builder(context)
            dBuilder.setTitle(title)
            dBuilder.setMessage(message)
            dBuilder.create().show()
        }

        val selectedBrand = intent.getStringExtra("BRAND")
        txtBrandName.text = "Products of $selectedBrand"

        val productsList = ArrayList<EProduct>()
        val productsURL = "http://192.168.43.25/OnlineStoreApp/fetch_eproducts.php?brand=$selectedBrand"
        val requestQ = Volley.newRequestQueue(this@FetchEProductsActivity)
        val jsonAR = JsonArrayRequest(Request.Method.GET, productsURL, null, Response.Listener {

            response ->  for (productJOIndex in 0.until(response.length())){
            productsList.add(EProduct(
                response.getJSONObject(productJOIndex).getInt("id"),
                response.getJSONObject(productJOIndex).getString("name"),
                response.getJSONObject(productJOIndex).getInt("price"),
                response.getJSONObject(productJOIndex).getString("picture")))
        }

            val pAdapter = EProductAdapter(this@FetchEProductsActivity, productsList)
            productsRV.layoutManager = LinearLayoutManager(this@FetchEProductsActivity)
            productsRV.adapter = pAdapter

        }, Response.ErrorListener { 
            error ->  dialogBuilder("Message", error.message, this)
        })

        requestQ.add(jsonAR)
    }
}
