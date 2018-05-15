package io.forus.me.services

import android.arch.lifecycle.LiveData
import io.forus.me.entities.Token
import io.forus.me.helpers.ThreadHelper
import io.forus.me.services.base.EthereumItemService

class TokenService : EthereumItemService<Token>() {

    override fun add(item: Token) {
        addToken(item)
    }

    override fun delete(item: Token) {
        deleteToken(item)
    }
    override fun getItem(address: String): Token {
        return DatabaseService.inject.tokenDao().getToken(address)
    }

    override fun getList(identity: String): List<Token> {
        return DatabaseService.inject.tokenDao().getTokens(identity)
    }

    override fun getLiveData(identity: String): LiveData<List<Token>> {
        return DatabaseService.inject.tokenDao().getTokensLiveData(identity)
    }

    override fun getLiveItem(address: String): LiveData<Token> {
        return DatabaseService.inject.tokenDao().getLiveToken(address)
    }

    override fun getThread(): ThreadHelper.DataThread {
        return thread
    }

    override fun update(item: Token) {
        updateToken(item)
    }

    companion object {
        private val thread: ThreadHelper.DataThread
                get() = ThreadHelper.dispense(ThreadHelper.TOKEN_THREAD)
        fun addToken(token: Token) {
            thread.postTask(Runnable { DatabaseService.inject.tokenDao().insert(token) })
        }

        fun deleteToken(token: Token) {
            thread.postTask(Runnable { DatabaseService.inject.tokenDao().delete(token)})
        }

        fun getTokensByIdentity(identity:String): List<Token>? {
            return DatabaseService.inject.tokenDao().getTokens(identity)
        }

        fun getTokenByAddressByIdentity(address:String, identity:String): Token? {
            return DatabaseService.inject.tokenDao().getTokenByAddressByIdentity(address, identity)
        }

        fun updateToken(token:Token) {
            thread.postTask(Runnable {DatabaseService.inject.tokenDao().update(token)})
        }
    }
}