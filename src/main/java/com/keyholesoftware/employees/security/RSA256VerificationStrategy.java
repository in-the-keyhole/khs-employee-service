package com.keyholesoftware.employees.security;

import static java.util.Collections.emptyList;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;

@Component
public class RSA256VerificationStrategy implements JWTVerificationStrategy {

	private static Logger log = LoggerFactory.getLogger(RSA256VerificationStrategy.class);
	private static final String BEARER = "Bearer";

	@Value("${keyhole.security.public-key-url}")
	private String publicKeyUrl;

	@Autowired
	private RestTemplate restTemplate;

	private final Map<String, RSAPublicKey> keyCache = Collections.synchronizedMap(new HashMap<String, RSAPublicKey>());

	@Override
	public Authentication verify(String jwt) {
		UsernamePasswordAuthenticationToken authentication = null;

		try {
			if (jwt != null) {
				SignedJWT signedJWT = SignedJWT.parse(jwt.replace(BEARER, ""));

				// VERIFY ALGORITHM
				if (signedJWT.getHeader().getAlgorithm().equals(JWSAlgorithm.RS256)) {

					// VERIFY CRYPTO
					String certificateId = (String) signedJWT.getHeader().getCustomParam("cid");
					JWSVerifier verifier = new RSASSAVerifier(getPublicKey(certificateId));
					signedJWT.verify(verifier);

					// VERIFY EXPIRATION
					Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
					boolean expired = new Date().after(expiration);
					if (!expired) {
						String user = signedJWT.getJWTClaimsSet().getSubject();
						authentication = user != null ? new UsernamePasswordAuthenticationToken(user, jwt, emptyList()) : null;
					}
				}
			}
		} catch (JOSEException | ParseException e) {
			log.error("An exception occurred verifying the supplied JWT: ", e);
		}
		return authentication;
	}

	private RSAPublicKey getPublicKey(String certificateId) {
		return keyCache.computeIfAbsent(certificateId, c -> {
			String publicKeyContent = restTemplate.getForObject(MessageFormat.format(publicKeyUrl, certificateId), String.class);
			Reader reader = new StringReader(publicKeyContent);
			PemFile pemFile;
			RSAPublicKey publicKey = null;
			
			try {
				pemFile = new PemFile(reader);
				byte[] keyBytes = pemFile.getPemObject().getContent();
				KeyFactory factory = KeyFactory.getInstance("RSA");
				X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
				publicKey = (RSAPublicKey) factory.generatePublic(keySpec);
				keyCache.put(c, publicKey);
			} catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
				log.error("An error occurred getting the public key", e);
			}
			return publicKey;
		});
	}

	class PemFile {

		private PemObject pemObject;

		public PemFile(Reader reader) throws IOException {
			PemReader pemReader = new PemReader(reader);
			try {
				this.pemObject = pemReader.readPemObject();
			} finally {
				pemReader.close();
			}
		}

		public PemObject getPemObject() {
			return pemObject;
		}
	}
}
