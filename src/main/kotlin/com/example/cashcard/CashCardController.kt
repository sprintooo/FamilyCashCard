package com.example.cashcard

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI


@RestController
@RequestMapping("/cashcards")
class CashCardController(
    private val cashCardRepository: CashCardRepository
) {

    @GetMapping("/{requestedId}")
    fun findById(@PathVariable requestedId: Long) : ResponseEntity<CashCard>{
        val cashCardOptional = cashCardRepository.findById(requestedId)
        return if (cashCardOptional.isPresent) {
            ResponseEntity.ok(cashCardOptional.get());
        } else {
            ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    private fun createCashCard(
        @RequestBody newCashCardRequest: CashCard,
        ucb: UriComponentsBuilder
    ): ResponseEntity<Void> {
        val savedCashCard = cashCardRepository.save(newCashCardRequest)
        val locationOfNewCashCard: URI = ucb
            .path("cashcards/{id}")
            .buildAndExpand(savedCashCard.id)
            .toUri()
        return ResponseEntity.created(locationOfNewCashCard).build()
    }

    @GetMapping
    private fun findAll(): ResponseEntity<Iterable<CashCard>> {
        return ResponseEntity.ok(cashCardRepository.findAll())
    }
}