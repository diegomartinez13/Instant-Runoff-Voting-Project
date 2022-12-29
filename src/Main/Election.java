package Main;

import java.io.*;

import DataStructures.List.ArrayList;

/**
 * The Election class
 * 
 * Where we decide who wins and is our new president base on the information provided
 * 
 * @author: Diego Martinez Garcia
 * @version: 2.0
 * @param <E>
 * @since 2022-02-22
 */
public class Election {
	//Private methods:
	/**Number of empty ballots in total*/
	private int emptyBallots = 0;
	
	/**Number of invalid ballots in total*/
	private int invalidBallots = 0;
	
	/**Number of ballots in total*/
	private int totalBallots = 0;
	
	/**ArrayList that has all the ballots that where found on the .csv*/
	private ArrayList<Ballot> ballots = new ArrayList<Ballot>(25);
	
	/**ArrayList that has all the candidates that where found on the .csv*/
	private ArrayList<Candidate> candidates = new ArrayList<Candidate>(10);
	
	/**Array that has the total count of the votes on first round
	 * to find an early winner*/
	private int[] votesRank1;
	
	/**Winner candidate*/
	private Candidate winner = null;
	
	/**ArrayList that has all the candidates that where eliminated*/
	private ArrayList<Candidate> eliminated = new ArrayList<Candidate>(10);
	
	/**Counter of candidates currently active*/
	private int totalCandidates;
	
	
	//Getters and setters:
	public int getEmptyBallots() {
		return emptyBallots;
	}
	public void setWinner(Candidate winner) {
		this.winner = winner;
	}
	public int getInvalidBallots() {
		return invalidBallots;
	}
	public Candidate getWinner() {
		return this.winner;
	}
	public int getTotalBallots() {
		return totalBallots;
	}
	public void setTotalBallots(int totalBallots) {
		this.totalBallots = totalBallots;
	}//End of getters and setters

	
	/**Constructor: Uses a string with the value of the path for the ballots.csv,
	 * and a string with the value of the path to the candidates.csv. It passes the paths to a
	 * BufferedReader, individually, to create candidates with type of Candidates and ballots of 
	 * class Ballots.
	 * 
	 * 
	 * @param path = path of the csv file to be used.
	 * @return Election object
	 * @throws IOException if files not found **/
	public Election(String pathBallot, String pathCandidate) throws IOException {
		// TODO Auto-generated constructor stub
		
		this.bufferCandidates(pathCandidate);//Looking for candidates:		
		this.bufferBallot(pathBallot);//Ballot creation with line that returns the bufferedReader:
		
		this.countBallots();//Counting ballots
		
		this.setVotes();//Counts and sets all the votes of the candidates
		
		this.setVotesRank1();//Sets votes of candidates (only the rank1's) to find early winner
		this.winnerStart();//Looks for an early winner
		
		if(this.getWinner() == null)//Checks if winner was found
			this.eliminationProcess();//If no winner found, the funtion with the elimination process if called
		
	}//End of constructor
	
	
	/**Finds a winner on the first round if the candidate has more than  the 50% of the votes**/
	public void winnerStart() {
		int maxVotes = getTotalBallots() - getInvalidBallots();
		int index = 0;
		for(int votesCount: votesRank1) {
			if(votesCount >= maxVotes/2) {
				this.setWinner(candidates.get(index));
			}
			else index++;
		}
	}
	
	
	/**Sets an array with all the votes with rank 1 for each candidate**/
	private void setVotesRank1() {
		votesRank1 = new int[candidates.size()];
		for(int i = 0; i < ballots.size(); i++) {
			Ballot bal = ballots.get(i);
			if(!bal.isEmptyBallot() && bal.isValidBallot()) {
				int index = bal.getCandidateByRank(1) - 1;
				Candidate ca = candidates.get(index);
				if(ca.isActive()) {
					votesRank1[index]++;
				}
			}
			
		}
	}
	
	
	/**Runs the methods that set the values in some arrays that will contain the counts of the votes and also
	 * implements an array that contains all the total votes that a candidate received, the order on the array corresponds
	 * to vote's ranking.  */
	public void setVotes() {
		for(Candidate ca: candidates) {//Creation of array that will contain the total of vote counts
			if(ca.isActive())
				ca.totalvotes = new int[totalCandidates];
		}

		for(Ballot ballot: ballots) {
			if(ballot.isValidBallot() && !ballot.isEmptyBallot()) {
				for(Candidate ca : ballot.ballotCandidates) {
					int rankOfVote = ca.getRank()-1;
					int idCandidate = ca.getId();
					if(ca.isActive()) { 	//if active adds count to candidate found on the array of candidates in election class 
						candidates.get(idCandidate-1).totalvotes[rankOfVote]++;
					}else
						continue;
				}
			}
		}

	}
	
	
	/**Counts ballots to find the totals of ballots, invalid ballots and empty ballots.*/
	private void countBallots(){
		for(int i = 0; i < ballots.size(); i++) {
			Ballot bal = ballots.get(i);
			if(bal.isEmptyBallot())
				this.emptyBallots++;
			if(!bal.isValidBallot())
				this.invalidBallots++;
			this.totalBallots++;
		}


	}
	
	
	/**Creates a BufferedReader to read a csv file that will be used to create candidates objects
	 * @param pathCandidate - String containing the path to a csv file.
	 * @throws IOException if line can not be read
	 * @throws FileNotFoundException if file not found*/
	private void bufferCandidates(String pathCandidate) {
		String candidateLine = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(pathCandidate));
			while((candidateLine = br.readLine()) != null) {
				String[] candidateInfo = candidateLine.split(",");
				Candidate candidate = new Candidate(candidateInfo[0], Integer.parseInt(candidateInfo[1]), true);
				candidates.add(candidate);
			}
			totalCandidates = candidates.size();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}// end of candidates search
	}
	
	
	/**Creates a BufferedReader to read a csv file that will be used to create ballot objects
	 * @param pathBallot - String containing the path to a csv file.
	 * @throws IOException if line can not be read
	 * @throws FileNotFoundException if file not found*/
	private void bufferBallot(String pathBallot) {
		String line = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(pathBallot));
			while((line = br.readLine()) != null) {
				Ballot newBallot = new Ballot(line, candidates);
				this.ballots.add(newBallot);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**Looks in all ballots for the candidate with the lower amounts of votes to remove it from the election process.
	 * @param idEliminate - id of the candidate that will be eliminated
	 */
	public void eliminateCandidate(int idEliminate) {
		
		for(Ballot ballot: ballots) {	//Looks in all ballots 
			if(!ballot.isEmptyBallot() && ballot.isValidBallot()) { // if they are valid and not empty
				
				//Finds the candidate in ballot with a vote with rank 1 and with the id that was passed
				Candidate topCandidate = ballot.getRank1Candidate();
				Candidate globalCandidate = candidates.get(topCandidate.getId()-1);
				if(topCandidate.getId() == idEliminate && topCandidate.isActive()) {
					if(!eliminated.contains(globalCandidate)) {
						eliminated.add(globalCandidate); 	//adds it to an arrayList that contains the eliminated candidates
						globalCandidate.setActive(false);	// sets the current candidate in ballot as NotActive so the can program ignores its votes
						totalCandidates-- ;		//reduces the counter of currently candidates active
					}
				}
				for(Candidate candidate : ballot.ballotCandidates) {
					if(candidate.getId() == idEliminate && candidate.isActive()) {
						candidate.setActive(false);
						ballot.eliminate(idEliminate);
						
					}
				
				}
			}
		}
		this.setVotes(); // calls function that counts all the votes in all ballots
	}
	
	
	/**Eliminates the candidate with the least votes with higher rank, if not found, checks the votes
	 * of a rank lower until finding a winner, when in the candidates array only two candidates are currently active
	 * if only one active, it set it as the winner.**/
	public void findWinnerLastTwo() {
		//Creating an arrayList to store the candidates that are currently active
		ArrayList<Candidate> lastTwo = new ArrayList<Candidate>(2);
		for(Candidate candidate : candidates) {
			if(candidate.isActive())
				lastTwo.add(candidate);
		}
		
		//Checking if in last round to elements where still active, and finds a looser between those.
		if(lastTwo.size() == 2) {
			Candidate candidate1 = lastTwo.get(0);
			Candidate candidate2 = lastTwo.get(1);
			
			if(candidate1.totalvotes[0] > candidate2.totalvotes[0])
				setWinner(candidate1);
			else if(candidate1.totalvotes[0] < candidate2.totalvotes[0])
				setWinner(candidate2);
			
			else { 
				
				if(candidate1.totalvotes[1] > candidate2.totalvotes[1])
					setWinner(candidate1);				
				else if(candidate1.totalvotes[1] < candidate2.totalvotes[1])
					setWinner(candidate2);

				else {
					
					if(candidate1.getId() > candidate2.getId())
						setWinner(candidate2);
					else
						setWinner(candidate1);
				}
			}
		}
		else
			setWinner(lastTwo.first());
		
	}
	
	
	/**Function that if no early winner has found, it starts to eliminate candidates that have the lower amount of votes
	 * with rank of 1, if there is a tie it looks between the candidates the one with the lower amount of votes with rank of 2, if a tie,
	 * it repeats the process but with the votes with one rank lower, if the votes of both candidates in tie are equal it eliminates
	 * the one with higher ID value.*/
	public void eliminationProcess() {
		
		//finding the lowest votes for rank1
		while(totalCandidates > 2) {
			int smallerVoteCount = 1000;
			int rank = 0;
			
			//finding the the candidates that have the lowest votes with a rank 1
			for(Candidate candidate: candidates) {
				if(candidate.isActive())
					if(candidate.totalvotes[0] < smallerVoteCount)
						smallerVoteCount = candidate.totalvotes[0];
			}
			

			ArrayList<Candidate> smallerVoteCandidatesTemp = new ArrayList<Candidate>(candidates.size());
			ArrayList<Candidate> smallerVoteCandidates = new ArrayList<Candidate>(candidates.size());

			//adding candidates to the arrays that will help us to sort and eliminate.
			for(Candidate candidate: candidates) {
				if(candidate.isActive())
					if(candidate.totalvotes[0] == smallerVoteCount)
						smallerVoteCandidates.add(candidate);
			}
			
			
			/*Process of checking if array has 2 or more candidates with the lower amount of votes
			 * in rank 1. */
			while (smallerVoteCandidates.size()>=2) {
		 
				smallerVoteCount = 1000;
				rank ++;
				for(Candidate candidate: smallerVoteCandidates) { // Looks for smaller count of votes with rank i
					if(candidate.totalvotes[rank] < smallerVoteCount)
						smallerVoteCount = candidate.totalvotes[rank];
				}
				
				for(Candidate candidate: smallerVoteCandidates) {// Adds candidates with lower votes with rank one 
					if(candidate.totalvotes[rank] == smallerVoteCount)
						smallerVoteCandidatesTemp.add(candidate);
				}
				
				smallerVoteCandidates.clear(); //Free up memory
				smallerVoteCandidates = smallerVoteCandidatesTemp; //Reuse previous arrayList
				
				if(smallerVoteCandidates.size() == 2) {
					//if votes are equal, we eliminate the one with highest Id.
					if(smallerVoteCandidates.get(0).totalvotes[0] == smallerVoteCandidates.get(1).totalvotes[0] &&
							smallerVoteCandidates.get(0).totalvotes[1] == smallerVoteCandidates.get(1).totalvotes[1]) {
						
						if(smallerVoteCandidates.get(0).getId() > smallerVoteCandidates.get(1).getId()) {
							smallerVoteCandidates.remove(smallerVoteCandidates.get(0));
						}
							
						else{
							smallerVoteCandidates.remove(smallerVoteCandidates.get(1));
						}
					}	
				}
			}
			this.eliminateCandidate(smallerVoteCandidates.get(0).getId()); //Eliminates the candidate felt in the array (the candidate with lower votes)
		}
		this.findWinnerLastTwo(); // finds winner between the last to candidates that are active.
	}
	
	
	public static void main(String[] args) {
		String pathBallots1 = "./inputFiles/ballots.csv"; //Path to ballots.csv file to be used.
		String pathBallots2 = "./inputFiles/ballots2.csv"; //Path to ballots2.csv file to be used.
		String pathBallots3 = "./inputFiles/ballots3.csv"; //Path to ballots3.csv file to be used.
		String pathCandidates = "./inputFiles/candidates.csv";//Path to candidates.csv to get the names of the candidates
		
		try {
			
			//Creation of the election process
			Election el = new Election(pathBallots2, pathCandidates);
			
			//Result.txt file creation 
			if(el.getWinner()!= null) {
				BufferedWriter bw = new BufferedWriter(new FileWriter ("./results.txt"));
				bw.write("Number of ballots: " + el.totalBallots);
				bw.newLine();
				bw.write("Number of blank ballots: " + el.emptyBallots);
				bw.newLine();
				bw.write("Number of invalid ballots: " + el.invalidBallots);
				bw.newLine();
				for(int i = 0; i < el.eliminated.size(); i++) {
					Candidate eli = el.eliminated.get(i);
					bw.write("Round "+ (i+1) + ": " + eli.getName() + " was eliminated with " + (eli.totalvotes[0]) + " #1's");
					bw.newLine();
				}
				bw.write("Winner: " + el.getWinner().getName() + " wins with " + el.getWinner().totalvotes[0] + " #1's");
				bw.flush();
			}
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}