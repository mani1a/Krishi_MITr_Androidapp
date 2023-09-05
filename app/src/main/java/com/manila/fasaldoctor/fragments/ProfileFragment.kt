package com.manila.fasaldoctor.fragments

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.activity.LoginActivity
import com.manila.fasaldoctor.databinding.FragmentProfileBinding
import com.manila.fasaldoctor.model.User
import com.manila.fasaldoctor.utils.Layers
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
//    private var sharedName: String? = null
//    private var role: String? = null

    private var _binding : FragmentProfileBinding?= null
    private val binding get() = _binding
//    lateinit var sharedName : String
//    lateinit var role :String


    lateinit var sharedPreferences: SharedPreferences
    lateinit var firebaseDatabase: DatabaseReference
    lateinit var progressBar : ProgressDialog
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var imageUri : Uri
//    lateinit var uploadImg : User
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var userId:String


    override fun onCreate(savedInstanceState: Bundle?) {
//        binding = FragmentProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        arguments?.let {

        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        val firebaseAuth = FirebaseAuth.getInstance()
//        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users")
        firebaseDatabase =FirebaseDatabase.getInstance().getReference()
        firebaseStorage = FirebaseStorage.getInstance()




//Code to show Profile Images
        val userId = firebaseAuth.currentUser?.uid
        val storageReference : StorageReference = firebaseStorage.reference
        val imageRef = storageReference.child("Users_Profile_Images/$userId")
        val localFile = File.createTempFile("ProfileImg","jpeg")
        imageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding?.profileImg?.setImageBitmap(bitmap)
        }.addOnFailureListener {
            Toast.makeText(context,"Some Error Occureed",Toast.LENGTH_SHORT).show()
        }






        if (userId != null) {

            progressBar = ProgressDialog(context)
            progressBar.setTitle("Loading....")
            progressBar.show()

            firebaseDatabase.child("Users").child(userId).get().addOnSuccessListener {

                val name = it.child("name")
                val email = it.child("email")
                val role = it.child("role")

                _binding?.profileUserName?.text = name.value.toString()
                _binding?.profileEmail?.text =  email.value.toString()
                _binding?.profileRole?.text = role.value.toString()

                if (progressBar.isShowing)progressBar.dismiss()

            }

        }

        val imagePicker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->

            if (result.resultCode == RESULT_OK && result.data != null){
                val data : Intent = result.data!!
                imageUri = data.data!!

                binding?.profileImg?.setImageURI(imageUri)

                uploadPImg()

            }

        }

        binding?.uploadprofileimg?.setOnClickListener {
//            imageContract.launch("image/*")
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePicker.launch(intent)



        }

        binding?.btnLogout?.setOnClickListener {
            firebaseAuth.signOut()
            Toast.makeText(context,"Logged Out", Toast.LENGTH_SHORT).show()
            startActivity(Intent(context, LoginActivity::class.java))
//            finish()
//            var sharedPreferences: SharedPreferences? = null

//            sharedPreferences.edit().putBoolean("isLoggedIn",false).apply()
        }







        return binding?.root


    }

    private fun uploadPImg(){


        Layers.showProgressBar(requireContext())

        val userId = FirebaseAuth.getInstance().currentUser?.uid
//        imageUri = User.imageURl as Uri

//        val imageUrl : Uri = User(imageUrl = imageUri) as Uri

        if (userId != null) {
            firebaseStorage.getReference("Users_Profile_Images").child(userId).
            putFile(imageUri).addOnSuccessListener {
                task ->
                task.metadata?.reference?.downloadUrl?.addOnSuccessListener {
//                    var uploadImg : User
                    val uploadPic =  mapOf("Img-url" to it.toString())


//                    uploadImg = User()
//                    User().imageUrl = uploadPic
//                    uploadImg.imageUrl = uploadPic
//                    uploadImg.imageUrl = uploadPic


                    firebaseDatabase.child("Users").child(userId).child("profile_img").setValue(uploadPic)
                        .addOnSuccessListener {
                            Layers.hideProgressBar()
                        Toast.makeText(context,"Profile Image Updated",Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(context,"Failed to Upload,Retry",Toast.LENGTH_SHORT).show()

                    }

                }
            }
        }



    }

    private fun showCustomDialogBox(){
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.layout_upload_cardview)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val image : ImageView? = dialog?.findViewById(R.id.showSelectedImg)
        val uploadbtn : Button? = dialog?.findViewById(R.id.btn_upload)
        val cancelbtn: Button? = dialog?.findViewById(R.id.btn_cancel)

        if (image != null) {
            Glide.with(this).load(imageUri).into(image)
        }

        cancelbtn?.setOnClickListener {
            dialog.dismiss()
        }

        uploadbtn?.setOnClickListener {
            uploadPImg()
        }
        dialog?.show()


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}







