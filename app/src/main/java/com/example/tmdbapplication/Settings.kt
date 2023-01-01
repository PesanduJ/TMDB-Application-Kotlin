package com.example.tmdbapplication

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.NinePatch
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

lateinit var userProfilePicture:ShapeableImageView
lateinit var userFirstName:EditText
lateinit var userLastName:EditText
lateinit var userEmail:EditText
lateinit var userPhoneNo:EditText
lateinit var btnUpdate:Button

lateinit var database:DatabaseReference
lateinit var firebaseStorage:FirebaseStorage
lateinit var storageReference: StorageReference

lateinit var imageURI: Uri
var pickedPhoto: Uri? =null
var pickedBitMap: Bitmap? =null

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        btnUpdate=findViewById(R.id.updateUser)

        var userDetailsIntent = intent
        var id = userDetailsIntent.getStringExtra("nic")

        if (id != null) {
            getUserDetails(id)
        }

        btnUpdate.setOnClickListener(){

            userProfilePicture = findViewById(R.id.updateUserPic)
            userFirstName = findViewById(R.id.updateUserFirstName)
            userLastName = findViewById(R.id.updateUserLastName)
            userEmail = findViewById(R.id.updateUserEmail)
            userPhoneNo = findViewById(R.id.updateUserPhoneNo)

            if (btnUpdate.text == "Edit Information"){

                userProfilePicture.setEnabled(true)
                userFirstName.setEnabled(true)
                userLastName.setEnabled(true)
                userEmail.setEnabled(true)
                userPhoneNo.setEnabled(true);

                btnUpdate.setText("Update Information")
            }
            else{
                if (id != null) {
                    updateUserDetails(id)
                }
            }

        }

    }

    fun getUserDetails(id:String){

        userProfilePicture = findViewById(R.id.updateUserPic)
        userFirstName = findViewById(R.id.updateUserFirstName)
        userLastName = findViewById(R.id.updateUserLastName)
        userEmail = findViewById(R.id.updateUserEmail)
        userPhoneNo = findViewById(R.id.updateUserPhoneNo)

        database = FirebaseDatabase.getInstance().getReference("Users")
        var query: Query = database.orderByChild("nic").equalTo("$id")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    for (snapshot in snapshot.children) {
                        var firstName = snapshot.child("firstName").value.toString()
                        var lastName = snapshot.child("lastName").value.toString()
                        var email = snapshot.child("email").value.toString()
                        var phoneNo = snapshot.child("phoneNo").value.toString()

                        //set texts
                        userFirstName.setText("${firstName.toString()}")
                        userLastName.setText("${lastName.toString()}")
                        userEmail.setText("${email.toString()}")
                        userPhoneNo.setText("${phoneNo.toString()}")

                        getUserImage(id.toString())

                        userProfilePicture.setEnabled(false)
                        userFirstName.setEnabled(false)
                        userLastName.setEnabled(false)
                        userEmail.setEnabled(false)
                        userPhoneNo.setEnabled(false);
                    }
                }
                else{
                    Toast.makeText(applicationContext,"Unfortunate!", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getUserImage(nic:String){
        userProfilePicture = findViewById(R.id.updateUserPic)
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference.child("Users$nic")

        val localfile = File.createTempFile("tempImage","png")
        storageReference.getFile(localfile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            userProfilePicture.setImageBitmap(bitmap)

        }.addOnFailureListener {
        }

    }

    fun updateUserDetails(id: String){

        userProfilePicture = findViewById(R.id.updateUserPic)
        userFirstName = findViewById(R.id.updateUserFirstName)
        userLastName = findViewById(R.id.updateUserLastName)
        userEmail = findViewById(R.id.updateUserEmail)
        userPhoneNo = findViewById(R.id.updateUserPhoneNo)
        btnUpdate=findViewById(R.id.updateUser)

        database = FirebaseDatabase.getInstance().getReference("Users")

        var firstName = userFirstName.text.toString()
        var lastName = userLastName.text.toString()
        var email = userEmail.text.toString()
        var phoneNo = userPhoneNo.text.toString()

        var updateData = mapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email,
            "phoneNo" to phoneNo
        )

        database.child(id).updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(applicationContext,"User information has been updated!", Toast.LENGTH_SHORT).show()

            if(pickedBitMap == null){

            }else{
                uploadProfilePic(id.toString())
            }

            btnUpdate.setText("Edit Information")


        }.addOnFailureListener {
            Toast.makeText(applicationContext,"Something Went Wrong!!", Toast.LENGTH_SHORT).show()
        }

    }

    //**********************************************************************************************
    //gettiing user image from galary
    fun pickUserPhoto(view: View){
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }else{
            val galeriIntext = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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
                val galeriIntext = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntext,2)
            }
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        userProfilePicture =findViewById(R.id.updateUserPic)
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
            pickedPhoto = data.data
            if (Build.VERSION.SDK_INT >= 28){
                val source = ImageDecoder.createSource(this.contentResolver,pickedPhoto!!)
                pickedBitMap = ImageDecoder.decodeBitmap(source)
                userProfilePicture.setImageBitmap(pickedBitMap)

            }else{
                pickedBitMap = MediaStore.Images.Media.getBitmap(this.contentResolver,pickedPhoto)
                userProfilePicture.setImageBitmap(pickedBitMap)

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun uploadProfilePic(userNIC :String) {

        //imageURI = Uri.parse("android.resource://$packageName/${R.drawable.user_profile_icon}")
        imageURI = pickedPhoto!!
        storageReference = FirebaseStorage.getInstance().getReference("Users$userNIC")
        storageReference.putFile(imageURI).addOnSuccessListener {
        }.addOnFailureListener {
            Toast.makeText(applicationContext, "Unable to upload user image!", Toast.LENGTH_SHORT).show()
        }

    }
}