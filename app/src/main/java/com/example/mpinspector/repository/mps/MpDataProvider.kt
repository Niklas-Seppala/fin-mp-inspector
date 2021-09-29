package com.example.mpinspector.repository.mps

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.repository.models.FavoriteModel
import com.example.mpinspector.repository.models.MpModel

/**
 * Interface that provides the repository with application
 * related MP data.
 */
interface MpDataProvider {

    /**
     * Loads fresh members of parliament related data
     * from backend.
     */
    suspend fun loadFromWeb()

    /**
     * Gets the profile picture related to specified id.
     * @param id Int MP id.
     * @return Bitmap MP profile picture.
     */
    suspend fun getMpImage(id: Int): Bitmap

    /**
     * Stores the specified comment object.
     * @param comment CommentModel Comment to be stored.
     */
    suspend fun storeMpComment(comment: CommentModel)

    /**
     * Stores the specified favorite object.
     * @param favorite FavoriteModel Favorite to be stored.
     */
    suspend fun storeFavoriteMp(favorite: FavoriteModel)

    /**
     * Deletes specified favorite mp object from database.
     * @param favorite FavoriteModel favorite mp to be deleted.
     */
    suspend fun deleteFavoriteMp(favorite: FavoriteModel)

    /**
     * Get the CommentModels associated to
     * specified MP id from application's data storage.
     * @param id Int MP id.
     * @return LiveData<MutableList<CommentModel>>
     *     MP comments wrapped in Room LiveData
     */
    fun getMpComments(id: Int): LiveData<MutableList<CommentModel>>

    /**
     * Get the MpModel wrapped associated
     * to specified MP id from application's data storage.
     * @param id Int MP id.
     * @return LiveData<MpModel>
     */
    fun getMp(id: Int): LiveData<MpModel>

    /**
     * Get All of the MpModels from application's data
     * storage.
     * @return LiveData<List<MpModel>>
     */
    fun getMps(): LiveData<List<MpModel>>

    /**
     * Get all of the user's favored MpModels from application's
     * data storage.
     * @return LiveData<List<MpModel>> User's current favorite MPs.
     */
    fun getFavoriteMps(): LiveData<List<MpModel>>

    /**
     * Checks if the specified MP is currently stored inside
     * the user's favorites.
     * @param mpId Int MP id.
     * @return LiveData<Boolean> True if mp is currently in favorites.
     */
    fun isMpInFavorites(mpId: Int): LiveData<Boolean>
}