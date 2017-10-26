package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import bot.FightBot;

public class Simulator {

	private int populationSize;
	private String[] population; // random population
	private int[] fitness;
	private int generation;
	private float avgFitScore;
	private String[] functions = { "A", "S", "M", "D", "Cos", "Sin", "ATan" };
	
	private String[] terminal = { "Rand", "Const", "PosX", "PosY", "EPosX", "EPosY", "EAngle" };
	
	// position of enemy

	public static FightBot cBot, eBot; // currentBot enemyBot
	private Point cBotSpawn, eBotSpawn;
	public static Rectangle bounds;

	private int currentBot;
	private String enemy;
	public boolean draw = true;

	public static final int BOT_SIZE = 30;

	private int tickCount;
	private int matchLength;

	public Simulator() {
		populationSize = 300;
		population = new String[populationSize];
		for (int i = 0; i < population.length; i++)
			if (i % 2 == 0)
				population[i] = genRandFullTree(3);
			else
				population[i] = genRandHalfnHalfTree(3);
		System.out.println("\n\nGeneration " + generation);
		for(int i = 0; i < population.length; i++)
			System.out.println("Brain " + i + ": " + population[i]);
		fitness = new int[population.length];
		for (int i = 0; i < fitness.length; i++)
			fitness[i] = 10000;

		generation = 1;
		avgFitScore = 0;

		cBotSpawn = new Point(550, 550);
		eBotSpawn = new Point(0, 0);
		cBot = new FightBot(cBotSpawn.x, cBotSpawn.y);
		eBot = new FightBot(eBotSpawn.x, eBotSpawn.y);
		cBot.setEnemy(eBot);
		eBot.setEnemy(cBot);

		bounds = new Rectangle(0, 0, 600, 600);

		currentBot = 0;
		cBot.setBrain(population[currentBot]);
		enemy = population[(int)(Math.random() * population.length)];
		eBot.setBrain(enemy);

		tickCount = 0;
		matchLength = 500;
	}

	public long render(Graphics2D g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, 600, 630);

		if (!fitnessMet()) { // if requirements for ending the entire program
								// arn't met

			if (testingBots()) { // currentBot < popsize
				cBot.update();
				eBot.update();

				updateBullets(); // also checks for collision with bullets
				if(draw)
					drawMap(g);

				if (++tickCount > matchLength) { // current bot's fight is done
					int fitScore = (cBot.getLives() - eBot.getLives()) * 100; //higher is better
					if(fitScore > 0)
						fitScore *= 2; //encourage killing enemy
					System.out.println(fitScore);
					fitness[currentBot] = fitScore;

					currentBot++;

					tickCount = 0;

					cBot = new FightBot(cBotSpawn.x, cBotSpawn.y);
					cBot.setEnemy(eBot);
					eBot.setLocation(eBotSpawn.x, eBotSpawn.y);
					eBot.resetLives();
					if (currentBot < populationSize)
						cBot.setBrain(population[currentBot]);
				}

			} else { // creating next generation
				g.setColor(Color.black);
				g.setFont(new Font("Impact", 12, 30));
				g.drawString("CREATING GENERATION " + ++generation + "...", 160, 300);

				int total = 0;
				for(int i = 0; i < fitness.length; i++)
					total += fitness[i];
				avgFitScore = total / fitness.length;
				
				int max = 0;
				for(int i = 0; i < fitness.length; i++)
					if(fitness[i] > fitness[max])
						max = i;
				
				createNextGeneration();

				currentBot = 0;
				tickCount = 0;

				cBot = new FightBot(cBotSpawn.x, cBotSpawn.y);
				eBot = new FightBot(eBotSpawn.x, eBotSpawn.y);
				cBot.setEnemy(eBot);
				eBot.setEnemy(eBot);
				cBot.setBrain(population[currentBot]);
				eBot.setBrain(enemy);

				return 1500; // pauses thread for 1000 milliseconds
			}
			return 0; // no pause
		} else {
			// what to do if fitness has been met
			return -1; // closes the program
		}
	} // render

	private void updateBullets() {
		// Update bullets
		for (int i = 0; i < cBot.getBullets().size(); i++) {
			cBot.getBullets().get(i).update();
			Ellipse2D.Float eBot = new Ellipse2D.Float(Simulator.eBot.getX(), Simulator.eBot.getY(), 30, 30);
			if (eBot.contains(new Rectangle((int) cBot.getBullets().get(i).getX() - 1,
					(int) cBot.getBullets().get(i).getY() - 1, 3, 3))) {
				Simulator.eBot.looseLife();
				cBot.getBullets().remove(i--);
			}
		}
		for (int i = 0; i < eBot.getBullets().size(); i++) {
			eBot.getBullets().get(i).update();
			Ellipse2D.Float cBot = new Ellipse2D.Float(Simulator.cBot.getX(), Simulator.cBot.getY(), 30, 30);
			if (cBot.contains(new Rectangle((int) eBot.getBullets().get(i).getX() - 1,
					(int) eBot.getBullets().get(i).getY() - 1, 3, 3))) {
				Simulator.cBot.looseLife();
				eBot.getBullets().remove(i--);
			}
		}
	}

	private void drawMap(Graphics2D g) {
		g.setColor(Color.black);
		g.drawLine(0, 600, 600, 600);
		g.drawLine(0, 601, 600, 601);

		int ellipseSize = BOT_SIZE;
		Ellipse2D.Float cBot = new Ellipse2D.Float(Simulator.cBot.getX(), Simulator.cBot.getY(), ellipseSize,
				ellipseSize);
		Ellipse2D.Float eBot = new Ellipse2D.Float(Simulator.eBot.getX(), Simulator.eBot.getY(), ellipseSize,
				ellipseSize);

		g.setColor(Color.blue);
		g.fill(cBot);
		g.drawLine((int) cBot.getCenterX(), (int) cBot.getCenterY(),
				(int) (30 * Math.cos(this.cBot.getMoveAngle())) + (int) cBot.getCenterX(),
				(int) cBot.getCenterY() + (int) (30 * Math.sin(this.cBot.getMoveAngle())));

		g.setColor(Color.red);
		g.fill(eBot);
		g.drawLine((int) eBot.getCenterX(), (int) eBot.getCenterY(),
				(int) (30 * Math.cos(this.eBot.getMoveAngle())) + (int) eBot.getCenterX(),
				(int) eBot.getCenterY() + (int) (30 * Math.sin(this.eBot.getMoveAngle())));

		g.setColor(Color.black);
		for (int o = 0; o < Simulator.cBot.getBullets().size(); o++)
			g.fillRect((int) Simulator.cBot.getBullets().get(o).getX() - 1,
					(int) Simulator.cBot.getBullets().get(o).getY() - 1, 3, 3);
		for (int o = 0; o < Simulator.eBot.getBullets().size(); o++)
			g.fillRect((int) Simulator.eBot.getBullets().get(o).getX() - 1,
					(int) Simulator.eBot.getBullets().get(o).getY() - 1, 3, 3);

		g.setFont(new Font("Impact", 12, 18));
		g.drawString("Generation : " + generation, 5, 622);
		g.drawString("Bot " + (currentBot + 1), 500, 622);
		g.drawString("Average Fitness Score: " + avgFitScore, 210, 622);
	}

	// returns if the program can end
	private boolean fitnessMet() {
		// TODO: WRITE FITNESS MET
		return false;
	}

	private boolean testingBots() {
		return currentBot < populationSize;
	}

	private void createNextGeneration() {
		String[] newPopulation = new String[populationSize];
		
		int crossCount = (int)(populationSize * 0.9);
		int mutCount = (int)(populationSize * 0.05);
		int copyCount = populationSize - crossCount - mutCount;

		//90% through crossover
		for(int i = 0; i < crossCount; i++) 
			newPopulation[i] = crossover(chooseByTournament(), chooseByTournament());
		
		//5% by copying
		for(int i = crossCount; i < crossCount + copyCount; i++) 
			newPopulation[i] = chooseByTournament();
		
		
		//5% by mutation
		for(int i = crossCount + copyCount; i < populationSize; i++) 
			newPopulation[i] = mutateByPoint(chooseByTournament()); 
		
		population = newPopulation;
		
		System.out.println("\n\nGeneration " + generation);
		for(int i = 0; i < population.length; i++)
			System.out.println("Brain " + i + ": " + population[i]);
	}
	
	//chooses two random members and returns the most fit (the one with the lowest fitness value)
	private String chooseByTournament() {
		int first = (int)(Math.random() * population.length);
		int second = (int)(Math.random() * population.length);
		if(fitness[first] < fitness[second])
			return population[first];
		return population[second];
	}

	//switches the subtree of one tree at a random point with a subtree of the other at a random point
	private String crossover(String parent1, String parent2) {
		TreeNode one = prefixToTree(parent1.split(" "));
		TreeNode two = prefixToTree(parent1.split(" "));
		
		//Choose a random node from treeOne
		LinkedList<TreeNode> list = new LinkedList<TreeNode>();
		list.add(one);
		int index = 0;
		while(index < list.size()) {
			if(list.get(index).left != null)
				list.add(list.get(index).left);
			if(list.get(index).right != null)
				list.add(list.get(index).right);
			index++;
		}
		TreeNode oneRand = list.get( (int)(Math.random() * list.size()) );
		
		//Choose a random node from treeTwo
		list = new LinkedList<TreeNode>();
		list.add(two);
		index = 0;
		while(index < list.size()) {
			if(list.get(index).left != null)
				list.add(list.get(index).left);
			if(list.get(index).right != null)
				list.add(list.get(index).right);
			index++;
		}
		TreeNode twoRand = list.get( (int)(Math.random() * list.size()) );
		
		//Check of oneRand is root and if so return twoRand
		if(oneRand.equals(one)) 
			return twoRand.toString(); 
		
		
		//Find Parent of oneRand so we can change it to point at twoRand
		list = new LinkedList<TreeNode>();
		list.add(one);
		index = 0;
		boolean left = true;
		while(index < list.size()) {
			if(list.get(index).left == oneRand) {
				oneRand = list.get(index);
				break;
			} else if(list.get(index).right == oneRand) {
				oneRand = list.get(index);
				left = false;
				break;
			}
			if(list.get(index).left != null)
				list.add(list.get(index).left);
			if(list.get(index).right != null)
				list.add(list.get(index).right);
			index++;
		}
		
		if(left)
			oneRand.left = twoRand;
		else
			oneRand.right = twoRand;

		return one.toString();
	}
	
	private String mutateByPoint(String original) {
		String[] exp = original.split(" ");
		TreeNode t = prefixToTree(exp);
		t = mutateTreePointByPoint(t);
		return t.toString();
	}

	private TreeNode mutateTreePointByPoint(TreeNode t) {
		if (t == null)
			return null;

		if (Math.random() > 0.9) { // 10% mutation rate
			if (t.getValue().equals("A") || t.getValue().equals("S") || t.getValue().equals("M")
					|| t.getValue().equals("D") || t.equals("ATan")) {

				String s = functions[(int) (Math.random() * functions.length)];
				if (s.equals("Cos") || s.equals("Sin"))
					t.right = null;
				t.setValue(s);

			} else if (t.getValue().equals("Cos") || t.getValue().equals("Sin")) {

				String s = functions[(int) (Math.random() * functions.length)];
				if (s.equals("A") || s.equals("S") || s.equals("M") || s.equals("D") || s.equals("ATan")) {
					String right = terminal[(int) (Math.random() * terminal.length)];
					if (right.equals("Const"))
						right = (Math.random() * 20 - 10) + "";
					t.setRight(new TreeNode(right, null, null));
				}
				t.setValue(s);
			} else {
				String s = terminal[(int) (Math.random() * terminal.length)];
				if (s.equals("Const"))
					s = (Math.random() * 20 - 10) + "";
				t.setValue(s);
			}
		}
		t.left = mutateTreePointByPoint(t.left);
		t.right = mutateTreePointByPoint(t.right);
		return t;
	}

	// generates and returns a genetic program using the Full method with a
	// depth 'depth'
	public String genRandFullTree(int depth) {
		return genFullTree(0, depth).toString();
	}

	private TreeNode genFullTree(int depth, int maxDepth) {
		if (depth > maxDepth) {
			String s = terminal[(int) (Math.random() * terminal.length)];
			if (s.equals("Const"))
				s = (Math.random() * 20 - 10) + "";
			return new TreeNode(s, null, null);
		}
		String func = functions[(int) (Math.random() * functions.length)];
		if (func.equals("Cos") || func.equals("Sin"))
			return new TreeNode(func, genFullTree(depth + 1, maxDepth), null);
		return new TreeNode(func, genFullTree(depth + 1, maxDepth), genFullTree(depth + 1, maxDepth));
	}

	// generates and returns a genetic program using the Half and Half method
	// with a depth 'depth'
	public String genRandHalfnHalfTree(int depth) {
		return genHalfTree(0, depth).toString();
	}

	// recursive helper method for genRandHalfnHalf
	private TreeNode genHalfTree(int depth, int maxDepth) {
		if (depth > maxDepth || Math.random() > 0.5) {
			String s = terminal[(int) (Math.random() * terminal.length)];
			if (s.equals("Const"))
				s = (Math.random() * 20 - 10) + "";
			return new TreeNode(s, null, null);
		}
		String func = functions[(int) (Math.random() * functions.length)];
		if (func.equals("Cos") || func.equals("Sin"))
			return new TreeNode(func, genFullTree(depth + 1, maxDepth), null);
		return new TreeNode(func, genFullTree(depth + 1, maxDepth), genFullTree(depth + 1, maxDepth));

	}

	// takes in an expression as an array of Strings and turns it into a Tree
	private TreeNode prefixToTree(String[] exp) {
		Stack<TreeNode> stack = new Stack<TreeNode>();
		for (int i = exp.length - 1; i >= 0; i--) {

			if (exp[i].equals("A") || exp[i].equals("S") || exp[i].equals("D") || exp[i].equals("M") || exp[i].equals("ATan"))
				stack.add(new TreeNode(exp[i], stack.pop(), stack.pop()));
			else if (exp[i].equals("Cos") || exp[i].equals("Sin"))
				stack.add(new TreeNode(exp[i], stack.pop(), null));
			else
				stack.push(new TreeNode(exp[i], null, null));

		} // for loop
		return stack.pop();
	}

	private class TreeNode {

		private TreeNode left, right;
		private String val;

		public TreeNode(String val, TreeNode left, TreeNode right) {
			this.val = val;
			this.left = left;
			this.right = right;
		}

		@SuppressWarnings("unused")
		public void setLeft(TreeNode t) {
			left = t;
		}

		@SuppressWarnings("unused")
		public void setRight(TreeNode t) {
			right = t;
		}

		public void setValue(String s) {
			val = s;
		}

		@SuppressWarnings("unused")
		public String getValue() {
			return val;
		}

		@Override
		public String toString() {
			String left = this.left == null ? "" : this.left.toString().trim();
			String right = this.right == null ? "" : this.right.toString().trim();
			return val + " " + left + " " + right;
		}

	}

}
