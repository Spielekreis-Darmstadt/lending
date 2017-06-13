package info.armado.ausleihe.remote

import org.jboss.shrinkwrap.resolver.api.maven.Maven
import org.jboss.shrinkwrap.api.ShrinkWrap
import org.jboss.shrinkwrap.api.asset.EmptyAsset
import org.jboss.shrinkwrap.api.spec.WebArchive
import org.jboss.arquillian.container.test.api.Deployment

trait WebDeployment {
  @Deployment
  def createDeployment: WebArchive = {
    val result = ShrinkWrap
      .create(classOf[WebArchive])
      .addPackages(true, "info.armado.ausleihe")
      .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
      .addAsResource("datasets/")
      .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
      .addAsWebInfResource("wildfly-ds.xml")
      .addAsLibraries {
        Maven
          .resolver
          .resolve("org.scala-lang:scala-library:2.11.8")
          .withTransitivity
          .asFile: _*
      }
      .addAsLibraries {
        Maven
          .resolver
          .resolve("org.scalatest:scalatest_2.11:3.0.0")
          .withTransitivity
          .asFile: _*
      }

    result
  }
}