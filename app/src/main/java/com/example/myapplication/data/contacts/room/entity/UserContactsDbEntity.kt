package com.example.myapplication.data.contacts.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.myapplication.data.users.room.entity.UserDbEntity

@Entity(
    tableName = "user_contacts",
    foreignKeys = [
        ForeignKey(
            entity = UserDbEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
    ForeignKey(
        entity = ContactDbEntity::class,
        parentColumns = ["id"],
        childColumns = ["contact_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )
    ],
    primaryKeys = ["user_id","contact_id"],
    indices = [ Index("contact_id")]

)
data class UserContactsDbEntity (
    @ColumnInfo(name = "user_id") val accountId: Int,
    @ColumnInfo(name = "contact_id") val contactId: Int,
)



