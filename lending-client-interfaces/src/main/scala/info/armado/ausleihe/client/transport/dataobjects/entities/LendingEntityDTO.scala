package info.armado.ausleihe.client.transport.dataobjects.entities

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement, XmlSeeAlso}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(value = Array(classOf[GameDTO], classOf[IdentityCardDTO], classOf[EnvelopeDTO]))
abstract class LendingEntityDTO extends Serializable

