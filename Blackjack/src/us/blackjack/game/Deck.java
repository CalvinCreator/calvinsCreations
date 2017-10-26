package us.blackjack.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck
{
  private ArrayList<Integer> deck;
  
  public Deck()
  {
    createDeck();
  }
  
  private void createDeck()
  {
    deck = new ArrayList();
    

    for (int i = 1; i <= 13; i++) {
      deck.add(Integer.valueOf(i));
      deck.add(Integer.valueOf(i));
      deck.add(Integer.valueOf(i));
      deck.add(Integer.valueOf(i));
      deck.add(Integer.valueOf(i));
      deck.add(Integer.valueOf(i));
      deck.add(Integer.valueOf(i));
      deck.add(Integer.valueOf(i));
      deck.add(Integer.valueOf(i));
      deck.add(Integer.valueOf(i));
      deck.add(Integer.valueOf(i));
      deck.add(Integer.valueOf(i));
      deck.add(Integer.valueOf(i));
      deck.add(Integer.valueOf(i));
      deck.add(Integer.valueOf(i));
      deck.add(Integer.valueOf(i));
      deck.add(Integer.valueOf(i));
      deck.add(Integer.valueOf(i));
      deck.add(Integer.valueOf(i));
      deck.add(Integer.valueOf(i));
      deck.add(Integer.valueOf(i));
      deck.add(Integer.valueOf(i));
      deck.add(Integer.valueOf(i));
      deck.add(Integer.valueOf(i));
    }
    shuffle();
    
    int c = deck.size();
    for (int i = deck.size() - 1; i > c - 70; i--) {
      deck.remove(i);
    }
  }
  
  public void shuffle() {
    long seed = System.nanoTime();
    Collections.shuffle(deck, new Random(seed));
    Collections.shuffle(deck, new Random(seed));
  }
  
  public Integer getRandCard() {
    if (deck.size() == 0)
      createDeck();
    return (Integer)deck.remove((int)(Math.random() * deck.size()));
  }
  
  public void restart() {
    createDeck();
  }
}
