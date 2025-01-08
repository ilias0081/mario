// Ilias Belassel
// Mario.java

import javax.sound.midi.Sequencer;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.sound.midi.*;

class Mario extends JFrame{

  public Mario() {
    super("Super Mario Bros."); 
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    add(new MarioPanel());
    pack(); 
    setVisible(true);
   }	
 
  public static void main(String[] args) {
    Mario frame = new Mario();
  }
}