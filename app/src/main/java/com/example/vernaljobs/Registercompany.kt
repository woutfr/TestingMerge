package com.example.vernaljobs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register_company.*


class Registercompany : AppCompatActivity() {

    // the user has enter the company register page, which means he/she declared to be company => company = true
    companion object {
        var company: Boolean = true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_company)

        lateinit var option: Spinner
//        lateinit var result: TextView

        option = findViewById(R.id.spinner) as Spinner
//        result = findViewById(R.id.Spinner_text) as TextView

        val options = arrayOf("Delft","Elsewhere")

        option.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, options)

//        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//                result.text = "Please select an option"
//            }

//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                result.text = options.get(p2)
//            }



    }

    fun registerBack(view: View) {

        // Create an Intent to start the second activity
        val registerIntent = Intent(this, MainActivity::class.java)

        // Start the new activity.
        startActivity(registerIntent)

    }

    fun performRegister(view: View){
        val email = registercompany_edittext_email.text.toString()
        val password = registercompany_edittext_password.text.toString()
        val name = registercompany_edittext_companyname.text.toString()
        val phone = registercompany_edittext_phone.text.toString()
        val btw = registercompany_edittext_btw.text.toString()

        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty() || btw.isEmpty()) {
            Toast.makeText(this, "Please enter text in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (!registercompany_checkbox.isChecked) {
            Toast.makeText(this, "Please acknowledge the Terms & Conditions by checking the checkbox.", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("Register_company", "Email is: $email")
        Log.d("Register_company", "Password: $password")

        // FireBase authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // else if successful
                Log.d("Register", "Successfully created user with uid: ${it.result?.user?.uid}")
                Toast.makeText(this, "Successfully created account. You can now log in with the email address and password.", Toast.LENGTH_LONG).show()

                saveUserToFireDatabase()

                // now direct the user to the login page, the class of the activity after this one should take info about the user with it
                // because it will use it to determine whether the vacancy page (students) is opened or the post a vacancy page (company)

                // Create an Intent to start the second activity
                val registerIntent = Intent(this, MainActivity::class.java)

                // Pass the boolean company with it
                // registerIntent.putExtra("company", company) [only if it works and if is necessary]

                // when the user wants to go back, it closes the app (otherwise they would potentially register multiple times
                registerIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)

                // Start the new activity.
                startActivity(registerIntent)

                // pass the company boolean to the login page, such that the app can open the next screen properly for both student and company
                // val intent = Intent(this@Register_company,MainActivity::class.java)
                // intent.putExtra("Username","John Doe")  this one doesnt work properly, maybe not necessary
                // startActivity(intent)
            }
            .addOnFailureListener {
                Log.d("Register", "Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun saveUserToFireDatabase(){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, company, registercompany_edittext_companyname.text.toString(), registercompany_edittext_btw.text.toString(), registercompany_edittext_phone.text.toString(), registercompany_edittext_email.text.toString())

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Register_company", "We saved the user to Firebase Database")
            }
    }


}

// make a class that represents a user in the entire application

class User(val uid: String, val company: Boolean, val name: String, val btw: String, val phone: String, val email: String)




