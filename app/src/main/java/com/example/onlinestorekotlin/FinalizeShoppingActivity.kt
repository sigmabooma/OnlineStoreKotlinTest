package com.example.onlinestorekotlin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import kotlinx.android.synthetic.main.activity_finalize_shopping.*
import java.math.BigDecimal

class FinalizeShoppingActivity : AppCompatActivity() {

    var ttPrice: Long = 0
    // reusable alert dialog function to display messages
    private fun dialogBuilder(title: String, message: String?, context: Context){
        val dBuilder = AlertDialog.Builder(context)
        dBuilder.setTitle(title)
        dBuilder.setMessage(message)
        dBuilder.create().show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalize_shopping)

        val calculateTotalPriceUrl = "http://192.168.43.25/OnlineStoreApp/osimages/calculate_total_price.php?invoice_num=${intent.getStringExtra("LATEST_INVOICE_NUMBER")}"
        val requestQ = Volley.newRequestQueue(this@FinalizeShoppingActivity)
        val stringRequest = StringRequest(Request.Method.GET, calculateTotalPriceUrl, Response.Listener {
            response ->
            btnPaymentProcessing.text = "Pay $$response via PayPal now!"
            ttPrice = response.toLong()

        }, Response.ErrorListener {
            error ->  dialogBuilder("Message", error.message, this)
        })
        requestQ.add(stringRequest)

    val paypalConfig  = PayPalConfiguration()
        .environment(PayPalConfiguration
            .ENVIRONMENT_SANDBOX)
                .clientId(MyPayPal.clientID)
    val ppService = Intent(this@FinalizeShoppingActivity, PayPalService::class.java)
        ppService.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig)
        startService(ppService)

        btnPaymentProcessing.setOnClickListener {
            val ppProcessing = PayPalPayment(BigDecimal.valueOf(ttPrice),
                "USD", "Online Store Kotlin!",
                PayPalPayment.PAYMENT_INTENT_SALE)

            val paypalPaymentIntent = Intent(this, PaymentActivity::class.java)
            paypalPaymentIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig)
            paypalPaymentIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, ppProcessing)
            startActivityForResult(paypalPaymentIntent, 1000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                val intent = Intent(this, ThankYouActivity::class.java)
                startActivity(intent)
            } else{
                Toast.makeText(this, "Sorry! Something went wrong. Try Again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        stopService(Intent(this, PayPalService::class.java))
    }
}
