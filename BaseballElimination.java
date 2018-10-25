import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BaseballElimination {

	private class Score {
		public Score(int w, int l, int r) {
			this.w = w;
			this.l = l;
			this.r = r;
		}
		public int w;
		public int l;
		public int r;
	}

	private ArrayList<String> teams;
	private ArrayList<Score> scores;
	private int [][] g;

	// create a baseball division from given filename in format specified below
	public BaseballElimination(String filename) {
		In in = new In(filename);
		int count = in.readInt();
		teams = new ArrayList<>();
		scores = new ArrayList<>();
		g = new int [count][count];

		for (int i=0; i<count; i++) {
			String name = in.readString();
			teams.add(name);

			String w = in.readString();
			String l = in.readString();
			String r = in.readString();

 			Score score = new Score(Integer.parseInt(w), Integer.parseInt(l), Integer.parseInt(r));
			scores.add(score);

			for (int j=0; j<count; j++) {
				g[i][j] = Integer.parseInt(in.readString());
			}
		}
	}

	// number of teams
	public int numberOfTeams() {
		return teams.size();
	}

	private void check(String team) {
		if (team == null || !teams.contains(team)) {
			throw new java.lang.IllegalArgumentException();
		}
	}

	// all teams
	public Iterable<String> teams() {
		return teams;
	}

	// number of wins for given team
	public int wins(String team) {
		check(team);
		return scores.get(teams.indexOf(team)).w;
	}

	// number of losses for given team
	public int losses(String team) {
		check(team);
		return scores.get(teams.indexOf(team)).l;
	}

	// number of remaining games for given team
	public int remaining(String team) {
		check(team);
		return scores.get(teams.indexOf(team)).r;
	}

	// number of remaining games between team1 and team2
	public int against(String team1, String team2) {
		check(team1);
		check(team2);
		return g[teams.indexOf(team1)][teams.indexOf(team2)];
	}

	// is given team eliminated?
	public boolean isEliminated(String team) {
		check(team);
		return certificateOfElimination(team) != null;
	}

	private FordFulkerson build(String team) {
		int teamId = teams.indexOf(team);
		int count = numberOfTeams();
		int graphNode = count * (count-1) / 2 + 2;
		FlowNetwork flowNetwork = new FlowNetwork(graphNode);

		int id=1;
		int append=(count-1)*(count-2)/2 + 1;

		for (int i=0; i<count; i++) {
			for (int j=i+1; j<count; j++) {
				if (i == teamId || j == teamId) {
					continue;
				}

				int nodeId = id++;
				FlowEdge edge1 = new FlowEdge(0, nodeId,g[i][j]);
				int a = i>teamId ? i-1 : i;
				int b = j>teamId ? j-1 : j;
				FlowEdge edge2 = new FlowEdge(nodeId, append+a,Double.MAX_VALUE);
				FlowEdge edge3 = new FlowEdge(nodeId, append+b,Double.MAX_VALUE);

				flowNetwork.addEdge(edge1);
				flowNetwork.addEdge(edge2);
				flowNetwork.addEdge(edge3);
			}
		}

		for (int i=0; i<count; i++) {
			if (i == teamId) {
				continue;
			}

			Score score = scores.get(teamId);

			int a = i > teamId ? i-1 : i;

			FlowEdge edge = new FlowEdge(append+a, graphNode-1,
					score.w + score.r - scores.get(i).w);
			flowNetwork.addEdge(edge);
		}

		return new FordFulkerson(flowNetwork, 0, graphNode-1);
	}

	// subset R of teams that eliminates given team; null if not eliminated
	public Iterable<String> certificateOfElimination(String team) {
		check(team);

		ArrayList<String> array = new ArrayList<>();

		int teamId = teams.indexOf(team);
		Score s = scores.get(teamId);
		int max = s.w + s.r;
		for (int i=0; i<scores.size(); i++) {
			if (scores.get(i).w > max) {
				array.add(teams.get(i));
			}
		}

		if (array.size() == 0) {
			FordFulkerson ff = build(team);
			int count = numberOfTeams();
			int append=(count-1)*(count-2)/2 + 1;
			for (int i=0; i<count; i++) {
				if (i == teamId) {
					continue;
				}
				int a = i>teamId ? i-1 : i;
				if (ff.inCut(append+a)) {
					array.add(teams.get(i));
				}
			}
		}

		if (array.size() == 0) {
			return null;
		}

		return array;
	}

	public static void main(String[] args) {
		BaseballElimination division = new BaseballElimination("teams4.txt");
		for (String team : division.teams()) {
			if (division.isEliminated(team)) {
				StdOut.print(team + " is eliminated by the subset R = { ");
				for (String t : division.certificateOfElimination(team)) {
					StdOut.print(t + " ");
				}
				StdOut.println("}");
			}
			else {
				StdOut.println(team + " is not eliminated");
			}
		}
	}
}
