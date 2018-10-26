package info.armado.ausleihe.remote.converter

import java.time.Duration

import javax.xml.bind.annotation.adapters.XmlAdapter

class DurationAdapter extends XmlAdapter[String, Duration] {
  override def unmarshal(v: String): Duration = Duration.ofMinutes(v.toLong)

  override def marshal(v: Duration): String = v.toMinutes.toString
}