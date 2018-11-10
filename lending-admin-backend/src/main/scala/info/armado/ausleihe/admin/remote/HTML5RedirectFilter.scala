package info.armado.ausleihe.admin.remote

import java.util.regex.Pattern

import javax.servlet.http.HttpServletRequest
import javax.servlet.{Filter, FilterChain, ServletRequest, ServletResponse}

/**
  * A servlet filter used to redirect angular website calls to the index file in html 5 mode
  */
class HTML5RedirectFilter extends Filter {
  override def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain): Unit = request match {
    case httpRequest: HttpServletRequest if !isStatic(httpRequest) && !isApi(httpRequest) && !isWebSocket(httpRequest) =>
      request.getRequestDispatcher("/index.html").forward(request, response)
    case _ => chain.doFilter(request, response)
  }

  /**
    * Checks whether the given request contains an API call
    *
    * @param request The request to be checked
    * @return true if the request contains an API call, false otherwise
    */
  private def isApi(request: HttpServletRequest): Boolean = request.getRequestURI.contains("/rest/")

  /**
    * Checks whether the given request targets a websocket
    *
    * @param request The request to be checked
    * @return true if the request targets a websocket
    */
  private def isWebSocket(request: HttpServletRequest): Boolean =
    Option(request.getHeader("Sec-WebSocket-Extensions")).isDefined &&
      Option(request.getHeader("Sec-WebSocket-Key")).isDefined &&
      Option(request.getHeader("Sec-WebSocket-Version")).isDefined

  /**
    * Checks whether the given request targets a static resource
    *
    * @param request The request to be checked
    * @return true if the request targets a static resource
    */
  private def isStatic(request: HttpServletRequest): Boolean =
    Pattern.matches(".+\\.((html)|(css)|(js)|(svg)|(jpg)|(png))$", request.getRequestURI)
}
