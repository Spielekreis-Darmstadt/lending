/**
 * 
 */
package info.armado.ausleihe.faces.filter;

import info.armado.ausleihe.faces.beans.mobile.DetectedDevice;
import info.armado.ausleihe.faces.beans.mobile.MobileBean;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author marc
 *
 */
@WebFilter(urlPatterns = { "/envelopes/*", "/games/*", "/identitycards/*" }, filterName = "deviceFilter")
public class MobileDeviceFilter implements Filter {

	public static final String mobileDestination = "/mobile/index.xhtml";
	
	@Inject
	private MobileBean mobileBean;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		boolean redirected = false;

		if (request instanceof HttpServletRequest
				&& response instanceof HttpServletResponse) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;

			if (mobileBean.getDetectedDevice() == DetectedDevice.UNINITIALIZED) {
				mobileBean.initializeDevice(httpRequest);
			}

			// Es handelt sich um ein Smartphone
			if (!mobileBean.isIgnoreForwarding()
					&& mobileBean.getDetectedDevice() == DetectedDevice.MOBILEDEVICE) {
				httpResponse.sendRedirect(httpRequest.getContextPath() + MobileDeviceFilter.mobileDestination);
				redirected = true;
			}
		}

		if (!redirected)
			chain.doFilter(request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// nothing to do
	}

}
