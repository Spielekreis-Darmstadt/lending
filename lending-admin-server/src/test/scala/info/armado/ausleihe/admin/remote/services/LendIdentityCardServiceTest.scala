package info.armado.ausleihe.admin.remote.services

import info.armado.ausleihe.admin.transport.dataobjects.LendIdentityCardDTO
import info.armado.ausleihe.admin.transport.requests.ChangeOwnerRequestDTO
import org.arquillian.ape.rdbms.{ShouldMatchDataSet, UsingDataSet}
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource
import org.jboss.arquillian.junit.Arquillian
import org.jboss.arquillian.test.spi.ArquillianProxyException
import org.junit.Test
import org.junit.runner.RunWith
import org.scalatest.Matchers.{convertToAnyShouldWrapper, equal}
import org.scalatest.junit.JUnitSuite

object LendIdentityCardServiceTest extends WebDeployment

@RunWith(classOf[Arquillian])
class LendIdentityCardServiceTest extends JUnitSuite {
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(value = Array("datasets/initial.xml"))
  def selectAllLendIdentityCards(@ArquillianResteasyResource lendIdentityCardService: LendIdentityCardService): Unit =
    lendIdentityCardService.selectAllLendIdentityCards() should equal(
      Array(
        LendIdentityCardDTO("33000010", "44000013", "2016-11-05T10:00", 3, "Marc Arndt"),
        LendIdentityCardDTO("33000021", "44000024", "2016-11-05T10:00", 1, null),
        LendIdentityCardDTO("33000101", "44000104", "2016-11-05T10:00", 0, null)
      )
    )

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(value = Array("datasets/update-owner.xml"), excludeColumns = Array("LENDIDENTITYCARD.ID", "LENDIDENTITYCARD.LENDTIME"))
  def successfulUpdateOwner(@ArquillianResteasyResource lendIdentityCardService: LendIdentityCardService): Unit =
    lendIdentityCardService.updateOwner(ChangeOwnerRequestDTO("33000010", "Bob")) should equal(
      LendIdentityCardDTO("33000010", "44000013", "2016-11-05T10:00", 3, "Bob")
    )

  @Test(expected = classOf[ArquillianProxyException])
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def invalidBarcodeUpdateOwner(@ArquillianResteasyResource lendIdentityCardService: LendIdentityCardService): Unit =
    lendIdentityCardService.updateOwner(ChangeOwnerRequestDTO("33000011", "Bob"))

  @Test(expected = classOf[ArquillianProxyException])
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def unissuedBarcodeUpdateOwner(@ArquillianResteasyResource lendIdentityCardService: LendIdentityCardService): Unit =
    lendIdentityCardService.updateOwner(ChangeOwnerRequestDTO("33000032", "Bob"))
}