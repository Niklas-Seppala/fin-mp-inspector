package com.example.mpinspector.repository.mps

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.example.mpinspector.repository.models.*

/**
 * Interface that provides the repository with application
 * related MP data.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
interface MpDataProvider {

    /**
     * Loads newest data fro MPs from network and stores it to database.
     */
    suspend fun loadFromWeb()

    /**
     * Get MP's image.
     *
     * @param id: MP id
     * @param size ImageSize Enum
     * @param roundCorner Int round value.
     */
    suspend fun getMpImage(id: Int, size: ImageSize, roundCorner: Int = 30): Bitmap

    /**
     * Adds new comment to database.
     * @param comment CommentModel comment db model object.
     */
    suspend fun storeMpComment(comment: CommentModel)

    /**
     * Adds new favorite MP to database.
     * @param favorite FavoriteModel favorite db model object.
     */
    suspend fun addFavoriteMp(favorite: FavoriteModel)

    /**
     * Deletes favorite MP from database.
     * @param favorite FavoriteModel favorite db model object.
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

    /**
     * Get specified MP and his/hers comments bundled together.
     * @param id Int MP id
     * @return LiveData<MpWithComment> MP and comments.
     */
    fun getMpWithComments(id: Int): LiveData<MpWithComments>
}