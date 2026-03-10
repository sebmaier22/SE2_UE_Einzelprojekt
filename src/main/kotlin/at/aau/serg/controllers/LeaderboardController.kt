package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
// Aufgabe 2.2.2 Drei Importe notwendig:
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val gameResultService: GameResultService
) {
    // Aufgabe 2.2.1: Sortierung nach Zeit aufsteigend
    // Aufgabe 2.2.2:
    // rank wird ein optional RequestParam.
    // kein rank -> gesamtes Leaderboard
    // rank vorhanden -> Spieler + 3davor + 3danach
    //	ungültig -> HTTP 400
    @GetMapping
    fun getLeaderboard(@RequestParam(required = false) rank: Int?): List<GameResult> {

        val sorted = gameResultService.getGameResults()
            .sortedWith(compareByDescending<GameResult> { it.score }
                .thenBy { it.timeInSeconds })

        if (rank == null) {
            return sorted
        }

        if (rank < 1 || rank > sorted.size) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }

        val index = rank - 1

        val start = maxOf(0, index - 3)
        val end = minOf(sorted.size, index + 4)

        return sorted.subList(start, end)
    }
}