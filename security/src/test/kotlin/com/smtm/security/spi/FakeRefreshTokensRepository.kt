package com.smtm.security.spi

class FakeRefreshTokensRepository : RefreshTokensRepository {

    private val records = mutableListOf<Record>()

    override fun save(subject: Long, tokenId: String) {
        records.add(Record(subject, tokenId))
    }

    override fun exists(subject: Long, tokenId: String): Boolean {
        return records.contains(Record(subject, tokenId))
    }

    fun addRecord(record: Record) {
        records.add(record)
    }

    data class Record(val subject: Long, val id: String)
}

