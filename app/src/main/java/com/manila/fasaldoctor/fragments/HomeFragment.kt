package com.manila.fasaldoctor.fragments

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.activity.ChatMainActivity
import com.manila.fasaldoctor.activity.MLActivity
import com.manila.fasaldoctor.databinding.FragmentHomeBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding : FragmentHomeBinding?= null
    private val binding get() = _binding
    lateinit var firebaseStorageRefrence : FirebaseStorage
    lateinit var firebaseDatabase: DatabaseReference
    var role : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater,container,false)

//        firebaseStorageRefrence = FirebaseStorage.getInstance()
//        binding?.openCamera?.setOnClickListener {
//            val takePicintent =Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//
//            try {
//                startActivityForResult(takePicintent, 100)
//            } catch (e: ActivityNotFoundException) {
//                Toast.makeText(context, "Error" + e.localizedMessage, Toast.LENGTH_LONG).show()
//            }
//        }
//
//        val contract = registerForActivityResult(ActivityResultContracts
//            .GetContent()){
//            binding?.capturedImage3?.setImageURI(it)
//        }
//
//        binding?.selectImage?.setOnClickListener {
//            contract.launch("image/*")
//
//        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        firebaseDatabase = FirebaseDatabase.getInstance().reference
        if (userId != null) {
            firebaseDatabase.child("Users").child(userId).get().addOnSuccessListener {

                role = it.child("role").value.toString()

                if (role == "expert"){
                    binding?.cameraActivityLayout?.visibility = View.VISIBLE
                    binding?.chatLayout?.visibility = View.VISIBLE
                    binding?.progbar?.visibility = View.GONE
                    binding?.txtChat?.text = getString(R.string.chatwithfarmer)
                    binding?.btnChatwithExpert?.text = getString(R.string.chatwithfarmer)
                    binding?.weathercardlayout?.visibility = View.VISIBLE
                    binding?.kisaanNewslayout?.visibility = View.GONE
                    binding?.soilTestingLayout?.visibility = View.GONE
                    binding?.collabrateLayout?.visibility = View.VISIBLE
                }else{
                    binding?.cameraActivityLayout?.visibility = View.VISIBLE
                    binding?.chatLayout?.visibility = View.VISIBLE
                    binding?.progbar?.visibility = View.GONE
                    binding?.weathercardlayout?.visibility = View.VISIBLE
                    binding?.kisaanNewslayout?.visibility = View.VISIBLE
                    binding?.soilTestingLayout?.visibility = View.VISIBLE


                }


            }
        }




        binding?.btnhealcrop?.setOnClickListener {
            startActivity(Intent(context,MLActivity::class.java))
        }

        binding?.btnChatwithExpert?.setOnClickListener {
            startActivity(Intent(context,ChatMainActivity::class.java))

        }








        return binding?.root

//        return inflater.inflate(R.layout.fragment_home, container, false)
    }

//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK){
//            val imagemap = data?.extras?.get("data") as Bitmap
//            binding?.capturedImage3?.setImageBitmap(imagemap)
//
//        }
//        else{
//            super.onActivityResult(requestCode, resultCode, data)
//
//        }
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}