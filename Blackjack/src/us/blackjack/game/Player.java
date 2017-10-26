package us.blackjack.game;

import java.util.ArrayList;

public class Player
{
  private ArrayList<Integer> hand;
  private String username;
  
  public Player(String username)
  {
    hand = new ArrayList();
    this.username = username;
  }
  



  public void addCard(int card)
  {
    hand.add(Integer.valueOf(card));
  }
  
  public String getUsername() {
    return username;
  }
  
  public ArrayList<Integer> getHand() {
    return hand;
  }
  
  public void setHand(ArrayList<Integer> hand) {
    this.hand = hand;
  }
  
  public String toString() {
    String s = username + " ";
    for (Integer i : hand) {
      s = s + i + ", ";
    }
    return s;
  }
  
  public void newHand() {
    hand.removeAll(hand);
  }
}
