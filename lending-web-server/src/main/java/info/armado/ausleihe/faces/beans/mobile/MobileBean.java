/**
 * 
 */
package info.armado.ausleihe.faces.beans.mobile;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import au.com.flyingkite.mobiledetect.UAgentInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * A session bean used to save if the client wants to automatically be forwarded
 * to the mobile site, and what device type it is.
 * 
 * @author marc
 *
 */
@Named("mobile")
@SessionScoped
public class MobileBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This boolean tells if the client wants to ignore the mobile redirection.
	 * If the clients isn't a mobile device this value is ignored. Otherwise if
	 * this value if true the client doesn't get forwarded if he travels to an
	 * page not made for a mobile device.
	 */
	@Getter
	@Setter
	private boolean ignoreForwarding;

	/**
	 * This value show the type of device this client uses. If this value is
	 * DetectedDevice.UNINITIALIZED then is hasn't been checked what device type
	 * the client is. Otherwise the client can be a MOBILEDEVICE or a
	 * DESKTOPDEVICE.
	 */
	@Getter
	@Setter
	private DetectedDevice detectedDevice;

	@PostConstruct
	public void init() {
		this.ignoreForwarding = false;
		this.detectedDevice = DetectedDevice.UNINITIALIZED;
	}

	/**
	 * This method checks for the device that is sending the request to the
	 * application Depending on the device type the detectedDevice property will
	 * be set.
	 * 
	 * @param httpRequest
	 *            A request to the application containing the used device type
	 *            in its header
	 */
	public void initializeDevice(HttpServletRequest httpRequest) {
		String userAgent = httpRequest.getHeader("user-agent");
		String accept = httpRequest.getHeader("Accept");

		UAgentInfo agent = new UAgentInfo(userAgent, accept);

		if (agent.detectMobileQuick())
			this.detectedDevice = DetectedDevice.MOBILEDEVICE;
		else
			this.detectedDevice = DetectedDevice.DESKTOPDEVICE;
	}
}
