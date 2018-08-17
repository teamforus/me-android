package io.forus.me.android.domain.models.records

class Validator{

    var id: Long

    var name: String

    var title: String

    var imageUrl: String

    constructor(id: Long, name: String, title: String, imageUrl: String) {
        this.id = id
        this.name = name
        this.title = title
        this.imageUrl = imageUrl
    }
}