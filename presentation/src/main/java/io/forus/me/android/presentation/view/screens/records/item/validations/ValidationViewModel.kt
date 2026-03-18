package io.forus.me.android.presentation.view.screens.records.item.validations

class ValidationViewModel{

    enum class Type {
        header, validator
    }


    var type: Type

    var sectionName: String? = null

    var name: String? = null

    var title: String? = null




    constructor(sectionName: String){
        this.type = Type.header
        this.sectionName = sectionName
    }



    constructor(simpleValidation: io.forus.me.android.domain.models.records.Validation){
        this.type = Type.validator
        if(simpleValidation.organization != null) {
            this.name = simpleValidation.organization!!.name!!
        }else{
            this.name = simpleValidation.identityAddress
        }

    }
}