package com.stripe.android.financialconnections.domain

import com.stripe.android.financialconnections.launcher.InstantDebitsResult
import com.stripe.android.financialconnections.repository.ConsumerSessionProvider
import com.stripe.android.financialconnections.repository.FinancialConnectionsConsumerSessionRepository
import com.stripe.android.financialconnections.repository.FinancialConnectionsRepository
import com.stripe.android.model.ConsumerPaymentDetails.BankAccount
import javax.inject.Inject

internal fun interface CreateInstantDebitsResult {
    suspend operator fun invoke(
        bankAccountId: String,
    ): InstantDebitsResult
}

internal class RealCreateInstantDebitsResult @Inject constructor(
    private val consumerRepository: FinancialConnectionsConsumerSessionRepository,
    private val repository: FinancialConnectionsRepository,
    private val consumerSessionProvider: ConsumerSessionProvider,
) : CreateInstantDebitsResult {

    override suspend fun invoke(
        bankAccountId: String,
    ): InstantDebitsResult {
        val consumerSession = consumerSessionProvider.provideConsumerSession()
        val clientSecret = requireNotNull(consumerSession?.clientSecret) {
            "Consumer session client secret cannot be null"
        }

        val response = consumerRepository.createPaymentDetails(
            consumerSessionClientSecret = clientSecret,
            bankAccountId = bankAccountId,
        )

        val paymentDetails = response.paymentDetails.filterIsInstance<BankAccount>().first()

        val paymentMethod = repository.createPaymentMethod(
            paymentDetailsId = paymentDetails.id,
            consumerSessionClientSecret = clientSecret,
        )

        return InstantDebitsResult(
            paymentMethodId = paymentMethod.id,
            bankName = paymentDetails.bankName,
            last4 = paymentDetails.last4,
        )
    }
}
