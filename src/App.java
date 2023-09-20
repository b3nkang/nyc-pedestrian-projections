import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.io.FileNotFoundException;
import org.apache.commons.lang3.SystemUtils;
import java.util.regex.Matcher;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.providers.OpenStreetMap.*;
import de.fhpotsdam.unfolding.providers.MapBox;
import de.fhpotsdam.unfolding.providers.Google.*;
import de.fhpotsdam.unfolding.providers.Microsoft;
// import de.fhpotsdam.unfolding.utils.ScreenPosition;


public class App extends PApplet {

	UnfoldingMap map;
	String mapTitle;
	final float SCALE_FACTOR = 0.0002f; 
	final int DEFAULT_ZOOM_LEVEL = 11;
	final Location DEFAULT_LOCATION = new Location(40.7286683f, -73.997895f); // random NYC location to set program to start with
	String[][] data; 

	/**
	 * 	- when the user presses the `1` key, the code calls the showMay2021MorningCounts method to show the morning counts in May 2021, with blue bubble markers on the map.
	 * 	- when the user presses the `2` key, the code calls the showMay2021EveningCounts method to show the evening counts in May 2021, with blue bubble markers on the map.
	 * 	- when the user presses the `3` key, the code calls the showMay2021EveningMorningCountsDifferencemethod to show the difference between the evening and morning counts in May 2021.  If the evening count is greater, the marker should be a green bubble, otherwise, the marker should be a red bubble.
	 * 	- when the user presses the `4` key, the code calls the showMay2021VersusMay2019Counts method to show the difference between the average of the evening and morning counts in May 2021 and the average of the evening and morning counts in May 2019.  If the counts for 2021 are greater, the marker should be a green bubble, otherwise, the marker should be a red bubble.
	 * 	- when the user presses the `5` key, the code calls the customVisualization1 method to show data of your choosing, visualized with marker types of your choosing.
	 * 	- when the user presses the `6` key, the code calls the customVisualization2 method to show data of your choosing, visualized with marker types of your choosing.
	 */
	public void keyPressed() {
		// System.out.println("Key pressed: " + key);
		// complete this method
		switch (key) {
			case '1':
				showMay2021MorningCounts(data);
				break;
			case '2':
				showMay2021EveningCounts(data);
				break;
			case '3':
				showMay2021EveningMorningCountsDifference(data);
			 	break;
			case '4':
				showMay2021VersusMay2019Counts(data);
				break;
			case '5':
				showSep2016WeekendCounts(data);
				break;
			case '6':
				showMay2017MorningEveningAndWeekendTotalCountsAkaVeryUsefulVisualization(data);
		  }
		  

	}

	/**
	 * Adds markers to the map for the morning pedestrian counts in May 2021.
	 * These counts are in the second-to-last field in the CSV data file.  So we look at the second-to-last array element in our data array for these values.
	 * 
	 * @param data A two-dimensional String array, containing the data returned by the getDataFromLines method.
	 */
	public void showMay2021MorningCounts(String[][] data) {
		clearMap(); // clear any markers previously placed on the map
		mapTitle = "May 2021 Morning Pedestrian Counts";
		// complete this method - DELETE THE EXAMPLE CODE BELOW
		for (int k = 1; k < data.length; k++){
			// debugging
			/* System.out.println(data[k][1]);
			System.out.println(data[k][0]);
			System.out.println(data[k][data[k].length-3]); */
			//
			Location markerLocation = new Location(Float.parseFloat(data[k][1]), Float.parseFloat(data[k][0]));
			int dayX = Integer.parseInt(data[k][data[k].length-3]);
            float[] markerColor = {255, 0, 0, 127};
            float markerRadius = SCALE_FACTOR*dayX;
            MarkerBubble marker = new MarkerBubble(this, markerLocation, markerRadius, markerColor);
            map.addMarker(marker);
		}
	}

	/**
	 * Adds markers to the map for the evening pedestrian counts in May 2021.
	 * These counts are in the second-to-last field in the CSV data file.  So we look at the second-to-last array element in our data array for these values.
	 * 
	 * @param data A two-dimensional String array, containing the data returned by the getDataFromLines method.
	 */
	public void showMay2021EveningCounts(String[][] data) {
		clearMap(); // clear any markers previously placed on the map
		mapTitle = "May 2021 Evening Pedestrian Counts";
		// complete this method
		for (int k = 1; k < data.length; k++){
            // debugging
			/* System.out.println(data[k][1]);
			System.out.println(data[k][0]);
			System.out.println(data[k][data[k].length-2]); */
			//
			Location markerLocation = new Location(Float.parseFloat(data[k][1]), Float.parseFloat(data[k][0]));
            String dayXStr = data[k][data[k].length-2];
			int dayX = Integer.parseInt(dayXStr);
            float[] markerColor = {255, 0, 0, 127};
            float markerRadius = SCALE_FACTOR*dayX;
            MarkerBubble marker = new MarkerBubble(this, markerLocation, markerRadius, markerColor);
            map.addMarker(marker);
		}
	}

	/**
	 * Adds markers to the map for the difference between evening and morning pedestrian counts in May 2021.
	 * 
	 * @param data A two-dimensional String array, containing the data returned by the getDataFromLines method.
	 */
	public void showMay2021EveningMorningCountsDifference(String[][] data) {
		clearMap(); // clear any markers previously placed on the map
		mapTitle = "Difference Between May 2021 Evening and Morning Pedestrian Counts";
		// complete this method
		for (int k = 1; k < data.length; k++){
            // debugging
			/*System.out.println(data[k][1]);
			System.out.println(data[k][0]);
			System.out.println(data[k][data[k].length-3]);
			System.out.println(data[k][data[k].length-2]); */
			//
			Location markerLocation = new Location(Float.parseFloat(data[k][1]), Float.parseFloat(data[k][0]));
            int totalPeds = 0;
            String dayAMStr = data[k][data[k].length-3];
			String dayPMStr = data[k][data[k].length-2];
			int dayAM  = Integer.parseInt(dayAMStr);
			int dayPM = Integer.parseInt(dayPMStr);
            totalPeds = dayPM - dayAM;
            float[] markerColor = {255, 0, 0, 127};
            float markerRadius = SCALE_FACTOR*totalPeds;
            MarkerBubble marker = new MarkerBubble(this, markerLocation, markerRadius, markerColor);
            map.addMarker(marker);
		}
	}

	/**
	 * Adds markers to the map for the difference between the average pedestrian count in May 2021 and the average pedestrian count in May 2019.
	 * 
	 * @param data A two-dimensional String array, containing the data returned by the getDataFromLines method.
	 */
	public void showMay2021VersusMay2019Counts(String[][] data) {
		clearMap(); // clear any markers previously placed on the map
		mapTitle = "Difference Between May 2021 and May 2019 Pedestrian Counts";
		// complete this method
		for (int k = 1; k < data.length; k++){
		    // debugging
			//System.out.println(data[k][1] + "print 1");
			//System.out.println(data[k][0] + "print 2");
            Location markerLocation = new Location(Float.parseFloat(data[k][1]), Float.parseFloat(data[k][0]));
            int total21Peds = 0;
			int inner21Count = 0;
			int total19Peds = 0;
			int inner19Count = 0;

            for (int i = 1; i < 4; i++){
				String dayMay21Str = data[k][data[k].length-i];
				// debug
				//System.out.println(Arrays.toString(data[k]));
				//System.out.println(data[k][data[k].length-i]  + "print 3");
				int dayMay21 = Integer.parseInt(dayMay21Str);
				total21Peds += dayMay21;
				inner21Count++;
			}
			float may21av = total21Peds / (float) inner21Count;

			for (int f = 7; f < 10; f++){
				String dayMay19Str = data[k][data[k].length-f]+"trigger";
				// debug
				// System.out.println(dayMay19Str+"/interation"+f);
				// System.out.println(data[k][data[k].length-f]  + "print 4");
				if (!dayMay19Str.equals("trigger")){
					dayMay19Str = dayMay19Str.substring(0,dayMay19Str.length()-7);
					//debug
					//System.out.println("it iterated, failed");
					//System.out.println(dayMay19Str);
					int dayMay19 = Integer.parseInt(dayMay19Str);
					total19Peds += dayMay19;
					inner19Count++;	
				} 
			}
			float may19av = total19Peds / (float) inner19Count;
			float avDiff = may21av - may19av;
			float[] markerColor = {0, 0, 0, 127};
            if (avDiff >= 0){
				markerColor[1] = 255;
			} else {
				markerColor[0] = 255;
			}
            float markerRadius = SCALE_FACTOR*avDiff;
            MarkerBubble marker = new MarkerBubble(this, markerLocation, markerRadius, markerColor);
            map.addMarker(marker);
		}
	}

	public void showSep2016WeekendCounts(String[][] data) {
		
		clearMap(); // clear any markers previously placed on the map
		mapTitle = "September 2016 Weekend Pedestrian Counts";
		// complete this method
		for (int k = 1; k < data.length; k++){
			Location markerLocation = new Location(Float.parseFloat(data[k][1]), Float.parseFloat(data[k][0]));
            String dayXStr = data[k][data[k].length-22];
			int dayX = Integer.parseInt(dayXStr);
			//debug
			//System.out.println(dayX);
            float[] markerColor = {255, 0, 0, 127};
            float markerRadius = SCALE_FACTOR*dayX;
            MarkerBubble marker = new MarkerBubble(this, markerLocation, markerRadius, markerColor);
            map.addMarker(marker);
		}
	}

	public void showMay2017MorningEveningAndWeekendTotalCountsAkaVeryUsefulVisualization(String[][] data) {
		clearMap(); // clear any markers previously placed on the map
		mapTitle = "May 2017 AM, PM, and Weekend TOTAL Pedestrian Counts";
		// complete this method
		for (int k = 1; k < data.length; k++){
			Location markerLocation = new Location(Float.parseFloat(data[k][1]), Float.parseFloat(data[k][0]));
			int totalPeds = 0;
            String dayAMStr = data[k][data[k].length-21];
			String dayPMStr = data[k][data[k].length-20];
			String dayMDStr = data[k][data[k].length-19];
			//System.out.println(dayAMStr+"/"+dayPMStr+"/"+dayMDStr);
			int dayMD  = Integer.parseInt(dayMDStr);
			int dayAM  = Integer.parseInt(dayAMStr);
			int dayPM = Integer.parseInt(dayPMStr);
            totalPeds = dayPM + dayAM +dayMD;			
            float[] markerColor = {255, 0, 0, 127};
            float markerRadius = SCALE_FACTOR*totalPeds;
            MarkerBubble marker = new MarkerBubble(this, markerLocation, markerRadius, markerColor);
            map.addMarker(marker);
		}
	}

	/**
	 * Opens a file and returns an array of the lines within the file, as Strings with their line breaks removed.
	 * 
	 * @param filepath The filepath to open
	 * @return A String array, where each String contains the text of a line of the file, with its line break removed.
	 * @throws FileNotFoundException
	 */
	public String[] getLinesFromFile(String filepath) {
		String fullText = "";
		try {
		  // try to open the file and extract its contents
		  Scanner scn = new Scanner(new File(filepath));
		  while (scn.hasNextLine()) {
			String line = scn.nextLine();
			fullText += line + "\n"; 
		  }
		  scn.close();
		}
		catch (FileNotFoundException e) {
		  System.out.println("Oh no... can't find the file!");
		}
		String lines[] = fullText.split("[\n]+");
		return lines;
	}

	/**
	 * Takes an array of lines of text in comma-separated values (CSV) format and splits each line into a sub-array of data fields.
	 * This method skips any lines that don't contain mappable data (i.e. don't have any geospatial data in them) 
	 *
	 * @param lines A String array of lines of text, where each line is in comma-separated values (CSV) format.
	 * @return A two-dimensional String array, where each inner array contains the data from one of the lines, split by commas.
	 */
	public String[][] getDataFromLines(String[] lines) {
		// complete this method - DELETE THE EXAMPLE CODE BELOW
		String[][] master = new String[lines.length][];
        for (int i = 0; i < lines.length; i++) { 
            String[] eachLine = lines[i].split(",");
            String trimCoord = eachLine[0].substring(7,eachLine[0].length()-1);
            String addCommaCoord = trimCoord.replace(" ",",");
            eachLine[0] = addCommaCoord;
			if (i != 0){
                String[] coordSplit = eachLine[0].split(",");
                //System.out.println(Arrays.toString(coordSplit));
                eachLine[0] = coordSplit[0]; 
                eachLine = Arrays.copyOf(eachLine, eachLine.length + 1);
                System.arraycopy(eachLine, 1, eachLine, 2, eachLine.length - 2); 
                eachLine[1] = coordSplit[1]; 
            }
            master[i] = Arrays.copyOf(eachLine, eachLine.length);
        }
		String[] bugReplacementLoc27 = Arrays.copyOf(master[26], master[26].length + 3);
        for (int j = bugReplacementLoc27.length-1; j > bugReplacementLoc27.length-4; j--){
            bugReplacementLoc27[j] = "0";
        }
        master[26] = bugReplacementLoc27;
        return master;
	}

	public void setup() {
		size(1200, 800, P2D); // set the map window size, using the OpenGL 2D rendering engine
		map = getMap(); 

		try {
			String cwd = Paths.get("").toAbsolutePath().toString(); // the current working directory as an absolute path
			String path = Paths.get(cwd, "data", "PedCountLocationsMay2015.csv").toString(); // e.g "data/PedCountLocationsMay2015.csv" on Mac/Unix vs. "data\PedCountLocationsMay2015.csv" on Windows
			String[] lines = getLinesFromFile(path); 
			data = getDataFromLines(lines);
			//System.out.println(Arrays.deepToString(data)); // debugging

			map.zoomAndPanTo(DEFAULT_ZOOM_LEVEL, DEFAULT_LOCATION);

			// by default, we show markers for the morning counts in May 2021
			showMay2021MorningCounts(data);
			//showMay2021EveningCounts(data);
			//showMay2021EveningMorningCountsDifference(data);
			//showMay2021VersusMay2019Counts(data);
			//showSep2016WeekendCounts(data);
			//showMay2017MorningEveningAndWeekendTotalCountsAkaVeryUsefulVisualization(data);
		}
		catch (Exception e) {
			System.out.println("Error: could not load data from file: " + e);
		}

	} // setup

	private UnfoldingMap getMap() {
		// not all map providers work on all computers.
		// if you have trouble with the one selected, try the others one-by-one to see which one works for you.
		map = new UnfoldingMap(this, new Microsoft.RoadProvider());
		// map = new UnfoldingMap(this, new Microsoft.AerialProvider());
		// map = new UnfoldingMap(this, new GoogleMapProvider());
		// map = new UnfoldingMap(this);
		// map = new UnfoldingMap(this, new OpenStreetMapProvider());

		// enable some interactive behaviors
		MapUtils.createDefaultEventDispatcher(this, map);
		map.setTweening(true);
		map.zoomToLevel(DEFAULT_ZOOM_LEVEL);

		return map;
	}

	public void draw() {
		background(0);
		map.draw();
		drawTitle();
	}

	public void clearMap() {
		map.getMarkers().clear();
	}

	public void drawTitle() {
		fill(0);
		noStroke();
		rect(0, height-40, width, height-40); 
		textAlign(CENTER);
		fill(255);
		text(mapTitle, width/2, height-15);
	}

	public static void main(String[] args) {
		System.out.printf("\n###  JDK IN USE ###\n- Version: %s\n- Location: %s\n### ^JDK IN USE ###\n\n", SystemUtils.JAVA_VERSION, SystemUtils.getJavaHome());
		boolean isGoodJDK = SystemUtils.IS_JAVA_1_8;
		if (!isGoodJDK) {
			System.out.printf("Fatal Error: YOU MUST USE JAVA 1.8, not %s!!!\n", SystemUtils.JAVA_VERSION);
		}
		else {
			PApplet.main(".App");
		}
	}

}
