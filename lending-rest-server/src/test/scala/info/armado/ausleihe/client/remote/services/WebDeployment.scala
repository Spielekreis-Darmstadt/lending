package info.armado.ausleihe.client.remote.services

import org.jboss.arquillian.container.test.api.Deployment
import org.jboss.shrinkwrap.api.ShrinkWrap
import org.jboss.shrinkwrap.api.asset.EmptyAsset
import org.jboss.shrinkwrap.api.spec.WebArchive
import org.jboss.shrinkwrap.resolver.api.maven.Maven

trait WebDeployment {
  @Deployment
  def createDeployment: WebArchive = ShrinkWrap.create(classOf[WebArchive])
    .addPackages(true, "info.armado.ausleihe")
    .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
    .addAsResource("datasets/")
    .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
    .addAsWebInfResource("wildfly-ds.xml")
    .addAsLibraries(
      Maven.resolver()
        .loadPomFromFile("pom.xml")
        .importTestDependencies()
        .resolve()
        .withTransitivity()
        .asFile(): _*
    )
}
