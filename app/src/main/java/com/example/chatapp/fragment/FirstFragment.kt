package com.example.chatapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.R
import com.example.chatapp.activity.MainActivity
import com.example.chatapp.adapter.OnItemClickListener
import com.example.chatapp.adapter.UserAdapter
import com.example.chatapp.databinding.FragmentFirstBinding
import com.example.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirstFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val dbRef by lazy { FirebaseDatabase.getInstance().getReference("users") }
    private val userList: MutableList<User> = mutableListOf()
    private val userAdapter by lazy { UserAdapter(this) }
    private val uid by lazy { FirebaseAuth.getInstance().currentUser?.uid!! }
    private var name: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
        }
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (post in snapshot.children) {
                    val user = post.getValue(User::class.java)!!
                    if (uid != user.uid) {
                        userList.add(user)
                    } else {
                        name = user.name
                    }
                }
                userAdapter.submitList(userList)
                userAdapter.notifyDataSetChanged()
                binding.progressBar.isVisible = false
                (activity as MainActivity).supportActionBar?.title = name

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("@@@", error.message)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(user: User) {
        val bundle = bundleOf("user" to user)
        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
    }
}