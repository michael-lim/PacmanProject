import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame implements ActionListener{

	private JPanel pane; //main board
	private Board gameBoard; //game board
	private Login loginForm;
	private LoginFile loginData;

	public MainFrame(){
		//Set up the main frame
		super("Pacman");
		setVisible(true);
		setResizable(false);
		setLocation(300,75);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Container con = this.getContentPane();
		con.setPreferredSize(new Dimension(401,641));

		pane = new JPanel();
		pane.setLayout(new CardLayout());
		
		gameBoard = new Board();
		loginForm = new Login();
		loginData = loginForm.data;

		loginForm.giveActionListener(this);
		gameBoard.giveActionListener(this);
		pane.add(loginForm, "login");
		pane.add(gameBoard, "gameBoard");
		pane.requestFocus();
		con.add(pane);

		pack();
	}

	public String makePassword(char[] password){
		String result = "";
		for (char c:password)
			result+=c;
		return result;
	}

	public boolean badInput(String input){
		if (input.equals(null) || input.equals("") || input.contains("-") || input.contains("\\"))
			return true;
		return false;
	}

	public void loginGuest(CardLayout card){
		gameBoard.setUpBoard(1,3);
		gameBoard.guest = true;
		gameBoard.levelNum = 1;
		card.show(pane, "gameBoard");
		repaint();
	}

	public void loginUser(CardLayout card, String user){
		gameBoard.setUpBoard(loginData.getLevel(), 3);
		gameBoard.guest = false;
		gameBoard.username = user;
		gameBoard.highscore = loginData.getHighscore();
		gameBoard.levelNum = loginData.getLevel();
		card.show(pane, "gameBoard");
		repaint();
	}

	public void actionPerformed(ActionEvent e){
		JButton source = (JButton) e.getSource();
		CardLayout card = (CardLayout) pane.getLayout();
		if (source.equals(loginForm.guest))
			loginGuest(card);

		if (source.equals(gameBoard.mainMenu)){
			if (!gameBoard.guest){
				loginData.setRecord(gameBoard.username, gameBoard.highscore, gameBoard.levelNum);
			}
			gameBoard.setUpBoard(1,3);
			card.show(pane, "login");
		}

		if (source.equals(loginForm.enter)){
			String enteredUsername = loginForm.usernameInput.getText();
			String enteredPassword = makePassword(loginForm.passwordInput.getPassword());

			if (!loginData.usernameExists(enteredUsername) || badInput(enteredUsername))
				loginForm.loginErrorMessage();
			else if (!loginData.checkLogin(enteredUsername, enteredPassword))
				loginForm.loginErrorMessage();
			else
				loginUser(card, enteredUsername);
		}

		if (source.equals(loginForm.register)){
			String enteredUsername = loginForm.usernameInput.getText();
			String enteredPassword = makePassword(loginForm.passwordInput.getPassword());

			if (loginData.usernameExists(enteredUsername) || badInput(enteredUsername))
				loginForm.registerErrorMessage();
			else{
				loginData.createNew(enteredUsername, enteredPassword, 1, 0);
				loginForm.registerSuccessMessage();
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				MainFrame f = new MainFrame();
			}
		});
	}
}
