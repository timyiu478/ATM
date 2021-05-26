
public class transfer extends Transaction {
	private double amount; // amount to transfer
	private Keypad keypad; // reference to keypad
	private int transferto_accountNumber; // integrated account number the user transfer to
	private int transferto_selectnumber; // account number in the integrated account the user transfer to
	private final static int CANCELED = 0; // constant for cancel option

	public transfer(int userAccountNumber, int selectnumber, Screen atmScreen, BankDatabase atmBankDatabase,
			Keypad atmKeypad) {
		super(userAccountNumber, selectnumber, atmScreen, atmBankDatabase);// initialize superclass variables
		keypad = atmKeypad; // initialize references to keypad
	}

	public void execute() {
		BankDatabase bankDatabase = getBankDatabase(); // get reference
		Screen screen = getScreen(); // get reference

		transferto_accountNumber = promptfortransferaccount(); // obtain the account number which transfer to

		if (transferto_accountNumber != CANCELED) // check the user choose cancel or input account number
		{
			transferto_selectnumber = keypad.getInput(); // obtain the account
			
			// validate the account number and select number is in the Bank Database
			while (bankDatabase.checkaccountnumber(transferto_accountNumber) == false && transferto_accountNumber != 0
					|| bankDatabase.checkselectnumber(transferto_accountNumber, transferto_selectnumber) == false
							&& transferto_accountNumber != 0) {
				screen.displayMessageLine("\nInvalid account number. Please try again.");
				transferto_accountNumber = promptfortransferaccount();
				if(transferto_accountNumber != 0)
					transferto_selectnumber = keypad.getInput();
			}

			while (transferto_accountNumber == getIntegratedAccountNumber()
					&& transferto_selectnumber == getselectedAccountNumber()) { // validate the account number is not
																				// the current account
				screen.displayMessageLine("\nCan not transfer to current using bank account.");
				transferto_accountNumber = promptfortransferaccount();
				if(transferto_accountNumber != 0)
					transferto_selectnumber = keypad.getInput();
			}

			if (transferto_accountNumber != CANCELED) {
				amount = promptfortransferfund(); // obtain the transfer amount 
			}
			// check the user choose cancel or input amount
			if (amount != CANCELED && transferto_accountNumber != CANCELED) {
				// check the user has enough money in the account to transfer
				if (bankDatabase.getAvailableBalance(getIntegratedAccountNumber(), getselectedAccountNumber()) >= amount
						|| bankDatabase.validateAccountType(getIntegratedAccountNumber(),
								getselectedAccountNumber()) == 0
								&& (bankDatabase.getOverdrawn_limit(getIntegratedAccountNumber(),
										getselectedAccountNumber())
										+ bankDatabase.getAvailableBalance(getIntegratedAccountNumber(),
												getselectedAccountNumber())) >= amount) {

					bankDatabase.debit(getIntegratedAccountNumber(), getselectedAccountNumber(), amount);
					bankDatabase.credit(transferto_accountNumber, transferto_selectnumber, amount);
					screen.displayMessageLine("\ntransfering fund Succeed.");
				} else if (amount != CANCELED) {
					do { // not enough money available in user's account

						screen.displayMessageLine("\nInsufficient amount available in the Account.");
						amount = promptfortransferfund();
					} while (bankDatabase.getAvailableBalance(getIntegratedAccountNumber(),
							getselectedAccountNumber()) < amount && amount != CANCELED);

					if (amount != CANCELED) {

						bankDatabase.debit(getIntegratedAccountNumber(), getselectedAccountNumber(), amount);
						bankDatabase.credit(transferto_accountNumber, transferto_selectnumber, amount);
						screen.displayMessageLine("\nTransfering fund Succeed.");
					}
				}
			}
		}
		if (transferto_accountNumber == CANCELED || amount == CANCELED) {
			screen.displayMessageLine("\nCanceling transaction...");
			keypad.cancelinput();
		}

	}

	private int promptfortransferaccount() {
		Screen screen = getScreen();// get reference
		screen.displayMessageLine("\nTransfer funds Menu:");
		screen.displayMessage("Please enter AccountNumber" + "\n(For example:\"12345 0\" or \"0\" to cancel): ");
		int input = keypad.getInput(); // receive input of accountNumber

		// check whether the user canceled or entered a valid accountNumber
		if (input == CANCELED)
			return CANCELED; // return 0 for cancel
		else {
			return input; // return accountNumber
		}
	}

	private double promptfortransferfund() {
		Screen screen = getScreen(); // get reference
		screen.displayMessageLine("\nTransfer funds Menu:");
		screen.displayMessageLine(
				"From Current Account: " + getIntegratedAccountNumber() + " - " + getselectedAccountNumber());
		screen.displayMessageLine(
				"Transfer to  Account: " + transferto_accountNumber + " - " + transferto_selectnumber);
		screen.displayMessage("Transfer Fund" + "(or 0 to cancel): HK$");
		double input = keypad.getdoubleinput(); // receive input of fund
		if (input == CANCELED)
			return CANCELED;// return 0 for cancel
		else
			return input; // return transfer fund
	}
}
