package greer;
/**
 * A class that is intended to deal with anything dealing with the conversion of coordinates
 * 
 * @author Hunter
 *
 */
public class CoordinatesConversion {
	
	static int xFile[] = {125, 195, 265, 335, 405, 475, 545, 615}; //a,b,c,d,e,f,g,h
	static int yFile[] = {655, 585, 515, 445, 375, 305, 235, 165}; //1,2,3,4,5,6,7,8
	
	/**
	 * Returns an array of the two pixel coordinates for given coordinates.
	 * 
	 * @param boardCoordinate The Coordinates for a square. For example "H7" or "B2"
	 * 
	 * @return The Pixel Coordinates for a given square
	 */
	public int[] getPixelCoordinates(String boardCoordinate) {
		
		//converts Chess Coordinate to numerical coordinate
		int pixelCoordinate[] = convertNumericalCoordinates(boardCoordinate);	
		
		//convert to pixels
		pixelCoordinate[0] = xFile[pixelCoordinate[0]];
		pixelCoordinate[1] = yFile[pixelCoordinate[1]];
		
		return pixelCoordinate;
	}
	
	/**
	 * Returns the pixel coordinates given the numerical coordiantes
	 * 
	 * @param numericalCoordinate numerical coordiantes
	 * @return the pixel coordinates
	 */
	public int[] getPixelCoordinates(int[] numericalCoordinate) {
	
		numericalCoordinate[0] = xFile[numericalCoordinate[0]];
		numericalCoordinate[1] = yFile[numericalCoordinate[1]];
		
		return numericalCoordinate;
	}
	
	
	/**
	 * A function to convert Board Coordinates to Numerical Coordinates
	 * 
	 * @param boardCoordinate A standard chess cordinate input. "LN" or "C5"
	 * @return A 2-length array of the numerical coordinates with starting index 0
	 */
	public int[] convertNumericalCoordinates(String boardCoordinate) {
		//get first and second coordinate
		
		
		
		String firstCoordinate = boardCoordinate.substring(0, 1);
		String secondCoordinate = boardCoordinate.substring(1,2);
		
		
		int firstCoordinateInt = -1;
		int secondCoordinateInt = -1;
		
		//matches letter to numerical system
		switch(firstCoordinate) {
			case("A"):
				firstCoordinateInt = 0;
				break;
			case("B"):
				firstCoordinateInt = 1;
				break;
			case("C"):
				firstCoordinateInt= 2;
				break;
			case("D"):
				firstCoordinateInt = 3;
				break;
			case("E"):
				firstCoordinateInt = 4;
				break;
			case("F"):
				firstCoordinateInt = 5;
				break;
			case("G"):
				firstCoordinateInt = 6;
				break;
			case("H"):
				firstCoordinateInt = 7;
				break;
		}
		
		secondCoordinateInt = Integer.parseInt(secondCoordinate) - 1;
		
		int newCoordinateArray[] = {firstCoordinateInt, secondCoordinateInt};
		
		
		return newCoordinateArray;
	}
	
	/**
	 * A class to convert numeric Coordinates to algebraic coordinates
	 * 
	 * @param numeric the numeric coordinates to convert to algebraic
	 * @return the algebraic coordinates
	 */
	public String convertAlgebraCoordinates(String numeric) {
		String algebraicCoordinates = "";
		
		int firstInt = Integer.parseInt(numeric.substring(0, 1));
		int secondInt = Integer.parseInt(numeric.substring(1));
		
		switch(firstInt){
			case(0):
				algebraicCoordinates += "A";
				break;
			case(1):
				algebraicCoordinates += "B";
				break;
			case(2):
				algebraicCoordinates += "C";
				break;
			case(3):
				algebraicCoordinates += "D";
				break;
			case(4):
				algebraicCoordinates += "E";
				break;
			case(5):
				algebraicCoordinates += "F";
				break;
			case(6):
				algebraicCoordinates += "G";
				break;
			case(7):
				algebraicCoordinates += "H";
				break;
		}
		
		algebraicCoordinates += "" + (secondInt + 1);
		
		return algebraicCoordinates;
			
	}

}
