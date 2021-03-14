package com.smtm.transactions

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    features = ["doc/features"],
    glue = [
        "com.smtm.common",
        "com.smtm.transactions"
    ]
)
class TransactionsTest
