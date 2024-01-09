package com.test.featurestest.data.repository

import com.test.featurestest.domain.model.Office
import com.test.featurestest.domain.repository.OfficeRepository
import javax.inject.Inject

class OfficeRepositoryImpl @Inject constructor(): OfficeRepository {
    override fun getOffices(): List<Office> {

        return listOf(
            Office(oficinaId = 1, oficina = "OFICINA AQP"),
            Office(oficinaId = 2, oficina = "OFICINA LIMA")
        )
    }

}