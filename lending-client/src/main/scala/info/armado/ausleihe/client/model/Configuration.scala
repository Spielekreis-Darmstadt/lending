package info.armado.ausleihe.client.model

import javax.swing.JLabel
import java.awt.Dimension

object Configuration {
  var connectionProperties: ConnectionProperties = _ 
  
  var gamePrefix: String = _
  var otherGamePrefix: String = _
  var identityCardPrefix: String = _
  var envelopePrefix: String = _
 
  var operator: String = _
  
  def setup(properties: ConnectionProperties) : Unit = {
    connectionProperties = properties 
    
    identityCardPrefix = "33"
    envelopePrefix = "44";
    
    connectionProperties match {
      case ConnectionProperties(_, _, _, Some(Operators.Spielekreis)) =>
        gamePrefix = "11"
        otherGamePrefix = "22"
        operator = "Spielekreis"
      case ConnectionProperties(_, _, _, Some(Operators.BDKJ)) => 
        gamePrefix = "22"
        otherGamePrefix = "11"
        operator = "BDKJ"
      case ConnectionProperties(_, _, _, None) => 
    }
  }
}