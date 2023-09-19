package com.manila.fasaldoctor.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.adapter.RecentUsersAdapter
import com.manila.fasaldoctor.databinding.FragmentRecentChatsBinding
import com.manila.fasaldoctor.model.RecentUser

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecentChatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecentChatsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding : FragmentRecentChatsBinding? = null
    private val binding get() = _binding

    lateinit var recentRecyclerView: RecyclerView
    lateinit var recentUsersAdapter: RecentUsersAdapter
    lateinit var recentUserList : ArrayList<RecentUser>

    lateinit var firebaseDatabase: DatabaseReference
    lateinit var firebaseAuth: FirebaseAuth

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
        _binding = FragmentRecentChatsBinding.inflate(layoutInflater)

        recentUserList = ArrayList()
//        recentRecyclerView = findViewById(R.id.user_recent_recycler)
        recentRecyclerView = binding!!.userRecentRecycler
        recentUsersAdapter = RecentUsersAdapter(requireContext(),recentUserList)
        recentRecyclerView.layoutManager = LinearLayoutManager(context)
        recentRecyclerView.adapter = recentUsersAdapter

        firebaseDatabase = FirebaseDatabase.getInstance().reference
        firebaseAuth = FirebaseAuth.getInstance()

        val senderuid = firebaseAuth.currentUser!!.uid

//        supportActionBar?.title = "Recent Chats"
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        firebaseDatabase.child("RecentUsers").child(senderuid).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                recentUserList.clear()

                for (postsnap in snapshot.children){
                    val recentUser = postsnap.getValue(RecentUser::class.java)

                    if (recentUser == null){
                        binding?.txt?.visibility = View.VISIBLE
                        binding?.userRecentRecycler?.visibility = View.GONE
                        binding?.progbarr?.visibility = View.GONE

                    }else{
                        recentUserList.add(recentUser)
                        binding?.userRecentRecycler?.visibility = View.VISIBLE
                        binding?.progbarr?.visibility = View.GONE
                        binding?.txt?.visibility = View.GONE

                    }



                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })





        return binding?.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RecentChatsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecentChatsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}