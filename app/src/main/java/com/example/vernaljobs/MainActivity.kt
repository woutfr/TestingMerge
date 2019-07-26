package com.example.vernaljobs
// do you see this bram
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.vernaljobs.FAQ
import com.example.vernaljobs.Register
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {


//    lateinit var ButtonHoverMe: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        ButtonHoverMe = findViewById(R.id.login_button)
    }

    fun registerMe(view: View) {

        // Create an Intent to start the second activity
        val registerIntent = Intent(this, Register::class.java)

        // Start the new activity.
        startActivity(registerIntent)
    }

    fun goToFAQ(view: View) {

        // Create an Intent to start the second activity
        val registerIntent = Intent(this, FAQ::class.java)

        // Start the new activity.
        startActivity(registerIntent)
    }



//    fun goToForgotPassword(view: View) {
//
//        // Create an Intent to start the second activity
//        val registerIntent = Intent(this, forgotpassword::class.java)
//
//        // Start the new activity.
//        startActivity(registerIntent)
//    }
    fun performLogin(view: View) {
        val email = main_edittext_email.text.toString()
        val password = main_edittext_password.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both e-mail address and password.", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("Login", "Email is: $email")
        Log.d("Login", "Password is $password")

        // now initiate Firebase communication for signing in

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // else if successful
                Log.d("Login", "Successfully logged in: ${it.result?.user?.uid}")
                Toast.makeText(this, "Successfully logged in.", Toast.LENGTH_SHORT).show()

                // [TO DO] create an intent to start the second activity, if company this should be the post a vacancy page
                // otherwise this should be the page with all the vacancies

                val loginIntent = Intent(this, Navview::class.java)
                loginIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(loginIntent)
            }
            .addOnFailureListener {
                Log.d("Login", "Failed to log in: ${it.message}")
                Toast.makeText(this, "Failed to log in: ${it.message}", Toast.LENGTH_SHORT).show()
            }

    }

}
