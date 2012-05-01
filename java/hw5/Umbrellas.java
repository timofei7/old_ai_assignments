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
		//observations = new String[] {"+Umbrella", "+Umbrella", "+Umbrella"};

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
		rainS.put("-Umbrella", .1); //TODO: not .2
		norainS.put("+Umbrella", .2);
		norainS.put("-Umbrella", .8); //TODO: not .9
		
		transitionModel.put("+Rain", rainT);
		transitionModel.put("-Rain", norainT);
		sensorModel.put("+Rain", rainS);
		sensorModel.put("-Rain", norainS);
		
		System.out.println("my models: ");
		System.out.println(transitionModel);
		System.out.println(sensorModel);
		System.out.println();
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
			ppaths.put(state, new Node(state, null, initial_probability.get(state)));
		}
		
		
		//forward compute
		for (String e : observations)  //for each observation in order of time t
		{
			//System.out.println("OBS: " + e + " T: " + t);
			HashMap<String, Node> nppaths = new HashMap<String, Node>();
			
			for (String tostate : states) //for each possible state that we can go TO
			{
				//System.out.println("TOSTATE: " + tostate);
				
				
				Double myprob =1d;				
				Node nn = new Node(null, null, 0d);
				
				for (Node fromstate : ppaths.values())
				{
					//System.out.println("FROMSTATE: " + fromstate.state);
					
					myprob = fromstate.probability;
					
					Double predict = transitionModel.get(fromstate.state).get(tostate);
					Double update = sensorModel.get(tostate).get(e); ///TODO: should this be tostate? OMG IT SHOULD
					
					if (t==0) predict=1d; //on the 0th step we don't have sensor information
					
					Double both = predict*update;
					
					//System.out.println(myprob + " * "+predict+" * " + update + " = " + myprob * predict * update);
					
					myprob *= both;
					
					nn.updateMax(tostate, fromstate, myprob);
					if (t==0) nn.parent=null; //if we're at zero make sure no parent
				}
				nppaths.put(tostate, nn);
										
			}
			if (t==0) normalize(nppaths); // normalize the first step ignore the rest
			System.out.println(nppaths);
			ppaths = nppaths;
			t++;
		}
		
		
		//choose
		if (t==observations.length)
		{
			System.out.println("most probable sequence:");
			Node max = new Node(null, null, 0d);
			for (String state : states)
			{
				Node from = ppaths.get(state);
				max.updateMax(from.state, from.parent, from.probability);
			}
			System.out.println(max);
		}
		
	}
	
	
	
	/**
	 * normalizez
	 * @param in
	 * @return
	 */
	private void normalize(HashMap<String, Node> in)
	{
		Double tots = 0.0;
		for (Node d : in.values())
		{
			tots = tots + d.probability;
		}
		
		Double alpha = 1.0/tots;
		
		for (Node s : in.values())
		{
			s.probability = s.probability * alpha;
		}
	}

	
}
