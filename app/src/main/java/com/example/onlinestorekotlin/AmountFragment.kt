package com.example.onlinestorekotlin


import android.app.DialogFragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley


class AmountFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // reusable alert dialog function to display messages
        fun dialogBuilder(title: String, message: String?, context: Context){
            val dBuilder = AlertDialog.Builder(context)
            dBuilder.setTitle(title)
            dBuilder.setMessage(message)
            dBuilder.create().show()
        }

        // Inflate the layout for this fragment
        val fragmentView = inflater.inflate(R.layout.fragment_amount, container, false)

        val edtEnterAmount = fragmentView.findViewById<EditText>(R.id.edtEnterAmount)
        val btnAddToCart   = fragmentView.findViewById<ImageButton>(R.id.btnAddToCart)

        btnAddToCart.setOnClickListener {
           val ptoURL = "http://192.168.43.25/OnlineStoreApp/insert_temporary_order.php?email=${Person.email}&product_id=${Person.addToCartProductID}&amount=${edtEnterAmount.text}"
           val requestQ = Volley.newRequestQueue(activity)
           val stringRequest = StringRequest(Request.Method.GET, ptoURL, Response.Listener {
               response ->

               val intent = Intent(activity, CartProductsActivity::class.java)
               startActivity(intent)

           }, Response.ErrorListener {
               error -> dialogBuilder("Message", error.message, activity)
           })

            requestQ.add(stringRequest)
        }
        return fragmentView
    }


}
