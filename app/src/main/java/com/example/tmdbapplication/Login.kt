package com.example.tmdbapplication

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class Login : AppCompatActivity() {

    lateinit var userEmail: EditText
    lateinit var userPassword: EditText
    lateinit var cbRemember: CheckBox
    lateinit var login: Button
    lateinit var register: Button

    lateinit var database: DatabaseReference
    lateinit var SQLiteDB: SQLiteDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login = findViewById(R.id.login)
        register = findViewById(R.id.register)
        userEmail = findViewById(R.id.userEmail)
        userPassword = findViewById(R.id.userPassword)
        cbRemember = findViewById(R.id.cb_remember)


        //checkSQLiteForCredentials()

        login.setOnClickListener() {

            if (userEmail.text.toString() == "" || userPassword.text.toString() == "") {
                Toast.makeText(applicationContext, "Enter credentials!", Toast.LENGTH_SHORT).show()
            } else {
                login(userEmail.text.toString(), userPassword.text.toString())
            }
        }

        register.setOnClickListener() {
            var intent = Intent(applicationContext, Register::class.java)
            startActivity(intent)
        }
    }

    fun login(userEmail: String, userPassword: String) {

        cbRemember = findViewById(R.id.cb_remember)
        var userList = mutableListOf<User>()

        var firstName: String = ""
        var lastName: String = ""
        var nic: String = ""
        var phoneNo: String = ""
        var password: String = ""

        database = FirebaseDatabase.getInstance().getReference("Users")
        var query: Query = database.orderByChild("email").equalTo("$userEmail")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (snapshot in snapshot.children) {
                        snapshot.getValue(User::class.java)?.let { userList.add(it) }
                    }

                    for (data in userList) {
                        firstName = data.firstName.toString()
                        lastName = data.lastName.toString()
                        nic = data.nic.toString()
                        phoneNo = data.phoneNo.toString()
                        password = data.password.toString()
                    }

                    if (userPassword == password) {

                        if (cbRemember.isChecked) {
                            saveUserDetailsToSQLite(
                                firstName,
                                lastName,
                                nic,
                                userEmail,
                                phoneNo,
                                password
                            )
                        }

                        var myintent = Intent(applicationContext, UserDashboard::class.java)
                        myintent.putExtra("UserID",nic)
                        startActivity(myintent)
                    } else {
                        Toast.makeText(applicationContext, "Wrong credentials!", Toast.LENGTH_SHORT)
                            .show()
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun saveUserDetailsToSQLite(
        firstName: String,
        lastName: String,
        nic: String,
        email: String,
        phoneNo: String,
        password: String
    ) {
        SQLiteDB = openOrCreateDatabase("UserDB", MODE_PRIVATE, null)
        SQLiteDB.execSQL("CREATE TABLE IF NOT EXISTS UserData(firstName varchar(100),lastName varchar(100),nic varchar(100),email varchar(100),phoneNo varchar(100),password varchar(100))")
        SQLiteDB.execSQL("INSERT INTO UserData VALUES('"+firstName+"','"+lastName+"','"+nic+"','"+email+"','"+phoneNo+"','"+password+"')")
    }

    fun checkSQLiteForCredentials() {

        userEmail = findViewById(R.id.userEmail)
        userPassword = findViewById(R.id.userPassword)
        lateinit var myCursor: Cursor

//        SQLiteDB = openOrCreateDatabase("UserDB", MODE_PRIVATE, null)
//        SQLiteDB.execSQL("CREATE TABLE IF NOT EXISTS UserData(firstName varchar(100),lastName varchar(100),nic varchar(100),email varchar(100),phoneNo varchar(100),password varchar(100))")

        myCursor = SQLiteDB.rawQuery("SELECT * FROM UserData", null)

        if(myCursor.position == -1){

        }
        else{
            myCursor.moveToFirst()
            userEmail.setText("${myCursor.getString(3)}")
            userPassword.setText("${myCursor.getString(5)}")
        }






    }

}