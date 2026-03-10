package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.web.server.ResponseStatusException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

class LeaderboardControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: LeaderboardController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = LeaderboardController(mockedService)
    }

    @Test
    fun test_getLeaderboard_correctScoreSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)
        val third = GameResult(3, "third", 10, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard(null) // null steht dafür, dass rank nicht gesetzt ist.

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }
    /*
    @Test
    fun test_getLeaderboard_sameScore_CorrectIdSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 20, 10.0)
        val third = GameResult(3, "third", 20, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard()

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    } */


    @Test //Aufgabe 2.2.1 Test: Hier der neue Test für die zeitliche Sortierung
    fun test_getLeaderboard_sameScore_correctTimeSorting() {
        // id absichtlich nicht aufsteigend eingetragen um wirklich die Sortierung per zeit zu testen
        val first = GameResult(3, "first", 20, 10.0)
        val second = GameResult(1, "second", 20, 15.0)
        val third = GameResult(2, "third", 20, 20.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, third, first))

        val res: List<GameResult> = controller.getLeaderboard(null) // null steht dafür, dass rank nicht gesetzt ist.

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)

        // shortest time first
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    // Aufgabe 2.2.2: neuer Test für rank
    @Test
    fun test_getLeaderboard_withRank_returnsSurroundingPlayers() {
        val list = listOf(
            GameResult(1,"A",50,10.0),
            GameResult(2,"B",45,11.0),
            GameResult(3,"C",40,12.0),
            GameResult(4,"D",35,13.0),
            GameResult(5,"E",30,14.0),
            GameResult(6,"F",25,15.0),
            GameResult(7,"G",20,16.0),
            GameResult(8,"H",15,17.0)
        )

        whenever(mockedService.getGameResults()).thenReturn(list)

        val res = controller.getLeaderboard(5)

        assertEquals(7, res.size)
        assertEquals("B", res[0].playerName)
        assertEquals("H", res[6].playerName)
    }

    // 2.2.2: Test für ungültigen rank:
    @Test
    fun test_getLeaderboard_invalidRank_throws400() {

        whenever(mockedService.getGameResults()).thenReturn(emptyList())

        assertFailsWith<ResponseStatusException> {
            controller.getLeaderboard(-1)
        }
    }

    // Test für rank <1:
    @Test
    fun test_getLeaderboard_rankTooSmall_returns400() {

        whenever(mockedService.getGameResults()).thenReturn(
            listOf(GameResult(1,"A",10,10.0))
        )

        assertFailsWith<ResponseStatusException> {
            controller.getLeaderboard(0)
        }
    }
    // test für rank > size
    @Test
    fun test_getLeaderboard_rankTooLarge_returns400() {

        whenever(mockedService.getGameResults()).thenReturn(
            listOf(GameResult(1,"A",10,10.0))
        )

        assertFailsWith<ResponseStatusException> {
            controller.getLeaderboard(5)
        }
    }
}