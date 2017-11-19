package com.digitalmischief.TTT3D

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenuActivity : AppCompatActivity() {

    val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        setSignInOutButton()

        setOnClickListners()

    }

    fun setSignInOutButton(){
        if(isSignedIn()){
            showSignOutButton()
        } else {
            showSignInButton()
        }
    }


    fun showSignInButton(){
        signout_button.visibility = View.GONE
        signin_button.visibility = View.VISIBLE
    }

    fun showSignOutButton(){
        signout_button.visibility = View.VISIBLE
        signin_button.visibility = View.GONE
    }

    fun isSignedIn() : Boolean {
        return GoogleSignIn.getLastSignedInAccount(this) != null
    }

    fun setOnClickListners(){
        val onClickListener = View.OnClickListener{
            when(it){
                signin_button -> signInButtonClicked()
                signout_button -> signOutButtonClicked()
            }
        }

        signin_button.setOnClickListener(onClickListener)
        signout_button.setOnClickListener(onClickListener)
    }

    fun signInButtonClicked(){
        startSignInIntent()
    }

    fun signOutButtonClicked(){

    }

    private fun startSignInIntent() {

        val signInClientBuilder = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build()
        val signInClient = GoogleSignIn.getClient(this, signInClientBuilder)
        val intent = signInClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // The signed in account is stored in the result.
                val signedInAccount = result.signInAccount
            } else {
                var message = result.status.statusMessage
                if (message == null || message.isEmpty()) {
                    message = "Unknown Error"
                }
                AlertDialog.Builder(this).setMessage(message)
                        .setNeutralButton(android.R.string.ok, null).show()
            }
        }
    }
}