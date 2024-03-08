package io.forus.me.android.data.repository.vouchers.datasource
import io.forus.me.android.domain.models.currency.Currency
import io.forus.me.android.domain.models.vouchers.Transaction
import java.math.BigDecimal
import java.util.Date


fun mockTransactionsList(page: Int, perPage: Int): List<Transaction> {
        val transactions = mutableListOf<Transaction>()

        val totalMockItems = 2000
        val startId = (page - 1) * perPage + 1
        val endId = startId + perPage - 1

        if (startId <= totalMockItems) {
            for (id in startId..minOf(endId, totalMockItems)) {
                transactions.add(generateMockTransaction(id))
            }
        }

        return transactions
    }

    fun generateMockTransaction(id: Int): Transaction {
        val organization = null
        val currency = Currency()
        val amount = BigDecimal(id)
        val createdAt = Date()
        val state = "Active"

        return Transaction(id.toString(), organization, currency, amount, createdAt, null, state, null)
    }
