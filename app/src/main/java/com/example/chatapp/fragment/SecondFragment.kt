package com.example.chatapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.activity.MainActivity
import com.example.chatapp.adapter.MessageAdapter
import com.example.chatapp.databinding.FragmentSecondBinding
import com.example.chatapp.model.Message
import com.example.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.text.format.DateFormat
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private var user: User? = null
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val messageAdapter by lazy { MessageAdapter(auth.currentUser?.uid!!) }
    private val messageList: MutableList<Message> = mutableListOf()
    private val dbRef by lazy { FirebaseDatabase.getInstance().reference }
    private var senderRoom: String? = null
    private var receiveRoom: String? = null
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = arguments?.getParcelable("user")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uid = auth.currentUser?.uid!!
        (activity as MainActivity).supportActionBar?.title = user?.name
        linearLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = messageAdapter
        }
        senderRoom = user?.uid + uid
        receiveRoom = uid + user?.uid
        chatMessages(senderRoom!!)
        binding.btnSend.setOnClickListener {
            val text = binding.editText.text.toString().trim()
            messageAdapter.notifyDataSetChanged()
            if (text.isNotBlank()) {
                val time = DateFormat.format("HH:mm:ss", Date().time)
                val message = Message(text, uid, time.toString())
                dbRef.child("chats/$senderRoom/messages").push()
                    .setValue(message).addOnSuccessListener {
                        dbRef.child("chats/$receiveRoom/messages").push()
                            .setValue(message)
                    }
                binding.editText.setText("")
                (binding.recyclerView.layoutManager as LinearLayoutManager).scrollToPosition(messageAdapter.itemCount-1)
            }
        }
    }

    private fun chatMessages(senderRoom: String) {
        dbRef.child("chats/$senderRoom/messages").addValueEventListener(object :
            ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (message in snapshot.children) {
                    messageList.add(message.getValue(Message::class.java)!!)
                }
                messageAdapter.messageList = messageList
                Log.d("@@@", messageList.toString())
                messageAdapter.notifyDataSetChanged()
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
}