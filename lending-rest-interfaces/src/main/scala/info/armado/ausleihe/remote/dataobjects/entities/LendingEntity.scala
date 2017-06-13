package info.armado.ausleihe.remote.dataobjects.entities

import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.bind.annotation.XmlAccessType

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(value = Array(classOf[GameData], classOf[IdentityCardData], classOf[EnvelopeData]))
abstract class LendingEntity extends Serializable

