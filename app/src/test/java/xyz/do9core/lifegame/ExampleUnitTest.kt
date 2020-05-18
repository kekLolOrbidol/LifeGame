package xyz.do9core.lifegame

import org.junit.Test

import xyz.do9core.lifegame.view.booleanMatrix

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun `matrix set is correct`() {
        val m = booleanMatrix(1, 1, false)
        m[0, 0] = true
        assert(m[0, 0])

    }
}
