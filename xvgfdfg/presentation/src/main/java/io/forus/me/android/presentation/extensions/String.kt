package io.forus.me.android.presentation.extensions

fun String.maskSymbols(from: Int, to: Char): String {
    if(from >= this.length) return this
    return this.substring(0, from) + this.substring(from).map { _ -> to }.joinToString("")
}

fun String.maskStartingFromPreservingTail(from: Int, to: Char, unmaskedEnd:Int): String {
    if(from >= this.length) return this
    return this.substring(0, from) + this.substring(from, this.length-unmaskedEnd).map { _ -> to }.joinToString("") + this.substring(this.length-unmaskedEnd)
}