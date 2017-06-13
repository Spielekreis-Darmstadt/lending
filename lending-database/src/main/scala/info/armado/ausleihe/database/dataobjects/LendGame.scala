package info.armado.ausleihe.database.dataobjects

import java.time.LocalDateTime
import scala.beans.BeanProperty
import javax.persistence.Id
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.ManyToOne
import javax.persistence.Column
import javax.persistence.Table
import javax.persistence.Entity

object LendGame {
  def apply(game: Game, lendIdentityCard: LendIdentityCard, lendTime: LocalDateTime, returnTime: LocalDateTime): LendGame = {
    val lendGame = LendGame(game, lendIdentityCard, lendTime)
    
    lendGame.returnTime = returnTime
    
    lendGame
  }
  
  def apply(game: Game, lendIdentityCard: LendIdentityCard, lendTime: LocalDateTime): LendGame = {
    val lendGame = new LendGame
    
    lendGame.game = game
    lendGame.lendIdentityCard = lendIdentityCard
    lendGame.lendTime = lendTime
    
    lendGame
  }
  
  def unapply(lendGame: LendGame): Option[(Game, LendIdentityCard, LocalDateTime, LocalDateTime)] = 
    Some((lendGame.game, lendGame.lendIdentityCard, lendGame.lendTime, lendGame.returnTime))
}

@Entity
@Table
class LendGame {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @BeanProperty
  var id: Int = _

  @ManyToOne(optional = false)
  @BeanProperty
  var game: Game = _

  @ManyToOne(optional = false)
  @BeanProperty
  var lendIdentityCard: LendIdentityCard = _

  @Column(nullable = false)
  @BeanProperty
  var lendTime: LocalDateTime = _

  @Column
  @BeanProperty
  var returnTime: LocalDateTime = _

  override def equals(other: Any): Boolean = other match {
    case other: LendGame => other.isInstanceOf[LendGame] && this.id == other.id
    case _ => false
  }

  override def hashCode: Int = {
    val prime = 31
    var result = 1
    result = prime * result + id;
    return result
  }
}