package com.example.videowithfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.HashMap

class LoginPage : AppCompatActivity() {

    private var emailS: EditText? = null
    private var passwordS: EditText? = null
    private var signUpButton: Button? = null
    private var username: EditText? = null
    private var fullName: EditText? = null
    private var phone: EditText? = null
    private var firebaseAuthS: FirebaseAuth? = null
    private var userS: FirebaseUser? = null
    private var firebaseData: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        emailS = findViewById(R.id.emailSignUp)
        passwordS = findViewById(R.id.passSignUp)
        fullName = findViewById(R.id.nameSignUp)
        username = findViewById(R.id.usernameSignUp)
        phone = findViewById(R.id.phoneSignUp)
        signUpButton = findViewById(R.id.btnSignUp)
        firebaseAuthS = FirebaseAuth.getInstance()
        userS = firebaseAuthS?.currentUser
        firebaseData = FirebaseDatabase.getInstance().reference.child("Users")
            .child(firebaseAuthS?.currentUser!!.uid)


        signUpButton?.setOnClickListener {
            signup()
        }

    }

    //For fresh users.
    private fun signup() {

        val emailText = emailS?.text.toString().trim()
        val passwordText = passwordS?.text.toString().trim()
        val nameText = fullName?.text.toString().trim()
        val usernameText = username?.text.toString().trim()
        val phoneText = phone?.text.toString().trim()

        if (TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText) || TextUtils.isEmpty(
                nameText) || TextUtils.isEmpty(usernameText) || TextUtils.isEmpty(phoneText)
        ) {
            Toast.makeText(this, "Fields can't be empty.", Toast.LENGTH_SHORT).show()
        } else {

            //Registering user with us.
            firebaseAuthS?.createUserWithEmailAndPassword(emailText, passwordText)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Please verify your account.", Toast.LENGTH_SHORT).show()

                    //Sending email for verification.
                    userS?.sendEmailVerification()?.addOnCompleteListener { task1 ->
                        if (task1.isSuccessful) {

                            //for profile.
                            val userInfo = HashMap<String, Any>()
                            userInfo["Name"] = nameText
                            userInfo["Username"] = usernameText
                            userInfo["Phone Number"] = phoneText
                            firebaseData?.updateChildren(userInfo)?.addOnCompleteListener { task2 ->
                                if (!task2.isSuccessful) {
                                    Toast.makeText(this, task2.exception?.message, Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }

                            Toast.makeText(this, "Account created, login now to continue.", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginSignupPage::class.java))
                        } else {
                            Toast.makeText(this, task1.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }

        }

    }
}