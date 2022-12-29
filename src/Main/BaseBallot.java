package Main;

/**
 * @author Diego Martinez Garcia
 */
public interface BaseBallot {
	public int getBallotNum(); // returns the ballot number
	public int getRankByCandidate(int candidateID); // rank for that candidate
	public int getCandidateByRank(int rank); // candidate with that rank
	public boolean eliminate(int candidateId); // eliminates a candidate
}
