package info.armado.ausleihe.remote.results

import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAccessType

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(value = Array(classOf[IdentityCardEnvelopeNotBound], classOf[IdentityCardHasIssuedGames], classOf[IncorrectBarcode],
    classOf[Information], classOf[IssueGamesSuccess], classOf[IssueIdentityCardSuccess], 
    classOf[LendingEntityNotExists], classOf[LendingEntityInUse], classOf[ReturnGameSuccess], classOf[ReturnIdentityCardSuccess]))
class AbstractResult extends Serializable