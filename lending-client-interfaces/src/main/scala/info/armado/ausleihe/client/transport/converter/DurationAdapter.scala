package info.armado.ausleihe.client.transport.converter

import java.time.Duration
import java.lang.{Long => JLong}

import javax.xml.bind.annotation.adapters.XmlAdapter

class DurationAdapter extends XmlAdapter[JLong, Duration] {
  override def unmarshal(value: JLong): Duration =
    Option(value).map(duration => Duration.ofMinutes(duration)).orNull

  override def marshal(value: Duration): JLong =
    Option(value).map[JLong](duration => duration.toMinutes).orNull[JLong]
}
