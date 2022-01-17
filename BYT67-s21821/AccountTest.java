package b_Money;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;

	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));

		SweBank.deposit("Alice", new Money(1000000, SEK));
	}
	
	@Test
	public void testAddRemoveTimedPayment(){
		testAccount.addTimedPayment("1", 10, 10, new Money(10, SEK), SweBank, "Alice");
		assertTrue("Test : timedPayment exist", testAccount.timedPaymentExists("1"));
		testAccount.removeTimedPayment("1");
		assertFalse("Test : timedPayment exist", testAccount.timedPaymentExists("1"));
		testAccount.removeTimedPayment("1");
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		testAccount.addTimedPayment("1", 3, 0, new Money(1000, SEK), SweBank, "Alice");
		for (int i = 0; i < 10; i++) {
			testAccount.tick();
		}
		Assert.assertEquals("Test : balance after timed payment", Integer.valueOf(9995000), testAccount.getBalance().getAmount());
	}

	@Test
	public void testAddWithdraw() {
		testAccount.withdraw(new Money(3000, SEK));
		Assert.assertEquals("Test : withdraw Alice", Integer.valueOf(9997000), testAccount.getBalance().getAmount());
	}
	
	@Test
	public void testGetBalance() {
		Assert.assertEquals("Test: Get account balance", Integer.valueOf(10000000), testAccount.getBalance().getAmount());
	}
}
