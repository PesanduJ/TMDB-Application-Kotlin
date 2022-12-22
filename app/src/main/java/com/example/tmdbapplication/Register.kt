package com.example.tmdbapplication

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Register : AppCompatActivity() {

    var pickedPhoto: Uri? =null
    var pickedBitMap: Bitmap? =null

    lateinit var userPic: ShapeableImageView
    lateinit var userFisrtName: EditText
    lateinit var userLastName: EditText
    lateinit var userNIC: EditText
    lateinit var userEmail: EditText
    lateinit var userPhoneNo: EditText
    lateinit var userPasssword: EditText
    lateinit var userConfirmPasssword: EditText
    lateinit var registerUser: Button

    lateinit var database: DatabaseReference
    lateinit var storage: StorageReference
    lateinit var imageURI: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerUser = findViewById(R.id.registerUser)

        registerUser.setOnClickListener() {
            registerUser()
        }

    }

    fun pickPhoto(view: View){
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }else{
            val galeriIntext = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntext,2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1){
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val galeriIntext = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntext,2)
            }
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        userPic =findViewById(R.id.userPic)
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
            pickedPhoto = data.data
            if (Build.VERSION.SDK_INT >= 28){
                val source = ImageDecoder.createSource(this.contentResolver,pickedPhoto!!)
                pickedBitMap = ImageDecoder.decodeBitmap(source)
                userPic.setImageBitmap(pickedBitMap)
                Toast.makeText(applicationContext,"$pickedPhoto",Toast.LENGTH_LONG).show()
            }else{
                pickedBitMap = MediaStore.Images.Media.getBitmap(this.contentResolver,pickedPhoto)
                userPic.setImageBitmap(pickedBitMap)
                Toast.makeText(applicationContext,"$pickedPhoto",Toast.LENGTH_LONG).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun registerUser() {

        userPic = findViewById(R.id.userPic)
        userFisrtName = findViewById(R.id.userRegisterFirstName)
        userLastName = findViewById(R.id.userRegisterLastName)
        userNIC = findViewById(R.id.userRegisterNIC)
        userEmail = findViewById(R.id.userRegisterEmail)
        userPhoneNo = findViewById(R.id.userRegisterPhoneNo)
        userPasssword = findViewById(R.id.userRegisterPassword)
        userConfirmPasssword = findViewById(R.id.userRegisterConfirmPassword)

        database = FirebaseDatabase.getInstance().getReference("Users")
        val User = User(
            userFisrtName.text.toString(),
            userLastName.text.toString(),
            userNIC.text.toString(),
            userEmail.text.toString(),
            userPhoneNo.text.toString(),
            userPasssword.text.toString()
        )


        //validation of user registration
        if (userFisrtName.text.toString() == "") {
            Toast.makeText(applicationContext, "Enter first name!", Toast.LENGTH_SHORT).show()
        } else if (userLastName.text.toString() == "") {
            Toast.makeText(applicationContext, "Enter last name!", Toast.LENGTH_SHORT).show()
        } else if (userNIC.text.toString() == "") {
            Toast.makeText(applicationContext, "NIC must be provided!", Toast.LENGTH_SHORT).show()
        } else if (userEmail.text.toString() == "") {
            Toast.makeText(applicationContext, "Email must be provided!", Toast.LENGTH_SHORT).show()
        } else if (userPhoneNo.text.toString() == "") {
            Toast.makeText(
                applicationContext,
                "Working phone number must be provided!",
                Toast.LENGTH_SHORT
            ).show()
        } else if (userPasssword.text.toString() == "" || userConfirmPasssword.text.toString() == "") {
            Toast.makeText(applicationContext, "Enter a strong password!", Toast.LENGTH_SHORT)
                .show()
        } else if (userPasssword.text.toString() != userConfirmPasssword.text.toString()) {
            Toast.makeText(
                applicationContext,
                "Confirm password should eb equal to password!",
                Toast.LENGTH_SHORT
            ).show()
        } else {

            database.child(userNIC.text.toString()).setValue(User).addOnCompleteListener {

                if (it.isSuccessful) {
                    uploadProfilePic(userNIC.text.toString())
                    var myintent = Intent(applicationContext,Login::class.java)
                    startActivity(myintent)
                }

            }.addOnFailureListener {
                Toast.makeText(applicationContext, "Error occurred!", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun uploadProfilePic(userNIC :String) {

        //imageURI = Uri.parse("android.resource://$packageName/${R.drawable.user_profile_icon}")
        imageURI = pickedPhoto!!
        storage = FirebaseStorage.getInstance().getReference("Users$userNIC")
        storage.putFile(imageURI).addOnSuccessListener {
            Toast.makeText(this@Register, "User Registered!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this@Register, "Error occurred!", Toast.LENGTH_SHORT).show()
        }

    }






}