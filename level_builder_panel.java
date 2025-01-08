// Ilias Belassel
// level_builder_panel.java

/*
HOW TO USE:
- Left and right arrow keys are used to move through the level
- For the blocks, items, and enemies, click on the icons
to altrenate between them
- Selected items are applied to bricks and question blocks
- Press space to print your level into a double array

- Draw tool allows you to place any object onto the grid
- Erase allows you to delete anything on the grid
(for pipes, click on the top left corner to delete)


*/ 

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.lang.Thread;

class BuilderPanel extends JPanel  implements ActionListener, KeyListener, MouseListener{
 javax.swing.Timer timer;
 boolean []keys;
 private Image back;
 private int offset = 0;
 private int selection = 1;
 private int itemSelect = 0;
 private int enemySelect = 1;

 // keeps track of which type of object is selected
 private boolean onBlocks = true;
 boolean onPipes = false;
 private boolean onEnemies = false;
 //                 draw  erase  info
 private boolean[] tools = {true, false, false};

 private int pipeLength = 2;
 private boolean isPirhanna = false;

 
 private ArrayList<Rectangle> ground_blocks = new ArrayList<Rectangle>(); 
 private ArrayList<Rectangle> hard_blocks = new ArrayList<Rectangle>();
 private ArrayList<Rectangle> coins = new ArrayList<Rectangle>();
 private ArrayList<int[]> brick_blocks = new ArrayList<int[]>();
 private ArrayList<int[]> question_blocks = new ArrayList<int[]>();
 private ArrayList<int[]> pipes = new ArrayList<int[]>();

 private ArrayList<int[]> enemies = new ArrayList<int[]>();

 private ArrayList<Rectangle> all_rects = new ArrayList<Rectangle>();

 private Image ground;
 private Image hard;
 private Image brick;
 private Image question;
 private Image coin;

 private Image tmp;

 private Image OneUp;
 private Image FireFlower;
 private Image Star;

 private Image pipe;
 private Image pipe_top;
 private Image pipe_base;

 private Image goomba;
 private Image green_koopa;
 private Image red_koopa;


 // IMPORTANT SETTINGS
 String f = ""; // PATH TO FOLDER (ex. "C:/Users/Folder/")
 
 public BuilderPanel(){
  ground = new ImageIcon(f+"images/ground_block.png").getImage().getScaledInstance(50, 50, ABORT);
  hard = new ImageIcon(f+"images/hard_block_1.png").getImage().getScaledInstance(50, 50, ABORT);
  brick = new ImageIcon(f+"images/brick_block_1.png").getImage().getScaledInstance(50, 50, ABORT);
  question = new ImageIcon(f+"images/question_block_1.png").getImage().getScaledInstance(50, 50, ABORT);
  coin = new ImageIcon(f+"images/coin.png").getImage().getScaledInstance(50, 50, ABORT);

  OneUp = new ImageIcon(f+"images/1-up.png").getImage().getScaledInstance(50, 50, ABORT);
  FireFlower = new ImageIcon(f+"images/fireflower.png").getImage().getScaledInstance(50, 50, ABORT);
  Star = new ImageIcon(f+"images/star.png").getImage().getScaledInstance(50, 50, ABORT);

  pipe = new ImageIcon(f+"images/pipe.png").getImage().getScaledInstance(100, 100, ABORT);
  pipe_top = new ImageIcon(f+"images/pipe_top.png").getImage().getScaledInstance(100, 50, ABORT);
  pipe_base = new ImageIcon(f+"images/pipe_base.png").getImage().getScaledInstance(100, 50, ABORT);

  goomba = new ImageIcon(f+"images/goomba.png").getImage().getScaledInstance(50, 50, ABORT);
  green_koopa = new ImageIcon(f+"images/green_koopa.png").getImage().getScaledInstance(50, 50, ABORT);
  red_koopa = new ImageIcon(f+"images/red_koopa.png").getImage().getScaledInstance(50, 50, ABORT);



  back = new ImageIcon("back.png").getImage();
  addMouseListener(this);
  setPreferredSize( new Dimension(800, 900));
  keys = new boolean[2000];
  setFocusable(true);
  requestFocus();
  addKeyListener(this);
  timer = new javax.swing.Timer(20, this);
  timer.start();  
 }
 Image[] selectList = {ground,hard,brick,question,coin};
 
 @Override
  public void mousePressed(MouseEvent e) {
    // main grid
    if (e.getY() < 700){
      boolean notDuplicate = true;
      // gets x & y and converts it into coordinates
      double x = Math.floor((e.getX()+offset)/50.0);
      double y = Math.floor(e.getY()/50.0);
      for (int i = 0; i < all_rects.size(); i++){
        if (all_rects.get(i).x == (int)(x*50) && all_rects.get(i).y == (int)(y*50)){
          notDuplicate = false;
        }

      }
      if (notDuplicate && tools[0]){
        //System.out.println("hi");
        if(onBlocks){
          if (selection == 1){
            ground_blocks.add(new Rectangle((int)(x*50),(int)(y*50),50,50));
          }
          if (selection == 2){
            hard_blocks.add(new Rectangle((int)(x*50),(int)(y*50),50,50));
          }
          if (selection == 3){
            brick_blocks.add(new int[]{(int)(x*50),(int)(y*50),30+itemSelect});
          }
          if (selection == 4){
            question_blocks.add(new int[]{(int)(x*50),(int)(y*50),40+itemSelect});
          }
          if (selection == 5){
            coins.add(new Rectangle((int)(x*50),(int)(y*50),50,50));
          }
          all_rects.add(new Rectangle((int)(x*50),(int)(y*50),50,50));
        }
      if (onPipes){
          if (isPirhanna){
            pipes.add(new int[]{(int)(x*50),(int)(y*50),pipeLength,6});
          }
          else{
            pipes.add(new int[]{(int)(x*50),(int)(y*50),pipeLength,5});
          }
          all_rects.add(new Rectangle((int)(x*50),(int)(y*50),100,50*pipeLength));
        }
      if (onEnemies){
        if (enemySelect == 1){
          enemies.add(new int[]{(int)(x*50),(int)(y*50),4});
        }
        if (enemySelect == 2){
          enemies.add(new int[]{(int)(x*50),(int)(y*50),5});
        }
        if (enemySelect == 3){
          enemies.add(new int[]{(int)(x*50),(int)(y*50),6});
        }
        all_rects.add(new Rectangle((int)(x*50),(int)(y*50),50,50));
      }
      }
      if (tools[1]){
        x = (int)(x*50);
        y = (int)(y*50);
        for (int i = 0; i < ground_blocks.size(); i++){
          Rectangle tmp = ground_blocks.get(i);
          if (x == tmp.x && y == tmp.y){
            ground_blocks.remove(i);
            break;
          }
        }
        for (int i = 0; i < hard_blocks.size(); i++){
          Rectangle tmp = hard_blocks.get(i);
          if (x == tmp.x && y == tmp.y){
            hard_blocks.remove(i);
            break;
          }
        }
        for (int i = 0; i < brick_blocks.size(); i++){
          int[] tmp = brick_blocks.get(i);
          if (x == tmp[0] && y == tmp[1]){
            brick_blocks.remove(i);
            break;
          }
        }
        for (int i = 0; i < question_blocks.size(); i++){
          int[] tmp = question_blocks.get(i);
          if (x == tmp[0] && y == tmp[1]){
            question_blocks.remove(i);
            break;
          }
        }
        for (int i = 0; i < coins.size(); i++){
          Rectangle tmp = coins.get(i);
          if (x == tmp.x && y == tmp.y){
            coins.remove(i);
            break;
          }
        }

        for (int i = 0; i < all_rects.size(); i++){
          Rectangle tmp = all_rects.get(i);
          if (x == tmp.x && y == tmp.y){
            all_rects.remove(i);
            break;
          }
        }
        // pipes
        for (int i = 0; i < pipes.size(); i++){
          int[] tmp = pipes.get(i);
          if (x == tmp[0] && y == tmp[1]){
            pipes.remove(i);
            break;
          }
        }
        // enemies
        for (int i = 0; i < enemies.size(); i++){
          int[] tmp = enemies.get(i);
          if (x == tmp[0] && y == tmp[1]){
            enemies.remove(i);
            break;
          }
        }
      }
    }
    else{
      if((new Rectangle(460,710,50,50)).inside((int)(e.getX()),(int)(e.getY()))){
        tools[0] = true;
        tools[1] = false;
        tools[2] = false;
      }
      if((new Rectangle(460,770,50,50)).inside((int)(e.getX()),(int)(e.getY()))){
        tools[0] = false;
        tools[1] = true;
        tools[2] = false;
      }
      if((new Rectangle(460,830,50,50)).inside((int)(e.getX()),(int)(e.getY()))){
        tools[0] = false;
        tools[1] = false;
        tools[2] = true;
      }

      if ((new Rectangle(10,710,50,50)).inside((int)(e.getX()),(int)(e.getY()))){
        selection++;
        if (selection > 5){
          selection = 1;
        }
      }
      if ((new Rectangle(10,810,50,50)).inside((int)(e.getX()),(int)(e.getY()))){
        itemSelect++;
        if (itemSelect > 5){
          itemSelect = 0;
        }
      }
      if ((new Rectangle(270,710,20,20)).inside((int)(e.getX()),(int)(e.getY()))){
        pipeLength++;
        if (pipeLength > 11){
          pipeLength = 11;
        }
      }
      if ((new Rectangle(270,735,20,20)).inside((int)(e.getX()),(int)(e.getY()))){
        pipeLength--;
        if (pipeLength < 2){
          pipeLength = 2;
        }
      }
      if ((new Rectangle(160,850,20,20)).inside((int)(e.getX()),(int)(e.getY()))){
        if (isPirhanna){
          isPirhanna = false;
        }
        else{
          isPirhanna = true;
        }
      }
      if ((new Rectangle(320,710,20,20)).inside((int)(e.getX()),(int)(e.getY()))){
        onPipes = true;
        onBlocks = false;
        onEnemies = false;
      }
      if ((new Rectangle(120,710,20,20)).inside((int)(e.getX()),(int)(e.getY()))){
        onPipes = false;
        onBlocks = true;
        onEnemies = false;
      }
      if ((new Rectangle(420,710,20,20)).inside((int)(e.getX()),(int)(e.getY()))){
        onPipes = false;
        onBlocks = false;
        onEnemies = true;
      }
      if ((new Rectangle(360,710,50,50)).inside((int)(e.getX()),(int)(e.getY()))){
        enemySelect++;
        if (enemySelect > 3){
          enemySelect = 1;
        }
      }

    }
  }
 
 public void move(){
  if(keys[KeyEvent.VK_RIGHT]){    
   offset += 20;
  }
  if(keys[KeyEvent.VK_LEFT]){    
   offset -= 20;
   if (offset < 0){offset = 0;}
  }
 }
 
 public int a(int n){
  return n - offset;
 }
 
 @Override
 public void paint(Graphics g){
  Graphics2D g2d = (Graphics2D)g;
  // main
  g2d.setColor(Color.WHITE);
  g2d.fillRect(0,0,800,700);
  // enemies
  for (int i = 0; i < enemies.size(); i++){
    int[] tmp = enemies.get(i);
    if (tmp[2] == 4){
      g2d.drawImage(goomba,a(tmp[0]),tmp[1],null);
    }
    if (tmp[2] == 5){
      g2d.drawImage(green_koopa,a(tmp[0]),tmp[1],null);
    }
    if (tmp[2] == 6){
      g2d.drawImage(red_koopa,a(tmp[0]),tmp[1],null);
    }
  }
  // pipe
  for (int i = 0; i < pipes.size(); i++){
    int[] tmp = pipes.get(i);
    g2d.drawImage(pipe_top,a(tmp[0]),tmp[1],null);
    for (int a = 1; a < tmp[2]; a++){
      g2d.drawImage(pipe_base,a(tmp[0]),tmp[1]+a*50,null);
    }
  }
  // blocks
  for (int i = 0; i < ground_blocks.size(); i++){
    g2d.drawImage(ground,a(ground_blocks.get(i).x),ground_blocks.get(i).y,null);
  }
  for (int i = 0; i < hard_blocks.size(); i++){
    g2d.drawImage(hard,a(hard_blocks.get(i).x),hard_blocks.get(i).y,null);
  }
  for (int i = 0; i < brick_blocks.size(); i++){
    g2d.drawImage(brick,a(brick_blocks.get(i)[0]),brick_blocks.get(i)[1],null);
  }
  for (int i = 0; i < question_blocks.size(); i++){
    g2d.drawImage(question,a(question_blocks.get(i)[0]),question_blocks.get(i)[1],null);
  }
  for (int i = 0; i < coins.size(); i++){
    g2d.drawImage(coin,a(coins.get(i).x),coins.get(i).y,null);
  }
  g2d.setColor(Color.BLACK);
  for (int i = 0; i < 15000; i+=50){
    g2d.drawLine(a(i),0,a(i),700);
  }
  for (int i = 0; i < 700; i+=50){
    g2d.drawLine(0,i,800,i);
  }
  // toolbar
  g2d.setColor(Color.LIGHT_GRAY);
  g2d.fillRect(0,700,800,200);
  if (selection == 1){
    g2d.drawImage(ground,10,710,null);
  }
  if (selection == 2){
    g2d.drawImage(hard,10,710,null);
  }
  if (selection == 3){
    g2d.drawImage(brick,10,710,null);
  }
  if (selection == 4){
    g2d.drawImage(question,10,710,null);
  }
  if (selection == 5){
    g2d.drawImage(coin,10,710,null);
  }
  
  if (onBlocks){
    g2d.setColor(Color.BLACK);
  }
  else{
    g2d.setColor(Color.WHITE);
  }
  g2d.fillRect(120,710,20,20);

  if (itemSelect == 0){
    g2d.setColor(Color.WHITE);
    g2d.fillRect(10,810,50,50);
  }
  if (itemSelect == 1){
    g2d.drawImage(coin,10,810,null);
  }
  if (itemSelect == 2){
    g2d.drawImage(FireFlower,10,810,null);
  }
  if (itemSelect == 3){
    g2d.drawImage(Star,10,810,null);
  }
  if (itemSelect == 4){
    g2d.drawImage(OneUp,10,810,null);
  }
  if (itemSelect == 5){
    g2d.drawImage(coin,10,810,null);
    g2d.setColor(Color.BLACK);
    g2d.setFont(new Font("Arial",Font.PLAIN, 25));
    g2d.drawString("+",50,860);
  }
  // enemies
  if (enemySelect == 1){
    g2d.drawImage(goomba, 360,710,null);
  }
  if (enemySelect == 2){
    g2d.drawImage(green_koopa, 360,710,null);
  }
  if (enemySelect == 3){
    g2d.drawImage(red_koopa, 360,710,null);
  }
  // pipe
  g2d.setFont(new Font("Arial",Font.PLAIN, 25));
  g2d.drawImage(pipe,160,710,null);
  g2d.setColor(Color.WHITE);
  g2d.fillRect(270, 710, 20, 20);
  g2d.fillRect(270, 735, 20, 20);
  g2d.setColor(Color.BLACK);
  g2d.drawLine(280,715,280,725);
  g2d.drawLine(275,720,285,720);
  g2d.drawLine(275,745,285,745);
  g2d.setFont(new Font("Arial",Font.BOLD, 15));
  g2d.drawString("Height: "+Integer.toString(pipeLength),160,825);
  g2d.drawString("Pirhanna",190,865);
  if (isPirhanna){
    g2d.setColor(Color.BLACK);
  }
  else{
    g2d.setColor(Color.WHITE);
  }
  g2d.fillRect(160,850,20,20);
  if (onEnemies){
    g2d.setColor(Color.BLACK);
  }
  else{
    g2d.setColor(Color.WHITE);
  }
  g2d.fillRect(420,710,20,20);

  if (onPipes){
    g2d.setColor(Color.BLACK);
  }
  else{
    g2d.setColor(Color.WHITE);
  }
  g2d.fillRect(320,710,20,20);

  g2d.setFont(new Font("Arial",Font.BOLD, 15));
  g2d.setColor(Color.BLACK);
  g2d.drawString("Selected Block", 10, 780);
  g2d.drawString("Selected Item", 10, 880);
  g2d.drawLine(150,700,150,900);
  g2d.drawLine(350,700,350,900);
  g2d.drawLine(450,700,450,900);
  // tools
  if (tools[0]){
    g2d.setColor(Color.BLACK);
  }
  else{
    g2d.setColor(Color.WHITE);
  }
  g2d.fillRect(460,710,50,50);
  if (tools[1]){
    g2d.setColor(Color.BLACK);
  }
  else{
    g2d.setColor(Color.WHITE);
  }
  g2d.fillRect(460,770,50,50);
  if (tools[2]){
    g2d.setColor(Color.BLACK);
  }
  else{
    g2d.setColor(Color.WHITE);
  }
  g2d.fillRect(460,830,50,50);
  g2d.setColor(Color.BLACK);
  g2d.drawString("Draw",520,735);
  g2d.drawString("Erase",520,795);
  g2d.drawString("Info",520,855);
 }

 @Override
 public void actionPerformed(ActionEvent e){
  move();
  repaint();
  if (keys[KeyEvent.VK_SPACE]){
    int length = 0;
    for (int i = 0; i < all_rects.size(); i++){
      if (all_rects.get(i).x / 50 > length){
        length = all_rects.get(i).x / 50;
      }
    }
    // adding to primitive array
    // it converts the x and y into a grid coordinate so that it can add it
    // to the double array
    int[][] level = new int[14][length+1];
    // blocks
    for (int i = 0; i < ground_blocks.size(); i++){
      level[ground_blocks.get(i).y/50][ground_blocks.get(i).x/50] = 1;
    }
    for (int i = 0; i < hard_blocks.size(); i++){
      level[hard_blocks.get(i).y/50][hard_blocks.get(i).x/50] = 2;
    }
    for (int i = 0; i < brick_blocks.size(); i++){
      level[brick_blocks.get(i)[1]/50][brick_blocks.get(i)[0]/50] = brick_blocks.get(i)[2];
    }
    for (int i = 0; i < question_blocks.size(); i++){
      level[question_blocks.get(i)[1]/50][question_blocks.get(i)[0]/50] = question_blocks.get(i)[2];;
    }
    for (int i = 0; i < coins.size(); i++){
      level[coins.get(i).y/50][coins.get(i).x/50] = 3;
    }
    // pipes
    for (int i = 0; i < pipes.size(); i++){
      int[] tmp = pipes.get(i);
      level[tmp[1]/50][tmp[0]/50] = tmp[3]*10 + tmp[2] - 2;
    }
    // enemies
    for (int i = 0; i < enemies.size(); i++){
      int[] tmp = enemies.get(i);
      level[tmp[1]/50][tmp[0]/50] = tmp[2];
    }
    String array = "####################################################\n{";
    for (int y = 0; y < level.length; y++){
      array += "{";
      for (int x = 0; x < level[0].length; x++){
        if (level[y][x] < 10){
          array += " "+Integer.toString(level[y][x])+",";
        }
        else{
          array += Integer.toString(level[y][x])+",";
        }
      }
      array = array.substring(0, array.length()-1);
      array += "},\n";
    }
    array = array.substring(0, array.length()-2);
    array += "}";
    System.out.println(array);
  }
 }
 @Override
 public void keyPressed(KeyEvent e){  
  int code = e.getKeyCode();
  keys[code] = true; 
 }
 
 @Override
 public void keyReleased(KeyEvent e){
  int code = e.getKeyCode();
  keys[code] = false; 
 }
 
 @Override
 public void keyTyped(KeyEvent e){} 


@Override
 public void mouseReleased(MouseEvent e){}
@Override
 public void mouseEntered(MouseEvent e){}

 @Override
 public void mouseExited(MouseEvent e){}

 @Override
 public void mouseClicked(MouseEvent e){


 }
}