//вычисление определителя методом Гаусса
package com.example.engine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs

class ComputationEngine {

    suspend fun computeDeterminant(matrix: List<List<Double>>): Double = withContext(Dispatchers.Default) {
        val n = matrix.size

        require(matrix.all { it.size == n }) { "Матрица должна быть квадратной" }

        val a = matrix.map { it.toMutableList() }.toMutableList()
        var det = 1.0

        for (i in 0 until n) {
            var maxRow = i
            var maxVal = abs(a[i][i])
            for (k in i + 1 until n) {
                if (abs(a[k][i]) > maxVal) {
                    maxVal = abs(a[k][i])
                    maxRow = k
                }
            }

            if (maxVal < 1e-10) return@withContext 0.0

            if (maxRow != i) {
                a[i] = a[maxRow].also { a[maxRow] = a[i] }
                det *= -1
            }

            det *= a[i][i]

            for (k in i + 1 until n) {
                val factor = a[k][i] / a[i][i]
                for (j in i + 1 until n) {
                    a[k][j] -= factor * a[i][j]
                }
            }
        }
        return@withContext det
    }
}