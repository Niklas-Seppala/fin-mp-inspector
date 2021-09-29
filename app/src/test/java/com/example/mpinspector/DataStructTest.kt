package com.example.mpinspector

import com.example.mpinspector.utils.DataStructs
import org.junit.Test

import org.junit.Assert.*


class DataStructTest {
    @Test
    fun findDiffIndexesWorksOnWithNewIsSmaller() {

        val new = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val old = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        val correctResult = setOf(10, 11)

        val testResult = DataStructs.findDiffIndexes(old, new) { a, b -> a == b }

        assertArrayEquals(correctResult.toIntArray(), testResult.toIntArray())
    }

    @Test
    fun findDiffIndexesWorksOnWithOldIsSmaller() {

        val new = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val old = listOf(1, 2, 3, 4, 5, 6, 7, 8, )
        val correctResult = setOf(8, 9)

        val testResult = DataStructs.findDiffIndexes(old, new) { a, b -> a == b }

        assertArrayEquals(correctResult.toIntArray(), testResult.toIntArray())
    }

    @Test
    fun findDiffIndexesWorksOnWithDiffsInMiddle() {

        val new = listOf(1, 2, 3, 4, 5)
        val old = listOf(1, 2, 3, 5)
        val correctResult = setOf(3, 4)

        val testResult = DataStructs.findDiffIndexes(old, new) { a, b -> a == b }

        assertArrayEquals(correctResult.toIntArray(), testResult.toIntArray())
    }
}