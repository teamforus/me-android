package io.forus.me.android.domain.models.validators

class SimpleValidator{

    enum class Status {
        none, pending, approved, declined
    }

    var id: Long

    var organizationId: Long

    var name: String

    var title: String

    var imageUrl: String

    var status: Status

    constructor(id: Long, organizationId: Long, name: String, title: String, imageUrl: String, status: Status = Status.none) {
        this.id = id
        this.organizationId = organizationId
        this.name = name
        this.title = title
        this.imageUrl = imageUrl
        this.status = status
    }
}