package com.rspt.announcement.common.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter


@Converter(autoApply = true)
class JsonLongListConverter : AttributeConverter<MutableList<Long>, String> {
    private val gson = Gson()

    // 데이터베이스에서 가져올 때 (JSON → MutableList<Long>)
    override fun convertToEntityAttribute(dbData: String?): MutableList<Long>? {
        if (dbData.isNullOrBlank()) return null
        val collectionType = object : TypeToken<MutableList<Long>>() {}.type
        return gson.fromJson(dbData, collectionType)
    }

    // 엔티티를 데이터베이스에 저장할 때 (MutableList<Long> → JSON)
    override fun convertToDatabaseColumn(attribute: MutableList<Long>?): String? {
        return attribute?.let { gson.toJson(it) }
    }
}