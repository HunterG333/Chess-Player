package greer;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Dot {
	private JLabel label;
	private int[] coordinate;
	
	
	public void setLabel(JLabel label) {
		this.label = label;
	}
	
	public void setCoordinates(int[] coordinates) {
		this.coordinate = coordinates.clone();
	}
	
	public int[] getCoordinates() {
		return this.coordinate.clone();
	}
	
	final public void clearLabel(JFrame frame){
		try {
			frame.remove(label);
			frame.validate();
            frame.repaint();
			} catch(Exception e) {
				System.out.println("FAILED");
				//typically if the dots havent been instantiated yet
			}
	}
}
