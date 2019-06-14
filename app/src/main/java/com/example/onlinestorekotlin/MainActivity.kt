package com.example.onlinestorekotlin

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
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // reusable alert dialog function to display messages
        fun dialogBuilder(title: String, message: String?, context: Context){
            val dBuilder = AlertDialog.Builder(context)
            dBuilder.setTitle(title)
            dBuilder.setMessage(message)
            dBuilder.create().show()
        }

        activity_main_btnLogin.setOnClickListener {

            val loginURL = "http://192.168.43.25/OnlineStoreApp/login_app_user.php"

            val requestQ = Volley.newRequestQueue(this@MainActivity)
            val stringRequest = object :StringRequest(Request.Method.GET, loginURL, Response.Listener {

                response ->  if (response == "The user does exist"){

                Person.email = activity_main_edtEmail.text.toString()
                Toast.makeText(this@MainActivity, response, Toast.LENGTH_SHORT).show()
                val homeIntent = Intent(this@MainActivity, HomeScreen::class.java)
                startActivity(homeIntent)

            } else{

                dialogBuilder("Message", response, this)
            }

            }, Response.ErrorListener {

                error ->  dialogBuilder("Message", error.message, this)
            })

            {
                override fun getParams(): MutableMap<String, String> {
                    val myCredentialsMap = HashMap<String, String>()
                    myCredentialsMap["email"] = activity_main_edtEmail.text.toString()
                    myCredentialsMap["pass"] = activity_main_edtPassword.text.toString()
                    return myCredentialsMap
                }
            }
            requestQ.add(stringRequest) // added stringRequest to requestQ
        }

        activity_main_btnSignUp.setOnClickListener {
            val signUpIntent = Intent(this@MainActivity, SignUpLayout::class.java)
            startActivity(signUpIntent)
        }
    }
}
