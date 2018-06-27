/*
*   This file is part of java-hpgl2gcode.
*
*   java-hpgl2gcode is free software: you can redistribute it and/or modify
*   it under the terms of the GNU General Public License as published by
*   the Free Software Foundation, either version 3 of the License, or
*   (at your option) any later version.
*
*   java-hpgl2gcode is distributed in the hope that it will be useful,
*   but WITHOUT ANY WARRANTY; without even the implied warranty of
*   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*   GNU General Public License for more details.
*
*   You should have received a copy of the GNU General Public License
*   along with java-hpgl2gcode.  If not, see <http://www.gnu.org/licenses/>.
*/

package controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import model.Configuration;
import model.GCodeWriter;
import model.HPGLReader;
import model.Job;
import view.MainView;
import view.SelectFile;

public class Controller {
	private static Controller controller;
	private String author = "Dylan Van Assche";
	private String version = "V1.0.0";
	private String name = "HPGL2Gcode";
	
	private Controller() {
		// Controller stuff
		// Configuration for each JOB: engraving, milling, drilling all different jobs!
		MainView view = new MainView(this);
	}
	
	// Singleton pattern, only 1 instance may exist! Use .newInstance() to retrieve a Controller instance
	public static Controller newInstance() {
		if(controller == null) {
			controller = new Controller();
		}
		return controller;
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// Launch the Controller on application start
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Controller controller = Controller.newInstance();
	}

	public void convert(ArrayList<SelectFile> pickers) {
		ArrayList<Job> jobs = new ArrayList<Job>();
		Configuration configuration = new Configuration(20000, 10.0, -0.165, 500, 300, 40.0, 3.0);
		
		for(int i=0; i<pickers.size(); i++) {
			jobs.add(i, new Job(configuration));
			
			try {
				// Write this properly
				if(pickers.get(i).isSelected()) {
					FileReader fileIn = new FileReader(pickers.get(i).getPickedFileName().getText());
					HPGLReader reader = new HPGLReader(fileIn, jobs.get(i));
					reader.start();
					reader.join(); // wait until everything is read
					FileWriter fileOut = new FileWriter(pickers.get(i).getPickedFileName().getText() + ".gcode");
					GCodeWriter writer = new GCodeWriter(fileOut, jobs.get(i));
					writer.start();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
