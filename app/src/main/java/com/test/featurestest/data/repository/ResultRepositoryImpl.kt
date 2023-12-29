package com.test.featurestest.data.repository

import com.test.featurestest.domain.model.Result
import com.test.featurestest.domain.repository.ResultRepository
import javax.inject.Inject

class ResultRepositoryImpl @Inject constructor():ResultRepository{
    override fun getResults(): List<Result> {
        val results :List<Result> = listOf(
            Result(id = 1, description = "Resultado 001"),
            Result(id = 2, description = "Resultado 002")
        )

        return results
    }

}