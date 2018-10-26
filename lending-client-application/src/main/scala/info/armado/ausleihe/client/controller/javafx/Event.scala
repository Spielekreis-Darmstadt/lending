package info.armado.ausleihe.client.controller.javafx

import info.armado.ausleihe.client.model.{WrongChecksum, WrongLength}
import info.armado.ausleihe.client.transport.dataobjects.entities.{EnvelopeDTO, IdentityCardDTO}

sealed trait Event

case class WrongLengthEvent(wrongLength: WrongLength) extends Event

case class WrongChecksumEvent(wrongChecksum: WrongChecksum) extends Event

case object RepeatedGameScanEvent extends Event

case class DifferentRepeatedIdentityCardEvent(knownIdentityCard: IdentityCardDTO) extends Event

case class DifferentRepeatedEnvelopeEvent(knownEnvelope: EnvelopeDTO) extends Event

case class OtherGroupGameEvent(barcode: String) extends Event

case object ResetEvent extends Event