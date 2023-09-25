package com.manila.fasaldoctor.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.TextKeyListener.clear
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.adapter.FilteredUserAdapter
import com.manila.fasaldoctor.adapter.UsersAdapter
import com.manila.fasaldoctor.databinding.FragmentChatBinding
import com.manila.fasaldoctor.model.RecentUser
import com.manila.fasaldoctor.model.User

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding : FragmentChatBinding? = null
    private val binding get() = _binding

    lateinit var fbDatabase : DatabaseReference
    lateinit var firebaseAuth: FirebaseAuth

    lateinit var userrecyclerView : RecyclerView
    lateinit var userrecyclerAdapter: UsersAdapter
    lateinit var layoutManager: GridLayoutManager
    lateinit var userList : ArrayList<User>

    var crop1 : String? = null
    var crop2 : String? = null
    var crop3 : String? = null

    lateinit var role : String

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
        _binding = FragmentChatBinding.inflate(layoutInflater)

        firebaseAuth = FirebaseAuth.getInstance()
        fbDatabase = FirebaseDatabase.getInstance().reference

        binding?.checkPotato?.setOnCheckedChangeListener { buttonView, isChecked ->
            crop1 = "Potato"
        }
        binding?.checkTomato?.setOnCheckedChangeListener { buttonView, isChecked ->
            crop2 = "Tomato"
        }

        fbDatabase.child("Users").child(firebaseAuth.currentUser!!.uid).child("role").get()
            .addOnSuccessListener {
                role = it.value.toString()
                if (role == "farmer"){
                    binding?.dilterlayout?.visibility = View.VISIBLE
                }else{
                    binding?.dilterlayout?.visibility = View.GONE
                }

        }

        binding?.dilterlayout?.setOnClickListener {
            binding?.choosetochatLayout?.visibility = View.VISIBLE
            binding?.IVFilterdown?.visibility = View.GONE
            binding?.IVFilterUP?.visibility = View.VISIBLE
            binding?.txt?.text = "Click Icon To Collapse"

        }
        binding?.IVFilterUP?.setOnClickListener {
            binding?.choosetochatLayout?.visibility = View.GONE
            binding?.IVFilterdown?.visibility = View.VISIBLE
            binding?.IVFilterUP?.visibility = View.GONE
            binding?.txt?.text = "Click To Filter"
//            binding?.userRecycler?.visibility = View.VISIBLE

        }

        //recycler view codes
        userList = ArrayList()
        userrecyclerAdapter = UsersAdapter(requireContext(),userList)
//        userrecyclerView = view?.findViewById(R.id.user_recycler)!!
        userrecyclerView = binding!!.userRecycler
        userrecyclerView.layoutManager = LinearLayoutManager(context)
        userrecyclerView.adapter = userrecyclerAdapter

        // users list ---- codes
        fbDatabase.child("Users").addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()

                val role = snapshot.child(firebaseAuth.currentUser!!.uid).child("role").value.toString()

                for (postSnapshot in snapshot.children){

                    val currentUser = postSnapshot.getValue(User::class.java)

                    if ((firebaseAuth.currentUser?.uid != currentUser?.uid) && (currentUser?.role != role)){

                        userList.add(currentUser!!)

                        binding?.userRecycler?.visibility = View.VISIBLE
                        binding?.progbarr?.visibility = View.GONE

                    }




                }
                userrecyclerAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(context,"Some error Occurred , Please Restart the App",
                    Toast.LENGTH_LONG).show()

            }

        })















        return binding?.root
    }

    fun message(){
        fbDatabase.child("RecentUsers").child(firebaseAuth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (list in snapshot.children){
                        val message = list.getValue(RecentUser::class.java)

                        if (message?.messages == "true"){
                            

                        }


                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }




















    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}