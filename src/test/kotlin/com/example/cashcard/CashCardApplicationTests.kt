package com.example.cashcard

import com.jayway.jsonpath.JsonPath
import net.minidev.json.JSONArray
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.annotation.DirtiesContext
import java.net.URI


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CashCardApplicationTests {

	@Autowired
	private lateinit var restTemplate: TestRestTemplate

	@Test
	fun shouldReturnACashCardWhenDataIsSaved() {
		val response = restTemplate.getForEntity("/cashcards/99", String::class.java)
		assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
		val documentContext = JsonPath.parse(response.body)
		val id = documentContext.read<Number>("$.id")
		assertThat(id).isEqualTo(99)
		val amount: Double = documentContext.read("$.amount")
		assertThat(amount).isEqualTo(123.45)
	}

	@Test
	fun shouldNotReturnACashCardWithAnUnknownId() {
		val response = restTemplate.getForEntity("/cashcards/1000", String::class.java)
		assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
		assertThat(response.body).isBlank()
	}

	@Test
	fun shouldCreateANewCashCard() {
		val newCashCard = CashCard(null, 250.00)
		val createResponse = restTemplate.postForEntity("/cashcards", newCashCard, Void::class.java)
		assertThat(createResponse.statusCode).isEqualTo(HttpStatus.CREATED)

		val locationOfNewCashCard: URI? = createResponse.headers.location
		val getResponse: ResponseEntity<String> = restTemplate.getForEntity(locationOfNewCashCard, String::class.java)
		assertThat(getResponse.statusCode).isEqualTo(HttpStatus.OK)

		val documentContext = JsonPath.parse(getResponse.body)
		val id = documentContext.read<Number>("$.id")
		val amount = documentContext.read<Double>("$.amount")

		assertThat(id).isNotNull()
		assertThat(amount).isEqualTo(250.00)
	}

	@Test
	fun shouldReturnAllCashCardsWhenListIsRequested() {
		val response = restTemplate.getForEntity("/cashcards", String::class.java)
		assertThat(response.statusCode).isEqualTo(HttpStatus.OK)

		val documentContext = JsonPath.parse(response.body)
		val cashCardCount = documentContext.read<Int>("$.length()")
		assertThat(cashCardCount).isEqualTo(3)

		val ids: JSONArray = documentContext.read("$..id")
		assertThat(ids).containsExactlyInAnyOrder(99, 100, 101)

		val amounts: JSONArray = documentContext.read("$..amount")
		assertThat(amounts).containsExactlyInAnyOrder(123.45, 1.0, 150.00)
	}
}
