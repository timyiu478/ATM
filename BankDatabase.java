
public class BankDatabase {
	private Account accounts[]; // array of Accounts
	private IntegratedAccount IntegratedAccounts[]; // array of IntegratedAccount
	// no-argument BankDatabase constructor initializes accounts

	public BankDatabase() {
		IntegratedAccounts = new IntegratedAccount[2]; // just 2 IntegratedAccounts accounts for testing
		accounts = new Account[7]; // just 6 accounts for testing

		IntegratedAccounts[0] = new IntegratedAccount(12345, 54321);
		IntegratedAccounts[1] = new IntegratedAccount(98765, 56789);

		accounts[0] = new SavingAccount(12345, 54321, 1000.0, 1200.0); // saving account of IntegratedAccounts[0]
		accounts[1] = new CurrentAccount(12345, 54321, 2000.0, 2400.0);// current account of IntegratedAccounts[0]
		accounts[2] = new SavingAccount(98765, 56789, 1200.0, 1200.0);// saving account of IntegratedAccounts[1]
		accounts[3] = new CurrentAccount(98765, 56789, 200.0, 200.0); // current account IntegratedAccounts[1]
		accounts[4] = new CurrentAccount(98765, 56789, 3000.0, 4000.0);// current account IntegratedAccounts[1]
		accounts[5] = new CurrentAccount(98765, 56789, 300.0, 300.0);// current account IntegratedAccounts[1]
		accounts[6] = new CurrentAccount(98765, 56789, 300.0, 300.0);// current account IntegratedAccounts[1]
		
		IntegratedAccounts[0].setAccount(0, accounts[0]);// set IntegratedAccounts[0] contains this account
		IntegratedAccounts[0].setAccount(1, accounts[1]);// set IntegratedAccounts[0] contains this account
		IntegratedAccounts[1].setAccount(0, accounts[2]);// set IntegratedAccounts[1] contains this account
		IntegratedAccounts[1].setAccount(1, accounts[3]);// set IntegratedAccounts[1] contains this account
		IntegratedAccounts[1].setAccount(2, accounts[4]);// set IntegratedAccounts[1] contains this account
		IntegratedAccounts[1].setAccount(3, accounts[5]);// set IntegratedAccounts[1] contains this account
		IntegratedAccounts[1].setAccount(4, accounts[6]);// set IntegratedAccounts[1] contains this account
	} // end no-argument BankDatabase constructor

	// retrieve Account object containing specified account number
	private IntegratedAccount getIntegratedAccount(int IntegratedaccountNumber) {

		// loop through accounts searching for matching account number
		for (IntegratedAccount currentAccount : IntegratedAccounts) {
			// return current account if match found
			if (currentAccount.getIntegratedAccountNumber() == IntegratedaccountNumber)
				return currentAccount;
		} // end for

		return null; // if no matching account was found, return null
	} // end method getAccount

	// determine whether user-specified account number and PIN match
	// those of an account in the database
	public boolean authenticateUser(int userIntegratedAccountNumber, int userPIN) {
		// attempt to retrieve the account with the account number
		IntegratedAccount userIntegratedAccount = getIntegratedAccount(userIntegratedAccountNumber);

		// if account exists, return result of Account method validatePIN
		if (userIntegratedAccount != null)
			return userIntegratedAccount.validatePIN(userPIN);
		else
			return false; // account number not found, so return false
	} // end method authenticateUser

	// return available balance of Account with specified account number
	public double getAvailableBalance(int userIntegratedAccountNumber, int selectAccountnumber) {
		return getIntegratedAccount(userIntegratedAccountNumber).getAccount(selectAccountnumber).getAvailableBalance();
	} // end method getAvailableBalance

	// return total balance of Account with specified account number
	public double getTotalBalance(int userIntegratedAccountNumber, int selectAccountnumber) {
		return getIntegratedAccount(userIntegratedAccountNumber).getAccount(selectAccountnumber).getTotalBalance();
	} // end method getTotalBalance

	// credit an amount to Account with specified account number
	public void credit(int userIntegratedAccountNumber, int selectAccountnumber, double amount) {
		getIntegratedAccount(userIntegratedAccountNumber).getAccount(selectAccountnumber).credit(amount);
	} // end method credit

	// debit an amount from of Account with specified account number
	public void debit(int userIntegratedAccountNumber, int selectAccountnumber, double amount) {
		getIntegratedAccount(userIntegratedAccountNumber).getAccount(selectAccountnumber).debit(amount);
	} // end method debit

	// check whether the transferaccountNumber is in Bank Database
	public boolean checkaccountnumber(int transferaccountNumber) {
		for (int x = 0; x < accounts.length; x++) {
			if (accounts[x].getIntegratedAccountNumber() == transferaccountNumber)
				return true;
		}
		return false;
	}

	// return overdrawn limit of Account with specified account number
	public double getOverdrawn_limit(int userIntegratedAccountNumber, int selectAccountnumber) {
		CurrentAccount ca = (CurrentAccount) getIntegratedAccount(userIntegratedAccountNumber)
				.getAccount(selectAccountnumber);
		return ca.getOverdrawn_limit();
	}

	// return interest of Account with specified account number
	public double getInterest_rate(int userIntegratedAccountNumber, int selectAccountnumber) {
		SavingAccount sa = (SavingAccount) getIntegratedAccount(userIntegratedAccountNumber)
				.getAccount(selectAccountnumber);
		return sa.getInterest_rate();
	}

	// validate the account type is Saving or Current
	public int validateAccountType(int userIntegratedAccountNumber, int selectAccountnumber) {
		if (getIntegratedAccount(userIntegratedAccountNumber).getAccount(selectAccountnumber) instanceof SavingAccount)
			return 1;
		else
			return 0;
	}

	// return how many accounts in the IntegratedAccount
	public int getnumberofaccount(int userIntegratedAccountNumber) {
		return getIntegratedAccount(userIntegratedAccountNumber).getnumberofaccount();
	}

	// check the input whether the account in the IntegratedAccount
	public boolean checkselectnumber(int userIntegratedAccountNumber, int selectAccountnumber) {
		if (userIntegratedAccountNumber == 0)
			return false;
		else if (selectAccountnumber >= getnumberofaccount(userIntegratedAccountNumber))
			return false;
		else
			return true;
	}
}

// end class BankDatabase

/**************************************************************************
 * (C) Copyright 1992-2007 by Deitel & Associates, Inc. and * Pearson Education,
 * Inc. All Rights Reserved. * * DISCLAIMER: The authors and publisher of this
 * book have used their * best efforts in preparing the book. These efforts
 * include the * development, research, and testing of the theories and programs
 * * to determine their effectiveness. The authors and publisher make * no
 * warranty of any kind, expressed or implied, with regard to these * programs
 * or to the documentation contained in these books. The authors * and publisher
 * shall not be liable in any event for incidental or * consequential damages in
 * connection with, or arising out of, the * furnishing, performance, or use of
 * these programs. *
 *************************************************************************/