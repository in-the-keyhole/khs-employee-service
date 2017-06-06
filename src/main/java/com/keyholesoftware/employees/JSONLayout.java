package com.keyholesoftware.employees;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

@Plugin(name = "KeyholeJSONLayout", category = "core", elementType = "layout", printObject = true)
public class JSONLayout extends AbstractStringLayout {

	public static final Charset UTF8 = Charset.forName("UTF-8");
	private final JsonFactory jsonFactory = new JsonFactory();

	@PluginFactory
	public static JSONLayout createLayout() {
		return new JSONLayout();
	}

	protected JSONLayout() {
		super(UTF8);
	}

	@Override
	public String toSerializable(final LogEvent event) {
		try {
			final StringWriter stringWriter = new StringWriter();
			final JsonGenerator g = jsonFactory.createGenerator(stringWriter);
			g.writeStartObject();
			// standard stuff
			g.writeStringField("logger", event.getLoggerName());
			if (event.getMarker() != null) {
				g.writeArrayFieldStart("markers");
				for (Marker marker : event.getMarker().getParents()) {
					g.writeString(marker.getName());
				}
				g.writeString(event.getMarker().getName());
				g.writeEndArray();
			}
			g.writeStringField("level", event.getLevel().toString());
			g.writeNumberField("timestamp", event.getTimeMillis());
			g.writeStringField("threadName", event.getThreadName());

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null && authentication.isAuthenticated()) {
				g.writeStringField("user", authentication.getPrincipal() + "");
			}

			// the mdc now
			final ReadOnlyStringMap mdc = event.getContextData();
			if (mdc != null)
				mdc.forEach((k, v) -> writeField(g, k, v));

			// the message now
			final Message message = event.getMessage();
			if (message != null) {
				if (message instanceof MapMessage) {
					final MapMessage mapMessage = (MapMessage) message;
					final Map<String, String> map = mapMessage.getData();
					map.forEach((k, v) -> writeField(g, k, v));
				} else {
					g.writeStringField("msg", message.toString());
				}
			}

			// TODO : exception

			g.writeEndObject();
			g.close();
			stringWriter.append("\n");
			return stringWriter.toString();
		} catch (IOException e) {
			LOGGER.error("Could not write event as JSON", e);
		}
		return "Test test test";
	}

	private void writeField(final JsonGenerator g, final String name, final Object value) {
		try {
			g.writeObjectField(name, value);
		} catch (IOException e) {
			// TODO something
		}
	}
}
