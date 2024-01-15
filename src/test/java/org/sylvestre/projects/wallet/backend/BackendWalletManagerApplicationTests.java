package org.sylvestre.projects.wallet.backend;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendWalletManagerApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void testUsingStrictRegex() {
		emailAddress = "username@domain.com";
		regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
				+ "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
		assertTrue(EmailValidation.patternMatches(emailAddress, regexPattern));
	}
}
