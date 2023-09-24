//package com.manila.fasaldoctor.fragments
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//import com.manila.fasaldoctor.R
//import com.manila.fasaldoctor.activity.FeedPostActivity
//import com.manila.fasaldoctor.adapter.FeedAdapter
//import com.manila.fasaldoctor.databinding.FragmentFeedBinding
//import com.manila.fasaldoctor.model.PostData
//
//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
///**
// * A simple [Fragment] subclass.
// * Use the [Feed2Fragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class Feed2Fragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//
//
//    private var _binding : FragmentFeedBinding? = null
//    private val binding get() = _binding
//
//    lateinit var feedRecyclerView : RecyclerView
//    lateinit var feedAdapter: FeedAdapter
//    lateinit var postList : ArrayList<PostData>
//
//    lateinit var databaseReference: DatabaseReference
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        _binding = FragmentFeedBinding.inflate(inflater,container,false)
//
//        binding?.btnPost?.setOnClickListener {
//            startActivity(Intent(context,FeedPostActivity::class.java))
//        }
//
//        val posterID = FirebaseAuth.getInstance().currentUser?.uid
//        databaseReference = FirebaseDatabase.getInstance().reference
//
//        postList = ArrayList()
//        feedAdapter = FeedAdapter(requireContext(),postList)
////        feedRecyclerView = requireView().findViewById(R.id.recyclerView_feed_fragment)
//        feedRecyclerView = binding?.recyclerViewFeedFragment!!
//        feedRecyclerView.adapter = feedAdapter
//        feedRecyclerView.layoutManager = LinearLayoutManager(context)
//
//
//
//        // codes to show feeds
//        databaseReference.child("PostsSend").addValueEventListener(object : ValueEventListener{
//            @SuppressLint("SuspiciousIndentation")
//            override fun onDataChange(snapshot: DataSnapshot) {
//                postList.clear()
//
//                for (postSnap in snapshot.children){
//
//                    val postss = postSnap.getValue(PostData::class.java)
//
//                        postList.add(postss!!)
//
//                    binding?.feedprogressBar?.visibility = View.GONE
//                    binding?.recyclerViewFeedFragment?.visibility = View.VISIBLE
//
//
//
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//
//        })
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//        return binding?.root
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment Feed2Fragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            Feed2Fragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//}