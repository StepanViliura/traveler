package com.example.traveler.database

import com.example.traveler.data.UserInfo

class SignedUser {
    public var userInfo: UserInfo?=null

    companion object {
        fun getInstance() : SignedUser = signedUser
    }
}

private
val signedUser = SignedUser()