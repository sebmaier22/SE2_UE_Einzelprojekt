package at.aau.serg.controllers
import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import kotlin.test.assertEquals
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin
import kotlin.test.Test

class GameResultControllerTests {
    private lateinit var mockedService: GameResultService
    private lateinit var controller: GameResultController

    @BeforeEach
    // Vor jedem Test wird ein neuer Mock des Services erstellt.
    // Dadurch stellt man sicher, dass Tests unabhängig voneinander laufen.
    fun setup() {
        mockedService = mock()
        // Der Controller bekommt den Mock-Service injiziert.
        // So testen wir nur den Controller und nicht die echte Service-Logik.
        controller = GameResultController(mockedService)
    }

    @Test
    fun test_getGameResult_returnsObject() {
        // Dieser Test überprüft, ob der Controller das Ergebnis vom Service korrekt zurückgibt.
        // Der Mock-Service wird so konfiguriert, dass er bei ID 1 ein bestimmtes Ergebnis zurückgibt.
        val result = GameResult(1, "player", 10, 5.0)

        whenever(mockedService.getGameResult(1)).thenReturn(result)

        val res = controller.getGameResult(1)
        // Überprüfen, ob der Controller tatsächlich den Service aufgerufen hat:
        verify(mockedService).getGameResult(1)
        // Überprüfen, ob das zurückgegebene Objekt korrekt weitergegeben wurde:
        assertEquals(result, res)
    }

    @Test
    fun test_getAllGameResults_returnsList() {
        // Dieser Test prüft, ob der Controller eine Liste von Spielergebnissen korrekt vom Service zurückgibt.
        val list = listOf(GameResult(1,"player",10,5.0))

        whenever(mockedService.getGameResults()).thenReturn(list)

        val res = controller.getAllGameResults()

        verify(mockedService).getGameResults()
        assertEquals(list, res)
    }

    @Test
    fun test_addGameResult_callsService() {
        // Dieser Test stellt sicher, dass der Controller das übergebene Ergebnis an den Service weiterleitet.
        val result = GameResult(0,"player",10,5.0)

        controller.addGameResult(result)

        verify(mockedService).addGameResult(result)
    }

    @Test
    fun test_deleteGameResult_callsService() {
        // Dieser Test überprüft, ob der Controller die Löschoperation korrekt an den Service weiterleitet.
        controller.deleteGameResult(1)

        verify(mockedService).deleteGameResult(1)
    }


}