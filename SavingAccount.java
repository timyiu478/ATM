
public class SavingAccount extends Account {
	private double interest_rate = 0.001;// the default interest rate is 01%

	SavingAccount(int theAccountNumber, int thePIN, double theAvailableBalance, double theTotalBalance) {
		super(theAccountNumber, thePIN, theAvailableBalance, theTotalBalance);// initialize superclass variables

	}

	// return the interest rate
	public double getInterest_rate() {
		return interest_rate;
	}

	// change the interest rate
	public void setInterest_rate(double interest_rate) {
		this.interest_rate = interest_rate;
	}

}
