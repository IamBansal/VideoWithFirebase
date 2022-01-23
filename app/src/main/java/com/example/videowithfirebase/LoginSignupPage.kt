package com.example.videowithfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginSignupPage : AppCompatActivity() {

    private var email: EditText? = null
    private var password: EditText? = null
    private var signInButton: Button? = null
    private var forgot: TextView? = null
    private var signInGoogleText: TextView? = null
    private var signInGoogleLogo: ImageView? = null
    private var newUser: TextView? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var user: FirebaseUser? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_signup_page)

        email = findViewById(R.id.etEmail)
        password = findViewById(R.id.etPassword)
        signInButton = findViewById(R.id.btnLogin)
        forgot = findViewById(R.id.tvForgot)
        newUser = findViewById(R.id.tvNewUser)
        firebaseAuth = FirebaseAuth.getInstance()
        user = firebaseAuth?.currentUser

        signInButton?.setOnClickListener {
            login()
        }

        forgot?.setOnClickListener {
            resetPassword()
        }

        newUser?.setOnClickListener {
            signUpNewUser()
        }

    }

    //For signing up new user.
    private fun signUpNewUser() {
        startActivity(Intent(this, LoginPage::class.java))
    }

    //Reset new password if forgotten the old one.
    private fun resetPassword() {
        startActivity(Intent(this, ResetPassword::class.java))
    }

    //For signing in registered and verified user.
    private fun login() {

        val emailText = email?.text.toString().trim()
        val passwordText = password?.text.toString().trim()

        //Checking if any field is empty or not.
        if (TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText)) {
            Toast.makeText(applicationContext, "Fields can't be empty", Toast.LENGTH_SHORT).show()

//            TODO("Have to use alert dialog instead of toast.")

        } else {
            //Signing in the user.
            firebaseAuth?.signInWithEmailAndPassword(emailText, passwordText)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        //For signing in only verified users.
                        if (user?.isEmailVerified == true) {
                            startActivity(Intent(this, AppMainPage::class.java))

                            Toast.makeText(
                                applicationContext,
                                "Signed in successfully.",
                                Toast.LENGTH_SHORT
                            ).show()

                            //For clearing details entered once signed in.
                            email?.text = null
                            password?.text = null

                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Email not verified.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            task.exception?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

    }

    //On pressing back.
    override fun onBackPressed() {
        finishAffinity()
    }

}