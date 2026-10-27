package com.example.domosense.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "domo_sense_prefs"
)

// Enum para el tipo de sesion
enum class SessionType {
    NONE,       // No ha iniciado sesion
    GUEST,      // Entro como invitado
    REGISTERED  // Inicio sesion con cuenta
}

class UserPreferences(private val context: Context) {

    companion object {
        private val ONBOARDING_COMPLETADO = booleanPreferencesKey("onboarding_completado")
        private val SESSION_TYPE = stringPreferencesKey("session_type")
    }

    val onboardingCompletado: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[ONBOARDING_COMPLETADO] ?: false
        }

    // Flow que emite el tipo de sesion actual
    val sessionType: Flow<SessionType> = context.dataStore.data
        .map { preferences ->
            val value = preferences[SESSION_TYPE] ?: SessionType.NONE.name
            SessionType.valueOf(value)
        }

    suspend fun marcarOnboardingCompletado() {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETADO] = true
        }
    }

    // --- ESTAS SON LAS FUNCIONES QUE FALTABAN ---
    suspend fun guardarSession(type: SessionType) {
        context.dataStore.edit { preferences ->
            preferences[SESSION_TYPE] = type.name
        }
    }

    suspend fun cerrarSession() {
        context.dataStore.edit { preferences ->
            preferences[SESSION_TYPE] = SessionType.NONE.name
        }
    }
}