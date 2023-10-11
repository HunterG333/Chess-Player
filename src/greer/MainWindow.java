package greer;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.SwingConstants;

public class MainWindow {

	
	private JFrame frame;
	Piece pieceObject;;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();	
		
		//gets screen width
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		
		frame.setPreferredSize(new Dimension(size.height, size.width));
		
		initializeBoard();
		
		frame.pack();
	    frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
			    int keyCode = e.getKeyCode();
			    if (keyCode == KeyEvent.VK_ESCAPE) {
			    	frame.setVisible(false); //you can't see me!
			    	frame.dispose();
			    }
			    
			  }
		});
	}
	

	/**
	 * Initializes the board
	 */
	private void initializeBoard() {
		
		//creates Chess Board Icon label
		JLabel chessBoardLabel = 	new JLabel("");
		chessBoardLabel.setBounds(60, 100, 700, 700);
		
		ImageIcon chessBoard = new ImageIcon(this.getClass().getResource("/ChessBoard.jpg"));
		Image image = chessBoard.getImage(); // transform it 
		Image newimg = image.getScaledInstance(700, 700, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		chessBoard = new ImageIcon(newimg);  // transform it back
		
		frame.getContentPane().setLayout(null);
		chessBoardLabel.setIcon(chessBoard);
		frame.getContentPane().add(chessBoardLabel);
		
		Game newGame = new Game(frame);
		
	}
	
	
	
}
