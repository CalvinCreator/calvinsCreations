package us.blackjack.game;

import java.util.ArrayList;

public class Dealer
{
  private Deck deck;
  private ArrayList<Integer> hand;
  
  public Dealer() {
    deck = new Deck();
    hand = new ArrayList();
    hand.add(deck.getRandCard());
    hand.add(deck.getRandCard());
  }
  
  public void newHand() {
    hand.removeAll(hand);
    hand.add(deck.getRandCard());
    hand.add(deck.getRandCard());
  }
  
  public void deal(Player p) {
    p.addCard(deck.getRandCard().intValue());
    p.addCard(deck.getRandCard().intValue());
  }
  
  public void hit(Player p) {
    p.addCard(deck.getRandCard().intValue());
  }
  
  public void hit() {
    hand.add(deck.getRandCard());
  }
  
  public ArrayList<Integer> getHand() {
    return hand;
  }
  
  public void setHand(ArrayList<Integer> hand) {
    this.hand = hand;
  }
}
