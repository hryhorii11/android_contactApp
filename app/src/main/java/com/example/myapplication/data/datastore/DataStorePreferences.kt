package com.example.myapplication.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.myapplication.domain.model.LoginData
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.model.UserIdTokens
import kotlinx.coroutines.flow.first

import javax.inject.Inject


class DataStorePreferences @Inject constructor(private val dataStore: DataStore<Preferences>) {
    private val ACCESS_TOKEN = stringPreferencesKey("access_token")
    private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    private val USER_ID = intPreferencesKey("user_id")
    private val emailKey = stringPreferencesKey("email")
    private val passwordKey = stringPreferencesKey("passwordKey")
    private val isAuthorizedKey = booleanPreferencesKey("auth")


    suspend fun saveUserIdTokens(userId: Int, accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
            preferences[USER_ID] = userId
        }
    }

    suspend fun logout() {
        dataStore.edit { it.clear() }
    }

    suspend fun getLoginData(): LoginData {
        val preferences = this.dataStore.data.first()
        return LoginData(preferences[emailKey] ?: "", preferences[passwordKey] ?: "")
    }

    suspend fun rememberUser(loginData: LoginData) {
        dataStore.edit {
            it[emailKey] = loginData.email
            it[passwordKey] = loginData.password
        }
    }

    suspend fun saveNewTokens(accessToken: String, refreshToken: String) {
        dataStore.edit {
            it[ACCESS_TOKEN] = accessToken
            it[REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun setAuthorizeState(isAuthorize: Boolean) {
        dataStore.edit {
            it[isAuthorizedKey] = isAuthorize
        }
    }

    suspend fun getAuthorizeState(): Boolean {
        val preferences = this.dataStore.data.first()
        return preferences[isAuthorizedKey] ?: false
    }

    suspend fun getUserIdTokens(): UserIdTokens {
        val preferences = this.dataStore.data.first()
        return UserIdTokens(
            preferences[USER_ID] ?: -1,
            preferences[ACCESS_TOKEN] ?: "",
            preferences[REFRESH_TOKEN] ?: ""
        )

    }
}