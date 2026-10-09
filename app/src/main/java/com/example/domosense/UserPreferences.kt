package com.example.domosense.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para acceder al DataStore desde cualquier Context
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "domo_sense_prefs"
)

class UserPreferences(private val context: Context) {

    companion object {
        private val ONBOARDING_COMPLETADO = booleanPreferencesKey("onboarding_completado")
    }

    // Flow que emite true/false según si el onboarding ya se completó
    val onboardingCompletado: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[ONBOARDING_COMPLETADO] ?: false
        }

    // Marcar el onboarding como completado
    suspend fun marcarOnboardingCompletado() {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETADO] = true
        }
    }
}