package com.example.cashcard

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import java.io.IOException
import java.util.*
import kotlin.test.Test


@JsonTest
internal class CashCardJsonTest {
    @Autowired
    private val json: JacksonTester<CashCard>? = null

    @Autowired
    private val jsonList: JacksonTester<Array<CashCard>>? = null

    private lateinit var cashCards: List<CashCard>

    @BeforeEach
    fun setUp() {
        cashCards = listOf(
            CashCard(99L, 123.45),
            CashCard(100L, 1.0),
            CashCard(101L, 150.00)
        )
    }

    @Test
    @Throws(IOException::class)
    fun cashCardSerializationTest() {
        val cashCard = cashCards[0]
        assertThat(json!!.write(cashCard)).isStrictlyEqualToJson("single.json")
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.id")
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.id")
            .isEqualTo(99)
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.amount")
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.amount")
            .isEqualTo(123.45)
    }

    @Test
    @Throws(IOException::class)
    fun cashCardDeserializationTest() {
        val expected = """
                {
                    "id": 99,
                    "amount": 123.45
                }
                
                """.trimIndent()
        assertThat(json!!.parse(expected))
            .isEqualTo(CashCard(99L, 123.45))
        assertThat(json.parseObject(expected).id).isEqualTo(99)
        assertThat(json.parseObject(expected).amount).isEqualTo(123.45)
    }

    @Test
    @Throws(IOException::class)
    fun cashCardListSerializationTest() {
        assertThat(jsonList!!.write(cashCards.toTypedArray())).isStrictlyEqualToJson("list.json")
    }

    @Test
    @Throws(IOException::class)
    fun cashCardListDeserializationTest() {
        val expected = """
         [
            { "id": 99, "amount": 123.45 },
            { "id": 100, "amount": 1.00 },
            { "id": 101, "amount": 150.00 }
         ]
         
         """.trimIndent()
        assertThat(jsonList!!.parse(expected)).isEqualTo(cashCards.toTypedArray())
    }
}