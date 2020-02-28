package com.example.news2.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.news2.database.NewsDatabase
import com.example.news2.database.entities.asDomainModel
import com.example.news2.model.SourceDomain
import com.example.news2.network.NetworkSource
import com.example.news2.network.NewsNetwork
import com.example.news2.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for fetching devbyte videos from the network and storing them on disk
 */

class SourceRepository(private val database: NewsDatabase) {

    val sources: LiveData<List<SourceDomain>> = Transformations.map(database.sourceDao.getSources()) {
        it.asDomainModel()
    }

    /**
     * Refresh the videos stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     */
    private val API_KEY = "94294f4227bf4600849e1697d6a48ec1"
    suspend fun refreshSources() {
        withContext(Dispatchers.IO) {
            val sourceList = NewsNetwork.network.getSources(API_KEY)
            database.sourceDao.insert(sourceList.asDatabaseModel())
        }
    }
}