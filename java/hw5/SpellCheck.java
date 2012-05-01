package hw5;

import java.util.HashMap;
import java.util.LinkedList;


/**
 * implements the viterbi algorithm for spellcheck problem
 * 
 * helpful links: 
 * 	http://www.comp.leeds.ac.uk/roger/HiddenMarkovModels/html_dev/main.html
 *  http://ftp.dcs.warwick.ac.uk/reports/cs-rr-238.ps.gz
 * @author tim
 */
public class SpellCheck
{
	private final char[] states; //the possible states
	private final char[] observations; // the observations per timeslot t 
	
	public SpellCharacters tchars;
	public SpellCharacters echars;

	public final boolean logOn=true; // turns Math.log on or off
	
	/**
	 * construct some umbrellas
	 */
	public SpellCheck(String good, String bad)
	{
		tchars = new SpellCharacters(good);
		echars = new SpellCharacters(bad);
				
		
		states = tchars.dict;
		
		observations = echars.string.toCharArray();

		
		System.out.println(observations);
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
		TransitionModel transitionModel = new TransitionModel();
		SensorModel sensorModel = new SensorModel();
		
		int t = 0; //time seq
		
		//initialize each state at t=0 to the initial probability
		HashMap<String, Node> ppaths = new HashMap<String, Node>();
		for (Character cstate : states)
		{
			String state = cstate.toString();
			if (logOn)
				ppaths.put(state, new Node(state, null, Math.log(initial_probability.get(state)), true));
			else
				ppaths.put(state, new Node(state, null, initial_probability.get(state)));
		}
		
		
		//forward compute
		for (Character ee : observations)  //for each observation in order of time t
		{
			String e = ee.toString();
			transitionModel.setLast(e);
			
			//System.out.println("OBS: " + e + " T: " + t);
			HashMap<String, Node> nppaths = new HashMap<String, Node>();
			
			for (Character ctostate : states) //for each possible state that we can go TO
			{
				String tostate = ctostate.toString();
				
				//System.out.println("TOSTATE: " + tostate);
				
				
				Double myprob;
				Node nn;
				if (logOn)
				{
					myprob = Math.log(1d);
					nn = new Node(null, null, Math.log(-0), true);
				}
				else
				{
					myprob = 1d;
					nn = new Node(null, null, 0d);
				}
				
				for (Node fromstate : ppaths.values())
				{
					//System.out.println("FROMSTATE: " + fromstate.state);
					
					myprob = fromstate.probability;
					
					Double predict = transitionModel.get(fromstate.state, tostate);
					Double update = sensorModel.get(tostate, e); ///TODO: should this be tostate? OMG IT SHOULD
					
					if (t==0)
						predict=1d; //on the 0th step we don't have sensor information
					
					Double both;
					if (logOn)
					{
						both = Math.log(predict) + Math.log(update);
						myprob += both;
					}
					else
					{
						both = predict * update;					
						myprob *= both;
					}
					
					//System.out.println(both);
					//System.out.println(myprob + " * "+predict+" * " + update + " = " + myprob * predict * update);

					nn.updateMax(tostate, fromstate, myprob);
					if (t==0) nn.parent=null; //if we're at zero make sure no parent
				}
				//transitionModel.setLast(e);
				if (nn.state!=null) //TODO: this is bad, this means that we aren't figuring out a path at every step
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
			Node max;
			if (logOn)
				max = new Node(null, null, Math.log(-0), true);
			else
				max = new Node(null, null, 0d);
			for (Character cstate : states)
			{
				String state = cstate.toString();
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
		if (logOn)
		{
			Double tots = 0.0;
			for (Node d : in.values())
			{
				tots = tots + Math.exp(d.probability);
			}
			
			Double alpha = 1.0/tots;
			
			for (Node s : in.values())
			{
				s.probability = Math.log(Math.exp(s.probability) * alpha);
			}
		}
		else
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

	
	
	
	/**
	 * this is the initial probabiltiy
	 * simply 1/27th for any letter for now
	 */
	private static class initial_probability
	{
		
		static Double get(String state)
		{
			return 1d/27d;
		}
	}
	
	/**
	 * this is the transition model
	 * stores the last few characters and give P(A | BC) probability
	 * TODO: this doesn't work right
	 */
	private class TransitionModel
	{
		LinkedList<String> last;
		
		TransitionModel()
		{
			last = new LinkedList<String>();
		}
		
		void setLast(String l)
		{
			last.addLast(l);
		}
		
		Double get(String fromstate, String tostate)
		{
			Double t = tchars.bigramCondProb(tostate.charAt(0), last.getLast() + fromstate);
			//System.out.println("P("+ tostate.charAt(0)+"|"+last.getLast() + fromstate + ") = " + t);
			//System.out.println(last);
			if (logOn)
				t = (t==0d) ? Math.log(-0) : t;
			return t;
		}

	}
	
	
	
	/**
	 * this is the sensor model, models the error probable in the text
	 *
	 */
	private class SensorModel
	{
		
		SensorModel()
		{
		}
		
		Double get(String tostate, String e)
		{
			if (e == tostate)
				return .9d;
			else
				return .1d;
		}
		

	}
	
}
