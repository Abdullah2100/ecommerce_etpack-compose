package com.example.eccomerce_app.services

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object LatLngKserialize : KSerializer<LatLng> {
    override val descriptor = PrimitiveSerialDescriptor("LatLng", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: LatLng) =
        encoder.encodeString("${value.latitude},${value.latitude}")

    override fun deserialize(decoder: Decoder): LatLng {
        val result = decoder.decodeString()
        val arrayResult = result.split(",")
        val lat = arrayResult[0] as Double
        val lng = arrayResult[1] as Double

        return LatLng(lat,lng)
    }
}