package org.waabox.log.spring;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;

import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;

import org.springframework.web.context.WebApplicationContext;

import org.springframework.web.method.support.*;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;

import org.springframework.web.servlet.support.*;

/** Configuration class for Domain DomainLog.
 *
 * @author waabox (me[at]waabox[dot]org)
 */
@Configuration
public class DomainLogConfiguration
  extends AbstractDispatcherServletInitializer implements WebMvcConfigurer {

  /** {@inheritDoc}. */
  @Override
  public void addResourceHandlers(final ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/log/**")
      .addResourceLocations("classpath:/org/waabox/log/resources/");
  }

  /** {@inheritDoc}. */
  @Override
  public void onStartup(final ServletContext servletContext)
      throws ServletException {
    super.onStartup(servletContext);
    servletContext.addFilter("DomainLogFilter", DomainLogFilter.class);
  }

  /** {@inheritDoc}. */
  @Override
  public void configurePathMatch(final PathMatchConfigurer configurer) {
  }

  /** {@inheritDoc}. */
  @Override
  public void configureContentNegotiation(
      final ContentNegotiationConfigurer configurer) {
  }

  /** {@inheritDoc}. */
  @Override
  public void configureAsyncSupport(final AsyncSupportConfigurer configurer) {
  }

  /** {@inheritDoc}. */
  @Override
  public void configureDefaultServletHandling(
      final DefaultServletHandlerConfigurer configurer) {
  }

  /** {@inheritDoc}. */
  @Override
  public void addFormatters(final FormatterRegistry registry) {
  }

  /** {@inheritDoc}. */
  @Override
  public void addInterceptors(final InterceptorRegistry registry) {
  }

  /** {@inheritDoc}. */
  @Override
  public void addCorsMappings(final CorsRegistry registry) {
  }

  /** {@inheritDoc}. */
  @Override
  public void addViewControllers(final ViewControllerRegistry registry) {
  }

  /** {@inheritDoc}. */
  @Override
  public void configureViewResolvers(final ViewResolverRegistry registry) {
  }

  /** {@inheritDoc}. */
  @Override
  public void addArgumentResolvers(
      final List<HandlerMethodArgumentResolver> argumentResolvers) {
  }

  /** {@inheritDoc}. */
  @Override
  public void addReturnValueHandlers(
      final List<HandlerMethodReturnValueHandler> returnValueHandlers) {
  }

  /** {@inheritDoc}. */
  @Override
  public void configureMessageConverters(
      final List<HttpMessageConverter<?>> converters) {
  }

  /** {@inheritDoc}. */
  @Override
  public void extendMessageConverters(
      final List<HttpMessageConverter<?>> converters) {
  }

  /** {@inheritDoc}. */
  @Override
  public void configureHandlerExceptionResolvers(
      final List<HandlerExceptionResolver> exceptionResolvers) {
  }

  /** {@inheritDoc}. */
  @Override
  public void extendHandlerExceptionResolvers(
      final List<HandlerExceptionResolver> exceptionResolvers) {
  }

  /** {@inheritDoc}. */
  @Override
  public Validator getValidator() {
    return null;
  }

  /** {@inheritDoc}. */
  @Override
  public MessageCodesResolver getMessageCodesResolver() {
    return null;
  }

  /** {@inheritDoc}. */
  @Override
  protected WebApplicationContext createServletApplicationContext() {
    return null;
  }

  /** {@inheritDoc}. */
  @Override
  protected String[] getServletMappings() {
    return null;
  }

  /** {@inheritDoc}. */
  @Override
  protected WebApplicationContext createRootApplicationContext() {
    return null;
  }

}
