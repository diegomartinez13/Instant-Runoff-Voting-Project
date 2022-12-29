package Main;

/**
 * The Candidate Running for President
 * 
 * @author Diego Martinez Garcia
 * @version 2.0
 * @since 2022-02-22
 */

public class Candidate {
	//private attributes:
	private String name = "";
	private int id = -1;
	private boolean active = true;
	private int rank = 0;
	
	//public attributes:
	/**Array that contains counters of the candidate votes, where the index == rank of vote - 1*/
	public int[] totalvotes;
	
	
	//Getters and setters:
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	/**Prints a String with the value of the counters found on totalvotes*/
	public String getTotalVotesString(){
		String votes = "[";
		for(int vote : totalvotes) {
			votes += Integer.toString(vote);
		}
		votes +="]";
		return votes;
	}//End of getters and setters
	
	/**Creates a candidate object that stores its name, id and if its active
	 * @param name - Name of the candidate
	 * @param id - id give to the candidate
	 * @param active - boolean value that determines if candidate active or not
	 * 
	 * @return Object type Candidate**/
	public Candidate(String name, int id, boolean active) {
		this.setName(name);
		this.setId(id);
		this.setActive(active);
	}
	
	public Candidate(String name, int id) {
		this(name, id, true);
	}
	
	public Candidate() {
		this("", 0, true);
	}//End of constructors
	
}
