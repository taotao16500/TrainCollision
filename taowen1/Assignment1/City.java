import java.util.ArrayList;


public class City {
	private String name;
	private ArrayList storms;

	public City(String name) {
		this.name = name;
		this.storms = new ArrayList();
	}

	public boolean wasHit(Storm s){
		if(storms.contains(s)){
			return true;
		}
		else
			return false;
	}

	public boolean onSamePath(City c){
		while(c.storms.size()!= 0 && this.storms.size()!=0) {
			if (this.storms.equals(c.storms)) {
				return true;
			}
		}
			return false;
	}

	public void addStorm(Storm s){
		storms.add(s);
		if(s.getCities().contains(this)){
			return;
		}
		else
			s.addCity(this);
	}

	public boolean equals(Object testcity){
		while(testcity instanceof City ){
			City city = (City)testcity;
			if(this.name == city.name && this.storms.size()== city.storms.size()){
				int index = 0;
				int counter = 0;
				while(index != city.storms.size()){
					if(city.storms.contains(this.storms.get(index))){
						counter = counter + 1;
					}
					index = index + 1;
				}
				if(counter == city.storms.size()){
					return true;
				}
			}
			else
				return false;

		}
		return false;
	}

	public String getName(){
		return name;
	}

	public String toString(){
		int index = 0;
		String result = "";
		if(storms.size() > 1) {
			while (index < storms.size() - 1) {
				if (storms.get(index) instanceof Storm) {
					Storm teststorm = (Storm) storms.get(index);
					result = result + teststorm.getName() + ", ";
				}
				index = index + 1;
			}
			if(storms.get(storms.size()-1) instanceof Storm){
				Storm teststorm = (Storm)storms.get(storms.size()-1);
				result = result + teststorm.getName();
			}
			return name + " ("+result+")";
		}
		else if(storms.size()==0){
			return name + " ("+result+")";
		}
		else{
			if(storms.get(storms.size()-1) instanceof Storm){
				Storm teststorm = (Storm)storms.get(storms.size()-1);
				result = result + teststorm.getName();
			}
			return name + " ("+result+")";
		}
		//return name + " ("+result+")";
	}
}
