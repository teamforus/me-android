package io.forus.me.helpers

interface EthereumHelper {
    companion object {

        fun isAddressValid(address:String): Boolean {
            return address.length == 42 && address.startsWith("0x")
        }
    }
}