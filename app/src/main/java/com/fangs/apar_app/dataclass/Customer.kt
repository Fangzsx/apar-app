package com.fangs.apar_app.dataclass

import java.time.LocalDate

data class Customer(
    val lastname : String,
    val firstname : String,
    val middlename : String,
    val houseSt : String,
    val phaseSubd : String,
    val city : String,
    val birthday : String,
    val contactNumber : Long,
    val signedUpDate : LocalDate

    )