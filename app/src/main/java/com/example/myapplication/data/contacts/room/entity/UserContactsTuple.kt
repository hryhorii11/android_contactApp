package com.example.myapplication.data.contacts.room.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.myapplication.data.users.room.entity.UserDbEntity


data class UserContactsTuple (
    @Embedded val userDbEntity: UserDbEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = UserContactsDbEntity::class,
            parentColumn = "user_id",
            entityColumn = "contact_id"
        )
    )
    val contacts: List<ContactDbEntity>
)

