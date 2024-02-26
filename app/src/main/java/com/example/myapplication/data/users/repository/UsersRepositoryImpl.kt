package com.example.myapplication.data.users.repository

import com.example.myapplication.data.api.UserService
import com.example.myapplication.data.datastore.DataStorePreferences
import com.example.myapplication.data.room.AppDatabase
import com.example.myapplication.data.users.room.entity.UserDbEntity
import com.example.myapplication.domain.model.CurrentUser
import com.example.myapplication.domain.model.EditBody
import com.example.myapplication.domain.model.LoginData
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.model.UserFromLogin
import com.example.myapplication.presentation.utils.Constants.AUTHORIZATION_HEADER
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val remoteData: UserService,
    private val db: AppDatabase,
    private val dataStore: DataStorePreferences
) : UsersRepository {
    private fun <T> doRequest(
        request: suspend () -> T,
        dbAction: (T) -> Unit,
    ) = flow {
        request().also { data ->
            dbAction(data)
            emit(Result.success(data))
        }
    }.flowOn(Dispatchers.IO).catch { exception ->
        emit(Result.failure(exception))
    }

    override fun login(loginData: LoginData) = doRequest(
        request = {
            remoteData.loginUser(loginData).also {
                currentUser = it.data.user.toCurrentUser(it.data.accessToken, it.data.refreshToken)
            }
        },
        dbAction = {
            db.getUsersDao().upsertUser(UserDbEntity.createEntity(currentUser))
        }

    )

    override fun register(user: User) = doRequest(
        request = {
            remoteData.registerUser(user).also {
                currentUser = it.data.user.toCurrentUser(it.data.accessToken, it.data.refreshToken)
            }
        },
        dbAction = { db.getUsersDao().upsertUser(UserDbEntity.createEntity(currentUser)) }
    )


    override fun editUser(body: EditBody) = doRequest(
        request = {
            remoteData.editUser(
                AUTHORIZATION_HEADER + currentUser.accessToken,
                currentUser.id.toInt(),
                body
            )
                .also {
                    currentUser =
                        it.data.user.toCurrentUser(
                            currentUser.accessToken,
                            currentUser.refreshToken
                        )
                }
        },
        dbAction = { db.getUsersDao().upsertUser(UserDbEntity.createEntity(currentUser)) }
    )

    override fun refreshToken() = doRequest(
        request = {
            currentUser = currentUser.copy(id = dataStore.getUserIdTokens().id.toLong())
            remoteData.refreshToken(dataStore.getUserIdTokens().refreshToken).also {
                currentUser = currentUser.copy(
                    accessToken = it.data.accessToken,
                    refreshToken = it.data.refreshToken
                )
            }
        },
        dbAction = {},
    )

    override fun getUser(userId: Int): UserFromLogin {
        return db.getUsersDao().getUser(userId).toUserUi()
    }

    companion object {
        var currentUser = CurrentUser(email = "")
    }
}
