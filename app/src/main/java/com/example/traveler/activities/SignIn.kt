package com.example.traveler.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiActivity
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_sign_in.*

import com.google.android.gms.tasks.Task
import android.support.annotation.Nullable
import com.google.android.gms.tasks.OnCompleteListener
import android.util.Log
import android.widget.Toast
import com.example.traveler.MainActivity
import com.example.traveler.R
import com.example.traveler.data.UserInfo

import com.example.traveler.database.SignedUser
import com.google.firebase.firestore.FirebaseFirestore

class SignIn : AppCompatActivity(){

    val RC_SIGN_IN: Int = 1
    val TAG: String = "SIGNIN"
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    var account: GoogleSignInAccount? = null
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        firebaseAuth = FirebaseAuth.getInstance()
        account = GoogleSignIn.getLastSignedInAccount(applicationContext)
        configureGoogleSignIn()
        setupUI()
    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            if (account != null) {
                startActivity(LogoSplash.getLaunchIntent(this))
                saveUserInFirestore(account)
            }

        }
    }

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .requestId()
            .requestProfile()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    private fun setupUI() {
        google_button.setOnClickListener {
            signIn()
        }

        disconnect_tv.setOnClickListener {
            disconnectAccount()
        }
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun disconnectAccount () {
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this, OnCompleteListener {
            Toast.makeText(this, "Google account disconnected!", Toast.LENGTH_LONG).show()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    this.account = account
                    firebaseAuthWithGoogle(account)
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                startActivity(LogoSplash.getLaunchIntent(this))
                saveUserInFirestore(acct)
            } else {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveUserInFirestore(acct: GoogleSignInAccount?) {
        val database = FirebaseFirestore.getInstance()
        val user = database.document("user/${acct?.id}")
        user.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    val info = document.get("userInfo") as HashMap<String, Any?>
                    SignedUser.getInstance().userInfo = UserInfo(
                        info.get("id").toString(),
                        info.get("email").toString(),
                        info.get("name").toString(),
                        info.get("surname").toString(),
                        info.get("car").toString()
                    )
                } else {
                    val credentials = HashMap<String, UserInfo>()
                    val userData = UserInfo(acct?.id, acct?.email, acct?.givenName, acct?.familyName, "")
                    SignedUser.getInstance().userInfo = userData;
                    credentials.put("userInfo", userData)
                    user.set(credentials).addOnCompleteListener {
                        Log.d(TAG, "User stored: ${userData.toString()}")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        /*val credentials = HashMap<String, UserInfo>()
        val userData = UserInfo(acct.id, acct.email, acct.givenName, acct.familyName)
        credentials.put("userInfo", userData)
        user.set(credentials).addOnCompleteListener {
            Toast.makeText(applicationContext, "User stored", Toast.LENGTH_LONG).show()
        }*/
    }


    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, SignIn::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

}