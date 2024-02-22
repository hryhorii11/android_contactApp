package com.example.myapplication.data.users.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.domain.model.CurrentUser
import com.example.myapplication.domain.model.UserFromLogin

@Entity(
    tableName = "users"
)
data class UserDbEntity(
    @PrimaryKey val id:Int,
    val name: String?,
    val email: String,
    val phone: String?,
    val address: String?,
    val career: String?
)
{
    fun toUserUi()=UserFromLogin(
        id,
        name,
        email,
        phone,
        address,
        career
    )

    companion object {
        fun createEntity(user: CurrentUser): UserDbEntity {
            return UserDbEntity(
                user.id.toInt(),
                user.name,
                user.email,
                user.phone,
                user.address,
                user.career
            )
        }
    }
}




