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
import kotlinx.android.synthetic.main.sign_up_layout.*

class SignUpLayout : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_layout)

        // reusable alert dialog function to display messages
        fun dialogBuilder(title: String, message: String?, context: Context){
            val dBuilder = AlertDialog.Builder(context)
            dBuilder.setTitle(title)
            dBuilder.setMessage(message)
            dBuilder.create().show()
        }

        sign_up_layout_btnSignUp.setOnClickListener {

            if(sign_up_layout_edtPassword.text.toString().equals(sign_up_layout_edtConfirmPassword.text.toString())){

                // registration process
                 val signUpURL = "http://192.168.43.25/OnlineStoreApp/join_new_user.php?email="+
                         sign_up_layout_edtEmail.text.toString() + "&username=" +
                         sign_up_layout_edtUsername.text.toString() +"&pass=" +
                         sign_up_layout_edtPassword.text.toString()

                 val requestQ = Volley.newRequestQueue(this@SignUpLayout)
                 val stringRequest = StringRequest(Request.Method.GET, signUpURL, Response.Listener { response ->

                    if(response.equals("A User with this Email Address already exists")){
                            dialogBuilder("Message", response, this)
                    } else{

                        // when a person signs up successfully
                        Person.email = sign_up_layout_edtEmail.text.toString()

                        Toast.makeText(this@SignUpLayout, response, Toast.LENGTH_SHORT).show()
                        val homeIntent = Intent(this@SignUpLayout, HomeScreen::class.java)
                        startActivity(homeIntent)
                    }

                }, Response.ErrorListener { error ->
                     dialogBuilder("Message", error.message, this)
                })

                requestQ.add(stringRequest)

            } else{
                dialogBuilder("Message", "Password Mismatch", this)
            }
        }

        sign_up_layout_btnLogin.setOnClickListener {
            finish()
        }

    }
}
