package com.orchardlane.sort.data.local

import android.content.Context
import android.content.SharedPreferences

class OrchardPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE, Context.MODE_PRIVATE)

    var lanesDone: Int
        get() = prefs.getInt(KEY_LANES_DONE, 0)
        set(value) = prefs.edit().putInt(KEY_LANES_DONE, value).apply()

    var currentLane: Int
        get() = prefs.getInt(KEY_CURRENT_LANE, 1)
        set(value) = prefs.edit().putInt(KEY_CURRENT_LANE, value).apply()

    var unlockedFaces: Set<String>
        get() = prefs.getStringSet(KEY_UNLOCKED_FACES, null) ?: setOf(DEFAULT_FACE)
        set(value) = prefs.edit().putStringSet(KEY_UNLOCKED_FACES, value).apply()

    var selectedFace: String
        get() = prefs.getString(KEY_SELECTED_FACE, DEFAULT_FACE) ?: DEFAULT_FACE
        set(value) = prefs.edit().putString(KEY_SELECTED_FACE, value).apply()

    var soundEnabled: Boolean
        get() = prefs.getBoolean(KEY_SOUND, true)
        set(value) = prefs.edit().putBoolean(KEY_SOUND, value).apply()

    var hapticsEnabled: Boolean
        get() = prefs.getBoolean(KEY_HAPTICS, true)
        set(value) = prefs.edit().putBoolean(KEY_HAPTICS, value).apply()

    var swipeDistance: String
        get() = prefs.getString(KEY_SWIPE, DEFAULT_SWIPE) ?: DEFAULT_SWIPE
        set(value) = prefs.edit().putString(KEY_SWIPE, value).apply()

    fun clearProgress() {
        prefs.edit()
            .remove(KEY_LANES_DONE)
            .remove(KEY_CURRENT_LANE)
            .remove(KEY_UNLOCKED_FACES)
            .remove(KEY_SELECTED_FACE)
            .apply()
    }

    private companion object {
        const val STORE = "orchard_sort_lane"
        const val KEY_LANES_DONE = "lanes_done"
        const val KEY_CURRENT_LANE = "current_lane"
        const val KEY_UNLOCKED_FACES = "unlocked_faces"
        const val KEY_SELECTED_FACE = "selected_face"
        const val KEY_SOUND = "sound_enabled"
        const val KEY_HAPTICS = "haptics_enabled"
        const val KEY_SWIPE = "swipe_distance"
        const val DEFAULT_FACE = "BLINKY"
        const val DEFAULT_SWIPE = "NORMAL"
    }
}
