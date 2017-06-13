/**
 * 
 */
package info.armado.ausleihe.faces.beans;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.extern.java.Log;

/**
 * @author marc
 *
 */
@Named("serverVersion")
@ApplicationScoped
@Log
public class ServerVersionBean {
	@Getter
	private String version;

	private LocalDateTime buildTime;

	private DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.GERMANY);

	private DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale.GERMANY);

	public String getBuildTime() {
		return buildTime.format(outputFormatter);
	}

	@PostConstruct
	private void init() {
		Properties properties = new Properties();
		
		try (BufferedInputStream stream = new BufferedInputStream(
				ServerVersionBean.class.getClassLoader().getResourceAsStream("version.txt"))) {
			properties.load(stream);
		} catch (IOException e) {
			// shouldn't happen!
			log.log(Level.SEVERE, "Couldn't load the version.txt file", e);
		}

		this.version = properties.getProperty("version");
		this.buildTime = LocalDateTime.parse(properties.getProperty("build.date"), inputFormatter);
	}
}
