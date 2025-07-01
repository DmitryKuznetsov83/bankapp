package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "POST /validate: сумма 100 → true"
    request {
        method 'POST'
        url '/blockers/cash-transactions/validate'
        headers {
            contentType(applicationJson())
            accept(applicationJson())
        }
        body(
                id: anyUuid(),
                status: "PENDING",
                comment: null,
                createdAt: null,
                updatedAt: null,
                userLogin: "TestUser",
                currency: "USD",
                type: "CASH_IN",
                sum: 100.00
        )
    }
    response {
        status 200
        headers {
            contentType(applicationJson())
        }
        body([
                valid: true
        ])
    }
}
