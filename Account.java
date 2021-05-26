public class Account extends IntegratedAccount {
	private double availableBalance; // funds available for withdrawal
	private double totalBalance; // funds available + pending deposits

	// Account constructor initializes attributes
	public Account(int theAccountNumber, int thePIN, double theAvailableBalance, double theTotalBalance) {
		super(theAccountNumber, thePIN);
		availableBalance = theAvailableBalance;
		totalBalance = theTotalBalance;
	} // end Account constructor

	// returns available balance
	public double getAvailableBalance() {
		return availableBalance;
	} // end getAvailableBalance

	// returns the total balance
	public double getTotalBalance() {
		return totalBalance;
	} // end method getTotalBalance

	// credits an amount to the account
	public void credit(double amount) {
		totalBalance += amount; // add to total balance
		availableBalance += amount; // add to available balance
	} // end method credit

	// debits an amount from the account
	public void debit(double amount) {
		availableBalance -= amount; // subtract from available balance
		totalBalance -= amount; // subtract from total balance
	} // end method debit

} // end class Account

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