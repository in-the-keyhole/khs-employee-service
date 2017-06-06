package com.keyholesoftware.employees.security;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.security.auth.x500.X500Principal;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.util.Supplier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.GenericFilterBean;

public class X509ValidCertificateFilter extends GenericFilterBean {

	private static final Logger log = LogManager.getLogger(X509ValidCertificateFilter.class);
	private static final Marker SECURITY_MARKER = MarkerManager.getMarker("SECURITY");
	private static final Marker INVALID_CERT_MARKER = MarkerManager.getMarker("INVALID CERTIFICATE").setParents(SECURITY_MARKER);
	private static final String CERT_ATTRIBUTE = "javax.servlet.request.X509Certificate";

	@Value("${keyhole.security.whitelist}")
	private String[] whitelist;

	public X509ValidCertificateFilter() {
		super();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		X509Certificate cert = this.extractCertificate(request);
		if (cert != null && validateCertificate(cert)) {
			filterChain.doFilter(request, response);
		} else {
			log.error(INVALID_CERT_MARKER, (Supplier<String>) () -> "No valid X.509 client authentication certificate found.");
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	private boolean validateCertificate(X509Certificate cert) {
		X500Principal principal = cert.getSubjectX500Principal();
		X500Name x500name = new X500Name(principal.getName());
		RDN cn = x500name.getRDNs(BCStyle.CN)[0];
		return Arrays.asList(whitelist).contains(cn.getFirst().getValue().toString());
	}

	private X509Certificate extractCertificate(ServletRequest request) {
		X509Certificate[] certs = (X509Certificate[]) request.getAttribute(CERT_ATTRIBUTE);
		if (certs != null && certs.length > 0) {
			if (log.isDebugEnabled()) {
				log.debug("X.509 client authentication certificate found:" + certs[0]);
			}
			return certs[0];
		}
		return null;
	}
}
