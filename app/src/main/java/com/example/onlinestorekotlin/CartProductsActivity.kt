package com.example.onlinestorekotlin

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_cart_products.*

class CartProductsActivity : AppCompatActivity() {

    // reusable alert dialog function to display messages
    private fun dialogBuilder(title: String, message: String?, context: Context){
        val dBuilder = AlertDialog.Builder(context)
        dBuilder.setTitle(title)
        dBuilder.setMessage(message)
        dBuilder.create().show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_products)



        val cartProductsURL = "http://192.168.43.25/OnlineStoreApp/fetch_temporary_order.php?email=${Person.email}"
        val cartProductsList = ArrayList<String>()
        val requestQ = Volley.newRequestQueue(this@CartProductsActivity)
        val jsonAR = JsonArrayRequest(Request.Method.GET, cartProductsURL, null, Response.Listener {
            response -> for(joIndex in 0.until(response.length())){
            cartProductsList.add("${response.getJSONObject(joIndex).getInt("id")} " +
                    "\n ${response.getJSONObject(joIndex).getString("name")} " +
                    "\n ${response.getJSONObject(joIndex).getInt("price")} " +
                    "\n ${response.getJSONObject(joIndex).getString("email")} " +
                    "\n ${response.getJSONObject(joIndex).getInt("amount")}")
        }

            val cartProductsAdapter = ArrayAdapter(this@CartProductsActivity, android.R.layout.simple_list_item_1, cartProductsList)
            cartProductsListView.adapter = cartProductsAdapter

        }, Response.ErrorListener {
            error -> dialogBuilder("Message", error.message, this)
        })

        requestQ.add(jsonAR)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cart_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when {

            item?.itemId == R.id.continueShoppingItem -> {
                val intent = Intent(this, HomeScreen::class.java)
                startActivity(intent)
            }

            item?.itemId == R.id.declineOrderItem -> {

                val deleteURL = "http://192.168.43.25/OnlineStoreApp/decline_order.php?email=${Person.email}"
                val requestQ = Volley.newRequestQueue(this@CartProductsActivity)
                val stringRequest = StringRequest(Request.Method.GET, deleteURL, Response.Listener {
                    val intent = Intent(this, HomeScreen::class.java)
                    startActivity(intent)
                }, Response.ErrorListener { error ->  dialogBuilder("Message", error.message, this)
                })

                requestQ.add(stringRequest)

            }

            item?.itemId == R.id.verifyOrderItem -> {
                val verifyOrderURL = "http://192.168.43.25/OnlineStoreApp/verify_order.php?email=${Person.email}"
                val requestQ = Volley.newRequestQueue(this@CartProductsActivity)
                val stringRequest = StringRequest(Request.Method.GET, verifyOrderURL, Response.Listener { response ->
                    val intent = Intent(this, FinalizeShoppingActivity::class.java)
                    intent.putExtra("LATEST_INVOICE_NUMBER", response)
                    startActivity(intent)
                    Toast.makeText(this, response, Toast.LENGTH_LONG).show()
                }, Response.ErrorListener { error ->  dialogBuilder("Message", error.message, this)
                })

                requestQ.add(stringRequest)
            }

        } // end of when statement

        return super.onOptionsItemSelected(item)
    }
}
