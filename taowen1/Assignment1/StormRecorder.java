import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class StormRecorder {
	public static void main(String[] args) throws IOException {
		//FileInputStream
		Scanner scanner = new Scanner(new FileInputStream("C:\\Users\\wenta\\Downloads\\StormList.txt"));
		String[] record;
		String[] Storm;
		String StormName;
		int StormYear;
		ArrayList<Storm> Storms = new ArrayList<Storm>();

		while(scanner.hasNextLine()){
			record = scanner.nextLine().split("\\|");
			Storm =record[0].split("\\s");

			Storm object =new Storm(Storm[0],Integer.parseInt(Storm[1]));
			Storms.add(object);
			int indexofrecord = 1;
			while(indexofrecord < record.length){
				City city = new City(record[indexofrecord]);
				object.addCity(city);
				indexofrecord++;
			}

		}
		//Get input from user
		BufferedReader kbd = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Ener the name of a City:");
		String input = kbd.readLine();
		if (input.equals("exit")){//If the user types the word exit should exit the program
			return;
		}
		else{
			//Serach the path to find the name
			City city = new City(input);
			//System.out.print(city.toString());
			int counter = 0;
			for (Storm stormelement:Storms){
				String[] cityString = new String[10];
				int index = 0;
				for(Object cityelement:stormelement.getCities()){
					if(cityelement instanceof City){
						City citytest = (City)cityelement;
						cityString[index]=citytest.getName().trim();
						index++;
					}
				}
				for(String element:cityString){
					if (element != null) {
						if (element.equals(input)) {
							city.addStorm(stormelement);
							counter++;
						}
					}
				}

			}
			if(counter > 0) {
				System.out.println(city.toString());
			}
			else
				System.out.println("This is not a valid city");
		}

	}

}