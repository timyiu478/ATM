public abstract class Transaction {
	private int IntegratedAccountNumber; // indicates account involved
	private int selectAccountNumber; // the account number which user selected
	private Screen screen; // ATM's screen
	private BankDatabase bankDatabase; // account info database

	// Transaction constructor invoked by subclasses using super()
	public Transaction(int userIntegratedAccountNumber, int selectnumber, Screen atmScreen,
			BankDatabase atmBankDatabase) {
		IntegratedAccountNumber = userIntegratedAccountNumber;
		selectAccountNumber = selectnumber;
		screen = atmScreen;
		bankDatabase = atmBankDatabase;
	} // end Transaction constructor

	// return the selected account number
	public int getselectedAccountNumber() {
		return selectAccountNumber;
	}

	// return account number
	public int getIntegratedAccountNumber() {
		return IntegratedAccountNumber;
	} // end method getIntegratedAccountNumber

	// return reference to screen
	public Screen getScreen() {
		return screen;
	} // end method getScreen

	// return reference to bank database
	public BankDatabase getBankDatabase() {
		return bankDatabase;
	} // end method getBankDatabase

	// perform the transaction (overridden by each subclass)
	abstract public void execute();
} // end class Transaction

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