package info.armado.ausleihe.client.transport.converter

import java.time.Year
import java.lang.{Integer => JInteger}

import javax.xml.bind.annotation.adapters.XmlAdapter

class YearAdapter extends XmlAdapter[JInteger, Year] {
  override def unmarshal(value: JInteger): Year =
    Option(value).map(year => Year.of(year)).orNull

  override def marshal(value: Year): JInteger =
    Option(value).map[JInteger](year => year.getValue).orNull[JInteger]
}
