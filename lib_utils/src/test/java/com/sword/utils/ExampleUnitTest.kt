package com.sword.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.sword.utils.code.RubbishCode
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        runCode()
    }

    fun runCode(){
        val rubbishCode = RubbishCode()
        rubbishCode.setPathAndPage("lib_code", "com.sword.utils.code1")
        rubbishCode.run()
        println("RubbishCode run success")
    }
}