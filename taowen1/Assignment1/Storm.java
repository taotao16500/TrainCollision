import java.util.ArrayList;

public class Storm {
	private String name;
	private ArrayList cities;
	private int year;

	public Storm(String name, int year) {
		this.name = name;
		this.year = year;
		this.cities = new ArrayList();
	}

	public void addCity(City c){
		cities.add(c);
		if(c.wasHit(this))
			return;
		else
			c.addStorm(this);
	}

	public boolean equals(Object tests){
		while(tests instanceof Storm){
			Storm s = (Storm) tests;
			if(this.name == s.name && this.year == s.year){
				return true;
			}
			else
				return false;
		}
		return false;
	}

	public ArrayList getCities(){
		return cities;
	}

	public String getName(){
		return name;
	}

	public int getYear(){
		return year;
	}

	public String toString(){
		if(cities.size()==0) {
			return name + ", " + year;
		}
		else {
			int index = 0;
			String result = "";
			while(index < cities.size()){
				if(cities.get(index) instanceof City){
					City testcity = (City)cities.get(index);
					result = result+System.lineSeparator()+testcity.getName();
				}
				index = index+1;
			}
			return name + ", " + year + result;
		}
	}
}
