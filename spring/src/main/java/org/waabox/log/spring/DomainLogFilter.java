package org.waabox.log.spring;

import java.io.IOException;

import javax.servlet.*;

import org.waabox.log.DomainLogger;

/** Cleans up the ThreadLocal storage located in the DomainLogger.
 *
 * @author waabox (me[at]waabox[dot]org)
 */
public class DomainLogFilter implements Filter {

  /** {@inheritDoc}. */
  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
  }

  /** {@inheritDoc}. */
  @Override
  public void doFilter(
      final ServletRequest request,
      final ServletResponse response,
      final FilterChain chain) throws IOException, ServletException {
    try {
      chain.doFilter(request, response);
    } finally {
      DomainLogger.cleanup();
    }
  }

  /** {@inheritDoc}. */
  @Override
  public void destroy() {
  }

}
