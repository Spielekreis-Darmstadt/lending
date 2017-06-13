package info.armado.ausleihe.remote.dataobjects.inuse

import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlAccessType

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(value = Array(classOf[NotInUse], classOf[GameInUse], classOf[IdentityCardInUse], classOf[EnvelopeInUse]))
abstract class AbstractInUseInformation extends Serializable