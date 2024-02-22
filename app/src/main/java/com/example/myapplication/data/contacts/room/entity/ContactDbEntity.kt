package com.example.myapplication.data.contacts.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.domain.model.UserFromLogin

@Entity(
    tableName = "contacts"
)
data class ContactDbEntity(
    @ColumnInfo(name="id") @PrimaryKey() val id:Int,
    @ColumnInfo(name="name") val name: String?,
    val email:String?,
    val phone:String?,
    val career:String?,
    val address:String?,
) {
    fun toContact():UserFromLogin=UserFromLogin(
        id,
        name,
        email!!,
        phone,
        address,
        career
    )
companion object{
    fun createEntity(contact:UserFromLogin):ContactDbEntity=ContactDbEntity(
        contact.id,
        contact.name,
        contact.email,
        contact.phone,
        contact.career,
        contact.address
    )

}
}

