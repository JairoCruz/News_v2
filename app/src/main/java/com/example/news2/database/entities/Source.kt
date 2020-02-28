package com.example.news2.database.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.news2.model.SourceDomain

/**
 * Source represents a source entity in the database
 */

@Entity(tableName = "Sources", indices = [Index(value = ["id"])])
data class Source constructor(
    @PrimaryKey
    @NonNull
    val id: String,
    val name: String,
    val description: String,
    val url: String,
    val category: String,
    val language: String,
    val country: String
)

/**
 * Map Source to domain entities
 */

fun List<Source>.asDomainModel(): List<SourceDomain> {

    return map {
        SourceDomain(
            id = it.id,
            name = it.name,
            description = it.description,
            url = it.url,
            category = it.category,
            language = it.language,
            country = it.country
        )
    }

}