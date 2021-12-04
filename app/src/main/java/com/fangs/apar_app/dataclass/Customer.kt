package com.fangs.apar_app.dataclass

import java.time.LocalDate

data class Customer(
    val lastname : String,
    val firstname : String,
    val birthday : LocalDate,
    val address : String,
    val contactNumber : Long,
    val signedUpDate : LocalDate

    )