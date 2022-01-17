package b_Money;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;

	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");

	}

	@Test
	public void testGetName() {
		Assert.assertEquals("Test: Bank names as expected", "SweBank", SweBank.getName());
	}

	@Test
	public void testGetCurrency() {
		Assert.assertEquals("Test: Currency of bank as expected", "SEK", SweBank.getCurrency().getName());
	}

	@Test
	public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException {
		DanskeBank.openAccount("Olha");
		Assert.assertEquals(DanskeBank.accountExists("Olha"), true);
	}

	@Test
	public void testDeposit() throws AccountDoesNotExistException {
		SweBank.deposit("Ulrika", new Money(10000, SEK));
		SweBank.deposit("Ulrika", new Money(20000, SEK));
		Assert.assertEquals("Test : Add deposits to account", Integer.valueOf(30000), SweBank.getBalance("Ulrika"));
	}

	@Test
	public void testWithdraw() throws AccountDoesNotExistException {
		DanskeBank.deposit("Gertrud", new Money(100000, DKK));
		DanskeBank.withdraw("Gertrud", new Money(100000, DKK));
		Assert.assertEquals("Balance after withdrawal", Integer.valueOf(0), DanskeBank.getBalance("Gertrud"));
		//DanskeBank.withdraw("Gertrud", new Money(200000, DKK));
	}
	
	@Test
	public void testGetBalance() throws AccountDoesNotExistException {
		Assert.assertEquals("Test: Get balance of Gertrud", Integer.valueOf(0), DanskeBank.getBalance("Gertrud"));
	}
	
	@Test
	public void testTransfer() throws AccountDoesNotExistException {
		SweBank.deposit("Ulrika", new Money(5000, SEK));
		SweBank.transfer("Ulrika", "Bob", new Money(5000, SEK));
		Assert.assertEquals("Test: transfer fromAccount balance", Integer.valueOf(0), SweBank.getBalance("Ulrika"));
		Assert.assertEquals("Test: transfer toAccount balance", Integer.valueOf(5000), SweBank.getBalance("Bob"));
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		Nordea.addTimedPayment("Bob", "567", 4, 4, new Money(100, SEK), SweBank, "Ulrika");
		Nordea.tick();
		Nordea.tick();
		Nordea.tick();
		Nordea.tick();
		assertEquals(100, SweBank.getBalance("Ulrika"),1);
	}
}
