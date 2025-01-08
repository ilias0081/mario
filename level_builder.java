// Ilias Belassel
// level_builder.java

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

class Builder extends JFrame{  
  public Builder() {
    super("Level Builder"); 
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    add(new BuilderPanel());
    pack(); 
    setVisible(true);
   }
 
  public static void main(String[] args) {
    Builder frame = new Builder();
  }
}