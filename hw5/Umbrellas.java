package hw5;

import java.util.HashMap;

/**
 * implements the viterbi algorithm for umbrella/weather problem
 * 
 * helpful links: 
 * 	http://www.comp.leeds.ac.uk/roger/HiddenMarkovModels/html_dev/main.html
 *  http://ftp.dcs.warwick.ac.uk/reports/cs-rr-238.ps.gz
 * @author tim
 */
public class Umbrellas
{
	private final HashMap<String, Double> initial_probability; //for t=0
	private final HashMap<String, HashMap<String, Double>> transitionModel; //the probabilities for state transitions P(X_t+1|x_t)
	private final HashMap<String, HashMap<String, Double>> sensorModel; //probability model relating sensors to states P(e_t+t|x_t+1)
	private final String[] states; //the possible states
	private final String[] observations; // the observations per timeslot t 

	
	/**
	 * construct some umbrellas
	 */
	public Umbrellas()
	{
		initial_probability = new HashMap<String, Double>(); 
		transitionModel = new HashMap<String, HashMap<String, Double>>();
		sensorModel = new HashMap<String, HashMap<String, Double>>(); 
		states = new String[] {"+Rain","-Rain"}; 
		observations = new String[] {"+Umbrella", "+Umbrella", "-Umbrella", "+Umbrella", "+Umbrella"}; 

		initial_probability.put("+Rain", 0.5);
		initial_probability.put("-Rain", 0.5);
		
		HashMap<String, Double> rainT = new HashMap<String, Double>();
		HashMap<String, Double> norainT = new HashMap<String, Double>();
		HashMap<String, Double> rainS = new HashMap<String, Double>();
		HashMap<String, Double> norainS = new HashMap<String, Double>();

		rainT.put("+Rain", .7);
		rainT.put("-Rain", .3);
		norainT.put("+Rain", .3); //these don't have to be symmetrical
		norainT.put("-Rain", .7);
		
		rainS.put("+Umbrella", .9); 
		rainS.put("-Umbrella", .2);
		norainS.put("+Umbrella", .2);
		norainS.put("-Umbrella", .9);
		
		transitionModel.put("+Rain", rainT);
		transitionModel.put("-Rain", norainT);
		sensorModel.put("+Rain", rainS);
		sensorModel.put("-Rain", norainS);
	}
	
	
	/**
	 * run viterbi
	 * a * P(e_t+1|X_t+1) max in x_t( P(X_t+1|x_t) max in x_1...x_t-1 P(x_1,...,x_t-1,x_t|e_1:t))
	 * a is a normalizing factor
	 * P(e_t+1|X_t+1) comes directly from the sensor model
	 * max in x_t... best probable state
	 * max in x_1...x_t-1... best probable path
	 */
	public void viterbi()
	{
		int t = 0; //time seq
		
		//initialize each state at t=0 to the initial probability
		HashMap<String, Node> ppaths = new HashMap<String, Node>();
		for (String state : states)
		{
			ppaths.put(state, new Node(state, null, initial_probability.get(state), initial_probability.get(state)));
		}
		
		System.out.println(ppaths);
		
		//forward compute
		for (String e : observations)  //for each observation in order of time t
		{
			t++;
			System.out.println("t: " + t);
			HashMap<String, Node> nppaths = new HashMap<String, Node>();
			for (String tostate : states) //for each possible state that we can go TO
			{
				System.out.println("tostate: " + tostate);
				Node nn = new Node(null, null, 0d, 0d);
				double sum = 0;
				for (Node fromstate : ppaths.values()) //calculate predict and update 
				{
					
					
					System.out.println("fromstate: " + fromstate);
					
					
					Double mystateprobability = 
						transitionModel.get(fromstate.state).get(tostate) * //predict
						fromstate.state_probability; //carry along  P(x_1,...,x_t-1,x_t|e_1:t)
					Double mypathprobability =  
						transitionModel.get(fromstate.state).get(tostate) * //predict
						fromstate.path_probability; //carry along  P(x_1,...,x_t-1,x_t|e_1:t)

					System.out.println("sp: " + 
										    	transitionModel.get(fromstate.state).get(tostate) + " * " + 
												fromstate.state_probability + " = " +
										    	transitionModel.get(fromstate.state).get(tostate) * //predict
												fromstate.state_probability); //carry along  P(x_1,...,x_t-1,x_t|e_1:t)
//					
//					System.out.println("pp: " + 
//										    	transitionModel.get(fromstate.state).get(tostate) + " * " + 
//										    	sensorModel.get(fromstate.state).get(e) + " * " +
//												fromstate.path_probability + " = " +
//										    	transitionModel.get(fromstate.state).get(tostate) * //predict
//										    	sensorModel.get(fromstate.state).get(e) *  //P(e_t+1|X_t+1) update
//												fromstate.path_probability); //carry along  P(x_1,...,x_t-1,x_t|e_1:t)
//					Double mystateprobability = 
//										    	transitionModel.get(fromstate.state).get(tostate) * //predict
//										    	sensorModel.get(fromstate.state).get(e) *  //P(e_t+1|X_t+1) update
//												fromstate.state_probability; //carry along  P(x_1,...,x_t-1,x_t|e_1:t)
//					Double mypathprobability =  
//				    							transitionModel.get(fromstate.state).get(tostate) * //predict
//				    							sensorModel.get(fromstate.state).get(e) *  //P(e_t+1|X_t+1) update
//				    							fromstate.path_probability; //carry along  P(x_1,...,x_t-1,x_t|e_1:t)
					sum = sum + mystateprobability;
					nn.updateMax(tostate, fromstate, mystateprobability, mypathprobability);
				}
				System.out.println("total: " + sum);
				nn.state_probability = sum;
				System.out.println("newstate: " + nn);
				nppaths.put(tostate, nn);
			}
			ppaths = nppaths;
		}
		
		
		//choose
		if (t==observations.length)
		{
			Node max = new Node(null, null, 0d, 0d);
			double sum = 0;
			for (String state : states)
			{
				Node from = ppaths.get(state);
				sum = sum + from.state_probability;
				max.updateMax(from.state, from.parent, from.state_probability, from.state_probability);
			}
			max.state_probability = sum;
			System.out.println(max);
		}
		
	}
	
	
	
	
	
	/**
	 * normalizez
	 * @param in
	 * @return
	 */
	private HashMap<String, Double> normalize(HashMap<String, Double> in)
	{
		Double tots = 0.0;
		for (Double d : in.values())
		{
			tots = tots + d;
		}
		
		HashMap<String, Double> n = new HashMap<String, Double>();
		Double alpha = 1.0/tots;
		
		for (String s : in.keySet())
		{
			n.put(s, in.get(s) * alpha);
		}
		
		return n;
	}
	
}
