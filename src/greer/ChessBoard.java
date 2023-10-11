package greer;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * A class that keeps track of the chess board and the pieces
 * 
 * @author Hunter
 *
 */
public class ChessBoard {
	
	GameLogic logicObject;
	JFrame frame;
	CoordinatesConversion conversionObject = new CoordinatesConversion();
	
	//only two possible pieces can check the king at a time
	private Piece [] checkingPieces = {null, null};
	
	private Piece[][] boardStatus = new Piece[8][8];
	//map that contains what squares cover what. B = black, W = white, M = mixed
	private String[][] pieceCoverageMap = new String[8][8];
	private Piece blackKing, whiteKing;
	
	public ChessBoard() {
		logicObject = new GameLogic(this);
	}
	
	public void setKings(Piece wk, Piece bk) {
		blackKing = bk;
		whiteKing = wk;
		logicObject.setKings(wk, bk);
	}
	
	public Piece getPiece(int[] coordinates) {
		return boardStatus[coordinates[0]][coordinates[1]];
	}
	
	public void destroyPiece(int[] coordinates) {
		Piece pieceToDestroy = boardStatus[coordinates[0]][coordinates[1]];
		boardStatus[coordinates[0]][coordinates[1]] = null;
		frame.remove(pieceToDestroy.getPieceJLabel());
		frame.validate();
        frame.repaint();
	}
	
	/**
	 * 
	 * @param coordinate
	 * @return A String indicating what coverage there is on a square
	 */
	public String getCoverage(int [] coordinate) {
		return pieceCoverageMap[coordinate[0]][coordinate[1]];
	}
	
	public Piece[] getCheckingPieces() {
		return checkingPieces;
	}
	public void setPieceCoverageMap() {
		pieceCoverageMap = updateCoverageMap(boardStatus);
	}
	
	public Piece[][] getBoardStatus() {
	    Piece[][] clonedBoard = new Piece[8][8];
	    for (int i = 0; i < 8; i++) {
	        for (int j = 0; j < 8; j++) {
	            if (boardStatus[i][j] != null) {
	                clonedBoard[i][j] = boardStatus[i][j]; // Perform a deep clone of the Piece object.
	            }
	        }
	    }
	    return clonedBoard;
	}

	
	/**
	 * A function that sets the checking variables and returns a coverage map
	 * @param boardStatusPieces A boardStatus which provides the potential for checking future moves to see how the coverage map reacts
	 * @return a coverage map
	 */
	public String[][] updateCoverageMap(Piece boardStatusPieces[][]) {
		String[][] coverageMap = new String[8][8];
		checkingPieces = new Piece[] {null, null};
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				
				Piece pieceToCheck = boardStatusPieces[x][y];
				
				if(pieceToCheck != null) {
					//check every piece type and add their coverage to the map
					if(pieceToCheck.getColor().equals("W")) {
						
						//check pieces and update map to white
						if(pieceToCheck.getPieceType().substring(1).equals("P")) { //the piece that is being checked is a pawn
							try {
								if(coverageMap[x+1][y+1] == null) {
									coverageMap[x+1][y+1] = "W";
								} else if(coverageMap[x+1][y+1].equals("B")) {
									coverageMap[x+1][y+1] = "M";
								}
								boolean squareEmpty = boardStatusPieces[x+1][y+1] == null;
								if(!squareEmpty) {
									boolean isKing = boardStatusPieces[x+1][y+1].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by Pawn");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
									}
								}
								
								
							} catch(Exception e) {
								System.out.println(e);
							}
							try {
								if(coverageMap[x-1][y+1] == null) {
									coverageMap[x-1][y+1] = "W";
								} else if(coverageMap[x-1][y+1].equals("B")) {
									coverageMap[x-1][y+1] = "M";
								}
								boolean squareEmpty = boardStatusPieces[x-1][y+1] == null;
								if(!squareEmpty) {
									boolean isKing = boardStatusPieces[x-1][y+1].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by Pawn");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
									}
								}
								
							} catch(Exception e) {
								System.out.println(e);
							}
						} else if(pieceToCheck.getPieceType().substring(1).equals("R")){ //the piece it is checking is a rook
							//check pos x
							for(int i = x + 1; i < 8; i++) {
								boolean squareEmpty = boardStatusPieces[i][y] == null;
								if(squareEmpty) {
									if(coverageMap[i][y] == null) {
										coverageMap[i][y] = "W";
									} else if(coverageMap[i][y] == "B") {
										coverageMap[i][y] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][y].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by Rook");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[i][y] == null) {
											coverageMap[i][y] = "W";
										} else if(coverageMap[i][y] == "B") {
											coverageMap[i][y] = "M";
										} 
									} else {
										if(coverageMap[i][y] == null) {
											coverageMap[i][y] = "W";
										} else if(coverageMap[i][y] == "B") {
											coverageMap[i][y] = "M";
										} 
										break;
									}
									
								}
							}
							//check neg x
							for(int i = x - 1; i >= 0; i--) {
								boolean squareEmpty = boardStatusPieces[i][y] == null;
								if(squareEmpty) {
									if(coverageMap[i][y] == null) {
										coverageMap[i][y] = "W";
									} else if(coverageMap[i][y] == "B") {
										coverageMap[i][y] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][y].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by Rook");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[i][y] == null) {
											coverageMap[i][y] = "W";
										} else if(coverageMap[i][y] == "B") {
											coverageMap[i][y] = "M";
										} 
									} else {
										if(coverageMap[i][y] == null) {
											coverageMap[i][y] = "W";
										} else if(coverageMap[i][y] == "B") {
											coverageMap[i][y] = "M";
										} 
										break;
									}
									
								}						
							}
							//check pos y
							for(int i = y + 1; i < 8; i++) {
								boolean squareEmpty = boardStatusPieces[x][i] == null;
								if(squareEmpty) {
									if(coverageMap[x][i] == null) {
										coverageMap[x][i] = "W";
									} else if(coverageMap[x][i] == "B") {
										coverageMap[x][i] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[x][i].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by Rook");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[x][i] == null) {
											coverageMap[x][i] = "W";
										} else if(coverageMap[x][i] == "B") {
											coverageMap[x][i] = "M";
										} 
									} else {
										if(coverageMap[x][i] == null) {
											coverageMap[x][i] = "W";
										} else if(coverageMap[x][i] == "B") {
											coverageMap[x][i] = "M";
										} 
										break;
									}
									
								}
							}
							//check neg y
							for(int i = y- 1; i >= 0; i--) {
								
								boolean squareEmpty = boardStatusPieces[x][i] == null;
								if(squareEmpty) {
									if(coverageMap[x][i] == null) {
										coverageMap[x][i] = "W";
									} else if(coverageMap[x][i] == "B") {
										coverageMap[x][i] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[x][i].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by Rook");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[x][i] == null) {
											coverageMap[x][i] = "W";
										} else if(coverageMap[x][i] == "B") {
											coverageMap[x][i] = "M";
										} 
									} else {
										if(coverageMap[x][i] == null) {
											coverageMap[x][i] = "W";
										} else if(coverageMap[x][i] == "B") {
											coverageMap[x][i] = "M";
										} 
										break;
									}
									
								}						
							}
						} else if(pieceToCheck.getPieceType().substring(1).equals("Kn")) {
							//8 cases for the 8 moves
							//spot 1
							try {
								if(coverageMap[x + 1][y + 2] == null) {
									coverageMap[x + 1][y + 2] = "W";
								} else if(coverageMap[x + 1][y + 2] == "B") {
									coverageMap[x + 1][y + 2] = "M";
								}
								boolean squareEmpty = boardStatusPieces[x+1][y+2] == null;
								if(!squareEmpty) {
									boolean isKing = boardStatusPieces[x+1][y+2].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by Knight");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
									}
								}
							} catch (Exception e) {
								
							}
							//spot 2
							try {
								if(coverageMap[x + 2][y + 1] == null) {
									coverageMap[x + 2][y + 1] = "W";
								} else if(coverageMap[x + 2][y + 1] == "B") {
									coverageMap[x + 2][y + 1] = "M";
								}
								boolean squareEmpty = boardStatusPieces[x+2][y+1] == null;
								if(!squareEmpty) {
									boolean isKing = boardStatusPieces[x+2][y+1].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by Knight");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
									}
								}
							} catch (Exception e) {
								
							}
							//spot 3
							try {
								if(coverageMap[x + 2][y - 1] == null) {
									coverageMap[x + 2][y - 1] = "W";
								} else if(coverageMap[x + 2][y - 1] == "B") {
									coverageMap[x + 2][y - 1] = "M";
								}
								boolean squareEmpty = boardStatusPieces[x+2][y-1] == null;
								if(!squareEmpty) {
									boolean isKing = boardStatusPieces[x+2][y-1].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by Knight");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
									}
								}
							} catch (Exception e) {
								
							}
							//spot 4
							try {
								if(coverageMap[x + 1][y - 2] == null) {
									coverageMap[x + 1][y - 2] = "W";
								} else if(coverageMap[x + 1][y - 2] == "B") {
									coverageMap[x + 1][y - 2] = "M";
								}
								boolean squareEmpty = boardStatusPieces[x+1][y-2] == null;
								if(!squareEmpty) {
									boolean isKing = boardStatusPieces[x+1][y-2].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by Knight");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
									}
								}
							} catch (Exception e) {
								
							}
							//spot 5
							try {
								if(coverageMap[x - 1][y - 2] == null) {
									coverageMap[x - 1][y - 2] = "W";
								} else if(coverageMap[x - 1][y - 2] == "B") {
									coverageMap[x - 1][y - 2] = "M";
								}
								boolean squareEmpty = boardStatusPieces[x-1][y-2] == null;
								if(!squareEmpty) {
									boolean isKing = boardStatusPieces[x-1][y-2].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by Knight");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
									}
								}
							} catch (Exception e) {
								
							}
							//spot 6
							try {
								if(coverageMap[x - 2][y - 1] == null) {
									coverageMap[x - 2][y - 1] = "W";
								} else if(coverageMap[x - 2][y - 1] == "B") {
									coverageMap[x - 2][y - 1] = "M";
								}
								boolean squareEmpty = boardStatusPieces[x-2][y-1] == null;
								if(!squareEmpty) {
									boolean isKing = boardStatusPieces[x-2][y-1].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by Knight");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
									}
								}
							} catch (Exception e) {
								
							}
							//spot 7
							try {
								if(coverageMap[x - 2][y + 1] == null) {
									coverageMap[x - 2][y + 1] = "W";
								} else if(coverageMap[x - 2][y + 1] == "B") {
									coverageMap[x - 2][y + 1] = "M";
								}
								boolean squareEmpty = boardStatusPieces[x-2][y+1] == null;
								if(!squareEmpty) {
									boolean isKing = boardStatusPieces[x-2][y+1].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by Knight");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
									}
								}
							} catch (Exception e) {
								
							}
							//spot 8
							try {
								if(coverageMap[x - 1][y + 2] == null) {
									coverageMap[x - 1][y + 2] = "W";
								} else if(coverageMap[x - 1][y + 2] == "B") {
									coverageMap[x - 1][y + 2] = "M";
								}
								boolean squareEmpty = boardStatusPieces[x-1][y+2] == null;
								if(!squareEmpty) {
									boolean isKing = boardStatusPieces[x-1][y+2].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by Knight");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
									}
								}
							} catch (Exception e) {
								
							}
							
						} else if(pieceToCheck.getPieceType().substring(1).equals("B")) {
							//pos x, pos y
							for(int i = x + 1, j = y + 1; i < 8 && j < 8; i++, j++) {
								boolean squareEmpty = boardStatusPieces[i][j] == null;
								if(squareEmpty) {
									if(coverageMap[i][j] == null) {
										coverageMap[i][j] = "W";
									} else if(coverageMap[i][j] == "B") {
										coverageMap[i][j] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][j].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by Bishop");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "W";
										} else if(coverageMap[i][j] == "B") {
											coverageMap[i][j] = "M";
										} 
									} else {
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "W";
										} else if(coverageMap[i][j] == "B") {
											coverageMap[i][j] = "M";
										} 
										break;
									}
									
								}
							}
							
							//pos x, neg y
							for(int i = x + 1, j = y - 1; i < 8 && j >= 0; i++, j--) {
								boolean squareEmpty = boardStatusPieces[i][j] == null;
								if(squareEmpty) {
									if(coverageMap[i][j] == null) {
										coverageMap[i][j] = "W";
									} else if(coverageMap[i][j] == "B") {
										coverageMap[i][j] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][j].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by Bishop");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "W";
										} else if(coverageMap[i][j] == "B") {
											coverageMap[i][j] = "M";
										}
									} else {
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "W";
										} else if(coverageMap[i][j] == "B") {
											coverageMap[i][j] = "M";
										} 
										break;
									}
									
								}
							}
							
							//neg x, neg y
							for(int i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j--) {
								boolean squareEmpty = boardStatusPieces[i][j] == null;
								if(squareEmpty) {
									if(coverageMap[i][j] == null) {
										coverageMap[i][j] = "W";
									} else if(coverageMap[i][j] == "B") {
										coverageMap[i][j] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][j].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by Bishop");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "W";
										} else if(coverageMap[i][j] == "B") {
											coverageMap[i][j] = "M";
										} 
									} else {
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "W";
										} else if(coverageMap[i][j] == "B") {
											coverageMap[i][j] = "M";
										} 
										break;
									}
									
								}
							}
							
							//neg x, pos y
							for(int i = x - 1, j = y + 1; i >= 0 && j < 8; i--, j++) {
								boolean squareEmpty = boardStatusPieces[i][j] == null;
								if(squareEmpty) {
									if(coverageMap[i][j] == null) {
										coverageMap[i][j] = "W";
									} else if(coverageMap[i][j] == "B") {
										coverageMap[i][j] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][j].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by Bishop");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "W";
										} else if(coverageMap[i][j] == "B") {
											coverageMap[i][j] = "M";
										}
									} else {
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "W";
										} else if(coverageMap[i][j] == "B") {
											coverageMap[i][j] = "M";
										} 
										break;
									}
									
								}
							}
						} else if(pieceToCheck.getPieceType().substring(1).equals("Q")) {
							//diagonals
							//pos x, pos y
							for(int i = x + 1, j = y + 1; i < 8 && j < 8; i++, j++) {
								
								boolean squareEmpty = boardStatusPieces[i][j] == null;
								if(squareEmpty) {
									
									if(coverageMap[i][j] == null) {
										coverageMap[i][j] = "W";
									} else if(coverageMap[i][j] == "B") {
										coverageMap[i][j] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][j].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by queen");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "W";
										} else if(coverageMap[i][j] == "B") {
											coverageMap[i][j] = "M";
										} 
									} else {
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "W";
										} else if(coverageMap[i][j] == "B") {
											coverageMap[i][j] = "M";
										} 
										break;
									}
									
								}
							}
							
							//pos x, neg y
							for(int i = x + 1, j = y - 1; i < 8 && j >= 0; i++, j--) {
								
								boolean squareEmpty = boardStatusPieces[i][j] == null;
								if(squareEmpty) {
									
									if(coverageMap[i][j] == null) {
										coverageMap[i][j] = "W";
									} else if(coverageMap[i][j] == "B") {
										coverageMap[i][j] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][j].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by queen");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "W";
										} else if(coverageMap[i][j] == "B") {
											coverageMap[i][j] = "M";
										} 
									} else {
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "W";
										} else if(coverageMap[i][j] == "B") {
											coverageMap[i][j] = "M";
										} 
										break;
									}
									
								}
							}
							
							//neg x, neg y
							for(int i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j--) {
								
								boolean squareEmpty = boardStatusPieces[i][j] == null;
								if(squareEmpty) {
									
									if(coverageMap[i][j] == null) {
										coverageMap[i][j] = "W";
									} else if(coverageMap[i][j] == "B") {
										coverageMap[i][j] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][j].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by queen");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "W";
										} else if(coverageMap[i][j] == "B") {
											coverageMap[i][j] = "M";
										} 
									} else {
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "W";
										} else if(coverageMap[i][j] == "B") {
											coverageMap[i][j] = "M";
										} 
										break;
									}
									
								}
							}
							
							//neg x, pos y
							for(int i = x - 1, j = y + 1; i >= 0 && j < 8; i--, j++) {
								
								boolean squareEmpty = boardStatusPieces[i][j] == null;
								if(squareEmpty) {
									
									if(coverageMap[i][j] == null) {
										coverageMap[i][j] = "W";
									} else if(coverageMap[i][j] == "B") {
										coverageMap[i][j] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][j].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by queen");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "W";
										} else if(coverageMap[i][j] == "B") {
											coverageMap[i][j] = "M";
										} 
									} else {
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "W";
										} else if(coverageMap[i][j] == "B") {
											coverageMap[i][j] = "M";
										} 
										break;
									}
									
								}
							}
							
							//straights
							//check pos x
							for(int i = x + 1; i < 8; i++) {
								
								boolean squareEmpty = boardStatusPieces[i][y] == null;
								if(squareEmpty) {
									
									if(coverageMap[i][y] == null) {
										coverageMap[i][y] = "W";
									} else if(coverageMap[i][y] == "B") {
										coverageMap[i][y] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][y].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by queen");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[i][y] == null) {
											coverageMap[i][y] = "W";
										} else if(coverageMap[i][y] == "B") {
											coverageMap[i][y] = "M";
										} 
									} else {
										if(coverageMap[i][y] == null) {
											coverageMap[i][y] = "W";
										} else if(coverageMap[i][y] == "B") {
											coverageMap[i][y] = "M";
										} 
										break;
									}
									
								}
							}
							//check neg x
							for(int i = x - 1; i >= 0; i--) {
								
								boolean squareEmpty = boardStatusPieces[i][y] == null;
								if(squareEmpty) {
									
									if(coverageMap[i][y] == null) {
										coverageMap[i][y] = "W";
									} else if(coverageMap[i][y] == "B") {
										coverageMap[i][y] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][y].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by queen");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[i][y] == null) {
											coverageMap[i][y] = "W";
										} else if(coverageMap[i][y] == "B") {
											coverageMap[i][y] = "M";
										} 
									} else {
										if(coverageMap[i][y] == null) {
											coverageMap[i][y] = "W";
										} else if(coverageMap[i][y] == "B") {
											coverageMap[i][y] = "M";
										} 
										break;
									}
									
								}						
							}
							//check pos y
							for(int i = y + 1; i < 8; i++) {
								
								boolean squareEmpty = boardStatusPieces[x][i] == null;
								if(squareEmpty) {
									
									if(coverageMap[x][i] == null) {
										coverageMap[x][i] = "W";
									} else if(coverageMap[x][i] == "B") {
										coverageMap[x][i] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[x][i].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by queen");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[x][i] == null) {
											coverageMap[x][i] = "W";
										} else if(coverageMap[x][i] == "B") {
											coverageMap[x][i] = "M";
										} 
									} else {
										if(coverageMap[x][i] == null) {
											coverageMap[x][i] = "W";
										} else if(coverageMap[x][i] == "B") {
											coverageMap[x][i] = "M";
										} 
										break;
									}
									
								}
							}
							//check neg y
							for(int i = y- 1; i >= 0; i--) {
								
								boolean squareEmpty = boardStatusPieces[x][i] == null;
								if(squareEmpty) {
									
									if(coverageMap[x][i] == null) {
										coverageMap[x][i] = "W";
									} else if(coverageMap[x][i] == "B") {
										coverageMap[x][i] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[x][i].equals(blackKing);
									if(isKing) {
										System.out.println("Black checked by queen");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[x][i] == null) {
											coverageMap[x][i] = "W";
										} else if(coverageMap[x][i] == "B") {
											coverageMap[x][i] = "M";
										} 
									} else {
										if(coverageMap[x][i] == null) {
											coverageMap[x][i] = "W";
										} else if(coverageMap[x][i] == "B") {
											coverageMap[x][i] = "M";
										} 
										break;
									}
									
								}						
							}
						} else if(pieceToCheck.getPieceType().substring(1).equals("K")) {
							//8 squares around a king
							try {
								//square 1
								if(coverageMap[x + 1][y + 1] == null) {
									coverageMap[x + 1][y + 1] = "W";
								} else if(coverageMap[x + 1][y + 1] == "B") {
									coverageMap[x + 1][y + 1] = "M";
								}
							} catch(Exception e) {
								
							}
							try {
								//square 2
								if(coverageMap[x + 1][y] == null) {
									coverageMap[x + 1][y] = "W";
								} else if(coverageMap[x + 1][y] == "B") {
									coverageMap[x + 1][y] = "M";
								}
							} catch(Exception e) {
								
							}
							try {
								//square 3
								if(coverageMap[x + 1][y - 1] == null) {
									coverageMap[x + 1][y - 1] = "W";
								} else if(coverageMap[x + 1][y - 1] == "B") {
									coverageMap[x + 1][y - 1] = "M";
								}
							} catch(Exception e) {
								
							}
							try {
								//square 4
								if(coverageMap[x][y - 1] == null) {
									coverageMap[x][y - 1] = "W";
								} else if(coverageMap[x][y - 1] == "B") {
									coverageMap[x][y - 1] = "M";
								}
							} catch(Exception e) {
								
							}
							try {
								//square 5
								if(coverageMap[x - 1][y - 1] == null) {
									coverageMap[x - 1][y - 1] = "W";
								} else if(coverageMap[x - 1][y - 1] == "B") {
									coverageMap[x - 1][y - 1] = "M";
								}
							} catch(Exception e) {
								
							}
							try {
								//square 6
								if(coverageMap[x - 1][y] == null) {
									coverageMap[x - 1][y] = "W";
								} else if(coverageMap[x - 1][y] == "B") {
									coverageMap[x - 1][y] = "M";
								}
							} catch(Exception e) {
								
							}
							try {
								//square 7
								if(coverageMap[x - 1][y + 1] == null) {
									coverageMap[x - 1][y + 1] = "W";
								} else if(coverageMap[x - 1][y + 1] == "B") {
									coverageMap[x - 1][y + 1] = "M";
								}
							} catch(Exception e) {
								
							}
							try {
								//square 8
								if(coverageMap[x][y + 1] == null) {
									coverageMap[x][y + 1] = "W";
								} else if(coverageMap[x][y + 1] == "B") {
									coverageMap[x][y + 1] = "M";
								}
							} catch(Exception e) {
								
							}
						}
						
					//piece is black
					} else {
						//check pieces and update map to black
						if(pieceToCheck.getPieceType().substring(1).equals("P")) { //the piece that is being checked is a pawn
							try {
								if(coverageMap[x+1][y-1] == null) {
									coverageMap[x+1][y-1] = "B";
								} else if(coverageMap[x+1][y-1].equals("W")) {
									coverageMap[x+1][y-1] = "M";
								}
								boolean squareEmpty = boardStatusPieces[x+1][y-1] == null;
								if(!squareEmpty) {
									boolean isKing = boardStatusPieces[x+1][y-1].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Pawn");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
									}
								}
								
							} catch(Exception e) {
								System.out.println(e);
							}
							try {
								if(coverageMap[x-1][y-1] == null) {
									coverageMap[x-1][y-1] = "B";
								} else if(coverageMap[x-1][y-1].equals("W")) {
									coverageMap[x-1][y-1] = "M";
								}
								boolean squareEmpty = boardStatusPieces[x-1][y-1] == null;
								if(!squareEmpty) {
									boolean isKing = boardStatusPieces[x-1][y-1].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Pawn");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
									}
								}
								
							} catch(Exception e) {
								System.out.println(e);
							}
						} else if(pieceToCheck.getPieceType().substring(1).equals("R")){ //the piece it is checking is a rook
							//check pos x
							for(int i = x + 1; i < 8; i++) {
								boolean squareEmpty = boardStatusPieces[i][y] == null;
								if(squareEmpty) {
									if(coverageMap[i][y] == null) {
										coverageMap[i][y] = "B";
									} else if(coverageMap[i][y] == "W") {
										coverageMap[i][y] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][y].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Rook");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[i][y] == null) {
											coverageMap[i][y] = "B";
										} else if(coverageMap[i][y] == "W") {
											coverageMap[i][y] = "M";
										}
									} else {
										if(coverageMap[i][y] == null) {
											coverageMap[i][y] = "B";
										} else if(coverageMap[i][y] == "W") {
											coverageMap[i][y] = "M";
										} 
										break;
									}
									
								}
							}
							//check neg x
							for(int i = x - 1; i >= 0; i--) {
								boolean squareEmpty = boardStatusPieces[i][y] == null;
								if(squareEmpty) {
									if(coverageMap[i][y] == null) {
										coverageMap[i][y] = "B";
									} else if(coverageMap[i][y] == "W") {
										coverageMap[i][y] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][y].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Rook");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[i][y] == null) {
											coverageMap[i][y] = "B";
										} else if(coverageMap[i][y] == "W") {
											coverageMap[i][y] = "M";
										} 
									} else {
										if(coverageMap[i][y] == null) {
											coverageMap[i][y] = "B";
										} else if(coverageMap[i][y] == "W") {
											coverageMap[i][y] = "M";
										} 
										break;
									}
									
								}						
							}
							//check pos y
							for(int i = y + 1; i < 8; i++) {
								boolean squareEmpty = boardStatusPieces[x][i] == null;
								if(squareEmpty) {
									if(coverageMap[x][i] == null) {
										coverageMap[x][i] = "B";
									} else if(coverageMap[x][i] == "W") {
										coverageMap[x][i] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[x][i].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Rook");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[x][i] == null) {
											coverageMap[x][i] = "B";
										} else if(coverageMap[x][i] == "W") {
											coverageMap[x][i] = "M";
										}
									} else {
										if(coverageMap[x][i] == null) {
											coverageMap[x][i] = "B";
										} else if(coverageMap[x][i] == "W") {
											coverageMap[x][i] = "M";
										} 
										break;
									}
									
								}
							}
							//check neg y
							for(int i = y- 1; i >= 0; i--) {
								boolean squareEmpty = boardStatusPieces[x][i] == null;
								if(squareEmpty) {
									if(coverageMap[x][i] == null) {
										coverageMap[x][i] = "B";
									} else if(coverageMap[x][i] == "W") {
										coverageMap[x][i] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[x][i].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Rook");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[x][i] == null) {
											coverageMap[x][i] = "B";
										} else if(coverageMap[x][i] == "W") {
											coverageMap[x][i] = "M";
										} 
									}else {
										if(coverageMap[x][i] == null) {
											coverageMap[x][i] = "B";
										} else if(coverageMap[x][i] == "W") {
											coverageMap[x][i] = "M";
										} 
										break;
									}
								}						
							}
						} else if(pieceToCheck.getPieceType().substring(1).equals("Kn")) {
							//8 cases for the 8 moves
							//spot 1
							try {
								if(coverageMap[x + 1][y + 2] == null) {
									coverageMap[x + 1][y + 2] = "B";
								} else if(coverageMap[x + 1][y + 2] == "W") {
									coverageMap[x + 1][y + 2] = "M";
								}
								boolean squareEmpty = boardStatusPieces[x+1][y+2] == null;
								if(!squareEmpty) {
									boolean isKing = boardStatusPieces[x+1][y+2].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Knight");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
									}
								}
							} catch (Exception e) {
								
							}
							//spot 2
							try {
								if(coverageMap[x + 2][y + 1] == null) {
									coverageMap[x + 2][y + 1] = "B";
								} else if(coverageMap[x + 2][y + 1] == "W") {
									coverageMap[x + 2][y + 1] = "M";
								}
								boolean squareEmpty = boardStatusPieces[x+2][y+1] == null;
								if(!squareEmpty) {
									boolean isKing = boardStatusPieces[x+2][y+1].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Knight");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
									}
								}
							} catch (Exception e) {
								
							}
							//spot 3
							try {
								if(coverageMap[x + 2][y - 1] == null) {
									coverageMap[x + 2][y - 1] = "B";
								} else if(coverageMap[x + 2][y - 1] == "W") {
									coverageMap[x + 2][y - 1] = "M";
								}
								boolean squareEmpty = boardStatusPieces[x+2][y-1] == null;
								if(!squareEmpty) {
									boolean isKing = boardStatusPieces[x+2][y-1].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Knight");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
									}
								}
							} catch (Exception e) {
								
							}
							//spot 4
							try {
								if(coverageMap[x + 1][y - 2] == null) {
									coverageMap[x + 1][y - 2] = "B";
								} else if(coverageMap[x + 1][y - 2] == "W") {
									coverageMap[x + 1][y - 2] = "M";
								}
								boolean squareEmpty = boardStatusPieces[x+1][y-2] == null;
								if(!squareEmpty) {
									boolean isKing = boardStatusPieces[x+1][y-2].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Knight");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
									}
								}
							} catch (Exception e) {
								
							}
							//spot 5
							try {
								if(coverageMap[x - 1][y - 2] == null) {
									coverageMap[x - 1][y - 2] = "B";
								} else if(coverageMap[x - 1][y - 2] == "W") {
									coverageMap[x - 1][y - 2] = "M";
								}
								boolean squareEmpty = boardStatusPieces[x-1][y-2] == null;
								if(!squareEmpty) {
									boolean isKing = boardStatusPieces[x-1][y-2].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Knight");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
									}
								}
							} catch (Exception e) {
								
							}
							//spot 6
							try {
								if(coverageMap[x - 2][y - 1] == null) {
									coverageMap[x - 2][y - 1] = "B";
								} else if(coverageMap[x - 2][y - 1] == "W") {
									coverageMap[x - 2][y - 1] = "M";
								}
								boolean squareEmpty = boardStatusPieces[x-2][y-1] == null;
								if(!squareEmpty) {
									boolean isKing = boardStatusPieces[x-2][y-1].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Knight");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
									}
								}
							} catch (Exception e) {
								
							}
							//spot 7
							try {
								if(coverageMap[x - 2][y + 1] == null) {
									coverageMap[x - 2][y + 1] = "B";
								} else if(coverageMap[x - 2][y + 1] == "W") {
									coverageMap[x - 2][y + 1] = "M";
								}
								boolean squareEmpty = boardStatusPieces[x-2][y+1] == null;
								if(!squareEmpty) {
									boolean isKing = boardStatusPieces[x-2][y+1].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Knight");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
									}
								}
							} catch (Exception e) {
								
							}
							//spot 8
							try {
								if(coverageMap[x - 1][y + 2] == null) {
									coverageMap[x - 1][y + 2] = "B";
								} else if(coverageMap[x - 1][y + 2] == "W") {
									coverageMap[x - 1][y + 2] = "M";
								}
								boolean squareEmpty = boardStatusPieces[x-1][y+2] == null;
								if(!squareEmpty) {
									boolean isKing = boardStatusPieces[x-1][y+2].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Knight");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
									}
								}
							} catch (Exception e) {
								
							}
						} else if(pieceToCheck.getPieceType().substring(1).equals("B")) {
							//pos x, pos y
							for(int i = x + 1, j = y + 1; i < 8 && j < 8; i++, j++) {
								boolean squareEmpty = boardStatusPieces[i][j] == null;
								if(squareEmpty) {
									if(coverageMap[i][j] == null) {
										coverageMap[i][j] = "B";
									} else if(coverageMap[i][j] == "W") {
										coverageMap[i][j] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][j].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Bishop");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "B";
										} else if(coverageMap[i][j] == "W") {
											coverageMap[i][j] = "M";
										}
									} else {
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "B";
										} else if(coverageMap[i][j] == "W") {
											coverageMap[i][j] = "M";
										} 
										break;
									}
									
								}
							}
							
							//pos x, neg y
							for(int i = x + 1, j = y - 1; i < 8 && j >= 0; i++, j--) {
								boolean squareEmpty = boardStatusPieces[i][j] == null;
								if(squareEmpty) {
									if(coverageMap[i][j] == null) {
										coverageMap[i][j] = "B";
									} else if(coverageMap[i][j] == "W") {
										coverageMap[i][j] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][j].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Bishop");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
									} else {
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "B";
										} else if(coverageMap[i][j] == "W") {
											coverageMap[i][j] = "M";
										} 
										break;
									}
									
								}
							}
							
							//neg x, neg y
							for(int i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j--) {
								boolean squareEmpty = boardStatusPieces[i][j] == null;
								if(squareEmpty) {
									if(coverageMap[i][j] == null) {
										coverageMap[i][j] = "B";
									} else if(coverageMap[i][j] == "W") {
										coverageMap[i][j] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][j].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Bishop");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "B";
										} else if(coverageMap[i][j] == "W") {
											coverageMap[i][j] = "M";
										}
									} else {
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "B";
										} else if(coverageMap[i][j] == "W") {
											coverageMap[i][j] = "M";
										} 
										break;
									}
									
								}
							}
							
							//neg x, pos y
							for(int i = x - 1, j = y + 1; i >= 0 && j < 8; i--, j++) {
								boolean squareEmpty = boardStatusPieces[i][j] == null;
								if(squareEmpty) {
									if(coverageMap[i][j] == null) {
										coverageMap[i][j] = "W";
									} else if(coverageMap[i][j] == "B") {
										coverageMap[i][j] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][j].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Bishop");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "W";
										} else if(coverageMap[i][j] == "B") {
											coverageMap[i][j] = "M";
										} 
									} else {
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "W";
										} else if(coverageMap[i][j] == "B") {
											coverageMap[i][j] = "M";
										} 
										break;
									}
									
								}
							}
						} else if(pieceToCheck.getPieceType().substring(1).equals("Q")) {
							//diagonals
							//pos x, pos y
							for(int i = x + 1, j = y + 1; i < 8 && j < 8; i++, j++) {
								boolean squareEmpty = boardStatusPieces[i][j] == null;
								if(squareEmpty) {
									if(coverageMap[i][j] == null) {
										coverageMap[i][j] = "B";
									} else if(coverageMap[i][j] == "W") {
										coverageMap[i][j] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][j].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Queen");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "B";
										} else if(coverageMap[i][j] == "W") {
											coverageMap[i][j] = "M";
										}
									} else {
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "B";
										} else if(coverageMap[i][j] == "W") {
											coverageMap[i][j] = "M";
										} 
										break;
									}
									
								}
							}
							
							//pos x, neg y
							for(int i = x + 1, j = y - 1; i < 8 && j >= 0; i++, j--) {
								boolean squareEmpty = boardStatusPieces[i][j] == null;
								if(squareEmpty) {
									if(coverageMap[i][j] == null) {
										coverageMap[i][j] = "B";
									} else if(coverageMap[i][j] == "W") {
										coverageMap[i][j] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][j].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Queen");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "B";
										} else if(coverageMap[i][j] == "W") {
											coverageMap[i][j] = "M";
										} 
									} else {
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "B";
										} else if(coverageMap[i][j] == "W") {
											coverageMap[i][j] = "M";
										} 
										break;
									}
									
								}
							}
							
							//neg x, neg y
							for(int i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j--) {
								boolean squareEmpty = boardStatusPieces[i][j] == null;
								if(squareEmpty) {
									if(coverageMap[i][j] == null) {
										coverageMap[i][j] = "B";
									} else if(coverageMap[i][j] == "W") {
										coverageMap[i][j] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][j].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Queen");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "B";
										} else if(coverageMap[i][j] == "W") {
											coverageMap[i][j] = "M";
										} 
									} else {
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "B";
										} else if(coverageMap[i][j] == "W") {
											coverageMap[i][j] = "M";
										} 
										break;
									}
									
								}
							}
							
							//neg x, pos y
							for(int i = x - 1, j = y + 1; i >= 0 && j < 8; i--, j++) {
								boolean squareEmpty = boardStatusPieces[i][j] == null;
								if(squareEmpty) {
									if(coverageMap[i][j] == null) {
										coverageMap[i][j] = "B";
									} else if(coverageMap[i][j] == "W") {
										coverageMap[i][j] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][j].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Queen");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "B";
										} else if(coverageMap[i][j] == "W") {
											coverageMap[i][j] = "M";
										} 
									} else {
										if(coverageMap[i][j] == null) {
											coverageMap[i][j] = "B";
										} else if(coverageMap[i][j] == "W") {
											coverageMap[i][j] = "M";
										} 
										break;
									}
									}
									
							}
							
							//straights
							//check pos x
							for(int i = x + 1; i < 8; i++) {
								boolean squareEmpty = boardStatusPieces[i][y] == null;
								if(squareEmpty) {
									if(coverageMap[i][y] == null) {
										coverageMap[i][y] = "B";
									} else if(coverageMap[i][y] == "W") {
										coverageMap[i][y] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][y].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Queen");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[i][y] == null) {
											coverageMap[i][y] = "B";
										} else if(coverageMap[i][y] == "W") {
											coverageMap[i][y] = "M";
										}
									} else {
										if(coverageMap[i][y] == null) {
											coverageMap[i][y] = "B";
										} else if(coverageMap[i][y] == "W") {
											coverageMap[i][y] = "M";
										} 
										break;
									}
									
								}
							}
							//check neg x
							for(int i = x - 1; i >= 0; i--) {
								boolean squareEmpty = boardStatusPieces[i][y] == null;
								if(squareEmpty) {
									if(coverageMap[i][y] == null) {
										coverageMap[i][y] = "B";
									} else if(coverageMap[i][y] == "W") {
										coverageMap[i][y] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[i][y].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Queen");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[i][y] == null) {
											coverageMap[i][y] = "B";
										} else if(coverageMap[i][y] == "W") {
											coverageMap[i][y] = "M";
										} 
									} else {
										if(coverageMap[i][y] == null) {
											coverageMap[i][y] = "B";
										} else if(coverageMap[i][y] == "W") {
											coverageMap[i][y] = "M";
										} 
										break;
									}
									
								}						
							}
							//check pos y
							for(int i = y + 1; i < 8; i++) {
								boolean squareEmpty = boardStatusPieces[x][i] == null;
								if(squareEmpty) {
									if(coverageMap[x][i] == null) {
										coverageMap[x][i] = "B";
									} else if(coverageMap[x][i] == "W") {
										coverageMap[x][i] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[x][i].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Queen");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[x][i] == null) {
											coverageMap[x][i] = "B";
										} else if(coverageMap[x][i] == "W") {
											coverageMap[x][i] = "M";
										} 
									} else {
										if(coverageMap[x][i] == null) {
											coverageMap[x][i] = "B";
										} else if(coverageMap[x][i] == "W") {
											coverageMap[x][i] = "M";
										} 
										break;
									}
									
								}
							}
							//check neg y
							for(int i = y- 1; i >= 0; i--) {
								boolean squareEmpty = boardStatusPieces[x][i] == null;
								if(squareEmpty) {
									if(coverageMap[x][i] == null) {
										coverageMap[x][i] = "B";
									} else if(coverageMap[x][i] == "W") {
										coverageMap[x][i] = "M";
									} 
								} else {
									boolean isKing = boardStatusPieces[x][i].equals(whiteKing);
									if(isKing) {
										System.out.println("White checked by Queen");
										if(checkingPieces[0] == null) {
											checkingPieces[0] = pieceToCheck;
										} else {
											checkingPieces[1] = pieceToCheck;
										}
										if(coverageMap[x][i] == null) {
											coverageMap[x][i] = "B";
										} else if(coverageMap[x][i] == "W") {
											coverageMap[x][i] = "M";
										} 
									} else {
										if(coverageMap[x][i] == null) {
											coverageMap[x][i] = "B";
										} else if(coverageMap[x][i] == "W") {
											coverageMap[x][i] = "M";
										} 
										break;
									}
									
								}						
							}
						} else if(pieceToCheck.getPieceType().substring(1).equals("K")) {
							//8 squares around a king
							try {
								//square 1
								if(coverageMap[x + 1][y + 1] == null) {
									coverageMap[x + 1][y + 1] = "B";
								} else if(coverageMap[x + 1][y + 1] == "W") {
									coverageMap[x + 1][y + 1] = "M";
								}
							} catch(Exception e) {
								
							}
							try {
								//square 2
								if(coverageMap[x + 1][y] == null) {
									coverageMap[x + 1][y] = "B";
								} else if(coverageMap[x + 1][y] == "W") {
									coverageMap[x + 1][y] = "M";
								}
							} catch(Exception e) {
								
							}
							try {
								//square 3
								if(coverageMap[x + 1][y - 1] == null) {
									coverageMap[x + 1][y - 1] = "B";
								} else if(coverageMap[x + 1][y - 1] == "W") {
									coverageMap[x + 1][y - 1] = "M";
								}
							} catch(Exception e) {
								
							}
							try {
								//square 4
								if(coverageMap[x][y - 1] == null) {
									coverageMap[x][y - 1] = "B";
								} else if(coverageMap[x][y - 1] == "W") {
									coverageMap[x][y - 1] = "M";
								}
							} catch(Exception e) {
								
							}
							try {
								//square 5
								if(coverageMap[x - 1][y - 1] == null) {
									coverageMap[x - 1][y - 1] = "B";
								} else if(coverageMap[x - 1][y - 1] == "W") {
									coverageMap[x - 1][y - 1] = "M";
								}
							} catch(Exception e) {
								
							}
							try {
								//square 6
								if(coverageMap[x - 1][y] == null) {
									coverageMap[x - 1][y] = "B";
								} else if(coverageMap[x - 1][y] == "W") {
									coverageMap[x - 1][y] = "M";
								}
							} catch(Exception e) {
								
							}
							try {
								//square 7
								if(coverageMap[x - 1][y + 1] == null) {
									coverageMap[x - 1][y + 1] = "B";
								} else if(coverageMap[x - 1][y + 1] == "W") {
									coverageMap[x - 1][y + 1] = "M";
								}
							} catch(Exception e) {
								
							}
							try {
								//square 8
								if(coverageMap[x][y + 1] == null) {
									coverageMap[x][y + 1] = "B";
								} else if(coverageMap[x][y + 1] == "W") {
									coverageMap[x][y + 1] = "M";
								}
							} catch(Exception e) {
								
							}
							
						}
					}
				}
			}
		}
		return coverageMap;
	}
	
	/**
	 * 
	 * @param numericalCoordinates the numerical coordinates of the square to check
	 * @return true if the square is empty, false if it is taken
	 */
	public boolean checkSquare(int []numericalCoordinates) {
		return (boardStatus[numericalCoordinates[0]][numericalCoordinates[1]] == null);
	}
	
	
	/**
	 * A function that moves the piece on a given square to another square.
	 * 
	 * THIS CODE DOES NOT DEAL WITH TAKING PIECES, ONLY RELOCATING THEM
	 * THIS CODE DOES NOT CURRENTLY DEAL WITH UPDATING THE GRAPHICS
	 * 
	 * @param pieceToMove A pointer to the piece that is getting moved
	 * @param endingSquare The square that the piece will get moved to Output form is "NN" or for example "03"
	 */
	public void movePiece(Piece pieceToMove, String endingSquare) {
		
		String currentSquare = pieceToMove.getCoordinate();
		
		
		int [] startingNumericalCoordinates = conversionObject.convertNumericalCoordinates(currentSquare);
		
		int xAxis = Integer.parseInt(endingSquare.substring(0, 1));
		int yAxis = Integer.parseInt(endingSquare.substring(1));
		
		boardStatus[xAxis][yAxis] = pieceToMove;
		boardStatus[startingNumericalCoordinates[0]][startingNumericalCoordinates[1]] = null;
		
		String endingSqaureAlgebraic = conversionObject.convertAlgebraCoordinates(endingSquare);
		pieceToMove.setCoordinate(endingSqaureAlgebraic);
		
		String coordinatesStr = pieceToMove.getCoordinate();
		int[] pixelCoordinates = conversionObject.getPixelCoordinates(coordinatesStr);
		
		JLabel pieceLabel = pieceToMove.getPieceJLabel();
		pieceLabel.setBounds(pixelCoordinates[0], pixelCoordinates[1], 80, 80);
		
		setPieceCoverageMap();
	}
	
	/**
	 *  This Function is responsible for the creation and placement of a new piece on the board.
	 * 
	 * BP: Black Pawn
	 * WP: White Pawn
	 * BR: Black Rook
	 * WR: White Rook
	 * BB: Black Bishop
	 * WB: White Bishop
	 * BKn: Black Knight
	 * WKn: White Knight
	 * BQ: Black Queen
	 * WQ: White Queen
	 * BK: Black King
	 * WK: White King
	 * 
	 * @param piecePointer The pointer for the piece object being used
	 */
	public void newPiece(Piece piecePointer) {
		
		this.frame = piecePointer.getFrame();
		String coordinate = piecePointer.getCoordinate();
		String imagePath = piecePointer.getImagePath();
		String pieceType = piecePointer.getPieceType();
		
		CoordinatesConversion conversionObject = new CoordinatesConversion();

		int []numericalCoordinates = conversionObject.convertNumericalCoordinates(coordinate);
		
		int []pixelCoordinateArr = conversionObject.getPixelCoordinates(coordinate);
		
		//creates new piece
		JLabel thisPiece = new JLabel("");
		thisPiece.setBounds(pixelCoordinateArr[0], pixelCoordinateArr[1], 80, 80);
		
		//sets new piece image
		ImageIcon thisPieceImage = new ImageIcon(this.getClass().getResource(imagePath));
		Image image = thisPieceImage.getImage(); // transform it 
		Image newimg = image.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		thisPieceImage = new ImageIcon(newimg);  // transform it back
		
		//add new piece
		thisPiece.setIcon(thisPieceImage);
		frame.getContentPane().add(thisPiece);
		
		// Set the z-order of thisPiece to bring it to the front
        frame.getContentPane().setComponentZOrder(thisPiece, 0);

        // Repaint the content pane to make the change in z-order immediately visible
        frame.getContentPane().repaint();
		
        boardStatus[numericalCoordinates[0]][numericalCoordinates[1]] = piecePointer;
		piecePointer.setPieceJLabel(thisPiece);
        
		piecePointer.setClickListener(logicObject);
		
	}
	
	
}
