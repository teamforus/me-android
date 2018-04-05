package io.forus.me.services

/**
 * Created by martijn.doornik on 05/03/2018.
 */
class ValidationService {

    companion object {
        fun isValidAddress(address:String): Boolean {
            return (address.length == 22 && address.startsWith("0x"))
        }
    }
}