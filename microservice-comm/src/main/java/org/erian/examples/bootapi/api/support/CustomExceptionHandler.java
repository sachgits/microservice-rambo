package org.erian.examples.bootapi.api.support;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.erian.examples.bootapi.service.exception.ServiceException;
import org.erian.modules.constants.MediaTypes;
import org.erian.modules.mapper.JsonMapper;

import com.google.common.collect.Maps;

@ControllerAdvice(annotations = { RestController.class })
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

	private JsonMapper jsonMapper = new JsonMapper();

	@ExceptionHandler(value = { ServiceException.class })
	public final ResponseEntity<ErrorResult> handleServiceException(ServiceException ex, HttpServletRequest request) {
		// inject servletRequest, will be used for print the URL and request source when exception
		logError(ex, request);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(MediaTypes.JSON_UTF_8));
		ErrorResult result = new ErrorResult(ex.errorCode.code, ex.getMessage());
		return new ResponseEntity<ErrorResult>(result, headers, HttpStatus.valueOf(ex.errorCode.httpStatus));
	}

	@ExceptionHandler(value = { Exception.class })
	public final ResponseEntity<ErrorResult> handleGeneralException(Exception ex, HttpServletRequest request) {
		logError(ex, request);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(MediaTypes.JSON_UTF_8));
		ErrorResult result = new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		return new ResponseEntity<ErrorResult>(result, headers, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * override ResponseEntityExceptionHandler, add a logger
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		logError(ex);

		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute("javax.servlet.error.exception", ex, WebRequest.SCOPE_REQUEST);
		}

		return new ResponseEntity<Object>(body, headers, status);
	}

	public void logError(Exception ex) {
		Map<String, String> map = Maps.newHashMap();
		map.put("message", ex.getMessage());
		logger.error(jsonMapper.toJson(map), ex);
	}

	public void logError(Exception ex, HttpServletRequest request) {
		Map<String, String> map = Maps.newHashMap();
		map.put("message", ex.getMessage());
		map.put("from", request.getRemoteAddr());
		String queryString = request.getQueryString();
		map.put("path", queryString != null ? (request.getRequestURI() + "?" + queryString) : request.getRequestURI());

		logger.error(jsonMapper.toJson(map), ex);
	}
}
