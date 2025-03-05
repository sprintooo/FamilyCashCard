package com.example.cashcard

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import java.io.IOException
import kotlin.test.Test


@JsonTest
internal class CashCardJsonTest {

    @Autowired
    private val json: JacksonTester<CashCard>? = null

    @Test
    @Throws(IOException::class)
    fun cashCardSerializationTest() {
        val cashCard: CashCard = CashCard(99L, 123.45)
        assertThat(json!!.write(cashCard)).isStrictlyEqualToJson("expected.json")
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.id")
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.id")
            .isEqualTo(99)
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.amount")
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.amount")
            .isEqualTo(123.45)
    }
}