package com.manila.fasaldoctor.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.activity.LoginActivity
import com.manila.fasaldoctor.databinding.FragmentProfileBinding

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
    lateinit var sharedName : String
    lateinit var role :String

    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
//        binding = FragmentProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        arguments?.let {
            sharedName = it.getString("userName").toString()
            role = it.getString("role").toString()
//            val sharedName = arguments?.getString("userName")
//            val role = arguments?.getString("checkedRole")
        }





    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val firebaseStorage = FirebaseStorage.getInstance()



        firebaseDatabase.getReference("Users").child(role).child(sharedName).get()
            .addOnSuccessListener {
                val name = it.child("name")
                val email = it.child("email")
                binding?.profileUserName?.text = name.toString()
                binding?.profileEmail?.text = email.toString()
            }



//        binding?.profileImg


        binding?.btnLogout?.setOnClickListener {
            firebaseAuth.signOut()
            Toast.makeText(context,"Logged Out", Toast.LENGTH_SHORT).show()
            startActivity(Intent(context, LoginActivity::class.java))
//            finish()
//            var sharedPreferences: SharedPreferences? = null

//            sharedPreferences.edit().putBoolean("isLoggedIn",false).apply()
        }















        return binding?.root

//        val view = inflater.inflate(R.layout.fragment_profile, container, false)
//        btnUpload = view.findViewById()






//        return view
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