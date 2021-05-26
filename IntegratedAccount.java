
public class IntegratedAccount {
	private int IntegratedAccountNumber; // account number
	private int pin; // PIN for authentication
	// returns account number
	private Account[] theaccount = new Account[6];

	IntegratedAccount(int theaccountNumber, int thepin) {
		IntegratedAccountNumber = theaccountNumber;
		pin = thepin;
	}

	// return the Integrated Account Number
	public int getIntegratedAccountNumber() {
		return IntegratedAccountNumber;
	} // end method getAccountNumber

	// determines whether a user-specified PIN matches PIN in Account
	public boolean validatePIN(int userPIN) {
		if (userPIN == pin)
			return true;
		else
			return false;
	} // end method validatePIN

	// return the account which the user selected
	public Account getAccount(int selectedaccountnumber) {
		return theaccount[selectedaccountnumber];
	}

	// set the IntegratedAccount contains the account
	public void setAccount(int selectedaccountnumber, Account ac) {
		theaccount[selectedaccountnumber] = ac;
	}

	// return how many number of accounts in the IntegratedAccounts
	public int getnumberofaccount() {
		int x = 1;
		while (theaccount[x] != null) {
			x++;
		}
		return x;
	}
}
