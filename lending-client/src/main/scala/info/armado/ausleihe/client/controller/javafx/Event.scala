package info.armado.ausleihe.client.controller.javafx

import info.armado.ausleihe.client.model.WrongChecksum

import info.armado.ausleihe.client.model.WrongLength
import info.armado.ausleihe.remote.dataobjects.entities.IdentityCardData
import info.armado.ausleihe.remote.dataobjects.entities.EnvelopeData

sealed trait Event
case class WrongLengthEvent(wrongLength: WrongLength) extends Event
case class WrongChecksumEvent(wrongChecksum: WrongChecksum) extends Event
case object RepeatedGameScanEvent extends Event
case class DifferentRepeatedIdentityCardEvent(knownIdentityCard: IdentityCardData) extends Event
case class DifferentRepeatedEnvelopeEvent(knownEnvelope: EnvelopeData) extends Event
case class OtherGroupGameEvent(barcode: String) extends Event
case object ResetEvent extends Event