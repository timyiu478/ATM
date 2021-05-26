
public class CurrentAccount extends Account {
	private double overdrawn_limit = 10000;// the default overdrawn limit is HK$10000

	public CurrentAccount(int theAccountNumber, int thePIN, double theAvailableBalance, double theTotalBalance) {
		super(theAccountNumber, thePIN, theAvailableBalance, theTotalBalance);// initialize superclass variables
	}

	public double getOverdrawn_limit() { // return the overdrawn limit
		return overdrawn_limit;
	}

	public void setOverdrawn_limit(double overdrawn_limit) { // change the overdrawn limit
		this.overdrawn_limit = overdrawn_limit;
	}

}
