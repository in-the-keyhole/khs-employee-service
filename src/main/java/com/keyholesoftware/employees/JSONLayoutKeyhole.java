package com.keyholesoftware.employees;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.ExtendedStackTraceElement;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

/**
 * A JSON Layout for log4j
 * 
 * @see log4j2.json
 * @see log4j2.component.properties
 * 
 * This layout handles JSON formatting including all: 
 * 	{@link Marker}
 * 	{@link Throwable}
 * 	{@link ThreadContext} data
 *  {@link ExtendedStackTraceElement}
 * 
 *  It will also handle the flattening of {@link MapMessage} payloads.
 *  
 *  Example:
 *		log.info(SOME_MARKER, (Supplier<MapMessage>) () -> new MapMessage().with("name", "value").with("anotherName", "anotherValue"));
 *	Produces:
 *		{@code
 *			{
 *				"timeMillis": 1498156000633,
 *				"thread": "hystrix-EmployeeService-1",
 *				"level": "INFO",
 *				"logger": "com.keyholesoftware.employees.EmployeeService",
 *				"markers": ["SOME MARKER", "SOME_MARKER_PARENT"]
 *				"name": "value",
 *				"anotherName": "anotherValue",
 *				"X-B3-ParentSpanId": "5d8e8b0103e4e8d4",
 *				"X-B3-SpanId": "5d8e8b0103e4e8d4",
 *				"X-B3-TraceId": "5d8e8b0103e4e8d4",
 *				"X-Span-Export": "false",
 *				"user": "developer"
 *			}
 * 		}
 * 
 * @author jaimeniswonger
 */
@Plugin(name = "JSONLayoutKeyhole", category = "core", elementType = "layout", printObject = true)
public class JSONLayoutKeyhole extends AbstractStringLayout {

	public static final Charset UTF8 = Charset.forName("UTF-8");
	private final JsonFactory jsonFactory = new JsonFactory();

	@PluginFactory
	public static JSONLayoutKeyhole createLayout() {
		return new JSONLayoutKeyhole();
	}

	protected JSONLayoutKeyhole() {
		super(UTF8);
	}

	@Override
	public String toSerializable(final LogEvent event) {
		final StringWriter stringWriter = new StringWriter();
		try {
			final JsonGenerator g = jsonFactory.createGenerator(stringWriter);
			g.writeStartObject();

			writeStandard(g, event);

			writeMarkers(g, event.getMarker());

			writeMessage(g, event.getMessage());

			writeThrown(g, event.getThrown());

			writeUser(g);

			writeContextData(g, event.getContextData());

			g.writeEndObject();
			g.close();
			stringWriter.append("\n");
		} catch (IOException e) {
			LOGGER.error("Could not write event as JSON", e);
		}
		return stringWriter.toString();
	}

	private void writeStandard(JsonGenerator g, LogEvent event) throws IOException {
		g.writeNumberField("timeMillis", event.getTimeMillis());
		g.writeStringField("thread", event.getThreadName());
		g.writeStringField("level", event.getLevel().toString());
		g.writeStringField("logger", event.getLoggerName());
	}

	private void writeContextData(JsonGenerator g, ReadOnlyStringMap contextData) {
		if (contextData != null)
			contextData.forEach((k, v) -> writeField(g, k, v));
	}

	private void writeUser(JsonGenerator g) throws IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			g.writeStringField("user", authentication.getPrincipal() + "");
		}
	}

	private void writeThrown(JsonGenerator g, final Throwable throwable) throws IOException {
		if (throwable != null) {
			g.writeFieldName("thrown");
			g.writeStartObject();
			g.writeStringField("message", throwable.getMessage());
			g.writeStringField("name", throwable.getClass().getName());

			Throwable cause = throwable.getCause();
			if (cause != null) {
				g.writeFieldName("cause");
				g.writeStartObject();
				g.writeStringField("message", cause.getMessage());
				g.writeStringField("name", cause.getClass().getName());

				writeStackTrace(g, cause);

				g.writeEndObject();
			}

			writeStackTrace(g, throwable);

			g.writeEndObject();
		}
	}

	private void writeStackTrace(JsonGenerator g, Throwable throwable) throws IOException {
		g.writeFieldName("stackTrace");
		g.writeStartArray();
		ThrowableProxy throwableProxy = new ThrowableProxy(throwable);
		for (ExtendedStackTraceElement s : throwableProxy.getExtendedStackTrace()) {
			g.writeStartObject();
			g.writeStringField("class", s.getClassName());
			g.writeStringField("method", s.getMethodName());
			g.writeStringField("file", s.getFileName());
			g.writeStringField("line", s.getLineNumber() + "");
			g.writeStringField("location", s.getLocation());
			g.writeStringField("version", s.getVersion());
			g.writeEndObject();
		}
		g.writeEndArray();
	}

	private void writeMessage(JsonGenerator g, final Message message) throws IOException {
		if (message != null) {
			if (message instanceof MapMessage) {
				final MapMessage mapMessage = (MapMessage) message;
				final Map<String, String> map = mapMessage.getData();
				map.forEach((k, v) -> writeField(g, k, v));
			} else {
				g.writeStringField("msg", message.toString());
			}
		}
	}

	private void writeMarkers(JsonGenerator g, Marker marker) throws IOException {
		if (marker != null) {
			g.writeArrayFieldStart("markers");
			Marker[] parents = marker.getParents();
			if (parents != null) {
				for (Marker parentMarker : marker.getParents()) {
					g.writeString(parentMarker.getName());
				}
			}
			g.writeString(marker.getName());
			g.writeEndArray();
		}
	}

	private void writeField(final JsonGenerator g, final String name, final Object value) {
		try {
			g.writeObjectField(name, value);
		} catch (IOException e) {
			LOGGER.error("Could not field as JSON", e);
		}
	}
}