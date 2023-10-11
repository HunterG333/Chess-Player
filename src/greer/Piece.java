package greer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Piece {
	
	private JFrame frame;
	private JLabel thisPiece;
	private String coordinate;
	private String imageSrc;
	private String pieceType;
	private String pieceColor;
	private boolean canEnPassant;
	private boolean canShortCastle = false;
	private boolean canLongCastle = false;
	ChessBoard boardObject;
	
	
	
	/**
	 *Instantiates a new piece object, responsible for creating all of the pieces
	 * 
	 * @param frame The main frame being used that the board is on
	 */
	public Piece(ChessBoard boardObject, JFrame frame, String coordinate, String imageSrc, String pieceType) {
		this.boardObject = boardObject;
		this.frame = frame;
		this.coordinate = coordinate;
		this.imageSrc = imageSrc;
		this.pieceType = pieceType;
		canEnPassant = false;
		
		if(pieceType.equals("WK") || pieceType.equals("BK")) {
			canShortCastle = true;
			canLongCastle = true;
		}
		
		pieceColor = pieceType.substring(0, 1);
		
		boardObject.newPiece(this);
		
		
	}
	
	public void setClickListener(GameLogic logicObject) {
		
		thisPiece.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e) {
				logicObject.clearDots(frame);
				logicObject.instantiateDots(Piece.this);
			}
			
		});
		
	}
	
	public boolean getShortCastleStatus() {
		return canShortCastle;
	}
	
	public void setShortCastleStatus(boolean b) {
		canShortCastle = b;
	}
	public boolean getLongCastleStatus() {
		return canLongCastle;
	}
	
	public void setLongCastleStatus(boolean b) {
		canLongCastle = b;
	}
	
	public boolean getEnPassantStatus() {
		return canEnPassant;
	}
	public void setEnPassantStatus(boolean b) {
		canEnPassant = b;
	}
	
	public JFrame getFrame() {
		return frame;
	}

	public String getCoordinate() {
		return coordinate;
	}
	
	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	public String getImagePath() {
		return imageSrc;
	}
	
	public void setPieceType(String pieceType) {
		this.pieceType = pieceType;
		pieceColor = pieceType.substring(0, 1);
	}
	
	public String getPieceType() {
		return pieceType;
	}
	
	public void setPieceJLabel(JLabel label) {
		this.thisPiece = label;
	}
	
	public JLabel getPieceJLabel() {
		return thisPiece;
	}
	
	public String getColor() {
		return pieceColor;
	}
	
	public String toString() {
		return pieceType + " is on " + coordinate;
	}
	
	public void setImgSrc(String imgSrc) {
		imageSrc = imgSrc;
		
	}
	
	
}
