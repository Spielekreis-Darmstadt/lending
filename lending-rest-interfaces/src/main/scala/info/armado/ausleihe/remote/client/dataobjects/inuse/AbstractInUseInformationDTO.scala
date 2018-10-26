package info.armado.ausleihe.remote.client.dataobjects.inuse

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement, XmlSeeAlso}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(value = Array(classOf[NotInUseDTO], classOf[GameInUseDTO], classOf[IdentityCardInUseDTO], classOf[EnvelopeInUseDTO]))
abstract class AbstractInUseInformationDTO extends Serializable