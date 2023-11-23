package io.forus.me.android.presentation.view.screens.records.item.validators

import io.forus.me.android.domain.models.validators.SimpleValidator

class ValidatorViewModel{

    enum class Type {
        header, validator, p2p
    }

    enum class Status {
        none, pending, approved, declined
    }


    var type: Type

    var sectionName: String? = null

    var id: Long? = null

    var name: String? = null

    var title: String? = null

    var imageUrl: String? = null

    var status: Status = Status.none

    constructor(sectionName: String){
        this.type = Type.header
        this.sectionName = sectionName
    }

    constructor(id: Long, name: String, title: String, imageUrl: String, status: Status = Status.approved) {
        this.type = Type.p2p
        this.id = id
        this.name = name
        this.title = title
        this.imageUrl = imageUrl
        this.status = status
    }

    constructor(simpleValidator: SimpleValidator){
        this.type = Type.validator
        this.id = simpleValidator.id
        this.name = simpleValidator.name
        this.title = simpleValidator.title
        this.imageUrl = simpleValidator.imageUrl
        this.status = Status.valueOf(simpleValidator.status.name)
    }
}