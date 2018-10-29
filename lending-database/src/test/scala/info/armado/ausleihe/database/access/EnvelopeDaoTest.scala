package info.armado.ausleihe.database.access

import info.armado.ausleihe.database.WebDeployment
import info.armado.ausleihe.database.barcode.Barcode
import info.armado.ausleihe.database.entities.Envelope
import javax.inject.Inject
import org.arquillian.ape.rdbms.{ShouldMatchDataSet, UsingDataSet}
import org.jboss.arquillian.junit.Arquillian
import org.junit.Test
import org.junit.runner.RunWith
import org.scalatest.Matchers.{convertToAnyShouldWrapper, equal}
import org.scalatest.junit.JUnitSuite

object EnvelopeDaoTest extends WebDeployment

@RunWith(classOf[Arquillian])
class EnvelopeDaoTest extends JUnitSuite {
  @Inject
  var envelopeDao: EnvelopeDao = _

  @Inject
  var identityCardDao: IdentityCardDao = _

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectByBarcode(): Unit = {
    envelopeDao.selectByBarcode(Barcode("44000013")) should equal(
      Some(Envelope(Barcode("44000013"), true))
    )

    envelopeDao.selectByBarcode(Barcode("44000046")) should equal(
      Some(Envelope(Barcode("44000046"), false))
    )

    envelopeDao.selectByBarcode(Barcode("33000010")) should equal(
      None
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectActivatedByBarcode(): Unit = {
    envelopeDao.selectActivatedByBarcode(Barcode("44000013")) should equal(
      Some(Envelope(Barcode("44000013"), true))
    )

    envelopeDao.selectActivatedByBarcode(Barcode("44000046")) should equal(
      None
    )

    envelopeDao.selectActivatedByBarcode(Barcode("33000010")) should equal(
      None
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectFromIdentityCard(): Unit = {
    identityCardDao.selectByBarcode(Barcode("33000010")).foreach(
      identityCard => envelopeDao.selectFromIdentityCard(identityCard) should equal(
        Some(Envelope(Barcode("44000013"), true))
      )
    )

    identityCardDao.selectByBarcode(Barcode("33000032")).foreach(
      identityCard => envelopeDao.selectFromIdentityCard(identityCard) should equal(
        None
      )
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectAllLend(): Unit = {
    envelopeDao.selectAllLend().toSet should equal(Set(
      Envelope(Barcode("44000013"), true),
      Envelope(Barcode("44000024"), true),
      Envelope(Barcode("44000104"), true)
    ))
  }
}
