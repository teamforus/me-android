package io.forus.me.android.presentation.view.screens.records.item.validations

import io.forus.me.android.data.entity.records.response.Validation
import io.forus.me.android.domain.models.validators.SimpleValidator

class ValidationViewModel{

    enum class Type {
        header, validator
    }

    /*enum class Status {
        none, pending, approved, declined
    }*/


    var type: Type

    var sectionName: String? = null

   // var id: Long? = null

    var name: String? = null

    var title: String? = null



   // var status: Status = Status.none

    constructor(sectionName: String){
        this.type = Type.header
        this.sectionName = sectionName
    }



    constructor(simpleValidation: io.forus.me.android.domain.models.records.Validation){
        this.type = Type.validator
       // this.id = simpleValidator.id
        if(simpleValidation.organization != null) {
            this.name = simpleValidation.organization!!.name!!
        }else{
            this.name = simpleValidation.identityAddress
        }

    }
}