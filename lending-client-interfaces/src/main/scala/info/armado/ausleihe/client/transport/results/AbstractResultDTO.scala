package info.armado.ausleihe.client.transport.results

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement, XmlSeeAlso}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(
  value = Array(
    classOf[IdentityCardEnvelopeNotBoundDTO],
    classOf[IdentityCardHasIssuedGamesDTO],
    classOf[IncorrectBarcodeDTO],
    classOf[InformationDTO],
    classOf[IssueGamesSuccessDTO],
    classOf[IssueIdentityCardSuccessDTO],
    classOf[LendingEntityNotExistsDTO],
    classOf[LendingEntityInUseDTO],
    classOf[ReturnGameSuccessDTO],
    classOf[ReturnIdentityCardSuccessDTO]
  )
)
class AbstractResultDTO extends Serializable
