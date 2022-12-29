package Main;

import DataStructures.List.ArrayList;

/**
 * The Ballot
 * 
 * Stores all the votes casted by one person who votes in the Poor Harbor Election
 * 
 * @author: Diego Martinez Garcia
 * @version: 2.0
 * @since: 2022-02-22
 */
public class Ballot implements BaseBallot{
	//Private attributes:
	
	/**Number of the ballot*/
	private int BallotNum = 0;
	
	private boolean validBallot = true;
	private boolean emptyBallot = false;
	
	/**ArrayList that contains all the votes in the ballot*/
	private ArrayList<String> votesArray;
	
	/**Candidate found in the ballot with a rank value of 1*/
	private Candidate rank1Candidate;
	
	
	//Public attribute:
	/**ArrayList that contains all the candidates found on the ballot.*/
	public ArrayList<Candidate> ballotCandidates;
	
	//getters and setters:
	public int getBallotNum() {
		return BallotNum;
	}
	private void setBallotNum(int ballotNum) {
		BallotNum = ballotNum;
	}
	public boolean isValidBallot() {
		return validBallot;
	}
	private void setValidBallot(boolean validBallot) {
		this.validBallot = validBallot;
	}
	public boolean isEmptyBallot() {
		return emptyBallot;
	}
	private void setEmptyBallot(boolean emptyBallot) {
		this.emptyBallot = emptyBallot;
	}
	public ArrayList<String> getVotesArray(){
		return votesArray;
	}
	public Candidate getRank1Candidate() {
		return rank1Candidate;
	}
	public void setRank1Candidate(Candidate rank1Candidate) {
		this.rank1Candidate = rank1Candidate;
	}//End of getters and setters
	
	
	
	/**Constructor:
	 * reads line, sets the ballot number and creates a votes array to store all the votes made on that ballot,
	 * also checks if the ballot is empty and if it is valid.
	 * 
	 * @param line - line of the csv file that corresponds to ballot
	 * @return Ballot obj **/
	public Ballot(String line, ArrayList<Candidate> candidatesArray) {
		
		this.setUpVotesArray(line, candidatesArray);//Setting up an ArrayList containing the votes read by the Buffer
		if(!votesArray.isEmpty()){//Checking if ballot is empty
			this.validation(candidatesArray);//Validating the ballot
			
		}else
			this.setEmptyBallot(true);
			
		this.setUpCandidates(candidatesArray);//Setting up an ArrayList containing candidates objects
		this.topCandidate(); // Finding top candidate
	
	}//End of constructor

	
	
	//Methods of the ballot class:
	
	/**Gets the rank of a candidate by they id number:
	 * @param candidateID - id number of the candidate we are looking for.
	 * @return rank - rank of the candidate, if not found returns -1**/
	@Override
	public int getRankByCandidate(int candidateID) {
		
		int rank = -1;
		for(Candidate ca : ballotCandidates) {
			if(ca.getId() == candidateID)
				rank = ca.getRank();
		}
		return rank;
	}

	/**Gets the id of a candidate by they rank number:
	 * @param rank - rank number of the candidate we are looking for.
	 * @return id - id of the candidate, if not found returns -1 **/
	@Override
	public int getCandidateByRank(int rank) {
		
		int id = -1;
		for(Candidate ca: ballotCandidates) {
			if(ca.getRank() == rank)
				id = ca.getId();
		}
		return id;
	}
	

	/**Eliminates a candidate by the id number if rank == 1:
	 * @param candidateID - id number of the candidate we are looking for.
	 * @return true/false - true if candidate was found and eliminated; false if candidate not found. **/
	@Override
	public boolean eliminate(int candidateId) {
		for(Candidate ca: ballotCandidates) {
			if(ca.getRank() > getRankByCandidate(candidateId))
				ca.setRank(ca.getRank()-1);
		}
		for(Candidate ca : ballotCandidates) {
			if(ca.getRank() > ballotCandidates.size())
				ca.setRank(ca.getRank()-1);
		}
		ballotCandidates.remove(ballotCandidates.get(this.getRankByCandidate(candidateId) -1 ));
		return true;
	}

	
	/**Returns a string with the votes in this ballot
	 * @return votesInBallot - string with the votes on this ballot**/
	public String getVotes(){
		String votesInBallot = "";
		for(int i = 0; i < votesArray.size();i++) {
			if(i == votesArray.size()-1)
				votesInBallot += votesArray.get(i);
			else 
				votesInBallot += votesArray.get(i) + "," ;
		}
		return votesInBallot;
	}

	/**Gets the candidate with rank 1 and creates a Candidate variable*/
	private void topCandidate() {
		for(Candidate ca: this.ballotCandidates) {
			if(ca.getRank() == 1)
				this.setRank1Candidate(ca);
		}
	}
	
	
	/**Sets up a new arrayList that contains the votes of the String given by the BufferedReader
	 * @param line - String line that the BufferedReader returned.
	 * @param candidatesArray - ArrayList that contains all the candidates that where found on the csv file.*/
	private void setUpVotesArray(String line, ArrayList<Candidate> candidatesArray) {
		String[] arrayLine = line.split(",");
		this.setBallotNum(Integer.parseInt(arrayLine[0]));
		this.votesArray = new ArrayList<String>(candidatesArray.size());
		
		for(int i = 1; i < arrayLine.length; i++) {
			this.votesArray.add(arrayLine[i]);
		}
	}
	
	
	/**Validates the given ballot using the following criteria:
	 * 1. No rank can be repeated
	 * 2. No id can be repeated
	 * 3. The number of ranks can not be larger than the amount of candidates
	 * @param candidatesArray- arrayList that has the candidates objects
	 */
	private void validation(ArrayList<Candidate> candidatesArray) {
		
		//ArrayList that will store the ranks and candidates id in the order that they were found on the csv
		ArrayList<String>ranks = new ArrayList<String>(votesArray.size());
		ArrayList<String>candidates = new ArrayList<String>(votesArray.size());
		
		//checks if value of ranks or id of candidates is repeated
		for(String vote: votesArray) {
			String[] voteArray = vote.split(":");
			if(candidates.contains(voteArray[0]) || ranks.contains(voteArray[1]))
				this.setValidBallot(false);
			candidates.add(voteArray[0]);
			ranks.add(voteArray[1]);
		}
		
		//checks if a rank in the ballot do not overcome the amount of candidates in the election
		for(String rank : ranks){
			int rankInt = Integer.parseInt(rank);
			if(rankInt > candidatesArray.size())
				this.setValidBallot(false);
		}
		
		//checks the order of the rank to find any out of order vote.
		for(int i = 0; i < ranks.size();i++) {
			if((i+1) != Integer.parseInt(ranks.get(i)))
				this.setValidBallot(false);	
		}
	}
	
	
	/**Sets up an arrayList with candidates objects that are present in the current ballot
	 * @param candidatesArray - arrayList with candidates objects*/
	private void setUpCandidates(ArrayList<Candidate> candidatesArray) {
		ballotCandidates = new ArrayList<Candidate>(candidatesArray.size());
		
		for(String vote: votesArray) {
			String[] voteDuo = vote.split(":");
			for(Candidate ca : candidatesArray) {
				if(Integer.parseInt(voteDuo[0]) == ca.getId()) {
					Candidate newCa = new Candidate(ca.getName(), ca.getId());
					newCa.setRank(Integer.parseInt(voteDuo[1]));
					ballotCandidates.add(newCa);
				}
			}
		}
			
	}
	//end of methods
}