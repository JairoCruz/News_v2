package com.example.news2.network

import com.example.news2.database.entities.Source
import com.example.news2.model.SourceDomain
import com.squareup.moshi.JsonClass

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 *
 * @see model package for
 */

/**
 * VideoHolder holds a list of Videos.
 *
 * This is to parse first level of our network result which looks like
 *
 * {
 *   "sources": []
 * }
 */

@JsonClass(generateAdapter = true)
data class NetworkSourceContainer(val sources: List<NetworkSource>)


/**
 * Videos represent a devbyte that can be played.
 */
@JsonClass(generateAdapter = true)
data class NetworkSource(
    val id: String,
    val name: String,
    val description: String,
    val url: String,
    val category: String,
    val language: String,
    val country: String,
    val isChecked: Boolean = false
)

/**
 * Convert Network results to database objects
 */
fun NetworkSourceContainer.asDomainModel(): List<SourceDomain> {
    return sources.map {
        SourceDomain(
            id = it.id,
            name = it.name,
            description = it.description,
            url = it.url,
            category = it.category,
            language = it.language,
            country = it.country,
            isChecked = it.isChecked
        )
    }

}

/**
 * Convert Network results to database objects
 */
fun NetworkSourceContainer.asDatabaseModel(): List<Source> {
    return sources.map {
        Source(
            id = it.id,
            name = it.name,
            description = it.description,
            url = it.url,
            category = it.category,
            language = it.language,
            country = it.country,
            isChecked = it.isChecked
        )
    }
}