package io.forus.me.android.domain.models.records

class Validator{

    enum class Status {
        none, pending, approved, declined
    }

    var id: Long

    var name: String

    var title: String

    var imageUrl: String

    var status: Status

    constructor(id: Long, name: String, title: String, imageUrl: String, status: Status = Status.none) {
        this.id = id
        this.name = name
        this.title = title
        this.imageUrl = imageUrl
        this.status = status
    }
}