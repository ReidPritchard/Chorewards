package com.example.chorewards.serializable

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Period

object PeriodSerializable : KSerializer<Period> {
    override fun deserialize(decoder: Decoder): Period {
        return Period.parse(decoder.decodeString())
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Period", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Period) {
        encoder.encodeString(value.toString())
    }
}