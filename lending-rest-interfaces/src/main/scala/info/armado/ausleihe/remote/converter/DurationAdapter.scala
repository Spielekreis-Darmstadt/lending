package info.armado.ausleihe.remote.converter

import javax.xml.bind.annotation.adapters.XmlAdapter
import java.time.Duration

class DurationAdapter extends XmlAdapter[String, Duration] {
	override def unmarshal(v: String): Duration = Duration.ofMinutes(v.toLong)
	override def marshal(v: Duration): String = v.toMinutes().toString()
}