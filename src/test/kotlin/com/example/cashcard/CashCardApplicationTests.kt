package com.example.cashcard

import com.jayway.jsonpath.JsonPath
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
}
