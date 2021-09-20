package com.example.mpinspector.utils

import com.example.mpinspector.R
import java.lang.Exception

class NoSuchPartyException(partyId: String, msg: String = "No such \"$partyId\" party mapped.")
    : Exception(msg)

object PartyMapper {

    data class Party(val name: Int, val icon: Int)

    private val partyMap = mapOf(
        "kok"  to Party(R.string.partyKok,  R.mipmap.ic_party_kok),
        "vas"  to Party(R.string.partyVas,  R.mipmap.ic_party_vas),
        "vihr" to Party(R.string.partyVihr, R.mipmap.ic_party_vihr),
        "ps"   to Party(R.string.partyPs,   R.mipmap.ic_party_ps),
        "sd"   to Party(R.string.partySd,   R.mipmap.ic_party_sd),
        "liik" to Party(R.string.partyLiik, R.mipmap.ic_party_liik),
        "r"    to Party(R.string.partyR,    R.mipmap.ic_party_r),
        "kd"   to Party(R.string.partyKd,   R.mipmap.ic_party_kd),
        "kesk" to Party(R.string.partyKesk, R.mipmap.ic_party_kesk)
    )

    @Throws(NoSuchPartyException::class)
    fun partyName(id: String) = partyMap[id]?.name ?: throw NoSuchPartyException(id)

    @Throws(NoSuchPartyException::class)
    fun partyIcon(id: String) = partyMap[id]?.icon ?: throw NoSuchPartyException(id)
}

