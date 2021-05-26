
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

// ATM.java
// Represents an automated teller machine

public class ATM extends JFrame{
	private boolean userAuthenticated; // whether user is authenticated
	private int currentIntegratedAccountNumber; // current user's integrated account number
	private int selectaccountnumber; // the account number which the user selected
	private CashDispenser cashDispenser; // ATM's cash dispenser
	private BankDatabase bankDatabase; // account information database
	
	private final int SavingAccount = 1; // determine saving account or current account
	private int numofdot=0; // count how many dot in string
	private int withdrawamount; // the amount of withdrawal
	private int transferto_accountNumber; // the integrated account number which transfer to 
	private int transferto_selectnumber; // the account number of the integrated account which transfer to 
	private double transferto_amount; // the amount of transfer
	private String accountnum=""; // the integrated account number
	private String pin=""; // the integrated account number password
	private String transferaccountnum="";  // the integrated account number which user want to transfer to
	private String withdrawamountbykeyinput="";// the certain amount of withdrawal which user choose
	private String transferamount=""; // the amount that user want to transfer
	private String decimalplaces="";
	private JButton key[] ; // 0 to 9 
	private JButton dot ; // .
	private JButton doublezero; // 00
	private JButton cancel; // use for cancel user decision
	private JButton enter; // Enter
	private JButton lbutton[]; // buttons in the screen left hand side
	private JButton rbutton[];// buttons in the screen right hand side
	private JButton clear; // a button use for clear the user input
	private JPasswordField pinpwfield; // use for collect and mask the pin input
	private JPanel screen; // a panel for show the ATM screen
	private JPanel lbuttonpanel; // a panel to control the lbutton[]
	private JPanel rbuttonpanel;// a panel to control the rbutton[]
	private JPanel buttonandscreenpanel; // a panel to control the lbutton[],the rbutton[] and screen
	private JPanel numberkey; //  a panel to control the key[],dot and doublezero
	private JPanel specialkey; // a panel to control cancel,clear and enter
	private JPanel keypanel;  // a panel to control numberkey and special key
	private JTextField accountnumtextfield;	// a textfield to collect the account number 
	private JTextField chooseamount;// a textfield to collect the withdraw amount 
	private JTextField transferaccounttextfield; // a textfield to collect the account number which transfer to 
	private JTextField transferamounttextfield; // a textfield to collect the amount which transfer to 

	 
	private performTransactions_handler theperformTransactions_handler ; // handle the action events in Transactions
	private authenticateUser_handler achandler;// handle the action events in authenticate User
	private selectAccount_handler selectachandler ;;// handle the action events in select Account
	private Withdrawal_handler thewithdrawal_handler ; // handle the action events in withdrawal
	private promptfortransferaccount_handler thepromptfortransferaccount_handler;// handle the action events in prompt for transfer account
	private promptfortransferfund_handler thepromptfortransferfund_handler;// handle the action events in prompt for transfer fund
	private withdrawconfirmation_handler thewithdrawconfirmation_handler; // handler the action events in withdrawal confirmation
	private transferconfirmation_handler thetransferconfirmation_handler; // handler the action events in transfer confirmation
	private BalanceInquiry_handler theBalanceInquiry_handler; // handler the action events in Balance Inquiry
	
	
	private authenticateUserthread theauthenticateUserthread;  // authenticate User
	private BalanceInquirythread theBalanceInquirythread;  // back to main menu after 10 seconds if user doesn't click the specific button
	private InvalidWithdrawalthread theInvalidWithdrawalthread; // run this thread if invalid withdrawal
	private withdrawalremiderthread thewithdrawalremiderthread; // run this thread if valid withdrawal
	private invalidtransferamountthread theinvalidtransferamountthread; // run this thread if invalid transfer amount
	private invalidtransferaccountthread theinvalidtransferaccountthread;// run this thread if invalid transfer account number
	private carddispenedthread thecarddispenedthread; // remind user to take card
	private welcomethread thewelcomethread; // show to welcome and authenticate User
	
	// control start ,yield or stop the above thread
	private Thread authenticateUser;  
	private Thread theInvalidWithdrawal;
	private Thread BalanceInquiry;
	private Thread withdrawalremider;
	private Thread invalidtransfer_amount;
	private Thread invalidtransferaccount;
	private Thread carddispened;
	private Thread welcome;
	
	
	// no-argument ATM constructor initializes instance variables
	public ATM() {
		userAuthenticated = false; // user is not authenticated to start
		currentIntegratedAccountNumber = 0; // no current account number to start
		cashDispenser = new CashDispenser(); // create cash dispenser
		bankDatabase = new BankDatabase(); // create account info database
		
		 key =  new JButton[10];  // create 0-9
		for(int x =0;x<10;x++) {
			 key[x]= new JButton(String.valueOf(x));
		}
		 dot = new JButton("."); // create dot
		 doublezero = new JButton("00"); // create 00
		
		 cancel = new JButton("Cancel"); // create cancel button
		 clear  = new JButton("Clear");// create clear button
		 enter = new JButton("Enter");// create enter button
		
		 lbutton =  new JButton[4]; // create 4 left button in the screen left hand side
		for(int x = 0;x<4;x++) {
			lbutton[x]= new JButton("");
			lbutton[x].setActionCommand("l"+String.valueOf(x)); // set their action command 
		}
		
		 rbutton =  new JButton[4];// create 4 right button in the screen right hand side
		for(int x = 0;x<4;x++) {
			rbutton[x]= new JButton("");
			rbutton[x].setActionCommand("r"+String.valueOf(x));// set their action command 
		}
		
		 screen = new JPanel(); // create screen panel
		 lbuttonpanel = new JPanel();// create left button panel
		 rbuttonpanel = new JPanel();// create right button panel
		 buttonandscreenpanel = new JPanel();// create button and screen panel
		
		 numberkey = new JPanel();	// create number key panel	
		 specialkey = new JPanel();// create special key panel
		 keypanel = new JPanel();// create key panel
		
		lbuttonpanel.setLayout(new GridLayout(4, 1)); // set the panel layout 
		for(int x = 0;x<4;x++) {
		lbuttonpanel.add(lbutton[x]); // add left buttons to lbuttonpanel
		}
		
		rbuttonpanel.setLayout(new GridLayout(4, 1));// set the panel layout 
		for(int x = 0;x<4;x++) {
		rbuttonpanel.add(rbutton[x]);// add right buttons to rbuttonpanel
		}
		
		numberkey.setLayout(new GridLayout(4, 3));// set the panel layout 
		for(int x = 7;x<10;x++) {
			numberkey.add(key[x]); // add 7,8,9 to number key panel
		}
		for(int x = 4;x<7;x++) {
			numberkey.add(key[x]); // add 4,5,6 to number key panel
		}
		for(int x = 1;x<4;x++) {
			numberkey.add(key[x]); // add 1,2,3 to number key panel
		}
		
		numberkey.add(key[0]);   // add 0,.,00 to number key panel
		numberkey.add(dot);
		numberkey.add(doublezero);
		
		specialkey.setLayout(new GridLayout(3, 1)); // set the panel layout 
		specialkey.add(cancel); // add cancel , clear , enter to special key panel
		specialkey.add(clear);
		specialkey.add(enter);
		
		keypanel.setLayout(new BorderLayout()); // set the panel layout 
		keypanel.add(numberkey,BorderLayout.CENTER); // add number key panel to key panel's center
		keypanel.add(specialkey,BorderLayout.EAST);// add special key panel to key panel's east
		
		buttonandscreenpanel.setLayout(new BorderLayout()); // set the panel layout
		buttonandscreenpanel.add(lbuttonpanel,BorderLayout.WEST); // add left button panel to this panel's west
		buttonandscreenpanel.add(screen,BorderLayout.CENTER);// add screen panel to key panel's center
		buttonandscreenpanel.add(rbuttonpanel,BorderLayout.EAST);// add right button panel to key panel's east
		
		this.getContentPane().setLayout(new BorderLayout());// set the atm panel layout
		this.getContentPane().add(buttonandscreenpanel,BorderLayout.CENTER); // add buttonandscreenpanel to atm panel's center
		this.getContentPane().add(keypanel,BorderLayout.SOUTH);// add keypanel to atm panel's south
		
		theperformTransactions_handler = new performTransactions_handler(); // create different handler to handler different action event 
		 achandler = new authenticateUser_handler();
		 selectachandler = new selectAccount_handler();
		 thewithdrawal_handler = new Withdrawal_handler();
		 thewithdrawconfirmation_handler = new withdrawconfirmation_handler();
		 thetransferconfirmation_handler = new transferconfirmation_handler();
		 theBalanceInquiry_handler = new BalanceInquiry_handler();
		 thepromptfortransferaccount_handler = new promptfortransferaccount_handler(); 
		 thepromptfortransferfund_handler = new promptfortransferfund_handler();
		 
		 theauthenticateUserthread = new authenticateUserthread(); // create different objects
		 theBalanceInquirythread = new BalanceInquirythread();
		 theInvalidWithdrawalthread = new InvalidWithdrawalthread();
		 thewithdrawalremiderthread = new withdrawalremiderthread();
		 theinvalidtransferamountthread = new invalidtransferamountthread();
		 theinvalidtransferaccountthread = new invalidtransferaccountthread();
		 thecarddispenedthread = new carddispenedthread();
		 thewelcomethread = new welcomethread();
		 
	} // end no-argument ATM constructor
	// start ATM
	
	// handler invalid transfer account situation 
	private class invalidtransferaccountthread implements Runnable{
		public void run() { 
			try {			
				TimeUnit.SECONDS.sleep(2); // stop the present thread 2 seconds
				promptfortransferaccount(); // back to input transfer account
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
    }
	}
	// handle invalid transfer amount situation
	private class invalidtransferamountthread implements Runnable{
		public void run() { 
			try {			
				TimeUnit.SECONDS.sleep(2);// stop the present thread 2 seconds
				promptfortransferfund();// back to input transfer fund
				
			} catch (InterruptedException e) {
				e.printStackTrace();// handle InterruptedException
			}	
    }
	}
	
	// use to remind user take cash and card and back to greeting screen and authenticate user again
	private class withdrawalremiderthread implements Runnable{
		public void run() { 
			try {			
				TimeUnit.SECONDS.sleep(2);// stop the present thread 2 seconds
				JLabel cardejected = new JLabel("Card Ejected",0); // create label
				cardejected.setFont(new Font("monospaced", Font.BOLD|Font.ITALIC , 16));// change the cardejected font type and size
				screen.add(cardejected); // add label to screen panel
				
				screen.repaint(); // repaint the screen panel
		    	screen.revalidate(); // tells the layout manager to recalculate the layout
		    	
		    	TimeUnit.SECONDS.sleep(2);// stop the present thread 2 seconds
		    	remindusertakecash(); // remind user take cash 
		    	
		    	TimeUnit.SECONDS.sleep(2);// stop the present thread 2 seconds
		    	JLabel cashdispended = new JLabel("Cash Dispended",0);// create label
		    	cashdispended.setFont(new Font("monospaced", Font.BOLD|Font.ITALIC , 16));// change the cardejected font type and size
		    	screen.add(cashdispended);// add label to screen panel
		    	screen.repaint();// repaint the screen panel
		    	screen.revalidate();// tells the layout manager to recalculate the layout
		    	
		    	TimeUnit.SECONDS.sleep(2);// stop the present thread 2 seconds
		    	welcome();// show greeting screen 
		    	
		    	TimeUnit.SECONDS.sleep(2);// stop the present thread 2 seconds
		    	authenticateUser(); // authenticate user
		    	
			} catch (InterruptedException e) {
				e.printStackTrace(); // handle InterruptedException
			}			
	}
	}
	
	// show greeting screen and authenticate user
	private class welcomethread implements Runnable{
		public void run() { 
			try {	
				TimeUnit.SECONDS.sleep(2);// stop the present thread 2 seconds
				welcome();	// show greeting screen 
				TimeUnit.SECONDS.sleep(2);// stop the present thread 2 seconds
				insertcard();
				TimeUnit.SECONDS.sleep(2);// stop the present thread 2 seconds
				authenticateUser();	// authenticate user
				
			} catch (InterruptedException e) {
				e.printStackTrace();// handle InterruptedException
			}			
	}
}
	
	// back to main menu after 10 seconds if user doesn't click the button
	private class BalanceInquirythread implements Runnable{
		public void run() { 
			try {			
				TimeUnit.SECONDS.sleep(10); // stop the present thread 10 seconds
				performTransactions();// back to main menu
				
			} catch (InterruptedException e) {
				e.printStackTrace();// handle InterruptedException
			}			
	}
}
	// handle invalid withdrawal 
    private class InvalidWithdrawalthread implements Runnable{
    	public void run() { 
			try {			
				TimeUnit.SECONDS.sleep(2); // stop the present thread 2 seconds
				Withdrawal();// back to withdrawal selection
				
			} catch (InterruptedException e) {
				e.printStackTrace();// handle InterruptedException
			}	
    }
    }
	
	private class carddispenedthread implements Runnable{
		public void run() { 
			try {	
				TimeUnit.SECONDS.sleep(2);// stop the present thread 2 seconds
				
		    	screen.removeAll(); // remove all components in screen panel
		    	
		    	screen.setLayout(new GridLayout(4,1)); // set the layout 
		    	JLabel remindtakecard = new JLabel("Remember take your card",0); // create label
		    	remindtakecard.setFont(new Font("monospaced", Font.BOLD|Font.ITALIC , 16)); // set the label font size and type
		    	screen.add(new JLabel("")); // add the label to screen panel
		    	screen.add(remindtakecard); // add the label to screen panel
		    	
		    	screen.repaint();// repaint the screen pane
		    	screen.revalidate(); // tells the layout manager to recalculate the layout
				TimeUnit.SECONDS.sleep(2);// stop the present thread 2 seconds
				carddispened();
				
			} catch (InterruptedException e) {
				e.printStackTrace();// handle InterruptedException
			}	
    }
	}
	
	// authenticate user
	private class authenticateUserthread implements Runnable{
		public void run() { 
			try {
			TimeUnit.SECONDS.sleep(2);// stop the present thread 2 seconds
			authenticateUser();	// authenticate user
			
		} catch (InterruptedException e) {
			e.printStackTrace();// handle InterruptedException
		}	
		}
	}
	
	public void run() {
		welcome = new Thread(thewelcomethread); // create Thread
		welcome.start(); // start the Thread 
		Thread.yield(); // yield the Thread
	
	} // end method run
    

    private void insertcard() {
    	JLabel insertcard = new JLabel("Please insert the card",0); // create label
    	insertcard.setFont(new Font("monospaced", Font.BOLD|Font.ITALIC , 16));// set label font size and type
		screen.add(insertcard); // add the label in screen panel
		screen.revalidate();// repaint the screen panel
		screen.repaint();// recalculate the layout
    }
    
    // show card dispened
    private void carddispened() {
    
    	JLabel cashdispended = new JLabel("Cash Dispended",0); // create label
    	cashdispended.setFont(new Font("monospaced", Font.BOLD|Font.ITALIC , 16)); // set label font type and size
    	
    	screen.add(cashdispended); // add label to screen panel
    	screen.repaint(); // repaint the screen panel 
    	screen.revalidate(); // recalculate the layout
    	
    	run();
    	
    }
    
    // run this method when user click the cancel button at withdrawal
    private void cancelwithdraw() {
    	
    	carddispened = new Thread(thecarddispenedthread); // create the Thread 
    	carddispened.start(); // start the Thread 
    	Thread.yield(); // yield the Thread
    	
    	screen.removeAll(); // remove all components in screen panel
    	screen.setLayout(new BorderLayout()); // set the screen panel layout
    	
    	JLabel cancelwithdraw = new JLabel("Canceling withdrawal...",0); // create label
    	cancelwithdraw.setFont(new Font("monospaced", Font.BOLD|Font.ITALIC , 18)); // set the label font type and size
    	screen.add(cancelwithdraw,BorderLayout.CENTER); // add the label to screen panel
    	
    	screen.repaint();// repaint the screen panel 
    	screen.revalidate();// recalculate the layout
    } 
    
 // run this method when user click the cancel button at transfer
    private void canceltransfer() {
    	carddispened = new Thread(thecarddispenedthread);// create the Thread
    	carddispened.start();// start the Thread
    	Thread.yield(); // yield the Thread
    	
    	screen.removeAll(); // remove all components in screen panel
    	screen.setLayout(new BorderLayout());// set the screen panel layout
    	
    	JLabel canceltransfer = new JLabel("Canceling transaction...",0);// create label
    	canceltransfer.setFont(new Font("monospaced", Font.BOLD|Font.ITALIC , 18));// set the label font type and size
    	screen.add(canceltransfer,BorderLayout.CENTER);// add the label to screen panel
    	
    	screen.repaint();// repaint the screen panel 
    	screen.revalidate();// recalculate the layout
    }
    
    // handle invalid transfer account number situation
    private void invalidtransferaccount() {
    	
    	invalidtransferaccount = new Thread(theinvalidtransferaccountthread);// create the Thread
    	invalidtransferaccount.start();// start the Thread
    	Thread.yield();// yield the Thread
    	
    	screen.removeAll(); // remove all components in screen panel
    	
    	screen.setLayout(new BorderLayout());// set the screen panel layout
    	
    	JLabel invalidaccountnum = new JLabel("Invalid account number. Please try again.",0);// create label
    	JLabel currentaccountnum = new JLabel("Can not transfer to current using bank account.",0);// create label
    	
    	invalidaccountnum.setFont(new Font("monospaced", Font.BOLD|Font.ITALIC , 16));// set the label font type and size
    	currentaccountnum.setFont(new Font("monospaced", Font.BOLD|Font.ITALIC , 16));// set the label font type and size
    	
    	if(bankDatabase.checkaccountnumber(transferto_accountNumber) == false||
    			bankDatabase.checkselectnumber(transferto_accountNumber, transferto_selectnumber) == false) {
    	      screen.add(invalidaccountnum,BorderLayout.CENTER); // add this label to screen panel if transfer account number doesn't exist
    	}														 
    	else if(transferto_accountNumber == currentIntegratedAccountNumber&&transferto_selectnumber==selectaccountnumber) {
    		 screen.add(currentaccountnum,BorderLayout.CENTER); // add this label to screen panel if transfer account number == current account number
    	}
    	
    	screen.repaint();// repaint the screen panel 
    	screen.revalidate();// recalculate the layout
    }
    
    // handle invalid transfer amount situation
    private void invalidtransferamount() {
    	
    	invalidtransfer_amount = new Thread(theinvalidtransferamountthread);// create the Thread
		invalidtransfer_amount.start();// start the Thread
		Thread.yield();// yield the Thread
    	
    	screen.removeAll();// remove all components in screen panel
    	
		screen.setLayout(new BorderLayout());// set the screen panel layout
		
		JLabel invalidtransferamount = new JLabel("Invalid transfer amount. Please input again.",0);// create label
		JLabel insufficientamount = new JLabel("Insufficient amount available in the Account.",0);// create label
		
		invalidtransferamount.setFont(new Font("monospaced", Font.BOLD|Font.ITALIC , 16));// set the label font type and size
		insufficientamount.setFont(new Font("monospaced", Font.BOLD|Font.ITALIC , 16));// set the label font type and size
		
		if(decimalplaces.length()>2&&numofdot>0) {
		screen.add(invalidtransferamount,BorderLayout.CENTER); // add this label to screen panel if decimalplaces.length()>2 and numofdot >0
		}
		else {
		screen.add(insufficientamount,BorderLayout.CENTER); // add this label to screen panel if decimalplaces.length()>2 and numofdot >0 is false
		}
		
		screen.revalidate();// repaint the screen panel
		screen.repaint();// recalculate the layout
	
    }
    
    // handle the transfer succeed situation
    private void transfersucceed() {
    	 	
    	authenticateUser = new Thread(theauthenticateUserthread);// create the Thread
		authenticateUser.start();// start the Thread
		Thread.yield();// yield the Thread
    	
    	screen.removeAll(); // remove all components in screen panel
    	
    	screen.setLayout(new GridLayout(4,1));// set the screen panel layout
    	JLabel transfersucceed = new JLabel("Transfering fund Succeed.",0);// create label
    	transfersucceed.setFont(new Font("monospaced", Font.BOLD|Font.ITALIC , 16));// set the label font type and size
    	
    	screen.add(new JLabel("")); // add label to screen panel
    	screen.add(transfersucceed);// add label to screen panel
    	
    	screen.repaint();// repaint the screen panel
    	screen.revalidate();// recalculate the layout
    	
    
    }
    
    // transfer confirmation
    private void transfer_confirmation() {
    	enter.removeActionListener(thetransferconfirmation_handler); // remove buttons's action listener
    	cancel.removeActionListener(thetransferconfirmation_handler);
    	
    	screen.removeAll();// remove all components in screen panel
    	
    	final DecimalFormat df = new DecimalFormat("$#,##0.00"); // set the decimal format 
    	
    	JLabel transfermenu = new JLabel("Confrimation",0);// create label
		JLabel currrentaccount = new JLabel("From Current Account: " + currentIntegratedAccountNumber + " - " + selectaccountnumber,0);// create label
		JLabel transferaccount = new JLabel("Transfer to  Account: " + transferto_accountNumber + " - " + transferto_selectnumber,0);// create label
		JLabel transferfund = new JLabel("Transfer Fund: "+String.valueOf(df.format(transferto_amount)),0);// create label
		JLabel Enter = new JLabel("Press \"Enter\" to confirm",0);// create label
		
		screen.setLayout(new GridLayout(5,1));// set the screen layout
		
		screen.add(transfermenu); // add labels to screen panel
		screen.add(currrentaccount);
		screen.add(transferaccount);
		screen.add(transferfund);		
		screen.add(Enter);	
		
		screen.revalidate(); // repaint the screen panel
		screen.repaint();// recalculate the layout
		
		enter.addActionListener(thetransferconfirmation_handler); // add the buttons's action listener
		cancel.addActionListener(thetransferconfirmation_handler);
    }
    
    // remind user take the cash
    private void remindusertakecash() {
    	
    	screen.removeAll(); // remove all components in screen panel
    	
    	screen.setLayout(new GridLayout(4,1));// set the screen layout
    	
    	JLabel remindtakecash = new JLabel("Remember take your cash",0);// create label
    	remindtakecash.setFont(new Font("monospaced", Font.BOLD|Font.ITALIC , 16));// set the label font size and type
    	
    	screen.add(new JLabel(""));// add labels to screen panel
    	screen.add(remindtakecash);
    	
    	screen.repaint();// repaint the screen panel
    	screen.revalidate();// recalculate the layout
    }
    
    // remind user take the card
    private void remindusertakecard() {
    	
    	withdrawalremider = new Thread(thewithdrawalremiderthread);// create Thread
    	withdrawalremider.start(); // start the Thread
    	Thread.yield(); // yield the Thread

    	screen.removeAll(); // remove all components in screen panel
    	
    	screen.setLayout(new GridLayout(4,1));// set the screen layout
    	
    	JLabel remindtakecard = new JLabel("Remember take your card",0);// create label
    	remindtakecard.setFont(new Font("monospaced", Font.BOLD|Font.ITALIC , 16));// set the label font size and type
    	
    	screen.add(new JLabel(""));// add labels to screen panel
    	screen.add(remindtakecard);
    	
    	screen.repaint();// repaint the screen panel
    	screen.revalidate();// recalculate the layout
    }
    
    // withdrawal confirmation
    private void withdrawal_confirmation() {
    	
    	enter.removeActionListener(thewithdrawconfirmation_handler);// remove buttons's action listener
    	cancel.removeActionListener(thewithdrawconfirmation_handler);
    	
    	screen.removeAll();// remove all components in screen panel
    	
    	screen.setLayout(new GridLayout(4,1));// set the screen layout
    	
    	JLabel confirmation = new JLabel("\"Confirmation\"",0);// create labels
    	JLabel withdrawamountlabel = new JLabel("Withdraw Amount:"+withdrawamount,0);
    	JLabel Enter = new JLabel("Press \"Enter\" to confirm",0);
    	
    	screen.add(confirmation);// add labels to screen panel
    	screen.add(withdrawamountlabel);
    	screen.add(Enter);
    	
    	screen.revalidate();// repaint the screen panel
    	screen.repaint();// recalculate the layout
    	
    	enter.addActionListener(thewithdrawconfirmation_handler);// add buttons's action listener
    	cancel.addActionListener(thewithdrawconfirmation_handler);
    }
    
    // handle insufficient fund situation
    private void Insufficient_funds() {
    	
    	theInvalidWithdrawal = new Thread(theInvalidWithdrawalthread);// create Thread
    	theInvalidWithdrawal.start(); // start Thread
		Thread.yield();	// yield Thread
    	
    	screen.removeAll(); // remove all components in screen panel
    	
		screen.setLayout(new BorderLayout()); // set screen panel layout
		
		JLabel welcome = new JLabel("Insufficient funds in your account.Please choose a smaller amount.",0);// create label
		welcome.setFont(new Font("monospaced", Font.BOLD|Font.ITALIC , 16));// set label font size and type
		
		screen.add(welcome,BorderLayout.CENTER); // add label to screen panel
		
		screen.revalidate();// repaint the screen panel
		screen.repaint();// recalculate the layout
    }
    
    // handle insufficient cash situation
    private void Insufficient_cash() {
    	
    	theInvalidWithdrawal = new Thread(theInvalidWithdrawalthread);// create Thread
    	theInvalidWithdrawal.start();// start Thread
		Thread.yield();// yield Thread
		
    	screen.removeAll();// remove all components in screen panel
    	
		screen.setLayout(new BorderLayout()); // set screen panel layout
		
		JLabel welcome = new JLabel("Insufficient cash available in the ATM.Please choose a smaller amount.",0);// create label
		welcome.setFont(new Font("monospaced", Font.BOLD|Font.ITALIC , 16));// set label font size and type
		
		screen.add(welcome,BorderLayout.CENTER);// add label to screen panel
		
		screen.revalidate();// repaint the screen panel
		screen.repaint();// recalculate the layout
    }
	
    // handle invalid withdraw amount situation
    private void invalidwithdrawamount() {
    	
    	theInvalidWithdrawal = new Thread(theInvalidWithdrawalthread);// create Thread
    	theInvalidWithdrawal.start();// start Thread
		Thread.yield();// yield Thread
		
    	screen.removeAll();// remove all components in screen panel
    	
		screen.setLayout(new BorderLayout());// set screen panel layout
		
		JLabel invalidinput = new JLabel("Invalid input. Please input again",0);// create label
		invalidinput.setFont(new Font("monospaced", Font.BOLD|Font.ITALIC , 16));// set label font size and type
		
		screen.add(invalidinput,BorderLayout.CENTER);// add label to screen panel
		
		screen.revalidate();// repaint the screen panel
		screen.repaint();// recalculate the layout
    }
	
    // show greeting screen 
    private void welcome() {
		
		screen.removeAll();// remove all components in screen panel
		
		screen.setLayout(new GridLayout(4,1));// set screen panel layout
		
		JLabel welcome = new JLabel("Welcome",0);// create label
		welcome.setFont(new Font("monospaced", Font.BOLD|Font.ITALIC , 24));// set label font size and type
		
		screen.add(new JLabel(""));// add label to screen panel
		screen.add(welcome);// add label to screen panel
		
		screen.revalidate();// repaint the screen panel
		screen.repaint();// recalculate the layout
		

	}
	
	// attempts to authenticate user against database
	private void authenticateUser() {

		
		accountnum =""; // reset account number
    	pin = ""; // reset pin
    	
    	// remove buttons's action listener
		for(int x =0;x<10;x++) {
			key[x].removeActionListener(achandler);
		}
		cancel.removeActionListener(achandler);
		doublezero.removeActionListener(achandler);
		clear.removeActionListener(achandler);
		enter.removeActionListener(achandler);
		
		
		screen.removeAll();// remove all components in screen panel

		screen.setLayout(new GridLayout(4,1));// set screen panel layout
		
		JPanel accountnumpanel = new JPanel(); // create account number panel 
		JPanel pinpanel = new JPanel(); // create pin panel 
	
		JLabel accountlabel = new JLabel("Account Number:"); // create labels
		JLabel pinlabel = new JLabel    ("PIN                      :");
		
		accountnumtextfield = new JTextField(30); // create text field
		
		pinpwfield = new JPasswordField(30); // create password field
		
		
		
		
		accountnumpanel.setLayout(new FlowLayout()); // set account number panel layout
		pinpanel.setLayout(new FlowLayout()); // set pin panel layout
		
		accountnumpanel.add(accountlabel); // add label to account number panel 
		accountnumpanel.add(accountnumtextfield);// add text field to account number panel 
		
		pinpanel.add(pinlabel); // add label to pin panel
		pinpanel.add(pinpwfield); // add password field to pin panel 
		
		
		screen.add(new JLabel("")); // add label to screen panel
		screen.add(new JLabel(""));	// add label to screen panel
		screen.add(accountnumpanel);// add accountnumpanel to screen panel
		screen.add(pinpanel);		// add pinpanel to screen panel
		
		// add buttons's action listener
		for(int x =0;x<10;x++) {
			key[x].addActionListener(achandler);
		}
		cancel.addActionListener(achandler);
		clear.addActionListener(achandler);
		enter.addActionListener(achandler);
		doublezero.addActionListener(achandler);
		
		
		screen.revalidate();// repaint the screen panel
		screen.repaint();// recalculate the layout


	} // end method authenticateUser

	// display the main menu and perform transactions
	private void performTransactions() {
		
		// remove buttons's action listener
		for(int x = 2;x<4;x++) { 
			lbutton[x].removeActionListener(theperformTransactions_handler);
			rbutton[x].removeActionListener(theperformTransactions_handler);
		}
		enter.removeActionListener(theperformTransactions_handler);
    	
    	
		screen.removeAll();// remove all components in screen panel
		
		screen.setLayout(new GridLayout(4,2));// set screen panel layout
		
		screen.add(new JLabel("")); // add labels to screen panel
		screen.add(new JLabel(""));
		screen.add(new JLabel(""));
		screen.add(new JLabel(""));
		
		screen.add(new JLabel(("View my balance"),2));
		screen.add(new JLabel(("Withdraw cash"),4));
		screen.add(new JLabel(("Transfer funds"),2));
		screen.add(new JLabel(("Exit"),4));
		
		// add buttons's action listener
		for(int x = 2;x<4;x++) {
			lbutton[x].addActionListener(theperformTransactions_handler);
			rbutton[x].addActionListener(theperformTransactions_handler);
		}
		enter.addActionListener(theperformTransactions_handler);
		
		
		screen.revalidate();// repaint the screen panel
		screen.repaint();// recalculate the layout
		
	} // end method performTransactions


	// show the select account interface and return the user selection
	private void selectAccount(int accountNumber) {
		
		screen.removeAll();// remove all components in screen panel
	
		// remove buttons's action listener
		 enter.removeActionListener(selectachandler);
		for(int x=0;x<4;x++) {
			lbutton[x].removeActionListener(selectachandler);
			rbutton[x].removeActionListener(selectachandler);
		}
		
		
		JLabel account[] = new JLabel[6]; // create labels
		for(int x = 0;x < bankDatabase.getnumberofaccount(accountNumber); x++) { // set the label text
			account[x] = new JLabel("");
			if(bankDatabase.validateAccountType(accountNumber,x)==1)
				account[x].setText("(Saving) "+String.valueOf(accountNumber) +"-"+String.valueOf(x));
			else
				account[x].setText("(Current) "+String.valueOf(accountNumber) +"-"+String.valueOf(x));
			screen.add(account[x]);
		}
		
		// add buttons's action listener
		 cancel.addActionListener(selectachandler);
		 enter.addActionListener(selectachandler);
		for(int x=0;x<4;x++) {
			lbutton[x].addActionListener(selectachandler);
			rbutton[x].addActionListener(selectachandler);
		}
	
		
		screen.revalidate();// repaint the screen panel
		screen.repaint();// recalculate the layout
		
	}

	// show the BalanceInquiry interface
	private void BalanceInquiry() {
	
		rbutton[3].removeActionListener(theBalanceInquiry_handler);// remove button's action listener
		
		final DecimalFormat df = new DecimalFormat("$#,##0.00"); // set decimal format 
		
		// get and store available balance 
		double availableBalance = bankDatabase.getAvailableBalance(currentIntegratedAccountNumber,selectaccountnumber); 
		// get and store total balance 
		double totalBalance = bankDatabase.getTotalBalance(currentIntegratedAccountNumber,selectaccountnumber);
		
		
		screen.removeAll();// remove all components in screen panel
		
		screen.setLayout(new GridLayout(6,1)); // set screen panel layout
		
		JLabel balanceinfo = new JLabel("Balance Information:",0); // create labels
		JLabel availablebalance = new JLabel(" - Available balance: "+df.format(availableBalance),0);
		JLabel totalbalance = new JLabel(" - Total balance:     "+df.format(totalBalance),0);
		
		
		screen.add(new JLabel("")); // add labels to screen panel
		screen.add(balanceinfo);
		screen.add(availablebalance);
		screen.add(totalbalance);
		
		
		// if account is saving account, get and store interest rate , add interestrate label to screen panel
		if (bankDatabase.validateAccountType(currentIntegratedAccountNumber,
				selectaccountnumber) == SavingAccount) 
		{ 
			double interest_rate = bankDatabase.getInterest_rate(currentIntegratedAccountNumber,
					selectaccountnumber);
			JLabel interestrate = new JLabel(" - Interest Rate:      "+ interest_rate * 100 + "%",0);
			screen.add(interestrate);
		}
		else {	// if account is current account, get and store overdrawn limit , add overdrawnlimit label to screen panel
			double overdrawn_limit = bankDatabase.getOverdrawn_limit(currentIntegratedAccountNumber,
					selectaccountnumber);
			JLabel overdrawnlimit = new JLabel(" - Overdrawn Limit:   "+overdrawn_limit,0);
			screen.add(overdrawnlimit);
		}
		
		screen.add(new JLabel("Back to Main Menu",4)); // add label to screen panel
		
		rbutton[3].addActionListener(theBalanceInquiry_handler);// add button's action listener
		
		screen.revalidate();// repaint the screen panel
		screen.repaint();// recalculate the layout
		
		

	};
	
	// show the Withdrawal interface and attempts to withdraw 
	private void Withdrawal() {
		withdrawamountbykeyinput=""; //reset withdrawamountbykeyinput
		
		cancel.removeActionListener(achandler);// remove button's action listeners

		for(int x=0;x<10;x++) {
			key[x].removeActionListener(thewithdrawal_handler);
			if(x<3) {
				lbutton[x].removeActionListener(thewithdrawal_handler);
				
			}
			if(x<3) {
				rbutton[x].removeActionListener(thewithdrawal_handler);
			}
		}
		doublezero.removeActionListener(thewithdrawal_handler);
		enter.removeActionListener(thewithdrawal_handler);
		clear.removeActionListener(thewithdrawal_handler);
		cancel.removeActionListener(thewithdrawal_handler);
		cancel.removeActionListener(achandler);
		
		screen.removeAll(); // remove components to screen panel
		
		screen.setLayout(new GridLayout(4,2)); // set screen panel layout
		
		
		JLabel amount[] = new JLabel[5];  // create labels 
		amount[0] = new JLabel("HK$100",2);
		amount[1] = new JLabel("HK$200",4);
		amount[2] = new JLabel("HK$400",2);
		amount[3] = new JLabel("HK$500",4);
		amount[4] = new JLabel("HK$1000",2);
		
		
		
		for(int x = 0;x<5;x++) { // add labels to screen panel
			screen.add(amount[x]);
		}
		screen.add(new JLabel("Back to Main Menu",4));
		
		
		JLabel Choosewithdrawamount = new JLabel("Choose a withdrawal amount: ",4);// add label to screen panel			
		chooseamount= new JTextField(); // create text field
		
		JPanel textfieldpanel = new JPanel(new GridLayout(5,1)); // set the textfieldpanel layout

		textfieldpanel.add(new JLabel());// add label to textfieldpanel
		textfieldpanel.add(new JLabel());// add label to textfieldpanel
		textfieldpanel.add(chooseamount);// add text field to textfieldpanel
		textfieldpanel.add(new JLabel());// add label to textfieldpanel
		textfieldpanel.add(new JLabel());// add label to textfieldpanel
		
		screen.add(Choosewithdrawamount); // add label to screen panel
		screen.add(textfieldpanel); // add textfieldpanel to screen panel
		
		// add buttons's action listener
		for(int x=0;x<10;x++) { 
			key[x].addActionListener(thewithdrawal_handler);
			
			if(x<3) {
				lbutton[x].addActionListener(thewithdrawal_handler);
				
			}
			if(x<3) {
				rbutton[x].addActionListener(thewithdrawal_handler);
			}
		}
		doublezero.addActionListener(thewithdrawal_handler);
		enter.addActionListener(thewithdrawal_handler);
		clear.addActionListener(thewithdrawal_handler);
		cancel.addActionListener(thewithdrawal_handler);
		
		screen.revalidate();// repaint the screen panel
		screen.repaint();// recalculate the layout
	}
	
	// show the prompt for transfer account interface and attempts to input a account which transfer to 
	private void promptfortransferaccount() {
		transferaccountnum = ""; // reset transferaccountnum
		
		cancel.removeActionListener(achandler); // remove buttons's action listener

		 for(int x=0;x<10;x++) {
	        	key[x].removeActionListener(thepromptfortransferaccount_handler);
	        }
	        clear.removeActionListener(thepromptfortransferaccount_handler);
	        enter.removeActionListener(thepromptfortransferaccount_handler);
	        cancel.removeActionListener(thepromptfortransferaccount_handler);
	        rbutton[3].removeActionListener(thepromptfortransferaccount_handler);
	        
		screen.removeAll(); // remove all components in screen panel 
		
		screen.setLayout(new GridLayout(6,1)); // set screen panel layout
        
        
        JLabel trnasferaccountlabel = new JLabel("Please enter AccountNumber :(For example:\"12345-0\")",0);// create label
        transferaccounttextfield = new JTextField(30); // create text field 
        JPanel transferaccounttextfieldpanel = new JPanel(); // create panel
        JLabel transfermenu = new JLabel("Transfer funds Menu:",0); // create label
        
        transferaccounttextfieldpanel.add(new JLabel("")); // add labels to transferaccounttextfieldpanel
        transferaccounttextfieldpanel.add(transferaccounttextfield);
        transferaccounttextfieldpanel.add(new JLabel(""));
        
        screen.add(new JLabel("")); // add label to screen panel     
        screen.add(transfermenu); // add label to screen panel
        screen.add(trnasferaccountlabel);  // add label to screen panel   
        screen.add(transferaccounttextfieldpanel);// add transferaccounttextfieldpanel to screen panel
        screen.add(new JLabel("")); // add label to screen panel 
        screen.add(new JLabel("Back to Main Menu",4));// add label to screen panel 
        
        
        
        for(int x=0;x<10;x++) {// add buttons's action listener
        	key[x].addActionListener(thepromptfortransferaccount_handler);
        }
        clear.addActionListener(thepromptfortransferaccount_handler);
        enter.addActionListener(thepromptfortransferaccount_handler);
        cancel.addActionListener(thepromptfortransferaccount_handler);
        rbutton[3].addActionListener(thepromptfortransferaccount_handler);
        
		screen.revalidate();// repaint the screen panel
		screen.repaint();// recalculate the layout
	}
	
	// show the prompt for transfer fund interface and attempts to transfer fund
	private void promptfortransferfund() {
		
		for(int x= 0;x<10;x++) { // remove buttons's action listener
			key[x].removeActionListener(thepromptfortransferfund_handler);
		}
		dot.removeActionListener(thepromptfortransferfund_handler);
		doublezero.removeActionListener(thepromptfortransferfund_handler);
		clear.removeActionListener(thepromptfortransferfund_handler);
		enter.removeActionListener(thepromptfortransferfund_handler);
		cancel.removeActionListener(thepromptfortransferfund_handler);
		
		screen.removeAll();// remove all components in screen panel
		
		numofdot =0; // reset numofdot
		transferamount = ""; // reset transferamount
		
		JLabel transfermenu = new JLabel("Transfer funds Menu:",0);// create labels
		JLabel currrentaccount = new JLabel("From Current Account: " + currentIntegratedAccountNumber + " - " + selectaccountnumber,0);
		JLabel transferaccount = new JLabel("Transfer to  Account: " + transferto_accountNumber + " - " + transferto_selectnumber,0);
		JLabel transferfund = new JLabel("Transfer Fund: ");
		transferamounttextfield = new JTextField(20);// create text field
		JPanel transferfundpanel = new JPanel(); // create panel 
		
		screen.setLayout(new GridLayout(5,1)); // set screen panel layout
		
		transferfundpanel.add(transferfund); // add label to transferfundpanel
		transferfundpanel.add(transferamounttextfield);// ad text field to transferfundpanel
		
		screen.add(new JLabel("")); // add labels to screen panel 
		screen.add(transfermenu); 
		screen.add(currrentaccount); 
		screen.add(transferaccount); 
		screen.add(transferfundpanel); // add transferfundpanel to screen panel 
		
		
		
		for(int x= 0;x<10;x++) {// add buttons's action listener
			key[x].addActionListener(thepromptfortransferfund_handler);
		}
		dot.addActionListener(thepromptfortransferfund_handler);
		doublezero.addActionListener(thepromptfortransferfund_handler);
		clear.addActionListener(thepromptfortransferfund_handler);
		enter.addActionListener(thepromptfortransferfund_handler);
		cancel.addActionListener(thepromptfortransferfund_handler);
		
		screen.revalidate();// repaint the screen panel
		screen.repaint();// recalculate the layout
	}

	// handle invalid account number or pin situation
	private void invalidauthenticateUser() {
  	screen.removeAll(); // remove all components in screen panel
  	
	screen.setLayout(new BorderLayout()); // set the screen panel layout
	
	JLabel invalidauthentication = new JLabel("Invalid account number or PIN. Please try again.",0); //create label
	invalidauthentication.setFont(new Font("monospaced", Font.BOLD|Font.ITALIC , 18)); // set the label font size and type
	
	screen.add(invalidauthentication,BorderLayout.CENTER); // add label to screen panel 
	
	screen.repaint(); // repaint the screen panel
	screen.revalidate(); // recalculate the screen panel
	}

	// inner class for button event handling
	private class authenticateUser_handler implements ActionListener{
		// handle button event 
		public void actionPerformed(ActionEvent event) {
			
				for(int x = 0;x<10;x++) {
					// set account number
				if(event.getActionCommand() == key[x].getText()&&accountnum.length()<5) {
					accountnum= accountnum.concat(key[x].getText());
					accountnumtextfield.setText(accountnum);
				}
					// set pin 
				else if(event.getActionCommand() == key[x].getText()&&pin.length()<5) {
					pin = pin.concat(key[x].getText());
					pinpwfield.setText(pin);
				}
				}
				// set account number
				if(event.getActionCommand() == doublezero.getText()&&accountnum.length()<5) {
					accountnum= accountnum.concat(doublezero.getText());
					accountnumtextfield.setText(accountnum);
				}
				// set pin 
				else if(event.getActionCommand() == doublezero.getText()&&pin.length()<5) {
					pin = pin.concat(doublezero.getText());
					pinpwfield.setText(pin);
				}
				
				// reset account number and pin
                if(event.getActionCommand()==clear.getText()) {	
                	accountnum="";
                	pin="";
                	accountnumtextfield.setText(accountnum);
                	pinpwfield.setText(pin);
                }
             
                if(event.getActionCommand()==cancel.getText()) {
                	// remove buttons's action listener
                	for(int x =0;x<10;x++) {
        				key[x].removeActionListener(this);
        				if(x<4) {
        					lbutton[x].removeActionListener(this);
        					rbutton[x].removeActionListener(this);
        				}
        			}
        			cancel.removeActionListener(this);
        			clear.removeActionListener(this);
        			enter.removeActionListener(this);
        			doublezero.removeActionListener(this);
        			
        			carddispened = new Thread(thecarddispenedthread); // create the Thread 
        	    	carddispened.start(); // start the Thread 
        	    	Thread.yield(); // yield the Thread
        			
                }
                if(event.getActionCommand() == enter.getText()) {
                	
                	if(accountnum.length()!=0&&pin.length()!=0)// authenticate user input
                	userAuthenticated = bankDatabase.authenticateUser(Integer.parseInt(accountnum), Integer.parseInt(pin));
                	
            	    if(userAuthenticated&&accountnum.length()!=0&&pin.length()!=0) {
            	    	// remove buttons's action listener
            			for(int x =0;x<10;x++) {
            				key[x].removeActionListener(this);
            				if(x<4) {
            					lbutton[x].removeActionListener(this);
            					rbutton[x].removeActionListener(this);
            				}
            			}
            			clear.removeActionListener(this);
            			enter.removeActionListener(this);
            			doublezero.removeActionListener(this);
            			
            	    	currentIntegratedAccountNumber = Integer.parseInt(accountnum);// set the currentIntegratedAccountNumber
            	    	userAuthenticated = true;
            	    	selectAccount(currentIntegratedAccountNumber); // select account 
            	    }
            	    else {
            	    	authenticateUser = new Thread(theauthenticateUserthread); // create thread
            	    	authenticateUser.start(); // start thread
            			Thread.yield(); // yield thread
            	    	invalidauthenticateUser(); 
            	    }
                }

			}
	}
	// inner class for button event handling
    private class selectAccount_handler implements ActionListener{
    	// handle button event
    	public void actionPerformed(ActionEvent event) {
    			
    	for(int x = 0;x<4;x++) {
    		if(bankDatabase.getnumberofaccount(currentIntegratedAccountNumber)<5) {
    		if(x<bankDatabase.getnumberofaccount(currentIntegratedAccountNumber)&&event.getActionCommand() == lbutton[x].getActionCommand()) {
    				// remove buttonss's action listener 
    				 cancel.removeActionListener(this);
        			 enter.removeActionListener(this);
        			for(int i =0;i<4;i++) {
        				lbutton[i].removeActionListener(this);
        				rbutton[i].removeActionListener(this);
        			}
        			
    			    selectaccountnumber = x; // set selectaccountnumber 
    				performTransactions();
    			}
    			}
    		else if(x*2<bankDatabase.getnumberofaccount(currentIntegratedAccountNumber)&&event.getActionCommand() ==lbutton[x].getActionCommand()) {    			    
    				 {
    					 // remove buttonss's action listener 
    					 cancel.removeActionListener(this);
            			 enter.removeActionListener(this);
            			for(int i =0;i<4;i++) {
            				lbutton[i].removeActionListener(this);
            				rbutton[i].removeActionListener(this);
            			}
    					selectaccountnumber = x*2;// set selectaccountnumber 
    					performTransactions();
    				}
    			}
    		else if(x*2+1<bankDatabase.getnumberofaccount(currentIntegratedAccountNumber)&&event.getActionCommand() ==rbutton[x].getActionCommand()) {
    				 // remove buttonss's action listener
    				 cancel.removeActionListener(this);
        			 enter.removeActionListener(this);
        			for(int i =0;i<4;i++) {
        				lbutton[i].removeActionListener(this);
        				rbutton[i].removeActionListener(this);
        			}
    					selectaccountnumber = x*2+1;// set selectaccountnumber 
    					performTransactions();
    				
    			}
    	
    		}
    		
    	}
    }
 // inner class for button event handling
    private class performTransactions_handler implements ActionListener{
    	// handle button event
    	public void actionPerformed(ActionEvent event) {
    		if(event.getActionCommand() == rbutton[3].getActionCommand()||event.getActionCommand() == lbutton[2].getActionCommand()
    				||event.getActionCommand() == rbutton[2].getActionCommand()||event.getActionCommand() == lbutton[3].getActionCommand()) {
    			// remove buttons's action listener
    			for(int x = 2;x<4;x++) {
    				lbutton[x].removeActionListener(this);
    				rbutton[x].removeActionListener(this);
    			}
    			cancel.removeActionListener(this);
    			enter.removeActionListener(this);
    		}
    		
    		if(event.getActionCommand() == rbutton[3].getActionCommand()) {

    			selectAccount(currentIntegratedAccountNumber);
    		}
    		
    		if(event.getActionCommand() == lbutton[2].getActionCommand()) {
 			
    			BalanceInquiry = new Thread(theBalanceInquirythread);// create thread
    			BalanceInquiry.start(); // start the thread
    			Thread.yield();  	// yield the thread		
    			BalanceInquiry();


    		}
    		if(event.getActionCommand() == rbutton[2].getActionCommand()) {

    			Withdrawal();
    		}
    		if(event.getActionCommand() == lbutton[3].getActionCommand()) {

    			promptfortransferaccount();

    		}
    	}
    }
 // inner class for button event handling
    private class Withdrawal_handler implements ActionListener{
    	// handle button event
    	public void actionPerformed(ActionEvent event) {
    		// get and set the available balance
    		double availableBalance = bankDatabase.getAvailableBalance(currentIntegratedAccountNumber,
					selectaccountnumber);
    		
    		if(event.getActionCommand()==lbutton[0].getActionCommand()||event.getActionCommand()==lbutton[1].getActionCommand()
    				||event.getActionCommand()==lbutton[2].getActionCommand()
    				||event.getActionCommand()==rbutton[0].getActionCommand()||event.getActionCommand()==rbutton[1].getActionCommand()
    				||event.getActionCommand()==enter.getActionCommand()
    				) {
    			
    	  		if(event.getActionCommand()==lbutton[0].getActionCommand()) {
        			
        			withdrawamount = 100;// set the withdraw amount

        		}
        		if(event.getActionCommand()==lbutton[1].getActionCommand()) {			
        			withdrawamount = 400;// set the withdraw amount
        		
        		}
        		if(event.getActionCommand()==lbutton[2].getActionCommand()) {
     
        			withdrawamount = 1000;// set the withdraw amount
        	
        		}
        		if(event.getActionCommand()==rbutton[0].getActionCommand()) { 			
        			withdrawamount = 200;// set the withdraw amount
        			
        		}
        		if(event.getActionCommand()==rbutton[1].getActionCommand()) {
        			withdrawamount = 500;// set the withdraw amount
        
        		}
        		if(event.getActionCommand() == enter.getText()&&withdrawamountbykeyinput.length()>0) {       			
        			withdrawamount = Integer.parseInt(withdrawamountbykeyinput);// set the withdraw amount
        		}
    			if((withdrawamount <= availableBalance||bankDatabase.validateAccountType(currentIntegratedAccountNumber, selectaccountnumber) == 0
						&& (bankDatabase.getOverdrawn_limit(currentIntegratedAccountNumber,
								selectaccountnumber) + availableBalance) >= withdrawamount)&&withdrawamount%100 ==0&&withdrawamount>99) {
    		if (cashDispenser.isSufficientCashAvailable(withdrawamount)) {
    			//remove buttons's action listener
    					for(int x=0;x<10;x++) {
    	    				key[x].removeActionListener(this);
    	    				if(x<3) {
    	    					lbutton[x].removeActionListener(this);
    	    					
    	    				}
    	    				if(x<3) {
    	    					rbutton[x].removeActionListener(this);
    	    				}
    	    			}
    	        	
    	    			enter.removeActionListener(this);
    	    			clear.removeActionListener(this);
    	    			cancel.removeActionListener(this);
    	    			
    	    			withdrawal_confirmation();
    			}
    			else {
    					Insufficient_cash();
    				}
    	   }else if(withdrawamount%100 !=0||withdrawamount <100) {
    				invalidwithdrawamount();
    			}
		    else {
    				Insufficient_funds();
    			}

    		}
    		if(event.getActionCommand()==rbutton[2].getActionCommand()||event.getActionCommand()==cancel.getActionCommand()) {
    			//remove buttons's action listener
    			for(int x=0;x<10;x++) {
    				key[x].removeActionListener(this);
    				if(x<3) {
    					lbutton[x].removeActionListener(this);
    					
    				}
    				if(x<3) {
    					rbutton[x].removeActionListener(this);
    				}
    			}
        	
    			enter.removeActionListener(this);
    			clear.removeActionListener(this);
    			cancel.removeActionListener(this);
    			
    		}
    		
    		if(event.getActionCommand()==rbutton[2].getActionCommand()) {

    			performTransactions();
			} 
    		
    		if(event.getActionCommand()==cancel.getActionCommand()) {

				cancelwithdraw();
			} 	
        	
    	
        		
        		
        		for(int x = 0;x<10;x++) {
    				if(event.getActionCommand() == key[x].getText()) {
    					// set the withdrawamountbykeyinput and chooseamount
    					withdrawamountbykeyinput= withdrawamountbykeyinput.concat(key[x].getText());
    					chooseamount.setText(withdrawamountbykeyinput);
    				}
    			}
        		if(event.getActionCommand() == doublezero.getText()) {
        			// set the withdrawamountbykeyinput and chooseamount
    				withdrawamountbykeyinput= withdrawamountbykeyinput.concat(doublezero.getText());
    				chooseamount.setText(withdrawamountbykeyinput);
    			}
        		
        		
        		if(event.getActionCommand() == clear.getText()) {
        			// reset the withdrawamountbykeyinput and chooseamount
        			withdrawamountbykeyinput="";
        			chooseamount.setText(withdrawamountbykeyinput);
        		}
        		
    	
    	}

    	}
 // inner class for button event handling
    private class BalanceInquiry_handler implements ActionListener{
    	@SuppressWarnings("deprecation")
    	// handle button event
		public void actionPerformed(ActionEvent event) {
    		if(event.getActionCommand()==rbutton[3].getActionCommand()) {
    			//remove button's action listener
    			rbutton[3].removeActionListener(this);
    			BalanceInquiry.stop(); // stop the thread
    			performTransactions(); 
    		}
    	}
    }
 // inner class for button event handling
    private class promptfortransferaccount_handler implements ActionListener{
    	// handle button event
    	public void actionPerformed(ActionEvent event) {
    		if(event.getActionCommand()==cancel.getActionCommand()
    				||event.getActionCommand()==enter.getText()||event.getActionCommand()==rbutton[3].getActionCommand()) {
    			for(int x=0;x<10;x++) {
     	        	key[x].removeActionListener(this);
     	        }
     	        clear.removeActionListener(this);
     	       cancel.removeActionListener(this);
     	        enter.removeActionListener(this);
     	        rbutton[3].removeActionListener(this);
    		}
    		if(event.getActionCommand()==rbutton[3].getActionCommand()) {
    			performTransactions();
    		}
    		
    		if(event.getActionCommand()==cancel.getActionCommand()) {
    			
				canceltransfer();
			}
    		
    		if(transferaccountnum.length()==5) {
				transferaccountnum= transferaccountnum.concat("-");
    		transferaccounttextfield.setText(transferaccountnum);
    		}    	
    		for(int x = 0;x<10;x++) {
				if(event.getActionCommand() == key[x].getText()&&transferaccountnum.length()<7) {					
					
					transferaccountnum= transferaccountnum.concat(key[x].getText());
					transferaccounttextfield.setText(transferaccountnum);
				}
    		}	
    		    
    		
                if(event.getActionCommand()==clear.getText()) {	
                	transferaccountnum = "";
                	transferaccounttextfield.setText(transferaccountnum);
                }
                if(event.getActionCommand()==enter.getText()) {
                	if(transferaccountnum.length()>=5)
                	transferto_accountNumber = Integer.parseInt(transferaccountnum.substring(0, 5));
                	if(transferaccountnum.length()>6)
                	transferto_selectnumber = Integer.parseInt( transferaccountnum.substring(6,7));
                
         	        
                	if(bankDatabase.checkaccountnumber(transferto_accountNumber) == false||
                			bankDatabase.checkselectnumber(transferto_accountNumber, transferto_selectnumber) == false) {
                	
                		invalidtransferaccount();
                	}
                	else if(transferto_accountNumber == currentIntegratedAccountNumber&&transferto_selectnumber==selectaccountnumber) {
                	
                		invalidtransferaccount();
                	}
                	else {               		 
                		promptfortransferfund();
                	}
                }
    	}
    }
 // inner class for button event handling
    private class promptfortransferfund_handler implements ActionListener{
    	// handle button event
    	public void actionPerformed(ActionEvent event) {
    		if(event.getActionCommand()==cancel.getActionCommand()) {
    			//remove buttons's action listener
    			for(int x= 0;x<10;x++) {
        			key[x].removeActionListener(this);
        		}
        		dot.removeActionListener(this);
        		doublezero.removeActionListener(this);
        		clear.removeActionListener(this);
        		enter.removeActionListener(this);
        		cancel.removeActionListener(this);
				canceltransfer();
			}
    		
    		for(int x = 0;x<10;x++) {
				if(event.getActionCommand() == key[x].getText()) {					
					// set transfer amount
					transferamount = transferamount.concat(key[x].getText());
					transferamounttextfield.setText(transferamount);
				}
    		}		
    			if(event.getActionCommand() == dot.getText()&&numofdot==0&&transferamount.length()>0) {					
				numofdot++;// make sure number of dot can not more than 1
				transferamount= transferamount.concat(dot.getText());// set transfer amount	
				transferamounttextfield.setText(transferamount);
    			}
    			if(event.getActionCommand() == doublezero.getText()) {	
    				// set transfer amount
					transferamount= transferamount.concat(doublezero.getText());
					transferamounttextfield.setText(transferamount);
				}
                if(event.getActionCommand()==clear.getText()) {
                	// reset number of dot and transfer amount
                	numofdot =0;
                	transferamount = "";
                	transferamounttextfield.setText(transferamount);
                }
                
                
                if(event.getActionCommand()==enter.getText()) {
                	// parse string to double and store in transferto_amount
                	transferto_amount = Double.parseDouble(transferamount);
                	int dotindex = transferamount.indexOf(".");// get the index of dot 
                	decimalplaces = transferamount.substring(dotindex+1); // extracts the characters from a string start from dotindex+1
                	
                	// remove buttons's action listener
                	for(int x= 0;x<10;x++) {
            			key[x].removeActionListener(this);
            		}
            		dot.removeActionListener(this);
            		doublezero.removeActionListener(this);
            		clear.removeActionListener(this);
            		enter.removeActionListener(this);
            		cancel.removeActionListener(this);
            		
                	if(decimalplaces.length()>2&&numofdot>0) {              		
                		invalidtransferamount();
                	}
                	else if(bankDatabase.getAvailableBalance(currentIntegratedAccountNumber, selectaccountnumber) >= transferto_amount
    						|| bankDatabase.validateAccountType(currentIntegratedAccountNumber,
    								selectaccountnumber) == 0
    								&& (bankDatabase.getOverdrawn_limit(currentIntegratedAccountNumber,
    										selectaccountnumber)
    										+ bankDatabase.getAvailableBalance(currentIntegratedAccountNumber,
    												selectaccountnumber)) >= transferto_amount) 
                	{

                		transfer_confirmation();

                	}
                	else{
                		invalidtransferamount();
                		
                	}
                }
    		}
    	}
 // inner class for button event handling
    private class withdrawconfirmation_handler implements ActionListener{
    	// handle button event
    	public void actionPerformed(ActionEvent event) {
    		
    		if(event.getActionCommand()==cancel.getActionCommand()) {
    			// remove buttons's action listen
				cancel.removeActionListener(this);
				enter.removeActionListener(this);
				cancelwithdraw();
			}
		
    		
    		if(event.getActionCommand() == enter.getActionCommand()) {
    			// remove buttons's action listen
    			cancel.removeActionListener(this);
    			enter.removeActionListener(this);
    			bankDatabase.debit(currentIntegratedAccountNumber, selectaccountnumber, withdrawamount);// debit amount from user present account    				
				cashDispenser.dispenseCash(withdrawamount);
				remindusertakecard();
    		}
    	}
    }
 // inner class for button event handling
    private class transferconfirmation_handler implements ActionListener{
    	// handle button event
    	public void actionPerformed(ActionEvent event) {
    		if(event.getActionCommand()==enter.getActionCommand()) {
    			// remove buttons's action listener
    			cancel.removeActionListener(this);
    			enter.removeActionListener(this);
        		bankDatabase.debit(currentIntegratedAccountNumber, selectaccountnumber, transferto_amount);// decrease amount from user present account 
				bankDatabase.credit(transferto_accountNumber, transferto_selectnumber, transferto_amount); // increase amount to transfer account
				transfersucceed();
    		}
    		if(event.getActionCommand()==cancel.getActionCommand()) {
    			// remove buttons's action listener
    			cancel.removeActionListener(this);
    			enter.removeActionListener(this);
    			canceltransfer();
    		}
    	}
    }
} // end class ATM

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