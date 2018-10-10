package info.armado.ausleihe.database

import java.time.LocalDateTime

import info.armado.ausleihe.database.access.{EnvelopeDao, IdentityCardDao, LendIdentityCardDao}
import info.armado.ausleihe.database.barcode.Barcode
import info.armado.ausleihe.database.entities._
import javax.inject.Inject
import org.arquillian.ape.api.UsingDataSet
import org.arquillian.ape.rdbms.ShouldMatchDataSet
import org.jboss.arquillian.junit.Arquillian
import org.junit.Test
import org.junit.runner.RunWith
import org.scalatest.Matchers.{convertToAnyShouldWrapper, equal}
import org.scalatest.junit.JUnitSuite

object LendIdentityCardDaoTest extends WebDeployment

@RunWith(classOf[Arquillian])
class LendIdentityCardDaoTest extends JUnitSuite {
  @Inject
  var lendIdentityCardDao: LendIdentityCardDao = _

  @Inject
  var identityCardDao: IdentityCardDao = _

  @Inject
  var envelopeDao: EnvelopeDao = _

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def isIdentityCardIssued(): Unit = {
    identityCardDao.selectByBarcode(Barcode("33000010")).foreach(
      identityCard => lendIdentityCardDao.isIdentityCardIssued(identityCard) should equal(true)
    )

    identityCardDao.selectByBarcode(Barcode("33000021")).foreach(
      identityCard => lendIdentityCardDao.isIdentityCardIssued(identityCard) should equal(true)
    )

    identityCardDao.selectByBarcode(Barcode("33000032")).foreach(
      identityCard => lendIdentityCardDao.isIdentityCardIssued(identityCard) should equal(false)
    )

    identityCardDao.selectByBarcode(Barcode("33000043")).foreach(
      identityCard => lendIdentityCardDao.isIdentityCardIssued(identityCard) should equal(false)
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def isEnvelopeIssued(): Unit = {
    envelopeDao.selectByBarcode(Barcode("44000013")).foreach(
      envelope => lendIdentityCardDao.isEnvelopeIssued(envelope) should equal(true)
    )

    envelopeDao.selectByBarcode(Barcode("44000024")).foreach(
      envelope => lendIdentityCardDao.isEnvelopeIssued(envelope) should equal(true)
    )

    envelopeDao.selectByBarcode(Barcode("44000035")).foreach(
      envelope => lendIdentityCardDao.isEnvelopeIssued(envelope) should equal(false)
    )

    envelopeDao.selectByBarcode(Barcode("44000046")).foreach(
      envelope => lendIdentityCardDao.isEnvelopeIssued(envelope) should equal(false)
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectCurrentByIdentityCardAndEnvelope(): Unit = {
    (identityCardDao.selectByBarcode(Barcode("33000010")), envelopeDao.selectByBarcode(Barcode("44000013"))) match {
      case (Some(identityCard), Some(envelope)) =>
        lendIdentityCardDao.selectCurrentByIdentityCardAndEnvelope(identityCard, envelope) should equal(
          Some(LendIdentityCard(IdentityCard(Barcode("33000010"), true), Envelope(Barcode("44000013"), true), LocalDateTime.parse("2016-11-05T10:00:00.0"), "Marc Arndt"))
        )
    }

    (identityCardDao.selectByBarcode(Barcode("33000021")), envelopeDao.selectByBarcode(Barcode("44000024"))) match {
      case (Some(identityCard), Some(envelope)) =>
        lendIdentityCardDao.selectCurrentByIdentityCardAndEnvelope(identityCard, envelope) should equal(
          Some(LendIdentityCard(IdentityCard(Barcode("33000021"), true), Envelope(Barcode("44000024"), true), LocalDateTime.parse("2016-11-05T10:00:00.0")))
        )
    }

    (identityCardDao.selectByBarcode(Barcode("33000032")), envelopeDao.selectByBarcode(Barcode("44000035"))) match {
      case (Some(identityCard), Some(envelope)) =>
        lendIdentityCardDao.selectCurrentByIdentityCardAndEnvelope(identityCard, envelope) should equal(
          None
        )
    }

    (identityCardDao.selectByBarcode(Barcode("33000010")), envelopeDao.selectByBarcode(Barcode("44000024"))) match {
      case (Some(identityCard), Some(envelope)) =>
        lendIdentityCardDao.selectCurrentByIdentityCardAndEnvelope(identityCard, envelope) should equal(
          None
        )
    }
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectCurrentByIdentityCard(): Unit = {
    identityCardDao.selectByBarcode(Barcode("33000010")).foreach(
      identityCard => lendIdentityCardDao.selectCurrentByIdentityCard(identityCard) should equal(
        Some(LendIdentityCard(IdentityCard(Barcode("33000010"), true), Envelope(Barcode("44000013"), true), LocalDateTime.parse("2016-11-05T10:00:00.0"), "Marc Arndt"))
      )
    )

    identityCardDao.selectByBarcode(Barcode("33000021")).foreach(
      identityCard => lendIdentityCardDao.selectCurrentByIdentityCard(identityCard) should equal(
        Some(LendIdentityCard(IdentityCard(Barcode("33000021"), true), Envelope(Barcode("44000024"), true), LocalDateTime.parse("2016-11-05T10:00:00.0")))
      )
    )

    identityCardDao.selectByBarcode(Barcode("33000032")).foreach(
      identityCard => lendIdentityCardDao.selectCurrentByIdentityCard(identityCard) should equal(
        None
      )
    )

    identityCardDao.selectByBarcode(Barcode("33000043")).foreach(
      identityCard => lendIdentityCardDao.selectCurrentByIdentityCard(identityCard) should equal(
        None
      )
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectCurrentByIdentityCardBarcode(): Unit = {
    lendIdentityCardDao.selectCurrentByIdentityCardBarcode(Barcode("33000010")) should equal(
      Some(LendIdentityCard(IdentityCard(Barcode("33000010"), true), Envelope(Barcode("44000013"), true), LocalDateTime.parse("2016-11-05T10:00:00.0"), "Marc Arndt"))
    )

    lendIdentityCardDao.selectCurrentByIdentityCardBarcode(Barcode("33000021")) should equal(
      Some(LendIdentityCard(IdentityCard(Barcode("33000021"), true), Envelope(Barcode("44000024"), true), LocalDateTime.parse("2016-11-05T10:00:00.0")))
    )

    lendIdentityCardDao.selectCurrentByIdentityCardBarcode(Barcode("33000032")) should equal(
      None
    )

    lendIdentityCardDao.selectCurrentByIdentityCardBarcode(Barcode("33000043")) should equal(
      None
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectCurrentByEnvelope(): Unit = {
    envelopeDao.selectByBarcode(Barcode("44000013")).foreach(
      envelope => lendIdentityCardDao.selectCurrentByEnvelope(envelope) should equal(
        Some(LendIdentityCard(IdentityCard(Barcode("33000010"), true), Envelope(Barcode("44000013"), true), LocalDateTime.parse("2016-11-05T10:00:00.0"), "Marc Arndt"))
      )
    )

    envelopeDao.selectByBarcode(Barcode("44000024")).foreach(
      envelope => lendIdentityCardDao.selectCurrentByEnvelope(envelope) should equal(
        Some(LendIdentityCard(IdentityCard(Barcode("33000021"), true), Envelope(Barcode("44000024"), true), LocalDateTime.parse("2016-11-05T10:00:00.0")))
      )
    )

    envelopeDao.selectByBarcode(Barcode("44000035")).foreach(
      envelope => lendIdentityCardDao.selectCurrentByEnvelope(envelope) should equal(
        None
      )
    )

    envelopeDao.selectByBarcode(Barcode("44000046")).foreach(
      envelope => lendIdentityCardDao.selectCurrentByEnvelope(envelope) should equal(
        None
      )
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectCurrentByEnvelopeBarcode(): Unit = {
    lendIdentityCardDao.selectCurrentByEnvelopeBarcode(Barcode("44000013")) should equal(
      Some(LendIdentityCard(IdentityCard(Barcode("33000010"), true), Envelope(Barcode("44000013"), true), LocalDateTime.parse("2016-11-05T10:00:00.0"), "Marc Arndt"))
    )

    lendIdentityCardDao.selectCurrentByEnvelopeBarcode(Barcode("44000024")) should equal(
      Some(LendIdentityCard(IdentityCard(Barcode("33000021"), true), Envelope(Barcode("44000024"), true), LocalDateTime.parse("2016-11-05T10:00:00.0")))
    )

    lendIdentityCardDao.selectCurrentByEnvelopeBarcode(Barcode("44000035")) should equal(
      None
    )

    lendIdentityCardDao.selectCurrentByEnvelopeBarcode(Barcode("44000046")) should equal(
      None
    )

  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(value = Array("datasets/issue-identity-card.xml"), excludeColumns = Array("LENDIDENTITYCARD.ID", "LENDIDENTITYCARD.LENDTIME"))
  def issueIdentityCard(): Unit = {
    (identityCardDao.selectByBarcode(Barcode("33000032")), envelopeDao.selectByBarcode(Barcode("44000035"))) match {
      case (Some(identityCard), Some(envelope)) => {
        lendIdentityCardDao.isIdentityCardIssued(identityCard) should equal(false)
        lendIdentityCardDao.isEnvelopeIssued(envelope) should equal(false)

        lendIdentityCardDao.issueIdentityCard(identityCard, envelope)

        lendIdentityCardDao.isIdentityCardIssued(identityCard) should equal(true)
        lendIdentityCardDao.isEnvelopeIssued(envelope) should equal(true)
      }
    }
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(value = Array("datasets/return-identity-card.xml"), excludeColumns = Array("LENDIDENTITYCARD.RETURNTIME"))
  def returnIdentityCard(): Unit = {
    (identityCardDao.selectByBarcode(Barcode("33000010")), envelopeDao.selectByBarcode(Barcode("44000013"))) match {
      case (Some(identityCard), Some(envelope)) => {
        lendIdentityCardDao.isIdentityCardIssued(identityCard) should equal(true)
        lendIdentityCardDao.isEnvelopeIssued(envelope) should equal(true)

        lendIdentityCardDao.returnIdentityCard(identityCard, envelope)

        lendIdentityCardDao.isIdentityCardIssued(identityCard) should equal(false)
        lendIdentityCardDao.isEnvelopeIssued(envelope) should equal(false)
      }
    }
  }
}
