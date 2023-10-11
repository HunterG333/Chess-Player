package greer;

import java.awt.Image;
import javax.swing.BoxLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameLogic {
	
	
	//TODO:
	//BUG possible to castle if piece is taken and out of check? 
	//cannot castle if moves through check
	//cannot castle if you are in check
	
	JFrame popupFrame;

	boolean whiteTurn = true;
	boolean didEnPassant = false;
	boolean possibleCastle = false;
	boolean isWhiteInCheck = false;
	boolean isBlackInCheck = false;
	String whiteFileChecking = null, blackFileChecking = null;
	
	

	// initializes 32 spaces for dots-- can refine later
	private Dot[] dotArr = new Dot[32];
	int arrayCount;

	int lastClickedCoords[];

	CoordinatesConversion conversionObject = new CoordinatesConversion();
	JFrame frame;
	ChessBoard boardObject;
	Piece piecePointer;
	Piece lastMovedPiece = null;
	Piece whiteKing;
	Piece blackKing;
	Piece lastClickedPiece;
	
	/**
	 * Stores a list of data for moves that are possible to get out of check.
	 * If empty, it is checkmate
	 * 
	 * Piecetype = pt, direction = dd, #ofsquares = n
	 * Data is stored in a string in this format:
	 * pt.dd.n
	 */
	ArrayList<String> possibleCheckMoves = new ArrayList<String>();

	public GameLogic(ChessBoard boardObject) {
		this.boardObject = boardObject;
		
	}
	
	public void setKings(Piece wk, Piece bk) {
		whiteKing = wk;
		blackKing = bk;
	}
	
	public void updatePossibleCheckMoves(Piece king) {
		possibleCheckMoves.clear();
		int [] KC = conversionObject.convertNumericalCoordinates(king.getCoordinate());
		String pieceType = king.getPieceType();
		
		if(pieceType.equals("WK")) {
			String color = "W";
			//check immediate king movement
			try {
				int [] checkingInt = {KC[0] + 1, KC[1]};
				if(!boardObject.getCoverage(checkingInt).equals("B") && !boardObject.getCoverage(checkingInt).equals("M") && (boardObject.checkSquare(checkingInt) || !boardObject.getPiece(checkingInt).getColor().equals(king.getColor()))) {
					possibleCheckMoves.add("WK.PX.1");
				}
			} catch(Exception e) {
				
			}
			try {
				int [] checkingInt = {KC[0] + 1, KC[1] + 1};
				if(!boardObject.getCoverage(checkingInt).equals("B") && !boardObject.getCoverage(checkingInt).equals("M") && (boardObject.checkSquare(checkingInt) || !boardObject.getPiece(checkingInt).getColor().equals(king.getColor()))) {
					possibleCheckMoves.add("WK.NE.1");
				}
			} catch(Exception e) {
				
			}
			try {
				int [] checkingInt = {KC[0], KC[1] + 1};
				if(!boardObject.getCoverage(checkingInt).equals("B") && !boardObject.getCoverage(checkingInt).equals("M") && (boardObject.checkSquare(checkingInt) || !boardObject.getPiece(checkingInt).getColor().equals(king.getColor()))) {
					possibleCheckMoves.add("WK.PY.1");
				}
			} catch(Exception e) {
				
			}
			try {
				int [] checkingInt = {KC[0] - 1, KC[1] + 1};
				if(!boardObject.getCoverage(checkingInt).equals("B") && !boardObject.getCoverage(checkingInt).equals("M") && (boardObject.checkSquare(checkingInt) || !boardObject.getPiece(checkingInt).getColor().equals(king.getColor()))) {
					possibleCheckMoves.add("WK.NW.1");
				}
			} catch(Exception e) {
				
			}
			try {
				int [] checkingInt = {KC[0] - 1, KC[1]};
				if(!boardObject.getCoverage(checkingInt).equals("B") && !boardObject.getCoverage(checkingInt).equals("M") && (boardObject.checkSquare(checkingInt) || !boardObject.getPiece(checkingInt).getColor().equals(king.getColor()))) {
					possibleCheckMoves.add("WK.NX.1");
				}
			} catch(Exception e) {
				
			}
			try {
				int [] checkingInt = {KC[0] - 1, KC[1] - 1};
				if(!boardObject.getCoverage(checkingInt).equals("B") && !boardObject.getCoverage(checkingInt).equals("M") && (boardObject.checkSquare(checkingInt) || !boardObject.getPiece(checkingInt).getColor().equals(king.getColor()))) {
					possibleCheckMoves.add("WK.SW.1");
				}
			} catch(Exception e) {
				
			}
			try {
				int [] checkingInt = {KC[0], KC[1] - 1};
				if(!boardObject.getCoverage(checkingInt).equals("B") && !boardObject.getCoverage(checkingInt).equals("M") && (boardObject.checkSquare(checkingInt) || !boardObject.getPiece(checkingInt).getColor().equals(king.getColor()))) {
					possibleCheckMoves.add("WK.NY.1");
				}
			} catch(Exception e) {
				
			}
			try {
				int [] checkingInt = {KC[0] + 1, KC[1] - 1};
				if(!boardObject.getCoverage(checkingInt).equals("B") && !boardObject.getCoverage(checkingInt).equals("M") && (boardObject.checkSquare(checkingInt) || !boardObject.getPiece(checkingInt).getColor().equals(king.getColor()))) {
					possibleCheckMoves.add("WK.SE.1");
				}
			} catch(Exception e) {
				
			}
			
			//next check for pieces that can block check
			Piece [] checkingPieces = boardObject.getCheckingPieces();
			int [] CP1C = conversionObject.convertNumericalCoordinates(checkingPieces[0].getCoordinate());
			
			if(checkingPieces[1] == null) {
				
				String checkingFile = null;
				//find checking file
				if(CP1C[0] == KC[0]) {
					
					if(CP1C[1] > KC[1]) {
						checkingFile = "PY";
					} else {
						checkingFile = "NY";
					}
				} else if(CP1C[1] == KC[1]) {
					if(CP1C[0] > KC[0]) {
						checkingFile = "PX";
					} else{
						checkingFile = "NX";
					}
				} else if((CP1C[0] > KC[0] && CP1C[1] > KC[1]) || (CP1C[0] < KC[0] && CP1C[1] < KC[1])) {
					if(CP1C[0] > KC[0]) {
						checkingFile = "NE";
					} else {
						checkingFile = "SW";
					}
					
				} else {
					if(CP1C[0] < KC[0]) {
						checkingFile = "NW";
					} else {
						checkingFile = "SE";
					}
				}
				
				if(checkingFile.equals("PY")) {
					for(int i = KC[1] + 1; i < CP1C[1]; i++) {
						//check squares for occupancy of other pieces
						int [] squareToCheck = {KC[0], i};

						//trace out pieces that can move here
						ArrayList<Piece> pieceToMove = findCoverage(squareToCheck, color);
						for(int j = 0; j < pieceToMove.size(); j++) {
							String piece = pieceToMove.get(j).getPieceType();
							String squareToMove = conversionObject.convertAlgebraCoordinates("" + squareToCheck[0] + squareToCheck[1]);
							String total = piece + "." + squareToMove;
							possibleCheckMoves.add(total);
						}
					}
				}
				if(checkingFile.equals("NY")) {
					for(int i = KC[1] - 1; i > CP1C[1]; i--) {
						//check squares for occupancy of other pieces
						int [] squareToCheck = {KC[0], i};
						
						//trace out pieces that can move here
						ArrayList<Piece> pieceToMove = findCoverage(squareToCheck, color);
						for(int j = 0; j < pieceToMove.size(); j++) {
							String piece = pieceToMove.get(j).getPieceType();
							String squareToMove = conversionObject.convertAlgebraCoordinates("" + squareToCheck[0] + squareToCheck[1]);
							String total = piece + "." + squareToMove;
							possibleCheckMoves.add(total);
						}
					}
				}
				if(checkingFile.equals("PX")) {
					for(int i = KC[0] + 1; i < CP1C[0]; i++) {
						//check squares for occupancy of other pieces
						int [] squareToCheck = {i, KC[1]};
						
						//trace out pieces that can move here
						ArrayList<Piece> pieceToMove = findCoverage(squareToCheck, color);
						for(int j = 0; j < pieceToMove.size(); j++) {
							String piece = pieceToMove.get(j).getPieceType();
							String squareToMove = conversionObject.convertAlgebraCoordinates("" + squareToCheck[0] + squareToCheck[1]);
							String total = piece + "." + squareToMove;
							possibleCheckMoves.add(total);
						}
					}
				}
				if(checkingFile.equals("NX")) {
					for(int i = KC[0] - 1; i > CP1C[0]; i--) {
						//check squares for occupancy of other pieces
						int [] squareToCheck = {i, KC[1]};
						
						//trace out pieces that can move here
						ArrayList<Piece> pieceToMove = findCoverage(squareToCheck, color);
						for(int j = 0; j < pieceToMove.size(); j++) {
							String piece = pieceToMove.get(j).getPieceType();
							String squareToMove = conversionObject.convertAlgebraCoordinates("" + squareToCheck[0] + squareToCheck[1]);
							String total = piece + "." + squareToMove;
							possibleCheckMoves.add(total);
						}
					}
				}
				if(checkingFile.equals("NE")) {
					for(int i = KC[0] + 1, k = KC[1] + 1; i < CP1C[0]; i++, k++) {
						//check squares for occupancy of other pieces
						int [] squareToCheck = {i, k};

						//trace out pieces that can move here
						ArrayList<Piece> pieceToMove = findCoverage(squareToCheck, color);
						for(int j = 0; j < pieceToMove.size(); j++) {
							String piece = pieceToMove.get(j).getPieceType();
							String squareToMove = conversionObject.convertAlgebraCoordinates("" + squareToCheck[0] + squareToCheck[1]);
							String total = piece + "." + squareToMove;
							possibleCheckMoves.add(total);
						}
					}
				}
				if(checkingFile.equals("SE")) {
					for(int i = KC[0] + 1, k = KC[1] - 1; i < CP1C[0]; i++, k--) {
						//check squares for occupancy of other pieces
						int [] squareToCheck = {i, k};

						//trace out pieces that can move here
						ArrayList<Piece> pieceToMove = findCoverage(squareToCheck, color);
						for(int j = 0; j < pieceToMove.size(); j++) {
							String piece = pieceToMove.get(j).getPieceType();
							String squareToMove = conversionObject.convertAlgebraCoordinates("" + squareToCheck[0] + squareToCheck[1]);
							String total = piece + "." + squareToMove;
							possibleCheckMoves.add(total);
						}
					}
				}
				if(checkingFile.equals("NW")) {
					for(int i = KC[0] - 1, k = KC[1] + 1; i > CP1C[0]; i--, k++) {
						//check squares for occupancy of other pieces
						int [] squareToCheck = {i, k};

						//trace out pieces that can move here
						ArrayList<Piece> pieceToMove = findCoverage(squareToCheck, color);
						for(int j = 0; j < pieceToMove.size(); j++) {
							String piece = pieceToMove.get(j).getPieceType();
							String squareToMove = conversionObject.convertAlgebraCoordinates("" + squareToCheck[0] + squareToCheck[1]);
							String total = piece + "." + squareToMove;
							possibleCheckMoves.add(total);
						}
					}
				}
				if(checkingFile.equals("SW")) {
					for(int i = KC[0] - 1, k = KC[1] - 1; i > CP1C[0]; i--, k--) {
						//check squares for occupancy of other pieces
						int [] squareToCheck = {i, k};

						//trace out pieces that can move here
						ArrayList<Piece> pieceToMove = findCoverage(squareToCheck, color);
						for(int j = 0; j < pieceToMove.size(); j++) {
							String piece = pieceToMove.get(j).getPieceType();
							String squareToMove = conversionObject.convertAlgebraCoordinates("" + squareToCheck[0] + squareToCheck[1]);
							String total = piece + "." + squareToMove;
							possibleCheckMoves.add(total);
						}
					}
					
				}
				
				//next check for moves that attack the piece checking
				ArrayList<Piece> possibleAttackers = findCoverage(CP1C, color);
				for(int j = 0; j < possibleAttackers.size(); j++) {
					if(!possibleAttackers.get(j).getPieceType().substring(1).equals("P")) {
						String piece = possibleAttackers.get(j).getPieceType() + "X";
						String squareToMove = conversionObject.convertAlgebraCoordinates("" + CP1C[0] + CP1C[1]);
						String total = piece + "." + squareToMove;
						possibleCheckMoves.add(total);
					} 	
					
				}
				
				//special case for pawns. calculated manually here
				//check for black pawns
				try {
					//right up
					int [] int1 = {CP1C[0] + 1, CP1C[1] - 1};
					boolean pieceExists1 = !boardObject.checkSquare(int1);
					
					if(pieceExists1) {
						boolean isPawn1 = boardObject.getPiece(int1).getPieceType().substring(1).equals("P");
						boolean isSameColor1 = boardObject.getPiece(int1).getColor().equals(color);
						if(isPawn1 && isSameColor1) {
							String piece = boardObject.getPiece(int1).getPieceType() + "X";
							String squareToMove = conversionObject.convertAlgebraCoordinates("" + CP1C[0] + CP1C[1]);
							String total = piece + "." + squareToMove;
							possibleCheckMoves.add(total);
						}
					}
				} catch(Exception e) {
					
				}
				
				try {
					//left up
					int [] int2 = {CP1C[0] - 1, CP1C[1] - 1};
					boolean pieceExists2 = !boardObject.checkSquare(int2);
					
					if(pieceExists2) {
						boolean isPawn2 = boardObject.getPiece(int2).getPieceType().substring(1).equals("P");
						boolean isSameColor2 = boardObject.getPiece(int2).getColor().equals(color);
						if(isPawn2 && isSameColor2) {
							String piece = boardObject.getPiece(int2).getPieceType() + "X";
							String squareToMove = conversionObject.convertAlgebraCoordinates("" + CP1C[0] + CP1C[1]);
							String total = piece + "." + squareToMove;
							possibleCheckMoves.add(total);
						}
					}
				} catch(Exception e){
					
				}
				
				
				
				
			} //no else because the king movements were already mapped
			
		} else {
			//black
			String color = "B";
			try {
				int [] checkingInt = {KC[0] + 1, KC[1]};
				if(!boardObject.getCoverage(checkingInt).equals("W") && !boardObject.getCoverage(checkingInt).equals("M") && (boardObject.checkSquare(checkingInt) || !boardObject.getPiece(checkingInt).getColor().equals(king.getColor()))) {
					possibleCheckMoves.add("BK.PX.1");
				}
			} catch(Exception e) {
				
			}
			try {
				int [] checkingInt = {KC[0] + 1, KC[1] + 1};
				if(!boardObject.getCoverage(checkingInt).equals("W") && !boardObject.getCoverage(checkingInt).equals("M") && (boardObject.checkSquare(checkingInt) || !boardObject.getPiece(checkingInt).getColor().equals(king.getColor()))) {
					possibleCheckMoves.add("BK.NE.1");
				}
			} catch(Exception e) {
				
			}
			try {
				int [] checkingInt = {KC[0], KC[1] + 1};
				if(!boardObject.getCoverage(checkingInt).equals("W") && !boardObject.getCoverage(checkingInt).equals("M") && (boardObject.checkSquare(checkingInt) || !boardObject.getPiece(checkingInt).getColor().equals(king.getColor()))) {
					possibleCheckMoves.add("BK.PY.1");
				}
			} catch(Exception e) {
				
			}
			try {
				int [] checkingInt = {KC[0] - 1, KC[1] + 1};
				if(!boardObject.getCoverage(checkingInt).equals("W") && !boardObject.getCoverage(checkingInt).equals("M") && (boardObject.checkSquare(checkingInt) || !boardObject.getPiece(checkingInt).getColor().equals(king.getColor()))) {
					possibleCheckMoves.add("BK.NW.1");
				}
			} catch(Exception e) {
				
			}
			try {
				int [] checkingInt = {KC[0] - 1, KC[1]};
				if(!boardObject.getCoverage(checkingInt).equals("W") && !boardObject.getCoverage(checkingInt).equals("M") && (boardObject.checkSquare(checkingInt) || !boardObject.getPiece(checkingInt).getColor().equals(king.getColor()))) {
					possibleCheckMoves.add("BK.NX.1");
				}
			} catch(Exception e) {
				
			}
			try {
				int [] checkingInt = {KC[0] - 1, KC[1] - 1};
				if(!boardObject.getCoverage(checkingInt).equals("W") && !boardObject.getCoverage(checkingInt).equals("M") && (boardObject.checkSquare(checkingInt) || !boardObject.getPiece(checkingInt).getColor().equals(king.getColor()))) {
					possibleCheckMoves.add("BK.SW.1");
				}
			} catch(Exception e) {
				
			}
			try {
				int [] checkingInt = {KC[0], KC[1] - 1};
				if(!boardObject.getCoverage(checkingInt).equals("W") && !boardObject.getCoverage(checkingInt).equals("M") && (boardObject.checkSquare(checkingInt) || !boardObject.getPiece(checkingInt).getColor().equals(king.getColor()))) {
					possibleCheckMoves.add("BK.NY.1");
				}
			} catch(Exception e) {
				
			}
			try {
				int [] checkingInt = {KC[0] + 1, KC[1] - 1};
				if(!boardObject.getCoverage(checkingInt).equals("W") && !boardObject.getCoverage(checkingInt).equals("M") && (boardObject.checkSquare(checkingInt) || !boardObject.getPiece(checkingInt).getColor().equals(king.getColor()))) {
					possibleCheckMoves.add("BK.SE.1");
				}
			} catch(Exception e) {
				
			}
			
			//next check for pieces that can block check
			Piece [] checkingPieces = boardObject.getCheckingPieces();
			int [] CP1C = conversionObject.convertNumericalCoordinates(checkingPieces[0].getCoordinate());
			System.out.println("Checking Piece coordinate: " + CP1C[0] + CP1C[1]);
			
			if(checkingPieces[1] == null) {
				System.out.println("Possible to move a piece to block");
				
				String checkingFile = null;
				//find checking file
				if(CP1C[0] == KC[0]) {
					
					if(CP1C[1] > KC[1]) {
						checkingFile = "PY";
					} else {
						checkingFile = "NY";
					}
				} else if(CP1C[1] == KC[1]) {
					if(CP1C[0] > KC[0]) {
						checkingFile = "PX";
					} else{
						checkingFile = "NX";
					}
				} else if((CP1C[0] > KC[0] && CP1C[1] > KC[1]) || (CP1C[0] < KC[0] && CP1C[1] < KC[1])) {
					if(CP1C[0] > KC[0]) {
						checkingFile = "NE";
					} else {
						checkingFile = "SW";
					}
					
				} else {
					if(CP1C[0] < KC[0]) {
						checkingFile = "NW";
					} else {
						checkingFile = "SE";
					}
				}
				
				if(checkingFile.equals("PY")) {
					for(int i = KC[1] + 1; i < CP1C[1]; i++) {
						//check squares for occupancy of other pieces
						int [] squareToCheck = {KC[0], i};

						//trace out pieces that can move here
						ArrayList<Piece> pieceToMove = findCoverage(squareToCheck, color);
						for(int j = 0; j < pieceToMove.size(); j++) {
							String piece = pieceToMove.get(j).getPieceType();
							String squareToMove = conversionObject.convertAlgebraCoordinates("" + squareToCheck[0] + squareToCheck[1]);
							String total = piece + "." + squareToMove;
							possibleCheckMoves.add(total);
						}
					}
				}
				if(checkingFile.equals("NY")) {
					for(int i = KC[1] - 1; i > CP1C[1]; i--) {
						//check squares for occupancy of other pieces
						int [] squareToCheck = {KC[0], i};
						
						//trace out pieces that can move here
						ArrayList<Piece> pieceToMove = findCoverage(squareToCheck, color);
						for(int j = 0; j < pieceToMove.size(); j++) {
							String piece = pieceToMove.get(j).getPieceType();
							String squareToMove = conversionObject.convertAlgebraCoordinates("" + squareToCheck[0] + squareToCheck[1]);
							String total = piece + "." + squareToMove;
							possibleCheckMoves.add(total);
						}
					}
				}
				if(checkingFile.equals("PX")) {
					for(int i = KC[0] + 1; i < CP1C[0]; i++) {
						//check squares for occupancy of other pieces
						int [] squareToCheck = {i, KC[1]};
						
						//trace out pieces that can move here
						ArrayList<Piece> pieceToMove = findCoverage(squareToCheck, color);
						for(int j = 0; j < pieceToMove.size(); j++) {
							String piece = pieceToMove.get(j).getPieceType();
							String squareToMove = conversionObject.convertAlgebraCoordinates("" + squareToCheck[0] + squareToCheck[1]);
							String total = piece + "." + squareToMove;
							possibleCheckMoves.add(total);
						}
					}
				}
				if(checkingFile.equals("NX")) {
					for(int i = KC[0] - 1; i > CP1C[0]; i--) {
						//check squares for occupancy of other pieces
						int [] squareToCheck = {i, KC[1]};
						
						//trace out pieces that can move here
						ArrayList<Piece> pieceToMove = findCoverage(squareToCheck, color);
						for(int j = 0; j < pieceToMove.size(); j++) {
							String piece = pieceToMove.get(j).getPieceType();
							String squareToMove = conversionObject.convertAlgebraCoordinates("" + squareToCheck[0] + squareToCheck[1]);
							String total = piece + "." + squareToMove;
							possibleCheckMoves.add(total);
						}
					}
				}
				if(checkingFile.equals("NE")) {
					for(int i = KC[0] + 1, k = KC[1] + 1; i < CP1C[0]; i++, k++) {
						//check squares for occupancy of other pieces
						int [] squareToCheck = {i, k};

						//trace out pieces that can move here
						ArrayList<Piece> pieceToMove = findCoverage(squareToCheck, color);
						for(int j = 0; j < pieceToMove.size(); j++) {
							String piece = pieceToMove.get(j).getPieceType();
							String squareToMove = conversionObject.convertAlgebraCoordinates("" + squareToCheck[0] + squareToCheck[1]);
							String total = piece + "." + squareToMove;
							possibleCheckMoves.add(total);
						}
					}
				}
				if(checkingFile.equals("SE")) {
					for(int i = KC[0] + 1, k = KC[1] - 1; i < CP1C[0]; i++, k--) {
						//check squares for occupancy of other pieces
						int [] squareToCheck = {i, k};

						//trace out pieces that can move here
						ArrayList<Piece> pieceToMove = findCoverage(squareToCheck, color);
						for(int j = 0; j < pieceToMove.size(); j++) {
							String piece = pieceToMove.get(j).getPieceType();
							String squareToMove = conversionObject.convertAlgebraCoordinates("" + squareToCheck[0] + squareToCheck[1]);
							String total = piece + "." + squareToMove;
							possibleCheckMoves.add(total);
						}
					}
				}
				if(checkingFile.equals("NW")) {
					for(int i = KC[0] - 1, k = KC[1] + 1; i > CP1C[0]; i--, k++) {
						//check squares for occupancy of other pieces
						int [] squareToCheck = {i, k};

						//trace out pieces that can move here
						ArrayList<Piece> pieceToMove = findCoverage(squareToCheck, color);
						for(int j = 0; j < pieceToMove.size(); j++) {
							String piece = pieceToMove.get(j).getPieceType();
							String squareToMove = conversionObject.convertAlgebraCoordinates("" + squareToCheck[0] + squareToCheck[1]);
							String total = piece + "." + squareToMove;
							possibleCheckMoves.add(total);
						}
					}
				}
				if(checkingFile.equals("SW")) {
					for(int i = KC[0] - 1, k = KC[1] - 1; i > CP1C[0]; i--, k--) {
						//check squares for occupancy of other pieces
						int [] squareToCheck = {i, k};

						//trace out pieces that can move here
						ArrayList<Piece> pieceToMove = findCoverage(squareToCheck, color);
						for(int j = 0; j < pieceToMove.size(); j++) {
							String piece = pieceToMove.get(j).getPieceType();
							String squareToMove = conversionObject.convertAlgebraCoordinates("" + squareToCheck[0] + squareToCheck[1]);
							String total = piece + "." + squareToMove;
							possibleCheckMoves.add(total);
						}
					}
				}
				
				//next check for moves that attack the piece checking
				ArrayList<Piece> possibleAttackers = findCoverage(CP1C, color);
				for(int j = 0; j < possibleAttackers.size(); j++) {
					if(!possibleAttackers.get(j).getPieceType().substring(1).equals("P")) {
						String piece = possibleAttackers.get(j).getPieceType() + "X";
						String squareToMove = conversionObject.convertAlgebraCoordinates("" + CP1C[0] + CP1C[1]);
						String total = piece + "." + squareToMove;
						possibleCheckMoves.add(total);
					} 	
					
				}
				
				//special case for pawns. calculated manually here
				//check for black pawns
				
				try {
					//right up
					int [] int1 = {CP1C[0] + 1, CP1C[1] + 1};
					boolean pieceExists1 = !boardObject.checkSquare(int1);
					
					if(pieceExists1) {
						boolean isPawn1 = boardObject.getPiece(int1).getPieceType().substring(1).equals("P");
						boolean isSameColor1 = boardObject.getPiece(int1).getColor().equals(color);
						if(isPawn1 && isSameColor1) {
							String piece = boardObject.getPiece(int1).getPieceType() + "X";
							String squareToMove = conversionObject.convertAlgebraCoordinates("" + CP1C[0] + CP1C[1]);
							String total = piece + "." + squareToMove;
							possibleCheckMoves.add(total);
						}
					}
				}catch (Exception e) {
					
				}
				
				
				try {
					//left up
					int [] int2 = {CP1C[0] - 1, CP1C[1] + 1};
					boolean pieceExists2 = !boardObject.checkSquare(int2);
					
					if(pieceExists2) {
						boolean isPawn2 = boardObject.getPiece(int2).getPieceType().substring(1).equals("P");
						boolean isSameColor2 = boardObject.getPiece(int2).getColor().equals(color);
						if(isPawn2 && isSameColor2) {
							String piece = boardObject.getPiece(int2).getPieceType() + "X";
							String squareToMove = conversionObject.convertAlgebraCoordinates("" + CP1C[0] + CP1C[1]);
							String total = piece + "." + squareToMove;
							possibleCheckMoves.add(total);
						}
					}
				}catch (Exception e) {
					
				}
				
				
				
			} //no else because the king movements were already mapped
		}
		System.out.println("Possible moves: " + possibleCheckMoves);
	}
	
	/**
	 * Function that checks if a piece is pinned
	 * @param pieceToCheck the piece that we are checking 
	 * @param direction a String that indicates the direction to check
	 * @return a boolean indicating if the piece is pinned
	 */
	public boolean isPiecePinned(Piece pieceToCheck, String direction) {
		Piece pieceStatus[][] = boardObject.getBoardStatus();
		int[] coordinate = conversionObject.convertNumericalCoordinates(pieceToCheck.getCoordinate());
		String color = pieceToCheck.getColor();
		Piece king;
		if(color.equals("B")) {
			king = blackKing;
		} else {
			king = whiteKing;
		}
		int[] kingCoordinates = conversionObject.convertNumericalCoordinates(king.getCoordinate());
		
		if(isWhiteInCheck || isBlackInCheck) {
			System.out.println("ERROR: isPiecePinned function accessed when a piece is in check");
		}
		
		try {
			//check positive X
			if(direction.equals("PX")) {
				pieceStatus[coordinate[0]][coordinate[1]] = null;
				pieceStatus[coordinate[0] + 1][coordinate[1]] = pieceToCheck;
				
				String coverageMap [][] = boardObject.updateCoverageMap(pieceStatus).clone();
				
				boolean isInCheck = !coverageMap[kingCoordinates[0]][kingCoordinates[1]].equals(color);
				
				return isInCheck;
				
			} else if(direction.equals("NX")) {
				pieceStatus[coordinate[0]][coordinate[1]] = null;
				pieceStatus[coordinate[0] - 1][coordinate[1]] = pieceToCheck;
				
				String coverageMap [][] = boardObject.updateCoverageMap(pieceStatus).clone();
				
				boolean isInCheck = !coverageMap[kingCoordinates[0]][kingCoordinates[1]].equals(color);
				
				return isInCheck;
				
			} else if(direction.equals("NY")) {
				pieceStatus[coordinate[0]][coordinate[1]] = null;
				pieceStatus[coordinate[0]][coordinate[1] - 1] = pieceToCheck;
				
				String coverageMap [][] = boardObject.updateCoverageMap(pieceStatus).clone();
				
				boolean isInCheck = !coverageMap[kingCoordinates[0]][kingCoordinates[1]].equals(color);
				
				return isInCheck;
				
			} else if(direction.equals("PY")) {
				pieceStatus[coordinate[0]][coordinate[1]] = null;
				pieceStatus[coordinate[0]][coordinate[1] + 1] = pieceToCheck;
				
				String coverageMap [][] = boardObject.updateCoverageMap(pieceStatus).clone();
				
				boolean isInCheck = !coverageMap[kingCoordinates[0]][kingCoordinates[1]].equals(color);
				
				return isInCheck;
				
			} else if(direction.equals("NE")) {
				pieceStatus[coordinate[0]][coordinate[1]] = null;
				pieceStatus[coordinate[0] + 1][coordinate[1] + 1] = pieceToCheck;
				
				String coverageMap [][] = boardObject.updateCoverageMap(pieceStatus).clone();
				
				boolean isInCheck = !coverageMap[kingCoordinates[0]][kingCoordinates[1]].equals(color);
				
				return isInCheck;
				
			} else if(direction.equals("NW")) {
				pieceStatus[coordinate[0]][coordinate[1]] = null;
				pieceStatus[coordinate[0] - 1][coordinate[1] + 1] = pieceToCheck;
				
				String coverageMap [][] = boardObject.updateCoverageMap(pieceStatus).clone();
				
				boolean isInCheck = !coverageMap[kingCoordinates[0]][kingCoordinates[1]].equals(color);
				
				return isInCheck;
				
			} else if(direction.equals("SE")) {
				pieceStatus[coordinate[0]][coordinate[1]] = null;
				pieceStatus[coordinate[0] + 1][coordinate[1] - 1] = pieceToCheck;
				
				String coverageMap [][] = boardObject.updateCoverageMap(pieceStatus).clone();
				
				boolean isInCheck = !coverageMap[kingCoordinates[0]][kingCoordinates[1]].equals(color);
				
				return isInCheck;
				
			} else if(direction.equals("SW")) {
				pieceStatus[coordinate[0]][coordinate[1]] = null;
				pieceStatus[coordinate[0] - 1][coordinate[1] - 1] = pieceToCheck;
				
				String coverageMap [][] = boardObject.updateCoverageMap(pieceStatus).clone();
				
				boolean isInCheck = !coverageMap[kingCoordinates[0]][kingCoordinates[1]].equals(color);
				
				return isInCheck;
				
			}
		} catch(Exception e) {
			
		}
		
		
		
		return false;
		
	}
	

	/**
	 * A function that returns the pieces that are capable of moving to the given square. This function is primairly used in the check and checkmate cases to see what pieces can block check
	 * @param squareToCheck The square getting checked
	 * @param color the color of pieces to check
	 * @return an arrayList of the pieces that can move to that square
	 */
	private ArrayList<Piece> findCoverage(int[] squareToCheck, String color) {
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		//check all 8 directions to find a piece that can move to this square
		
		//pos x which has pieces rook and queen that can move to the square
		for(int i = squareToCheck[0] + 1; i < 8; i++) {
	
			int[] newSquare = {i, squareToCheck[1]};
			
			boolean squareOccupied = !boardObject.checkSquare(newSquare);
			
			if(squareOccupied) {
				boolean sameColor = boardObject.getPiece(newSquare).getColor().equals(color);
				boolean containsPiece = boardObject.getPiece(newSquare).getPieceType().substring(1).equals("Q") || boardObject.getPiece(newSquare).getPieceType().substring(1).equals("R");
				if(sameColor && containsPiece) {
					pieces.add(boardObject.getPiece(newSquare));
				}
				break;
			}
		}
		//neg x which has pieces rook and queen that can move to the square
		for(int i = squareToCheck[0] - 1; i >= 0; i--) {
	
			int[] newSquare = {i, squareToCheck[1]};
			
			boolean squareOccupied = !boardObject.checkSquare(newSquare);
			
			if(squareOccupied) {
				boolean sameColor = boardObject.getPiece(newSquare).getColor().equals(color);
				boolean containsPiece = boardObject.getPiece(newSquare).getPieceType().substring(1).equals("Q") || boardObject.getPiece(newSquare).getPieceType().substring(1).equals("R");
				if(sameColor && containsPiece) {
					pieces.add(boardObject.getPiece(newSquare));
				}
				break;
			}
		}
		//pos y which has pieces rook and queen that can move to the square
		for(int i = squareToCheck[1] + 1; i < 8; i++) {
	
			int[] newSquare = {squareToCheck[0], i};
			
			boolean squareOccupied = !boardObject.checkSquare(newSquare);
			
			if(squareOccupied) {
				boolean sameColor = boardObject.getPiece(newSquare).getColor().equals(color);
				boolean containsPiece = boardObject.getPiece(newSquare).getPieceType().substring(1).equals("Q") || boardObject.getPiece(newSquare).getPieceType().substring(1).equals("R");
				if(sameColor && containsPiece) {
					pieces.add(boardObject.getPiece(newSquare));
				}
				break;
			}
		}
		//neg y which has pieces rook and queen that can move to the square
		for(int i = squareToCheck[1] - 1; i >= 0; i--) {
	
			int[] newSquare = {squareToCheck[0], i};
			
			boolean squareOccupied = !boardObject.checkSquare(newSquare);
			
			if(squareOccupied) {
				boolean sameColor = boardObject.getPiece(newSquare).getColor().equals(color);
				boolean containsPiece = boardObject.getPiece(newSquare).getPieceType().substring(1).equals("Q") || boardObject.getPiece(newSquare).getPieceType().substring(1).equals("R");
				if(sameColor && containsPiece) {
					pieces.add(boardObject.getPiece(newSquare));
				}
				break;
			}
		}
		
		//check diagonals which can be queen or bishop
		//NE
		for(int i = squareToCheck[0] + 1, j = squareToCheck[1] + 1; i < 8 && j < 8; i++, j++) {
			
			int[] newSquare = {i, j};
			
			boolean squareOccupied = !boardObject.checkSquare(newSquare);
			
			if(squareOccupied) {
				boolean sameColor = boardObject.getPiece(newSquare).getColor().equals(color);
				boolean containsPiece = boardObject.getPiece(newSquare).getPieceType().substring(1).equals("Q") || boardObject.getPiece(newSquare).getPieceType().substring(1).equals("B");
				if(sameColor && containsPiece) {
					pieces.add(boardObject.getPiece(newSquare));
				}
				break;
			}
		}
		//NW
		for(int i = squareToCheck[0] - 1, j = squareToCheck[1] + 1; i >= 0 && j < 8; i--, j++) {
			
			int[] newSquare = {i, j};
			
			boolean squareOccupied = !boardObject.checkSquare(newSquare);
			
			if(squareOccupied) {
				boolean sameColor = boardObject.getPiece(newSquare).getColor().equals(color);
				boolean containsPiece = boardObject.getPiece(newSquare).getPieceType().substring(1).equals("Q") || boardObject.getPiece(newSquare).getPieceType().substring(1).equals("B");
				if(sameColor && containsPiece) {
					pieces.add(boardObject.getPiece(newSquare));
				}
				break;
			}
		}
		//SW
		for(int i = squareToCheck[0] - 1, j = squareToCheck[1] - 1; i >= 0 && j >= 0; i--, j--) {
			
			int[] newSquare = {i, j};
			
			boolean squareOccupied = !boardObject.checkSquare(newSquare);
			
			if(squareOccupied) {
				boolean sameColor = boardObject.getPiece(newSquare).getColor().equals(color);
				boolean containsPiece = boardObject.getPiece(newSquare).getPieceType().substring(1).equals("Q") || boardObject.getPiece(newSquare).getPieceType().substring(1).equals("B");
				if(sameColor && containsPiece) {
					pieces.add(boardObject.getPiece(newSquare));
				}
				break;
			}
		}
		
		//SE
		for(int i = squareToCheck[0] + 1, j = squareToCheck[1] - 1; i < 8 && j >= 0; i++, j--) {
			
			int[] newSquare = {i, j};
			
			boolean squareOccupied = !boardObject.checkSquare(newSquare);
			
			if(squareOccupied) {
				boolean sameColor = boardObject.getPiece(newSquare).getColor().equals(color);
				boolean containsPiece = boardObject.getPiece(newSquare).getPieceType().substring(1).equals("Q") || boardObject.getPiece(newSquare).getPieceType().substring(1).equals("B");
				if(sameColor && containsPiece) {
					pieces.add(boardObject.getPiece(newSquare));
				}
				break;
			}
		}
		
		//pawns
		if(color.equals("W")) {
			//white
			try {
				//check for piece 1 down
				int[] newSquare = {squareToCheck[0], squareToCheck[1] - 1};
				boolean containsPiece = !boardObject.checkSquare(newSquare);
				
				//if pawn is there, pawn can move, if it is not a pawn, break if statement
				if(containsPiece) {
					boolean isPawn = boardObject.getPiece(newSquare).getPieceType().substring(1).equals("P");
					if(isPawn) {
						pieces.add(boardObject.getPiece(newSquare));
					} 
				} else if(squareToCheck[1] == 3){ //empty, check next piece down if on the 4th row
					newSquare = new int[] {squareToCheck[0], squareToCheck[1] - 2};
					containsPiece = !boardObject.checkSquare(newSquare);
					
					if(containsPiece) {
						boolean isPawn = boardObject.getPiece(newSquare).getPieceType().substring(1).equals("P");
						if(isPawn) {
							pieces.add(boardObject.getPiece(newSquare));
						}
					}
					
				}
			} catch(Exception e) {
				
			}
			
			
		} else {
			//black
			try {
				//check for piece 1 up
				int[] newSquare = {squareToCheck[0], squareToCheck[1] + 1};
				boolean containsPiece = !boardObject.checkSquare(newSquare);
				
				//if pawn is there, pawn can move, if it is not a pawn, break if statement
				if(containsPiece) {
					boolean isPawn = boardObject.getPiece(newSquare).getPieceType().substring(1).equals("P");
					if(isPawn) {
						pieces.add(boardObject.getPiece(newSquare));
					} 
				} else if(squareToCheck[1] == 4){ //empty, check next piece up if on the 5th row
					newSquare = new int[] {squareToCheck[0], squareToCheck[1] + 2};
					containsPiece = !boardObject.checkSquare(newSquare);
					
					if(containsPiece) {
						boolean isPawn = boardObject.getPiece(newSquare).getPieceType().substring(1).equals("P");
						if(isPawn) {
							pieces.add(boardObject.getPiece(newSquare));
						}
					}
					
				}
			} catch(Exception e) {
				
			}
			
			
		}
		
		//knights
		//square 1
		try {
			int[] checkHorse = {squareToCheck[0] + 1, squareToCheck[1] + 2};
			boolean containsPiece = !boardObject.checkSquare(checkHorse);
			if(containsPiece) {
				boolean pieceIsKnight = boardObject.getPiece(checkHorse).getPieceType().substring(1).equals("Kn");
				boolean isSameColor = boardObject.getPiece(checkHorse).getColor().equals(color);
				if(isSameColor && pieceIsKnight) {
					pieces.add(boardObject.getPiece(checkHorse));
				}
			}
		} catch(Exception e) {
			
		}
		//square 2
		try {
			int[] checkHorse = {squareToCheck[0] + 2, squareToCheck[1] + 1};
			boolean containsPiece = !boardObject.checkSquare(checkHorse);
			if(containsPiece) {
				boolean pieceIsKnight = boardObject.getPiece(checkHorse).getPieceType().substring(1).equals("Kn");
				boolean isSameColor = boardObject.getPiece(checkHorse).getColor().equals(color);
				if(isSameColor && pieceIsKnight) {
					pieces.add(boardObject.getPiece(checkHorse));
				}
			}
		} catch(Exception e) {
			
		}
		//square 3
		try {
			int[] checkHorse = {squareToCheck[0] + 2, squareToCheck[1] - 1};
			boolean containsPiece = !boardObject.checkSquare(checkHorse);
			if(containsPiece) {
				boolean pieceIsKnight = boardObject.getPiece(checkHorse).getPieceType().substring(1).equals("Kn");
				boolean isSameColor = boardObject.getPiece(checkHorse).getColor().equals(color);
				if(isSameColor && pieceIsKnight) {
					pieces.add(boardObject.getPiece(checkHorse));
				}
			}
		} catch(Exception e) {
			
		}
		//square 4
		try {
			int[] checkHorse = {squareToCheck[0] + 1, squareToCheck[1] - 2};
			boolean containsPiece = !boardObject.checkSquare(checkHorse);
			if(containsPiece) {
				boolean pieceIsKnight = boardObject.getPiece(checkHorse).getPieceType().substring(1).equals("Kn");
				boolean isSameColor = boardObject.getPiece(checkHorse).getColor().equals(color);
				if(isSameColor && pieceIsKnight) {
					pieces.add(boardObject.getPiece(checkHorse));
				}
			}
		} catch(Exception e) {
			
		}
		//square 5
		try {
			int[] checkHorse = {squareToCheck[0] - 1, squareToCheck[1] - 2};
			boolean containsPiece = !boardObject.checkSquare(checkHorse);
			if(containsPiece) {
				boolean pieceIsKnight = boardObject.getPiece(checkHorse).getPieceType().substring(1).equals("Kn");
				boolean isSameColor = boardObject.getPiece(checkHorse).getColor().equals(color);
				if(isSameColor && pieceIsKnight) {
					pieces.add(boardObject.getPiece(checkHorse));
				}
			}
		} catch(Exception e) {
			
		}
		//square 6
		try {
			int[] checkHorse = {squareToCheck[0] - 2, squareToCheck[1] - 1};
			boolean containsPiece = !boardObject.checkSquare(checkHorse);
			if(containsPiece) {
				boolean pieceIsKnight = boardObject.getPiece(checkHorse).getPieceType().substring(1).equals("Kn");
				boolean isSameColor = boardObject.getPiece(checkHorse).getColor().equals(color);
				if(isSameColor && pieceIsKnight) {
					pieces.add(boardObject.getPiece(checkHorse));
				}
			}
		} catch(Exception e) {
			
		}
		//square 7
		try {
			int[] checkHorse = {squareToCheck[0] - 2, squareToCheck[1] + 1};
			boolean containsPiece = !boardObject.checkSquare(checkHorse);
			if(containsPiece) {
				boolean pieceIsKnight = boardObject.getPiece(checkHorse).getPieceType().substring(1).equals("Kn");
				boolean isSameColor = boardObject.getPiece(checkHorse).getColor().equals(color);
				if(isSameColor && pieceIsKnight) {
					pieces.add(boardObject.getPiece(checkHorse));
				}
			}
		} catch(Exception e) {
			
		}
		//square 8
		try {
			int[] checkHorse = {squareToCheck[0] - 1, squareToCheck[1] + 2};
			boolean containsPiece = !boardObject.checkSquare(checkHorse);
			if(containsPiece) {
				boolean pieceIsKnight = boardObject.getPiece(checkHorse).getPieceType().substring(1).equals("Kn");
				boolean isSameColor = boardObject.getPiece(checkHorse).getColor().equals(color);
				if(isSameColor && pieceIsKnight) {
					pieces.add(boardObject.getPiece(checkHorse));
				}
			}
		} catch(Exception e) {
			
		}
		
		return pieces;
	}

	/**
	 * Function that instantiates the dots that show where a player can move a piece
	 * 
	 * @param pieceType   The type of piece that was clicked. View piece class for
	 *                    specifics
	 * @param coordinates The numerical coordinates of the piece that was clicked
	 * @param frame       The main chess board frame
	 */
	public void instantiateDots(Piece piecePointer) {
		
		clearDots(frame);
		this.piecePointer = piecePointer;
		String coordinates = piecePointer.getCoordinate();
		this.frame = piecePointer.getFrame();
		String pieceType = piecePointer.getPieceType();

		int coords[] = conversionObject.convertNumericalCoordinates(coordinates);
		lastClickedCoords = coords.clone();
		didEnPassant = false;
		
		
		//logic that determines what pieces move where
		if(isWhiteInCheck && whiteTurn) {
			//king coordinates
			int [] KC = conversionObject.convertNumericalCoordinates(whiteKing.getCoordinate());
			updatePossibleCheckMoves(whiteKing);
			
			//check if white king is clicked
			if(pieceType.equals("WK")) {
				for(int i = 0; i < possibleCheckMoves.size(); i++) {
					String[] commands = possibleCheckMoves.get(i).split("\\.");
					if(commands[0].equals("WK")) {
						drawDotsLine(KC, commands[1], Integer.parseInt(commands[2]));
					}
				}
			}
			if(pieceType.equals("WQ")) {
				for(int i = 0; i < possibleCheckMoves.size(); i++) {
					String[] commands = possibleCheckMoves.get(i).split("\\.");
					if(commands[0].equals("WQ")) {
						int[] dotCoord = conversionObject.convertNumericalCoordinates(commands[1]);
						drawDotToScreen(dotCoord, "/dot.png");
					}
				}
			}
			if(pieceType.equals("WB")) {
				for(int i = 0; i < possibleCheckMoves.size(); i++) {
					String[] commands = possibleCheckMoves.get(i).split("\\.");
					if(commands[0].equals("WB")) {
						int[] dotCoord = conversionObject.convertNumericalCoordinates(commands[1]);
						drawDotToScreen(dotCoord, "/dot.png");
					}
				}
			}
			if(pieceType.equals("WKn")) {
				for(int i = 0; i < possibleCheckMoves.size(); i++) {
					String[] commands = possibleCheckMoves.get(i).split("\\.");
					if(commands[0].equals("WKn")) {
						int[] dotCoord = conversionObject.convertNumericalCoordinates(commands[1]);
						drawDotToScreen(dotCoord, "/dot.png");
					}
				}
			}
			if(pieceType.equals("WR")) {
				for(int i = 0; i < possibleCheckMoves.size(); i++) {
					String[] commands = possibleCheckMoves.get(i).split("\\.");
					if(commands[0].equals("WR")) {
						int[] dotCoord = conversionObject.convertNumericalCoordinates(commands[1]);
						drawDotToScreen(dotCoord, "/dot.png");
					}
				}
			}
			if(pieceType.equals("WP")) {
				for(int i = 0; i < possibleCheckMoves.size(); i++) {
					String[] commands = possibleCheckMoves.get(i).split("\\.");
					if(commands[0].equals("WP")) {
						int[] dotCoord = conversionObject.convertNumericalCoordinates(commands[1]);
						if(dotCoord[0] == coords[0]) {
							drawDotToScreen(dotCoord, "/dot.png");
						}
					}
				}
			}
			if(pieceType.equals("WQ")) {
				for(int i = 0; i < possibleCheckMoves.size(); i++) {
					String[] commands = possibleCheckMoves.get(i).split("\\.");
					if(commands[0].equals("WQX")) {
						int[] dotCoord = conversionObject.convertNumericalCoordinates(commands[1]);
						drawDotToScreen(dotCoord, "/redDot.png");
					}
				}
			}
			if(pieceType.equals("WB")) {
				for(int i = 0; i < possibleCheckMoves.size(); i++) {
					String[] commands = possibleCheckMoves.get(i).split("\\.");
					if(commands[0].equals("WBX")) {
						int[] dotCoord = conversionObject.convertNumericalCoordinates(commands[1]);
						drawDotToScreen(dotCoord, "/redDot.png");
					}
				}
			}
			if(pieceType.equals("WKn")) {
				for(int i = 0; i < possibleCheckMoves.size(); i++) {
					String[] commands = possibleCheckMoves.get(i).split("\\.");
					if(commands[0].equals("WKnX")) {
						int[] dotCoord = conversionObject.convertNumericalCoordinates(commands[1]);
						drawDotToScreen(dotCoord, "/redDot.png");
					}
				}
			}
			if(pieceType.equals("WR")) {
				for(int i = 0; i < possibleCheckMoves.size(); i++) {
					String[] commands = possibleCheckMoves.get(i).split("\\.");
					if(commands[0].equals("WXR")) {
						int[] dotCoord = conversionObject.convertNumericalCoordinates(commands[1]);
						drawDotToScreen(dotCoord, "/redDot.png");
					}
				}
			}
			if(pieceType.equals("WP")) {
				for(int i = 0; i < possibleCheckMoves.size(); i++) {
					String[] commands = possibleCheckMoves.get(i).split("\\.");
					if(commands[0].equals("WPX")) {
						int[] dotCoord = conversionObject.convertNumericalCoordinates(commands[1]);
						if(Math.abs(dotCoord[0] - coords[0]) == 1 && Math.abs(dotCoord[1] - coords[1]) == 1)
						{
							drawDotToScreen(dotCoord, "/redDot.png");
						}
					}
				}
			}
						
			
		} else if (isBlackInCheck && !whiteTurn) {
			
			//king coordinates
			int [] KC = conversionObject.convertNumericalCoordinates(blackKing.getCoordinate());
			updatePossibleCheckMoves(blackKing);
			
			if(pieceType.equals("BK")) {
				for(int i = 0; i < possibleCheckMoves.size(); i++) {
					String[] commands = possibleCheckMoves.get(i).split("\\.");
					if(commands[0].equals("BK")) {
						drawDotsLine(KC, commands[1], Integer.parseInt(commands[2]));
					}
				}
			}
			if(pieceType.equals("BQ")) {
				for(int i = 0; i < possibleCheckMoves.size(); i++) {
					String[] commands = possibleCheckMoves.get(i).split("\\.");
					if(commands[0].equals("BQ")) {
						int[] dotCoord = conversionObject.convertNumericalCoordinates(commands[1]);
						drawDotToScreen(dotCoord, "/dot.png");
					}
				}
			}
			if(pieceType.equals("BR")) {
				for(int i = 0; i < possibleCheckMoves.size(); i++) {
					String[] commands = possibleCheckMoves.get(i).split("\\.");
					if(commands[0].equals("BR")) {
						int[] dotCoord = conversionObject.convertNumericalCoordinates(commands[1]);
						drawDotToScreen(dotCoord, "/dot.png");
					}
				}
			}
			if(pieceType.equals("BKn")) {
				for(int i = 0; i < possibleCheckMoves.size(); i++) {
					String[] commands = possibleCheckMoves.get(i).split("\\.");
					if(commands[0].equals("BKn")) {
						int[] dotCoord = conversionObject.convertNumericalCoordinates(commands[1]);
						drawDotToScreen(dotCoord, "/dot.png");
					}
				}
			}
			if(pieceType.equals("BB")) {
				for(int i = 0; i < possibleCheckMoves.size(); i++) {
					String[] commands = possibleCheckMoves.get(i).split("\\.");
					if(commands[0].equals("BB")) {
						int[] dotCoord = conversionObject.convertNumericalCoordinates(commands[1]);
						drawDotToScreen(dotCoord, "/dot.png");
					}
				}
			}
			if(pieceType.equals("BP")) {
				for(int i = 0; i < possibleCheckMoves.size(); i++) {
					String[] commands = possibleCheckMoves.get(i).split("\\.");
					if(commands[0].equals("BP")) {
						int[] dotCoord = conversionObject.convertNumericalCoordinates(commands[1]);
						if(dotCoord[0] == coords[0]) {
							drawDotToScreen(dotCoord, "/dot.png");
						}
					}
				}
			}
			if(pieceType.equals("BQ")) {
				for(int i = 0; i < possibleCheckMoves.size(); i++) {
					String[] commands = possibleCheckMoves.get(i).split("\\.");
					if(commands[0].equals("BQX")) {
						int[] dotCoord = conversionObject.convertNumericalCoordinates(commands[1]);
						drawDotToScreen(dotCoord, "/redDot.png");
					}
				}
			}
			if(pieceType.equals("BR")) {
				for(int i = 0; i < possibleCheckMoves.size(); i++) {
					String[] commands = possibleCheckMoves.get(i).split("\\.");
					if(commands[0].equals("BRX")) {
						int[] dotCoord = conversionObject.convertNumericalCoordinates(commands[1]);
						drawDotToScreen(dotCoord, "/redDot.png");
					}
				}
			}
			if(pieceType.equals("BKn")) {
				for(int i = 0; i < possibleCheckMoves.size(); i++) {
					String[] commands = possibleCheckMoves.get(i).split("\\.");
					if(commands[0].equals("BKnX")) {
						int[] dotCoord = conversionObject.convertNumericalCoordinates(commands[1]);
						drawDotToScreen(dotCoord, "/redDot.png");
					}
				}
			}
			if(pieceType.equals("BB")) {
				for(int i = 0; i < possibleCheckMoves.size(); i++) {
					String[] commands = possibleCheckMoves.get(i).split("\\.");
					if(commands[0].equals("BBX")) {
						int[] dotCoord = conversionObject.convertNumericalCoordinates(commands[1]);
						drawDotToScreen(dotCoord, "/redDot.png");
					}
				}
			}
			if(pieceType.equals("BP")) {
				for(int i = 0; i < possibleCheckMoves.size(); i++) {
					String[] commands = possibleCheckMoves.get(i).split("\\.");
					if(commands[0].equals("BPX")) {
						int[] dotCoord = conversionObject.convertNumericalCoordinates(commands[1]);
						if(Math.abs(dotCoord[0] - coords[0]) == 1 && Math.abs(dotCoord[1] - coords[1]) == 1)
						{
							drawDotToScreen(dotCoord, "/redDot.png");
						}
					}
				}
			}
			
			
		} else if(!isBlackInCheck && !isWhiteInCheck){
		
			switch (pieceType) {
			case ("WP"):
				if (whiteTurn) {

					try {
						if (coords[1] == 1) {
							if (boardObject.checkSquare(new int[] { coords[0], coords[1] + 2 })
									&& boardObject.checkSquare(new int[] { coords[0], coords[1] + 1 })) {
								// instantiate two dots because pawn is on starting file and nothing is blocking
								// it

								if(!isPiecePinned(piecePointer, "PY")) {
									drawDotsLine(coords, "PY", 2);
								}
								

							}
						}

						if (boardObject.checkSquare(new int[] { coords[0], coords[1] + 1 })) {
							if(!isPiecePinned(piecePointer, "PY")) {
								drawDotsLine(coords, "PY", 1);
							}
						}
					} catch (Exception e) {

					}

					try {
						// check if there is a piece diagonal
						if (!boardObject.checkSquare(new int[] { coords[0] + 1, coords[1] + 1 }) == true && boardObject
								.getPiece(new int[] { coords[0] + 1, coords[1] + 1 }).getColor().equals("B")) {
							if(!isPiecePinned(piecePointer, "NE")) {
								drawDotToScreen((new int[] { coords[0] + 1, coords[1] + 1 }), "/redDot.png");
							}
							
						}
						if (!boardObject.checkSquare(new int[] { coords[0] - 1, coords[1] + 1 }) == true && boardObject
								.getPiece(new int[] { coords[0] - 1, coords[1] + 1 }).getColor().equals("B")) {
							if(!isPiecePinned(piecePointer, "NW")) {
								drawDotToScreen((new int[] { coords[0] - 1, coords[1] + 1 }), "/redDot.png");
							}
							
						}
					} catch (Exception e) {

					}

					try {
						// check for enPassantCase
						if (boardObject.getPiece(new int[] { coords[0] + 1, coords[1] }) != null) {
							if (boardObject.getPiece(new int[] { coords[0] + 1, coords[1] }).getEnPassantStatus()) {
								
								if(!isPiecePinned(piecePointer, "NE")) {
									drawDotToScreen((new int[] { coords[0] + 1, coords[1] + 1 }), "/redDot.png");
									didEnPassant = true;
								}
								
								
							}
						}
					} catch (Exception e) {

					}
					try {
						if (boardObject.getPiece(new int[] { coords[0] - 1, coords[1] }) != null) {
							if (boardObject.getPiece(new int[] { coords[0] - 1, coords[1] }).getEnPassantStatus()) {
								if(!isPiecePinned(piecePointer, "NW")) {
									drawDotToScreen((new int[] { coords[0] - 1, coords[1] + 1 }), "/redDot.png");
									didEnPassant = true;
								}
							}
						}
					} catch (Exception e) {

					}
				}
				break;
			case ("BP"):
				if (!whiteTurn) {
					try {
						if (coords[1] == 6) {
							if (boardObject.checkSquare(new int[] { coords[0], coords[1] - 2 }) == true
									&& boardObject.checkSquare(new int[] { coords[0], coords[1] - 1 }) == true) {
								// instantiate two dots because pawn is on starting file
								if(!isPiecePinned(piecePointer, "NY")) {
									drawDotsLine(coords, "NY", 2);
								}
							}

						}

						if (boardObject.checkSquare(new int[] { coords[0], coords[1] - 1 }) == true) {
							if(!isPiecePinned(piecePointer, "NY")) {
								drawDotsLine(coords, "NY", 1);
							}
						}
					} catch (Exception e) {

					}

					try {
						// check if there is a piece diagonal
						if (!boardObject.checkSquare(new int[] { coords[0] + 1, coords[1] - 1 }) == true && boardObject
								.getPiece(new int[] { coords[0] + 1, coords[1] - 1 }).getColor().equals("W")) {
							if(!isPiecePinned(piecePointer, "NY")) {
								drawDotToScreen((new int[] { coords[0] + 1, coords[1] - 1 }), "/redDot.png");
							}
							
						}
						if (!boardObject.checkSquare(new int[] { coords[0] - 1, coords[1] - 1 }) == true && boardObject
								.getPiece(new int[] { coords[0] - 1, coords[1] - 1 }).getColor().equals("W")) {
							if(!isPiecePinned(piecePointer, "NY")) {
								drawDotToScreen((new int[] { coords[0] - 1, coords[1] - 1 }), "/redDot.png");
							}
	
						}
					} catch (Exception e) {

					}

					try {
						// check for enPassantCase
						if (boardObject.getPiece(new int[] { coords[0] + 1, coords[1] }) != null) {
							if (boardObject.getPiece(new int[] { coords[0] + 1, coords[1] }).getEnPassantStatus()) {
								if(!isPiecePinned(piecePointer, "NY")) {
									drawDotToScreen((new int[] { coords[0] + 1, coords[1] - 1 }), "/redDot.png");
									didEnPassant = true;
								}
								
							}
						}
					} catch (Exception e) {

					}
					try {
						if (boardObject.getPiece(new int[] { coords[0] - 1, coords[1] }) != null) {
							if (boardObject.getPiece(new int[] { coords[0] - 1, coords[1] }).getEnPassantStatus()) {
								if(!isPiecePinned(piecePointer, "NY")) {
									drawDotToScreen((new int[] { coords[0] - 1, coords[1] - 1 }), "/redDot.png");
									didEnPassant = true;
								}
							}
						}
					} catch (Exception e) {

					}
				}
				break;
			case ("WR"):
				if (whiteTurn) {
					if(!isPiecePinned(piecePointer, "PY")) {
						drawDotsLine(coords, "PY", 8);
					}
					if(!isPiecePinned(piecePointer, "NY")) {
						drawDotsLine(coords, "NY", 8);
					}
					if(!isPiecePinned(piecePointer, "PX")) {
						drawDotsLine(coords, "PX", 8);
					}
					if(!isPiecePinned(piecePointer, "NX")) {
						drawDotsLine(coords, "NX", 8);
					}
		
				}
				break;
			case ("BR"):
				if (!whiteTurn) {
					if(!isPiecePinned(piecePointer, "PY")) {
						drawDotsLine(coords, "PY", 8);
					}
					if(!isPiecePinned(piecePointer, "NY")) {
						drawDotsLine(coords, "NY", 8);
					}
					if(!isPiecePinned(piecePointer, "PX")) {
						drawDotsLine(coords, "PX", 8);
					}
					if(!isPiecePinned(piecePointer, "NX")) {
						drawDotsLine(coords, "NX", 8);
					}
				}
				break;
			case ("WB"):
				if (whiteTurn) {
					if(!isPiecePinned(piecePointer, "NW")) {
						drawDotsLine(coords, "NW", 8);
					}
					if(!isPiecePinned(piecePointer, "NE")) {
						drawDotsLine(coords, "NE", 8);
					}
					if(!isPiecePinned(piecePointer, "SW")) {
						drawDotsLine(coords, "SW", 8);
					}
					if(!isPiecePinned(piecePointer, "SE")) {
						drawDotsLine(coords, "SE", 8);
					}

				}
				break;
			case ("BB"):
				if (!whiteTurn) {
					if(!isPiecePinned(piecePointer, "NW")) {
						drawDotsLine(coords, "NW", 8);
					}
					if(!isPiecePinned(piecePointer, "NE")) {
						drawDotsLine(coords, "NE", 8);
					}
					if(!isPiecePinned(piecePointer, "SW")) {
						drawDotsLine(coords, "SW", 8);
					}
					if(!isPiecePinned(piecePointer, "SE")) {
						drawDotsLine(coords, "SE", 8);
					}
					
				}
				break;

			case ("WKn"):
				if (whiteTurn) {
					boolean knightIsPinned = isPiecePinned(piecePointer, "NW") || isPiecePinned(piecePointer, "SW") || isPiecePinned(piecePointer, "NE") || isPiecePinned(piecePointer, "SE") || isPiecePinned(piecePointer, "PY") || isPiecePinned(piecePointer, "PX") || isPiecePinned(piecePointer, "NX") || isPiecePinned(piecePointer, "NY");
					if(!knightIsPinned) {

						drawDotsSpecial(coords);
					}
					
					
				}
				break;
			case ("BKn"):
				if (!whiteTurn) {
					boolean knightIsPinned = isPiecePinned(piecePointer, "NW") || isPiecePinned(piecePointer, "SW") || isPiecePinned(piecePointer, "NE") || isPiecePinned(piecePointer, "SE") || isPiecePinned(piecePointer, "PY") || isPiecePinned(piecePointer, "PX") || isPiecePinned(piecePointer, "NX") || isPiecePinned(piecePointer, "NY");
					if(!knightIsPinned) {

						drawDotsSpecial(coords);
					}
				}
				break;
			case ("WK"):
				if (whiteTurn) {
					int [] KC = conversionObject.convertNumericalCoordinates(whiteKing.getCoordinate());
					
					//check immediate king movement
					try {
						if(!boardObject.getCoverage(new int[] {KC[0] + 1, KC[1]}).equals("B") && !boardObject.getCoverage(new int[] {KC[0] + 1, KC[1]}).equals("M")) {
							drawDotsLine(KC, "PX", 1);
						}
					} catch(Exception e) {
						
					}
					try {
						if(!boardObject.getCoverage(new int[] {KC[0] + 1, KC[1] + 1}).equals("B") && !boardObject.getCoverage(new int[] {KC[0] + 1, KC[1] + 1}).equals("M")) {
							drawDotsLine(KC, "NE", 1);
						}
					} catch(Exception e) {
						
					}
					try {
						if(!boardObject.getCoverage(new int[] {KC[0], KC[1] + 1}).equals("B") && !boardObject.getCoverage(new int[] {KC[0], KC[1] + 1}).equals("M")) {
							drawDotsLine(KC, "PY", 1);
						}
					} catch(Exception e) {
						
					}
					try {
						if(!boardObject.getCoverage(new int[] {KC[0] - 1, KC[1] + 1}).equals("B") && !boardObject.getCoverage(new int[] {KC[0] - 1, KC[1]  + 1}).equals("M")) {
							drawDotsLine(KC, "NW", 1);
						}
					} catch(Exception e) {
						
					}
					try {
						if(!boardObject.getCoverage(new int[] {KC[0] - 1, KC[1]}).equals("B") && !boardObject.getCoverage(new int[] {KC[0] - 1, KC[1]}).equals("M")) {
							drawDotsLine(KC, "NX", 1);
						}
					} catch(Exception e) {
						
					}
					try {
						if(!boardObject.getCoverage(new int[] {KC[0] - 1, KC[1] - 1}).equals("B") && !boardObject.getCoverage(new int[] {KC[0] - 1, KC[1] - 1}).equals("M")) {
							drawDotsLine(KC, "SW", 1);
						}
					} catch(Exception e) {
						
					}
					try {
						if(!boardObject.getCoverage(new int[] {KC[0], KC[1] - 1}).equals("B") && !boardObject.getCoverage(new int[] {KC[0], KC[1] - 1}).equals("M")) {
							drawDotsLine(KC, "NY", 1);
						}
					} catch(Exception e) {
						
					}
					try {
						if(!boardObject.getCoverage(new int[] {KC[0] + 1, KC[1] - 1}).equals("B") && !boardObject.getCoverage(new int[] {KC[0] + 1, KC[1] - 1}).equals("M")) {
							drawDotsLine(KC, "SE", 1);
						}
					} catch(Exception e) {
						
					}
					
					try {
						//piece can castle
						if(piecePointer.getShortCastleStatus()) {
							//no blocking pieces short side
							if(boardObject.checkSquare(new int[] {5, 0}) && boardObject.checkSquare(new int[] {6, 0}) && boardObject.getPiece(new int[] {7, 0}).getPieceType().equals("WR")) {
								drawDotToScreen(new int[] {6, 0}, "/dot.png");
								possibleCastle = true;
							}
						}
					} catch(Exception e) {
					
					}
					try {
						if(piecePointer.getLongCastleStatus()) {
							//no blocking pieces long side
							if(boardObject.checkSquare(new int[] {3, 0}) && boardObject.checkSquare(new int[] {2, 0}) && boardObject.checkSquare(new int[] {1, 0}) && boardObject.getPiece(new int[] {0, 0}).getPieceType().equals("WR")) {
								drawDotToScreen(new int[] {2, 0}, "/dot.png");
								possibleCastle = true;
						}
						
						}
					} catch(Exception e) {
						
					}	
					
					
				}
				break;
			case ("BK"):
				if (!whiteTurn) {
					int [] KC = conversionObject.convertNumericalCoordinates(blackKing.getCoordinate());
					
					//check immediate king movement
					try {
						if(!boardObject.getCoverage(new int[] {KC[0] + 1, KC[1]}).equals("W") && !boardObject.getCoverage(new int[] {KC[0] + 1, KC[1]}).equals("M")) {
							drawDotsLine(KC, "PX", 1);
						}
					} catch(Exception e) {
						
					}
					try {
						if(!boardObject.getCoverage(new int[] {KC[0] + 1, KC[1] + 1}).equals("W") && !boardObject.getCoverage(new int[] {KC[0] + 1, KC[1] + 1}).equals("M")) {
							drawDotsLine(KC, "NE", 1);
						}
					} catch(Exception e) {
						
					}
					try {
						if(!boardObject.getCoverage(new int[] {KC[0], KC[1] + 1}).equals("W") && !boardObject.getCoverage(new int[] {KC[0], KC[1] + 1}).equals("M")) {
							drawDotsLine(KC, "PY", 1);
						}
					} catch(Exception e) {
						
					}
					try {
						if(!boardObject.getCoverage(new int[] {KC[0] - 1, KC[1] + 1}).equals("W") && !boardObject.getCoverage(new int[] {KC[0] - 1, KC[1] + 1}).equals("M")) {
							drawDotsLine(KC, "NW", 1);
						}
					} catch(Exception e) {
						
					}
					try {
						if(!boardObject.getCoverage(new int[] {KC[0] - 1, KC[1]}).equals("W") && !boardObject.getCoverage(new int[] {KC[0] - 1, KC[1]}).equals("M")) {
							drawDotsLine(KC, "NX", 1);
						}
					} catch(Exception e) {
						
					}
					try {
						if(!boardObject.getCoverage(new int[] {KC[0] - 1, KC[1] - 1}).equals("W") && !boardObject.getCoverage(new int[] {KC[0] - 1, KC[1] - 1}).equals("M")) {
							drawDotsLine(KC, "SW", 1);
						}
					} catch(Exception e) {
						
					}
					try {
						if(!boardObject.getCoverage(new int[] {KC[0], KC[1] - 1}).equals("W") && !boardObject.getCoverage(new int[] {KC[0], KC[1] - 1}).equals("M")) {
							drawDotsLine(KC, "NY", 1);
						}
					} catch(Exception e) {
						
					}
					try {
						if(!boardObject.getCoverage(new int[] {KC[0] + 1, KC[1] - 1}).equals("W") && !boardObject.getCoverage(new int[] {KC[0] + 1, KC[1] - 1}).equals("M")) {
							drawDotsLine(KC, "SE", 1);
						}
					} catch(Exception e) {
						
					}
					
					try {
						//piece can castle
						if(piecePointer.getShortCastleStatus()) {
							//no blocking pieces short side
							if(boardObject.checkSquare(new int[] {5, 7}) && boardObject.checkSquare(new int[] {6, 7}) && boardObject.getPiece(new int[] {7, 7}).getPieceType().equals("BR")) {
								drawDotToScreen(new int[] {6, 7}, "/dot.png");
								possibleCastle = true;
							}
						}
					} catch(Exception e) {
						
					}
					try {
						if(piecePointer.getLongCastleStatus()) {
							//no blocking pieces long side
							if(boardObject.checkSquare(new int[] {3, 7}) && boardObject.checkSquare(new int[] {2, 7}) && boardObject.checkSquare(new int[] {1, 7}) && boardObject.getPiece(new int[] {0, 7}).getPieceType().equals("BR")) {
								drawDotToScreen(new int[] {2, 7}, "/dot.png");
								possibleCastle = true;
							}
						}
					} catch(Exception e) {
						
					}
					
					
				}
				break;
			case ("WQ"):
				if (whiteTurn) {
					
					if(!isPiecePinned(piecePointer, "NW")) {
						drawDotsLine(coords, "NW", 8);
					}
					if(!isPiecePinned(piecePointer, "NE")) {
						drawDotsLine(coords, "NE", 8);
					}
					if(!isPiecePinned(piecePointer, "SW")) {
						drawDotsLine(coords, "SW", 8);
					}
					if(!isPiecePinned(piecePointer, "SE")) {
						drawDotsLine(coords, "SE", 8);
					}
					if(!isPiecePinned(piecePointer, "PY")) {
						drawDotsLine(coords, "PY", 8);
					}
					if(!isPiecePinned(piecePointer, "NY")) {
						drawDotsLine(coords, "NY", 8);
					}
					if(!isPiecePinned(piecePointer, "PX")) {
						drawDotsLine(coords, "PX", 8);
					}
					if(!isPiecePinned(piecePointer, "NX")) {
						drawDotsLine(coords, "NX", 8);
					}
					
				}
				break;
			case ("BQ"):
				if (!whiteTurn) {
					if(!isPiecePinned(piecePointer, "NW")) {
						drawDotsLine(coords, "NW", 8);
					}
					if(!isPiecePinned(piecePointer, "NE")) {
						drawDotsLine(coords, "NE", 8);
					}
					if(!isPiecePinned(piecePointer, "SW")) {
						drawDotsLine(coords, "SW", 8);
					}
					if(!isPiecePinned(piecePointer, "SE")) {
						drawDotsLine(coords, "SE", 8);
					}
					if(!isPiecePinned(piecePointer, "PY")) {
						drawDotsLine(coords, "PY", 8);
					}
					if(!isPiecePinned(piecePointer, "NY")) {
						drawDotsLine(coords, "NY", 8);
					}
					if(!isPiecePinned(piecePointer, "PX")) {
						drawDotsLine(coords, "PX", 8);
					}
					if(!isPiecePinned(piecePointer, "NX")) {
						drawDotsLine(coords, "NX", 8);
					}
				}
				break;
			}
			
		}

		
	}

	/**
	 * Draws dots on the screen to show the player where they can move
	 * 
	 * @param numericalCoords The current coordinates of the piece selected
	 * @param direction       The direction the dots will spawn. P = Positive, N =
	 *                        Negative, Y and X are the coordinates. Diagonal
	 *                        Directions as follows. NE, SE, SW, NW
	 * @param amount          How many dots to spawn in that direction
	 */
	public void drawDotsLine(int[] numericalCoords, String direction, int amount) {

		int[] tempCoords = numericalCoords.clone();

		try {

			for (int i = 1; i <= amount; i++) {

				if (direction.equals("PY")) {

					tempCoords[1] += 1;

				} else if (direction.equals("NY")) {

					tempCoords[1] -= 1;

				} else if (direction.equals("PX")) {

					tempCoords[0] += 1;

				} else if (direction.equals("NX")) {

					tempCoords[0] -= 1;

				} else if (direction.equals("NE")) {

					tempCoords[0] += 1;
					tempCoords[1] += 1;

				} else if (direction.equals("SE")) {

					tempCoords[0] += 1;
					tempCoords[1] -= 1;

				} else if (direction.equals("SW")) {

					tempCoords[0] -= 1;
					tempCoords[1] -= 1;

				} else if (direction.equals("NW")) {

					tempCoords[0] -= 1;
					tempCoords[1] += 1;

				}

				// check if piece occupies coordinates
				// if it does, will not draw dots
				if (boardObject.getPiece(tempCoords) != null) {
					if (!boardObject.getPiece(tempCoords).getColor().equals(piecePointer.getColor())) {
						drawDotToScreen(tempCoords, "/redDot.png");
					}
					return;
				}
				
				drawDotToScreen(tempCoords, "/dot.png");

			}

		} catch (Exception e) {
			
		}

	}

	/**
	 * Draws dots for the knight piece
	 * 
	 * @param numericalCoords
	 */
	public void drawDotsSpecial(int[] numericalCoords) {
		//black knight temp coords are being updated wrong
		int[] tempCoords = numericalCoords.clone();

		try {
			tempCoords[0] += 1;
			tempCoords[1] += 2;

			if (boardObject.getPiece(tempCoords) != null) {
				if (!boardObject.getPiece(tempCoords).getColor().equals(piecePointer.getColor())) {
					drawDotToScreen(tempCoords, "/redDot.png");
				}
			} else {
				drawDotToScreen(tempCoords, "/dot.png");
			}

		} catch (Exception e) {

		}

		try {
			tempCoords[0] += 1;
			tempCoords[1] -= 1;

			if (boardObject.getPiece(tempCoords) != null) {
				if (!boardObject.getPiece(tempCoords).getColor().equals(piecePointer.getColor())) {
					drawDotToScreen(tempCoords, "/redDot.png");
				}
			} else {
				drawDotToScreen(tempCoords, "/dot.png");
			}

		} catch (Exception e) {

		}

		try {
			tempCoords[0] += 0;
			tempCoords[1] -= 2;

			if (boardObject.getPiece(tempCoords) != null) {
				if (!boardObject.getPiece(tempCoords).getColor().equals(piecePointer.getColor())) {
					drawDotToScreen(tempCoords, "/redDot.png");
				}
			} else {
				drawDotToScreen(tempCoords, "/dot.png");
			}

		} catch (Exception e) {

		}
		try {
			tempCoords[0] -= 1;
			tempCoords[1] -= 1;

			if (boardObject.getPiece(tempCoords) != null) {
				if (!boardObject.getPiece(tempCoords).getColor().equals(piecePointer.getColor())) {
					drawDotToScreen(tempCoords, "/redDot.png");
				}
			} else {
				drawDotToScreen(tempCoords, "/dot.png");
			}

		} catch (Exception e) {

		}
		try {
			tempCoords[0] -= 2;
			tempCoords[1] -= 0;

			if (boardObject.getPiece(tempCoords) != null) {
				if (!boardObject.getPiece(tempCoords).getColor().equals(piecePointer.getColor())) {
					drawDotToScreen(tempCoords, "/redDot.png");
				}
			} else {
				drawDotToScreen(tempCoords, "/dot.png");
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			tempCoords[0] -= 1;
			tempCoords[1] += 1;

			if (boardObject.getPiece(tempCoords) != null) {
				if (!boardObject.getPiece(tempCoords).getColor().equals(piecePointer.getColor())) {
					drawDotToScreen(tempCoords, "/redDot.png");
				}
			} else {
				drawDotToScreen(tempCoords, "/dot.png");
			}

		} catch (Exception e) {

		}
		try {
			tempCoords[0] -= 0;
			tempCoords[1] += 2;

			if (boardObject.getPiece(tempCoords) != null) {
				if (!boardObject.getPiece(tempCoords).getColor().equals(piecePointer.getColor())) {
					drawDotToScreen(tempCoords, "/redDot.png");
				}
			} else {
				drawDotToScreen(tempCoords, "/dot.png");
			}

		} catch (Exception e) {

		}
		try {
			tempCoords[0] += 1;
			tempCoords[1] += 1;

			if (boardObject.getPiece(tempCoords) != null) {
				if (!boardObject.getPiece(tempCoords).getColor().equals(piecePointer.getColor())) {
					drawDotToScreen(tempCoords, "/redDot.png");
				}
			} else {
				drawDotToScreen(tempCoords, "/dot.png");
			}

		} catch (Exception e) {
			
		}

	}

	/**Function that checks for stalemate
	 * @return true if in a stalemate, false if not
	 */
	public boolean checkStaleMate() {
		if(whiteTurn) {
			if(canKingMove(whiteKing)) {
				return false;
			}
			for(int x = 0; x < 8; x++) {
				for(int y = 0; y < 8; y++) {
					int square[] = {x, y};
					boolean pieceExists = !boardObject.checkSquare(square);
					if(pieceExists) {
						//check if it is a white piece
						boolean pieceColor = boardObject.getPiece(square).getColor().equals("W");
						if(pieceColor) {
							Piece pieceToCheck = boardObject.getPiece(square);
							//every piece by default can move freely except pawns
							if(pieceToCheck.getPieceType().substring(1).equals("P")) {
								//check if the pawn can move
								if(canPawnMove(pieceToCheck)){
									return false;
								}
							} else if(pieceToCheck.getPieceType().substring(1).equals("K")) {
								//do nothing bc we dont care
							} else {
								//other pieces exist so stalemate isn't possible
								return false;
							}
						}
					}
				}
			}
		} else {
			if(canKingMove(blackKing)) {
				return false;
			}
			for(int x = 0; x < 8; x++) {
				for(int y = 0; y < 8; y++) {
					int square[] = {x, y};
					boolean pieceExists = !boardObject.checkSquare(square);
					if(pieceExists) {
						//check if it is a black piece
						boolean pieceColor = boardObject.getPiece(square).getColor().equals("B");
						if(pieceColor) {
							Piece pieceToCheck = boardObject.getPiece(square);	
							//every piece by default can move freely except pawns
							if(pieceToCheck.getPieceType().substring(1).equals("P")) {
								//check if the pawn can move
								if(canPawnMove(pieceToCheck)) {
									return false;
								}
							} else if(pieceToCheck.getPieceType().substring(1).equals("K")) {
								//do nothing bc we dont care
							} else {
								//other pieces exist so stalemate isn't possible
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	private boolean canKingMove(Piece king) {
		int [] KC = conversionObject.convertNumericalCoordinates(king.getCoordinate());
		String color = king.getColor();		
		int checkingSquare[] = {KC[0] + 1, KC[1] + 1};
		
		try {
			checkingSquare = new int[]{KC[0] + 1, KC[1] + 1};
			if(boardObject.getCoverage(checkingSquare).equals(color)) {
				return true;
			}
		} catch(Exception e) {
			
		}
		try {
			checkingSquare = new int[]{KC[0] + 1, KC[1]};
			if(boardObject.getCoverage(checkingSquare).equals(color)) {
				return true;
			}
		} catch(Exception e) {
			
		}
		try {
			checkingSquare = new int[]{KC[0] + 1, KC[1] - 1};
			if(boardObject.getCoverage(checkingSquare).equals(color)) {
				return true;
			}
		} catch(Exception e) {
			
		}
		try {
			checkingSquare = new int[]{KC[0], KC[1] - 1};
			if(boardObject.getCoverage(checkingSquare).equals(color)) {
				return true;
			}
		} catch(Exception e) {
			
		}
		try {
			checkingSquare = new int[]{KC[0] - 1, KC[1] - 1};
			if(boardObject.getCoverage(checkingSquare).equals(color)) {
				return true;
			}
		} catch(Exception e) {
			
		}
		try {
			checkingSquare = new int[]{KC[0] - 1, KC[1]};
			if(boardObject.getCoverage(checkingSquare).equals(color)) {
				return true;
			}
		} catch(Exception e) {
			
		}
		try {
			checkingSquare = new int[]{KC[0] - 1, KC[1] + 1};
			if(boardObject.getCoverage(checkingSquare).equals(color)) {
				return true;
			}
		} catch(Exception e) {
			
		}
		try {
			checkingSquare = new int[]{KC[0], KC[1] + 1};
			if(boardObject.getCoverage(checkingSquare).equals(color)) {
				return true;
			}
		} catch(Exception e) {
			
		}
		
		return false;
	}

	private boolean canPawnMove(Piece pieceToCheck) {
		//as long as one square ahead is clear, the pawn can move
		boolean isWhite = pieceToCheck.getColor().equals("W");
		if(isWhite) {
			int coords[] = conversionObject.convertNumericalCoordinates(pieceToCheck.getCoordinate());
			int squareToCheck[] = {coords[0], coords[1] + 1};
			boolean freeSquare = boardObject.checkSquare(squareToCheck);
			if(freeSquare) {
				return true;
			}
			
			try {
				//check if pawn can attack
				squareToCheck = new int[] {coords[0] + 1, coords[1] + 1};
				boolean occupiedSquare = !boardObject.checkSquare(squareToCheck);
				if(occupiedSquare) {
					return true;
				}
			} catch(Exception e) {
				
			}
			try {
				//check if pawn can attack
				squareToCheck = new int[] {coords[0] - 1, coords[1] + 1};
				boolean occupiedSquare = !boardObject.checkSquare(squareToCheck);
				if(occupiedSquare) {
					return true;
				}
			} catch(Exception e) {
				
			}
			
		} else {
			int coords[] = conversionObject.convertNumericalCoordinates(pieceToCheck.getCoordinate());
			int squareToCheck[] = {coords[0], coords[1] - 1};
			boolean freeSquare = boardObject.checkSquare(squareToCheck);
			if(freeSquare) {
				return true;
			}
			
			try {
				//check if pawn can attack
				squareToCheck = new int[] {coords[0] + 1, coords[1] - 1};
				boolean occupiedSquare = !boardObject.checkSquare(squareToCheck);
				if(occupiedSquare) {
					return true;
				}
			} catch(Exception e) {
				
			}
			try {
				//check if pawn can attack
				squareToCheck = new int[] {coords[0] - 1, coords[1] - 1};
				boolean occupiedSquare = !boardObject.checkSquare(squareToCheck);
				if(occupiedSquare) {
					return true;
				}
			} catch(Exception e) {
				
			}
		}
		return false;
	}

	/**
	 * Class that draws the dots to the screen and also deals with some logic of where pieces can move
	 * 
	 * @param tempCoords the temporary coords of the piece to move
	 * @param imageSrc the imageSrc of the piece
	 */
	public void drawDotToScreen(int[] tempCoords, String imageSrc) {
		
		int pixelCoords[] = tempCoords.clone();
		pixelCoords = conversionObject.getPixelCoordinates(pixelCoords);

		// creates new dot
		JLabel thisPiece = new JLabel("");
		dotArr[arrayCount % 32] = new Dot();
		dotArr[arrayCount % 32].setLabel(thisPiece);
		dotArr[arrayCount % 32].setCoordinates(tempCoords);
		arrayCount++;

		thisPiece.setBounds(pixelCoords[0] + 15, pixelCoords[1] + 15, 50, 50); // 15 is calculated from (original pixel
																				// size - new pixel size) / 2

		// sets new piece image
		ImageIcon thisPieceImage = new ImageIcon(this.getClass().getResource(imageSrc));
		Image image = thisPieceImage.getImage(); // transform it
		Image newimg = image.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
		thisPieceImage = new ImageIcon(newimg); // transform it back

		// add new piece
		thisPiece.setIcon(thisPieceImage);
		frame.getContentPane().add(thisPiece);

		// Set the z-order of thisPiece to bring it to the front
		frame.getContentPane().setComponentZOrder(thisPiece, 0);

		// Repaint the content pane to make the change in z-order immediately visible
		frame.getContentPane().repaint();

		thisPiece.addMouseListener(new MouseAdapter() {
			int count = arrayCount - 1;

			public void mouseClicked(MouseEvent e) {

				int[] coordinates = dotArr[count].getCoordinates();

				Piece clickedPiece = boardObject.getPiece(lastClickedCoords);
				lastClickedPiece = clickedPiece;

				int[] pixelCoordinates = conversionObject.getPixelCoordinates(coordinates.clone());

				JLabel thisPieceJLabel = clickedPiece.getPieceJLabel();
				thisPieceJLabel.setBounds(pixelCoordinates[0], pixelCoordinates[1], 80, 80);
				
				try {
					
					if (boardObject.getPiece(coordinates) != null
							&& !boardObject.getPiece(coordinates).getColor().equals(piecePointer.getColor())) {
						boardObject.destroyPiece(coordinates);
					}
				} catch(Exception es) {
					
				}

				if (clickedPiece.getColor().equals("W")) {
					whiteTurn = false;
				} else {
					whiteTurn = true;
				}

				// En Passant special cases
				if (lastMovedPiece != null) {
					lastMovedPiece.setEnPassantStatus(false);
				}
				if ((clickedPiece.getPieceType().equals("WP") || clickedPiece.getPieceType().equals("BP"))
						&& Math.abs(coordinates[1] - lastClickedCoords[1]) == 2) {
					piecePointer.setEnPassantStatus(true);
				}

				// did en passant move
				if (didEnPassant && Math.abs(coordinates[0] - lastClickedCoords[0]) == 1) {
					int pieceCoordinates[] = { coordinates[0], lastClickedCoords[1] };
					boardObject.destroyPiece(pieceCoordinates);
				}
				
				
				
				//disable castle
				if(clickedPiece.getPieceType().equals("WK") || clickedPiece.getPieceType().equals("BK")) {
					clickedPiece.setLongCastleStatus(false);
					clickedPiece.setShortCastleStatus(false);
				}
				if(clickedPiece.getPieceType().equals("WR")) {
					String coordinate = clickedPiece.getCoordinate();
					int coords[] = conversionObject.convertNumericalCoordinates(coordinate);
					if(coords[0] == 7 && coords[1] == 0) {
						whiteKing.setShortCastleStatus(false);
					} if(coords[0] == 0 && coords[1] == 0) {
						whiteKing.setLongCastleStatus(false);
					}
				}
				if(clickedPiece.getPieceType().equals("BR")) {
					String coordinate = clickedPiece.getCoordinate();
					int coords[] = conversionObject.convertNumericalCoordinates(coordinate);
					if(coords[0] == 7 && coords[1] == 7) {
						blackKing.setShortCastleStatus(false);
					} 
					if(coords[0] == 0 && coords[1] == 7) {
						blackKing.setLongCastleStatus(false);
					}
				}
				
				//castle move white short
				if(possibleCastle && coordinates[0] == 6 && coordinates[1] == 0) {
					Piece whiteRook = boardObject.getPiece(new int[] {7, 0});
					boardObject.movePiece(whiteRook, "50");
				}
				//castle move white long
				if(possibleCastle && coordinates[0] == 2 && coordinates[1] == 0) {
					Piece whiteRook = boardObject.getPiece(new int[] {0, 0});
					boardObject.movePiece(whiteRook, "30");
				}
				//castle move black short
				if(possibleCastle && coordinates[0] == 6 && coordinates[1] == 7) {
					Piece whiteRook = boardObject.getPiece(new int[] {7, 7});
					boardObject.movePiece(whiteRook, "57");
				}
				//castle move black long
				if(possibleCastle && coordinates[0] == 2 && coordinates[1] == 7) {
					Piece whiteRook = boardObject.getPiece(new int[] {0, 7});
					boardObject.movePiece(whiteRook, "37");
				}
				
				//move any piece not covered by rules above
				boardObject.movePiece(clickedPiece, "" + coordinates[0] + coordinates[1]);
				
				//pawn promotion
				if(clickedPiece.getPieceType().equals("WP") && clickedPiece.getCoordinate().substring(1).equals("8")) {
					
					// Create a popup JFrame for the white pawn
				    popupFrame = new JFrame("White Pawn Promotion");
				    popupFrame.setSize(400, 100);
				    popupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				    popupFrame.setLocationRelativeTo(null);

				    JPanel outerPanel = new JPanel();
				    outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS)); // Use BoxLayout with Y_AXIS


				    JPanel innerPanel = new JPanel();
				    innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS)); // Use BoxLayout with X_AXIS

				    JButton queenButton = new JButton("Queen");
				    JButton bishopButton = new JButton("Bishop");
				    JButton rookButton = new JButton("Rook");
				    JButton knightButton = new JButton("Knight");
				    
				    queenButton.setActionCommand("Queen");
				    bishopButton.setActionCommand("Bishop");
				    rookButton.setActionCommand("Rook");
				    knightButton.setActionCommand("Knight");
				    
				    queenButton.addActionListener(buttonListener);
				    bishopButton.addActionListener(buttonListener);
				    rookButton.addActionListener(buttonListener);
				    knightButton.addActionListener(buttonListener);

				    // Add buttons for promoting to queen, rook, bishop, or knight to the inner panel
				    innerPanel.add(queenButton);
				    innerPanel.add(bishopButton);
				    innerPanel.add(rookButton);
				    innerPanel.add(knightButton);

				    // Add the label and the inner panel to the outer panel
				    outerPanel.add(innerPanel);

				    popupFrame.add(outerPanel);
				    popupFrame.setVisible(true);
				    
				    
				    
				}
				if(clickedPiece.getPieceType().equals("BP") && clickedPiece.getCoordinate().substring(1).equals("1")) {
					
					// Create a popup JFrame for the black pawn
				    popupFrame = new JFrame("Black Pawn Promotion");
				    popupFrame.setSize(400, 100);
				    popupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				    popupFrame.setLocationRelativeTo(null);

				    JPanel outerPanel = new JPanel();
				    outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS)); // Use BoxLayout with Y_AXIS


				    JPanel innerPanel = new JPanel();
				    innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS)); // Use BoxLayout with X_AXIS

				    JButton queenButton = new JButton("Queen");
				    JButton bishopButton = new JButton("Bishop");
				    JButton rookButton = new JButton("Rook");
				    JButton knightButton = new JButton("Knight");
				    
				    queenButton.setActionCommand("Queen");
				    bishopButton.setActionCommand("Bishop");
				    rookButton.setActionCommand("Rook");
				    knightButton.setActionCommand("Knight");
				    
				    queenButton.addActionListener(buttonListener);
				    bishopButton.addActionListener(buttonListener);
				    rookButton.addActionListener(buttonListener);
				    knightButton.addActionListener(buttonListener);

				    // Add buttons for promoting to queen, rook, bishop, or knight to the inner panel
				    innerPanel.add(queenButton);
				    innerPanel.add(bishopButton);
				    innerPanel.add(rookButton);
				    innerPanel.add(knightButton);

				    // Add the label and the inner panel to the outer panel
				    outerPanel.add(innerPanel);

				    popupFrame.add(outerPanel);
				    popupFrame.setVisible(true);
				    
				}
				
				//king checking rules
				int[] whiteKingCoordinates = conversionObject.convertNumericalCoordinates(whiteKing.getCoordinate());
				int[] blackKingCoordinates = conversionObject.convertNumericalCoordinates(blackKing.getCoordinate());
				
				String whiteCoverage = boardObject.getCoverage(whiteKingCoordinates);
				String blackCoverage = boardObject.getCoverage(blackKingCoordinates);
				isWhiteInCheck = false;
				isBlackInCheck = false;
				
				if(whiteCoverage != null) {
					if(whiteCoverage.equals("B") || whiteCoverage.equals("M")) {
						isWhiteInCheck = true;
						updatePossibleCheckMoves(whiteKing);
						if(possibleCheckMoves.isEmpty()) {
							System.out.println("CHECKMATE, BLACK WINS");
							return;
						}
						
						
					}
				}
				if(blackCoverage != null) {
					if(blackCoverage.equals("W") || blackCoverage.equals("M")) {
						isBlackInCheck = true;
						updatePossibleCheckMoves(blackKing);
						if(possibleCheckMoves.isEmpty()) {
							System.out.println("CHECKMATE, WHITE WINS");
							return;
						}
					}
				}
				
				if(checkStaleMate()) {
					System.out.println("STALEMATE");
					return;
				}
			
				

				clearDots(frame);
				lastMovedPiece = piecePointer;
				possibleCastle = false;

			}

		});

	}
	
	/**
	 * button listener for promoting pawns
	 */
	ActionListener buttonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

        	String command = e.getActionCommand();
     
        	JLabel pieceLabel = lastClickedPiece.getPieceJLabel();
        	String color = lastClickedPiece.getColor();
        	
        	
            
            if ("Queen".equals(command)) {
            	
            	if(color.equals("W")) {
            		ImageIcon thisPieceImage = new ImageIcon(this.getClass().getResource("/whiteQueen.png"));
            		Image image = thisPieceImage.getImage(); // transform it 
            		Image newimg = image.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
            		thisPieceImage = new ImageIcon(newimg);  // transform it back
            		
            		//set piece properties
            		lastClickedPiece.setImgSrc("/whiteQueen");
            		lastClickedPiece.setPieceType("WQ");
            		
            		pieceLabel.setIcon(thisPieceImage);
            		
            	} else {
            		ImageIcon thisPieceImage = new ImageIcon(this.getClass().getResource("/blackQueen.png"));
            		Image image = thisPieceImage.getImage(); // transform it 
            		Image newimg = image.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
            		thisPieceImage = new ImageIcon(newimg);  // transform it back
            		
            		//set piece properties
            		lastClickedPiece.setImgSrc("/blackQueen");
            		lastClickedPiece.setPieceType("BQ");
            		
            		pieceLabel.setIcon(thisPieceImage);
            	}
            	
            	
                
            } else if ("Bishop".equals(command)) {
            	
            	if(color.equals("W")) {
            		ImageIcon thisPieceImage = new ImageIcon(this.getClass().getResource("/whiteBishop.png"));
            		Image image = thisPieceImage.getImage(); // transform it 
            		Image newimg = image.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
            		thisPieceImage = new ImageIcon(newimg);  // transform it back
            		
            		//set piece properties
            		lastClickedPiece.setImgSrc("/whiteBishop");
            		lastClickedPiece.setPieceType("WB");
            		
            		pieceLabel.setIcon(thisPieceImage);
            		
            	} else {
            		ImageIcon thisPieceImage = new ImageIcon(this.getClass().getResource("/blackBishop.png"));
            		Image image = thisPieceImage.getImage(); // transform it 
            		Image newimg = image.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
            		thisPieceImage = new ImageIcon(newimg);  // transform it back
            		
            		//set piece properties
            		lastClickedPiece.setImgSrc("/blackBishop");
            		lastClickedPiece.setPieceType("BB");
            		
            		pieceLabel.setIcon(thisPieceImage);
            	}
                
            } else if ("Knight".equals(command)) {
            	
            	if(color.equals("W")) {
            		ImageIcon thisPieceImage = new ImageIcon(this.getClass().getResource("/whiteKnight.png"));
            		Image image = thisPieceImage.getImage(); // transform it 
            		Image newimg = image.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
            		thisPieceImage = new ImageIcon(newimg);  // transform it back
            		
            		//set piece properties
            		lastClickedPiece.setImgSrc("/whiteKnight");
            		lastClickedPiece.setPieceType("WKn");
            		
            		pieceLabel.setIcon(thisPieceImage);
            		
            	} else {
            		ImageIcon thisPieceImage = new ImageIcon(this.getClass().getResource("/blackKnight.png"));
            		Image image = thisPieceImage.getImage(); // transform it 
            		Image newimg = image.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
            		thisPieceImage = new ImageIcon(newimg);  // transform it back
            		
            		//set piece properties
            		lastClickedPiece.setImgSrc("/blackKnight");
            		lastClickedPiece.setPieceType("BKn");
            		
            		pieceLabel.setIcon(thisPieceImage);
            	}
                
            } else if ("Rook".equals(command)) {
            	
            	if(color.equals("W")) {
            		ImageIcon thisPieceImage = new ImageIcon(this.getClass().getResource("/whiteRook.png"));
            		Image image = thisPieceImage.getImage(); // transform it 
            		Image newimg = image.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
            		thisPieceImage = new ImageIcon(newimg);  // transform it back
            		
            		//set piece properties
            		lastClickedPiece.setImgSrc("/whiteRook");
            		lastClickedPiece.setPieceType("WR");
            		
            		pieceLabel.setIcon(thisPieceImage);
            		
            	} else {
            		ImageIcon thisPieceImage = new ImageIcon(this.getClass().getResource("/blackRook.png"));
            		Image image = thisPieceImage.getImage(); // transform it 
            		Image newimg = image.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
            		thisPieceImage = new ImageIcon(newimg);  // transform it back
            		
            		//set piece properties
            		lastClickedPiece.setImgSrc("/blackRook");
            		lastClickedPiece.setPieceType("BR");
            		
            		pieceLabel.setIcon(thisPieceImage);
            	}
                
            }
            
            boardObject.setPieceCoverageMap();
            popupFrame.dispose();
        	
        }
    };

	public void clearDots(JFrame frame) {
		for (int i = 0; i < dotArr.length; i++) {
			try {
				dotArr[i].clearLabel(frame);
			} catch (Exception e) {
				// typically if the dots havent been instantiated yet
			}
			arrayCount = 0;
		}
	}
	
	

}
