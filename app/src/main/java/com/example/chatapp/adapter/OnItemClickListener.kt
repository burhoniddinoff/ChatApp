package com.example.chatapp.adapter

import com.example.chatapp.model.User

interface OnItemClickListener {
    fun onClick(user: User)
}