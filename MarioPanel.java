// Ilias Belassel
// MarioPanel.java
/*
CONTROLS:
- Left & Right arrow keys to move
- Space to jump
- Z to run
- E to shoot fireball
- ENTER to start game
*/

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.awt.*;
import java.util.*;
import javax.sound.midi.*;

class MarioPanel extends JPanel  implements ActionListener, KeyListener{
 javax.swing.Timer timer;
 boolean []keys;
 private Image back;
 private Image ground_block;
 private Image mario;
 static Font mario_font;
 InputStream that = MarioPanel.class.getResourceAsStream("font.ttf");
 
 int guyx = 200;
 int guyy = 300;
 int offset = 0;
 int vx = 0;
 int vy = 0;
 int acceleration = 0;
 int add_vx = 0;
 int vx_limit = 0;
 int loadingTimer = 0;
 // 0 is small, 1 is big, 2 is fire mario, 3 is star mario
 int marioStatus = 0;
 int invincible = 0;
 boolean starMario = false;
 int starMarioTimer = 0;
 int starMarioColor = 0;
 
 private int level;
 private int sub_level;

 int offset_max;
 Color[] star_colors = {Color.RED,Color.ORANGE,Color.YELLOW,Color.GREEN,Color.BLUE,Color.PINK,Color.RED};

 int runIndex = 1;
 int lastVx;
 int animationCounter = 0;

 int time = 15000;

 private Rectangle saveblock = new Rectangle(0,0,1,1);
 Rectangle a = saveblock;
 Rectangle standingOn = new Rectangle(0,0,1,1);
 boolean fallcheck = true;
 Rectangle player = new Rectangle(200,300,40,40);
 
 private int screen = 0;
 int lives = 3;
 int score = 0;
 int coinCount = 0;
 Color bg_color = Color.CYAN;

 ArrayList<int[][]> level_1 = new ArrayList<int[][]>();
 ArrayList<int[][]> level_2 = new ArrayList<int[][]>();

// IMPORTANT SETTINGS
 String f = ""; // PATH TO FOLDER (ex. "C:/Users/Folder/")
 boolean mario_static_animation = true; // true - static mario (fully working), false - animated mario (kind of works but very buggy and jittery)
 
 public ArrayList<Rectangle> all_rects  = new ArrayList<Rectangle>();
 public ArrayList<Rectangle> ground_rects  = new ArrayList<Rectangle>();
 public ArrayList<Rectangle> hard_rects  = new ArrayList<Rectangle>();
 
 public ArrayList<BrickBlock> brick_rects  = new ArrayList<BrickBlock>();
 public ArrayList<QuestionBlock> question_rects  = new ArrayList<QuestionBlock>();
 public ArrayList<Coin> coins = new ArrayList<Coin>(); 
 
 public ArrayList<Goomba> goombas = new ArrayList<Goomba>();
 public ArrayList<Koopa> koopas = new ArrayList<Koopa>();

 public ArrayList<Pipe> pipes = new ArrayList<Pipe>();

 public ArrayList<Mushroom> mushrooms = new ArrayList<Mushroom>();
 public ArrayList<FireFlower> fire_flowers = new ArrayList<FireFlower>();
 public ArrayList<Fireball> fireballs = new ArrayList<Fireball>();
 public ArrayList<Pirhanna> pirhannas = new ArrayList<Pirhanna>();
 public ArrayList<Star> stars = new ArrayList<Star>();
 public ArrayList<CoinPop> coinPops = new ArrayList<CoinPop>();
 public ArrayList<OneUp> one_ups = new ArrayList<OneUp>();
 public ArrayList<Score> Score = new ArrayList<Score>();

 Image[] small_mario_left = {new ImageIcon(f+"images/mario_sprites/small_mario/mario_stand_left.png").getImage().getScaledInstance(50, 40, ABORT), new ImageIcon(f+"images/mario_sprites/small_mario/mario_run1_left.png").getImage().getScaledInstance(50, 40, ABORT), new ImageIcon(f+"images/mario_sprites/small_mario/mario_run2_left.png").getImage().getScaledInstance(50, 40, ABORT), new ImageIcon(f+"images/mario_sprites/small_mario/mario_run3_left.png").getImage().getScaledInstance(50, 40, ABORT), new ImageIcon(f+"images/mario_sprites/small_mario/mario_jump_left.png").getImage().getScaledInstance(50, 40, ABORT)};
 Image[] small_mario_right = {new ImageIcon(f+"images/mario_sprites/small_mario/mario_stand_right.png").getImage().getScaledInstance(50, 40, ABORT), new ImageIcon(f+"images/mario_sprites/small_mario/mario_run1_right.png").getImage().getScaledInstance(50, 40, ABORT), new ImageIcon(f+"images/mario_sprites/small_mario/mario_run2_right.png").getImage().getScaledInstance(50, 40, ABORT), new ImageIcon(f+"images/mario_sprites/small_mario/mario_run3_right.png").getImage().getScaledInstance(50, 40, ABORT), new ImageIcon(f+"images/mario_sprites/small_mario/mario_jump_right.png").getImage().getScaledInstance(50, 40, ABORT)};

 Image[] big_mario_left = {new ImageIcon(f+"images/mario_sprites/big_mario/mario_stand_left.png").getImage().getScaledInstance(50, 85, ABORT), new ImageIcon(f+"images/mario_sprites/big_mario/mario_run1_left.png").getImage().getScaledInstance(50, 85, ABORT), new ImageIcon(f+"images/mario_sprites/big_mario/mario_run2_left.png").getImage().getScaledInstance(50, 85, ABORT), new ImageIcon(f+"images/mario_sprites/big_mario/mario_run3_left.png").getImage().getScaledInstance(50, 85, ABORT), new ImageIcon(f+"images/mario_sprites/big_mario/mario_jump_left.png").getImage().getScaledInstance(50, 85, ABORT)};
 Image[] big_mario_right = {new ImageIcon(f+"images/mario_sprites/big_mario/mario_stand_right.png").getImage().getScaledInstance(50, 85, ABORT), new ImageIcon(f+"images/mario_sprites/big_mario/mario_run1_right.png").getImage().getScaledInstance(50, 85, ABORT), new ImageIcon(f+"images/mario_sprites/big_mario/mario_run2_right.png").getImage().getScaledInstance(50, 85, ABORT), new ImageIcon(f+"images/mario_sprites/big_mario/mario_run3_right.png").getImage().getScaledInstance(50, 85, ABORT), new ImageIcon(f+"images/mario_sprites/big_mario/mario_jump_right.png").getImage().getScaledInstance(50, 85, ABORT)};

 Image[] fire_mario_left = {new ImageIcon(f+"images/mario_sprites/fire_mario/mario_stand_left.png").getImage().getScaledInstance(50, 85, ABORT), new ImageIcon(f+"images/mario_sprites/fire_mario/mario_run1_left.png").getImage().getScaledInstance(50, 85, ABORT), new ImageIcon(f+"images/mario_sprites/fire_mario/mario_run2_left.png").getImage().getScaledInstance(50, 85, ABORT), new ImageIcon(f+"images/mario_sprites/fire_mario/mario_run3_left.png").getImage().getScaledInstance(50, 85, ABORT), new ImageIcon(f+"images/mario_sprites/fire_mario/mario_jump_left.png").getImage().getScaledInstance(50, 85, ABORT)};;
 Image[] fire_mario_right = {new ImageIcon(f+"images/mario_sprites/fire_mario/mario_stand_right.png").getImage().getScaledInstance(50, 85, ABORT), new ImageIcon(f+"images/mario_sprites/fire_mario/mario_run1_right.png").getImage().getScaledInstance(50, 85, ABORT), new ImageIcon(f+"images/mario_sprites/fire_mario/mario_run2_right.png").getImage().getScaledInstance(50, 85, ABORT), new ImageIcon(f+"images/mario_sprites/fire_mario/mario_run3_right.png").getImage().getScaledInstance(50, 85, ABORT), new ImageIcon(f+"images/mario_sprites/fire_mario/mario_jump_right.png").getImage().getScaledInstance(50, 85, ABORT)};


 Image title;
 Image hard;
 public static Image brick;
 public static Image question;
 public static Image coin;
 public static Image empty_block;

 Image tmp;

 public static Image OneUp;
 public static Image FireFlower;
 public static Image Star;
 public static Image mushroom;
 Image small_coin;
 public static Image fireball;

 Image pipe;
 public static Image pipe_top;
 public static Image pipe_base;

 public static Image goomba;
 public static Image green_koopa;
 public static Image green_shell;
 public static Image red_koopa;
 public static Image red_shell;
 public static Image pirhanna;


 int fireBallWait = 0;

 private Image small_mario;
 private Image big_mario;
 private Image fire_mario;

 Image castle;
 Image flag;

  // 0 is standing mario
  // 1-3 is run 
  // 4 is jump
 
 public MarioPanel(){
  level_1.add(Levels.level_1);
  level_1.add(Levels.level_1_sub);

  level_2.add(Levels.level_2_start);
  level_2.add(Levels.level_2_main);
  level_2.add(Levels.level_2_sub);
  level_2.add(Levels.level_2_end);

  level = 1;
  sub_level = 0;
  lives = 3;



  try{
    mario_font = Font.createFont(Font.TRUETYPE_FONT, that).deriveFont(24f);
  }
  catch(IOException ex){
    System.out.println(ex);	
  }
  catch(FontFormatException ex){
    System.out.println(ex);	
  }



  //mario = new ImageIcon("images/mario.png").getImage().getScaledInstance(50, 50, ABORT);
  
  //back = new ImageIcon("images/background.png").getImage();
  //ground_block = new ImageIcon("images/ground_block.png").getImage().getScaledInstance(50, 50, ABORT);
  //mario = new ImageIcon("images/mario.png").getImage().getScaledInstance(50, 50, ABORT);
  back = new ImageIcon(f+"images/background.png").getImage();
  title = new ImageIcon(f+"images/title.png").getImage().getScaledInstance(800, 700, ABORT);
  fireball = new ImageIcon(f+"images/fireball.png").getImage().getScaledInstance(20, 20, ABORT);
  pirhanna = new ImageIcon(f+"images/pirhanna.png").getImage().getScaledInstance(50, 75, ABORT);

  small_mario = new ImageIcon(f+"images/small_mario.png").getImage().getScaledInstance(50, 40, ABORT);
  big_mario = new ImageIcon(f+"images/big_mario.png").getImage().getScaledInstance(50, 85, ABORT);
  fire_mario = new ImageIcon(f+"images/fire_mario.png").getImage().getScaledInstance(50, 85, ABORT);
  
  ground_block = new ImageIcon(f+"images/ground_block.png").getImage().getScaledInstance(50, 50, ABORT);
  hard = new ImageIcon(f+"images/hard_block_1.png").getImage().getScaledInstance(50, 50, ABORT);
  brick = new ImageIcon(f+"images/brick_block_1.png").getImage().getScaledInstance(50, 50, ABORT);
  question = new ImageIcon(f+"images/question_block_1.png").getImage().getScaledInstance(50, 50, ABORT);
  coin = new ImageIcon(f+"images/coin.png").getImage().getScaledInstance(50, 50, ABORT);
  small_coin = new ImageIcon(f+"images/coin.png").getImage().getScaledInstance(15, 15, ABORT);

  empty_block = new ImageIcon(f+"images/empty_block.png").getImage().getScaledInstance(50, 50, ABORT);

  OneUp = new ImageIcon(f+"images/1-up.png").getImage().getScaledInstance(50, 50, ABORT);
  FireFlower = new ImageIcon(f+"images/fireflower.png").getImage().getScaledInstance(50, 50, ABORT);
  Star = new ImageIcon(f+"images/star.png").getImage().getScaledInstance(50, 50, ABORT);
  mushroom = new ImageIcon(f+"images/mushroom.png").getImage().getScaledInstance(50, 50, ABORT);

  pipe = new ImageIcon(f+"images/pipe.png").getImage().getScaledInstance(100, 100, ABORT);
  pipe_top = new ImageIcon(f+"images/pipe_top.png").getImage().getScaledInstance(100, 50, ABORT);
  pipe_base = new ImageIcon(f+"images/pipe_base.png").getImage().getScaledInstance(100, 50, ABORT);

  goomba = new ImageIcon(f+"images/goomba.png").getImage().getScaledInstance(50, 50, ABORT);
  green_koopa = new ImageIcon(f+"images/green_koopa.png").getImage().getScaledInstance(50, 50, ABORT);
  green_shell = new ImageIcon(f+"images/green_shell.png").getImage().getScaledInstance(50, 50, ABORT);
  red_koopa = new ImageIcon(f+"images/red_koopa.png").getImage().getScaledInstance(50, 50, ABORT);
  red_shell = new ImageIcon(f+"images/red_shell.png").getImage().getScaledInstance(50, 50, ABORT);

  castle = new ImageIcon(f+"images/castle.png").getImage().getScaledInstance(250, 250, ABORT);
  flag = new ImageIcon(f+"images/flag_pole.png").getImage().getScaledInstance(75, 450, ABORT);

  setPreferredSize(new Dimension(800, 700));
  keys = new boolean[2000];
  setFocusable(true);
  requestFocus();
  addKeyListener(this);
  timer = new javax.swing.Timer(20, this);
  timer.start();  
 }
 // resets everything and gathers level data
 public void loadLevel(int[][] level){
  offset_max = level[0].length*50;
  mushrooms = new ArrayList<Mushroom>();
  fire_flowers = new ArrayList<FireFlower>();
  fireballs = new ArrayList<Fireball>();
  pirhannas = new ArrayList<Pirhanna>();
  stars = new ArrayList<Star>();
  coinPops = new ArrayList<CoinPop>();
  one_ups = new ArrayList<OneUp>();
  Score = new ArrayList<Score>();


  all_rects = new ArrayList<Rectangle>();
  ground_rects = Levels.get_blocks(level).get(0);
  all_rects.addAll(ground_rects);
  
  hard_rects = Levels.get_blocks(level).get(1);
  all_rects.addAll(hard_rects);
  
  brick_rects = Levels.get_bricks(level);
  for (int i = 0; i < brick_rects.size(); i++){
    all_rects.add(brick_rects.get(i).rect);
  }
  
  question_rects = Levels.get_question_blocks(level);
  for (int i = 0; i < question_rects.size(); i++){
    all_rects.add(question_rects.get(i).rect);
  }
  
  coins = Levels.get_coins(level);
  goombas = Levels.get_goombas(level);
  koopas = Levels.get_koopas(level);
  pipes = Levels.get_pipes(level);
  for (int i = 0; i < pipes.size(); i++){
    all_rects.add(pipes.get(i).rect);
  }
 }
 
 public void move(){
   if (fallcheck){
      add_vx = 1;
    }
    else{
      add_vx = 3;
    }
  if(keys[KeyEvent.VK_RIGHT]){
    // sprint
    if (keys[KeyEvent.VK_Z]){
      vx_limit = 15;
    }
    else{
      vx_limit = 10;
    }
    if (vx < vx_limit){
      vx += add_vx;
    }
    if (vx > vx_limit){
      vx = vx_limit;
    }   
  }
  if(keys[KeyEvent.VK_LEFT]){
    if (keys[KeyEvent.VK_Z]){
      vx_limit = -15;
    }
    else{
      vx_limit = -10;
    }
    if (vx > vx_limit){
      vx -= add_vx;
    }
    if (vx < vx_limit){
      vx = vx_limit;
    }   
  }
   // friction with the ground
  if (!fallcheck){
    if (vx > 0){
      vx -= 2;
      if (vx < 0){vx=0;}
    }
    if (vx < 0){
      vx += 2;
      if (vx > 0){vx=0;}
    }
  }
  // jump height based on speed
  if (keys[KeyEvent.VK_SPACE] && !fallcheck){
    if (vx <= 10 && vx >= -10){
      vy = -17;
    }
    else{
      vy = -21;
    }
    fallcheck = true;
  }
  fireBallWait--;
  if (keys[KeyEvent.VK_E] && marioStatus == 2 && fireBallWait <= 0){
    fireBallWait = 10;
    fireballs.add(new Fireball(player.x+player.width,player.y+player.height/2));
  }
 }

 // applies offset to every object
 public int a(int n){
  return n - offset;
 }
 @Override
 public void paint(Graphics g){
  //System.out.println(marioStatus);
  Graphics2D g2d = (Graphics2D)g;
  if (screen == 0){
    g2d.setColor(Color.cyan);
    g2d.drawImage(title,0,0,null);
    if (keys[KeyEvent.VK_ENTER]){
      screen = 2;
    }
  }
  // everytime you die or go to the next level, it runs through this
  if (screen == 2){
    g2d.setColor(Color.BLACK);
    g2d.fillRect(0,0,800,700);
    loadingTimer++;
    g2d.setColor(Color.WHITE);
    g2d.setFont(mario_font);
    g2d.drawImage(small_mario,330,320,null);
    g2d.drawString("WORLD 1-"+level,300,290);
    g2d.drawString(" x "+Integer.toString(lives),390,350);
    time = 15000;
    if (loadingTimer > 120){
      screen = 1;
      loadingTimer = 0;
      if (level == 1){
        loadLevel(level_1.get(sub_level));
      }
      if (level == 2){
        loadLevel(level_2.get(sub_level));
      }
      player.x = 150;
      offset = 0;
      player.y = 560;
      if (level == 3){
        loadLevel(Levels.level_3);
        player.x = 50;
        player.y = 500;
      }
    }
  } 
  // main game screen
  if (screen == 1){
    g2d.setColor(bg_color);
    g2d.fillRect(0,0,800,700);
    if (level == 1 && sub_level == 0){
      g2d.drawImage(flag,a(9925),100,null);
      g2d.drawImage(castle,a(10150),350,null);
    }
    if (level == 2){
      if (sub_level == 0){
        g2d.drawImage(castle,a(0),350,null);
      }
      if (sub_level == 3){
        g2d.drawImage(flag,a(1075),100,null);
        g2d.drawImage(castle,a(1300),350,null);
      }
    }

    // draws and updates every game object
    for (int i = 0; i < ground_rects.size(); i++){
      g2d.drawImage(ground_block,a(ground_rects.get(i).x),ground_rects.get(i).y,null);
      //collide(ground_rects.get(i));
    }
    for (int i = 0; i < hard_rects.size(); i++){
      g2d.drawImage(hard,a(hard_rects.get(i).x),hard_rects.get(i).y,null);
      //collide(hard_rects.get(i));
    }
    for (int i = 0; i < brick_rects.size(); i++){
      brick_rects.get(i).draw(g2d,offset);
    }
    for (int i = 0; i < question_rects.size(); i++){
      question_rects.get(i).draw(g2d,offset);
    }
    for (int i = 0; i < coinPops.size(); i++){
      coinPops.get(i).draw(g2d,offset,this);
    }
    for (int i = 0; i < coins.size(); i++){
      coins.get(i).draw(g2d,offset);
      coins.get(i).collide(this);
    }
    for (int i = 0; i < goombas.size(); i++){
      goombas.get(i).draw(g2d,offset);
      goombas.get(i).collide(this);
    }
    for (int i = 0; i < koopas.size(); i++){
      koopas.get(i).draw(g2d,offset);
      koopas.get(i).collide(this);
    }
    for (int i = 0; i < pirhannas.size(); i++){
      pirhannas.get(i).draw(g2d,offset);
      pirhannas.get(i).collide(this);
    }
    for (int i = 0; i < pipes.size(); i++){
      pipes.get(i).draw(g2d,offset,this);
    }
    for (int i = 0; i < mushrooms.size(); i++){
      mushrooms.get(i).draw(g2d,offset);
      mushrooms.get(i).collide(this);
    }
    for (int i = 0; i < stars.size(); i++){
      stars.get(i).draw(g2d,offset);
      stars.get(i).collide(this);
    }
    for (int i = 0; i < fire_flowers.size(); i++){
      fire_flowers.get(i).draw(g2d,offset);
      fire_flowers.get(i).collide(this);
    }
    for (int i = 0; i < one_ups.size(); i++){
      one_ups.get(i).draw(g2d,offset);
      one_ups.get(i).collide(this);
    }
    for (int i = 0; i < fireballs.size(); i++){
      fireballs.get(i).draw(g2d,offset);
      fireballs.get(i).collide(this);
    }
    for (int i = 0; i < Score.size(); i++){
      Score.get(i).draw(g2d,offset,this);
    }
    for (int i = 0; i < all_rects.size(); i++){
      collide(all_rects.get(i));
    }

    // altrenates between rainbow colours while star mario
    if (starMario){
      starMarioColor++;
      if (starMarioColor > 6){
        starMarioColor = 0;
      }
      g2d.setColor(star_colors[starMarioColor]);
      g2d.fillRect(a(player.x),player.y,player.width,player.height);
    }

    if (mario_static_animation) {
      if (marioStatus == 0){
        g2d.drawImage(small_mario,a(player.x)-5,player.y,null);
      }
      else if (marioStatus == 1){
        g2d.drawImage(big_mario,a(player.x)-5,player.y,null);
      }
      else if (marioStatus == 2){
        g2d.drawImage(fire_mario,a(player.x)-5,player.y,null);
      }
    }
    else {
      
      // mario run
      int x = a(player.x)-5;
      int y = player.y;
      if (vy == 0) {
          if (vx == 0) {
              if (marioStatus == 0) {
                  if (lastVx > 0) {
                      g2d.drawImage(small_mario_right[0], x, y, null);
                  } else {
                      g2d.drawImage(small_mario_left[0], x, y, null);
                  }
              } else if (marioStatus == 1) {
                  if (lastVx > 0) {
                      g2d.drawImage(big_mario_right[0], x, y, null);
                  } else {
                      g2d.drawImage(big_mario_left[0], x, y, null);
                  }
              } else if (marioStatus == 2) {
                  if (lastVx > 0) {
                      g2d.drawImage(fire_mario_right[0], x, y, null);
                  } else {
                      g2d.drawImage(fire_mario_left[0], x, y, null);
                  }
              }
          } else {
              if (marioStatus == 0) {
                  if (vx > 0) {
                      g2d.drawImage(small_mario_right[runIndex], x, y, null);
                  } else {
                      g2d.drawImage(small_mario_left[runIndex], x, y, null);
                  }
              } else if (marioStatus == 1) {
                  if (vx > 0) {
                      g2d.drawImage(big_mario_right[runIndex], x, y, null);
                  } else {
                      g2d.drawImage(big_mario_left[runIndex], x, y, null);
                  }
              } else if (marioStatus == 2) {
                  if (vx > 0) {
                      g2d.drawImage(fire_mario_right[runIndex], x, y, null);
                  } else {
                      g2d.drawImage(fire_mario_left[runIndex], x, y, null);
                  }
              }

              if(keys[KeyEvent.VK_Z]) {
                runIndex++;
                animationCounter = 0;
                if (runIndex == 3) {
                    runIndex = 1;
                }
              }else{
                  animationCounter++;
                  if(animationCounter == 2){
                      animationCounter = 0;
                      runIndex++;
                      if (runIndex == 3) {
                          runIndex = 1;
                      }
                  }
              }
              
              lastVx = vx;
          }
      } 
      else {
          if (marioStatus == 0) {
              if (lastVx > 0) {
                  g2d.drawImage(small_mario_right[4], x, y, null);
              } else {
                  g2d.drawImage(small_mario_left[4], x, y, null);
              }
          } else if (marioStatus == 1) {
              if (lastVx > 0) {
                  g2d.drawImage(big_mario_right[4], x, y, null);
              } else {
                  g2d.drawImage(big_mario_left[4], x, y, null);
              }
          } else if (marioStatus == 2) {
              if (lastVx > 0) {
                  g2d.drawImage(fire_mario_right[4], x, y, null);
              } else {
                  g2d.drawImage(fire_mario_left[4], x, y, null);
              }
          }
      }
    }
  }

  // when you die, it runs through this
  if (screen == -1){
    coinCount = 0;
    score = 0;
    lives = 3;
    g2d.setColor(Color.BLACK);
    g2d.setFont(mario_font);
    g2d.fillRect(0,0,800,700);
    g2d.setColor(Color.WHITE);
    g2d.drawString("GAME OVER :(",250,300);
    g2d.drawString("PRESS [ENTER]",250,350);
    g2d.drawString("TO RESTART",250,400);
    if (keys[KeyEvent.VK_ENTER]){
      screen = 0;
    }
  }

  // when you get to the best level
  if (screen == 3){
    g2d.setColor(Color.BLACK);
    g2d.setFont(mario_font.deriveFont(20f));
    g2d.fillRect(0,0,800,700);
    g2d.setColor(Color.WHITE);
    g2d.drawString("CONGRATS YOU FINISHED! WITHOUT PEACH...",10,200);
    g2d.drawString("IT'S OKAY, SHE WASN'T INTERESTED.",10,250);
    g2d.drawString("PLAY THE BONUS LEVEL ?   [PRESS ENTER]",10,300);
    if (keys[KeyEvent.VK_ENTER]){
      screen = 2;
      level = 3;
    }
  }
  // HUD
  if (screen != 0){
    g2d.setColor(Color.WHITE);
    g2d.setFont(mario_font);
    g2d.drawString("MARIO",50,50);
    g2d.drawString(Integer.toString(score),50,75);
    g2d.drawImage(small_coin,250,30,null);
    g2d.drawString(" x "+Integer.toString(coinCount),260,50);
    g2d.drawString("WORLD",450,50);
    g2d.drawString("1 - "+level,450,75);
    g2d.drawString("TIME",675,50);
    g2d.drawString(""+(int)(Math.floor(time/50)),700,75);
  }
 }

 // if fire mario (2) downgrades to big mario (1)
 // big mario to small mario (0)
 // small mario to death, game over if you have 0 lives
 public void takeHit(){
  if (invincible <= 0 && !starMario){
    if (marioStatus == 2){
      marioStatus = 1;
    }
    else if (marioStatus == 1){
      marioStatus = 0;
      player.y += 40;
    }
    else{
      lives--;
      if (lives == 0){
        screen = -1;
      }
      else{
        screen = 2;
      }
    }
  }
  if (invincible <= 0){
    invincible = 120;
  }
 }


 // basically for each side, it tries to detect if you slightly went inside the rect
 // by subtracting respective sides of the player and block with each other
 // once it knows, it teleports you out of the block and resets velocity values
 public void collide(Rectangle block){
    int pRight = player.x + player.width;
    int pBottom = player.y + player.height;
    int bRight = block.x + block.width;
    int bBottom = block.y + block.height;
    if (player.intersects(block)){
      // floor collisions
      if (block.y - pBottom >= -30 && block.y - pBottom < 0 && vy > 0){
        player.y = block.y - player.height;
        vy = 0;
        fallcheck = false;
        saveblock = block;
      }
      // ceiling collisions
      if (bBottom - player.y <=30 && bBottom - player.y > 0 && vy < 0){
        // checks if its a brick or question block
        for (int i = 0; i < brick_rects.size(); i++){
          if (block.x == brick_rects.get(i).rect.x && block.y == brick_rects.get(i).rect.y){
            brick_rects.get(i).collide(this);
          }
        }
        for(int i = 0; i < question_rects.size(); i++){
          if (block.x == question_rects.get(i).rect.x && block.y == question_rects.get(i).rect.y){
            question_rects.get(i).collide(this);
          }
        }
        player.y = bBottom;
        vy *= -0.5;
      }
      
      // left-hand collisions
      if (pBottom - block.y > 1){
      if (block.x + block.width - player.x <= 15){
        player.x = block.x + block.width;
        vx=0;
      }
      pRight = player.x + player.width;
      // right-hand collisions
      if (pRight - block.x <= 15){
        player.x = block.x - player.width;
        vx=0;
      }
      }
      
    }
    
 }



 @Override
 public void actionPerformed(ActionEvent e){

  if (screen == 1){
    //System.out.println("x: "+player.x+", y: "+player.y);
    move();
    invincible--;
    // movement with offset
    player.x += vx;
    if (a(player.x) > 350){
      offset += vx;
      player.x = 350 + offset;
    }
    if (a(player.x) < 150){
      offset += vx;
      player.x = 150 + offset;
      
    }

    if (coinCount == 100){
      lives += 1;
      coinCount = 0;
    }

    // star mario

    if (starMario){
      starMarioTimer--;
      if (starMarioTimer <= 0){
        starMario = false;
      }
      // kills everything mario touches, adds score
      for (int i = 0; i < goombas.size(); i++){
        if (goombas.get(i).rect.intersects(player)){
          Score.add(new Score(goombas.get(i).rect.x-offset,goombas.get(i).rect.y,100));
          goombas.remove(i);
          break;
        }
      }
      for (int i = 0; i < koopas.size(); i++){
        if (koopas.get(i).rect.intersects(player)){
          Score.add(new Score(koopas.get(i).rect.x-offset,koopas.get(i).rect.y,200));
          koopas.remove(i);
          break;
        }
      }
      for (int i = 0; i < pirhannas.size(); i++){
        if (pirhannas.get(i).rect.intersects(player)){
          Score.add(new Score(pirhannas.get(i).rect.x-offset,pirhannas.get(i).rect.y,200));
          pirhannas.remove(i);
          break;
        }
      }
    }

    // death by falling out of the level
    if (player.y > 700){
      marioStatus = 0;
      player.height = 40;
      lives -= 1;
      if (lives == 0){
        screen = -1;
      }
      else{
        screen = 2;
      }
      vx = 0;
    }
    time -= 1;
    if (time <= 0){
      time = 15000;
      lives -= 1;
      screen = 2;
    }


    // downwards acceleration
    player.y += vy;
    if (fallcheck){
      vy += 1;
    }
    // to detect if the player is still on the ground, it needs to keep a floor block that mario already
    // collided with. that is the ground block and once mario is off, mario starts to fall again
    // it makes a new rect of mario that collides with the floor block
    standingOn = new Rectangle(player.x,player.y+1,50,50);
    if (!standingOn.intersects(saveblock)){
      fallcheck = true;
    }
    if (marioStatus == 0){
      player.height = 40;
    }
    else if (marioStatus == 1){
      player.height = 80;
    }

    // transfers mario between sub areas of levels using the area above the pipe
    if (level == 1){
      if (player.intersects(new Rectangle(2850,320,100,100)) && keys[KeyEvent.VK_DOWN] && sub_level == 0){
        sub_level = 1;
        offset = 0;
        switchTheme("underground");
        loadLevel(level_1.get(sub_level));
        player.x = 60;
        player.y = 50;
      }
      if (sub_level == 1 && player.intersects(new Rectangle(650,410,100,100)) && keys[KeyEvent.VK_DOWN]){
        sub_level = 0;
        offset = 8000;
        loadLevel(level_1.get(sub_level));
        switchTheme("overworld");
        player.x = 8200;
        player.y = 420;
      }
      if (player.x > 9950){
        level = 2;
        sub_level = 0;
        screen = 2;
        player.x  = 300;
        player.y = 440;
        vx = 0;
        
      }
    }
    if (level == 2){
      if (sub_level == 0 && player.intersects(new Rectangle(600,410,100,100)) && keys[KeyEvent.VK_DOWN]){
        sub_level = 1;
        loadLevel(level_2.get(sub_level));
        switchTheme("underground");
        offset = 0;
        player.x = 100;
        player.y = 50;
      }
      if (sub_level == 1 && player.intersects(new Rectangle(5150,360,100,100)) && keys[KeyEvent.VK_DOWN]){
        sub_level = 2;
        loadLevel(level_2.get(sub_level));
        offset = 0;
        player.x = 50;
        player.y = 50;
        vx = 0;
      }
      if (sub_level == 2 && player.intersects(new Rectangle(650,500,100,100)) && keys[KeyEvent.VK_DOWN]){
        sub_level = 1;
        loadLevel(level_2.get(sub_level));
        offset = 5500;
        player.x = 5700;
        player.y = 410;
        vx = 0;
      }
      if (sub_level == 1 && player.intersects(new Rectangle(8200,270,100,100)) && keys[KeyEvent.VK_DOWN]){
        sub_level = 3;
        switchTheme("overworld");
        loadLevel(level_2.get(sub_level));
        offset = 0;
        player.x = 150;
        player.y = 400;
        vx = 0;
      }
      if (sub_level == 3 && player.x > 1125){
        screen = 3;
        level = 3;
        offset = 0;
        vx = 0;
      }
    }
  }
  repaint();
}

 // same as collide for the player but returns collisions for game objects
 public boolean[] collide(Rectangle rect1, Rectangle rect2){
  boolean[] collisions = {false,false,false};
  if (rect1.intersects(rect2)){
    // floor collisions
    if (rect2.y - (rect1.y + rect1.height) >= -30 && rect2.y - (rect1.y + rect1.height) < 0){
      collisions[0] = true;
    }
    
    // left-hand collisions
    if ((rect1.y + rect1.height) - rect2.y > 1){
    if (rect2.x + rect2.width - rect1.x <= 15){
      collisions[1] = true;
    }

    // right-hand collisions
    if ((rect1.x + rect1.width) - rect2.x <= 15){
      collisions[2] = true;
    }
    }
    
  }
  return collisions;
 }

 // loads overworld & underground images
 public void switchTheme(String theme){
  if (theme.equals("overworld")){
    bg_color = Color.CYAN;
    ground_block = new ImageIcon(f+"images/ground_block.png").getImage().getScaledInstance(50, 50, ABORT);
    hard = new ImageIcon(f+"images/hard_block_1.png").getImage().getScaledInstance(50, 50, ABORT);
    brick = new ImageIcon(f+"images/brick_block_1.png").getImage().getScaledInstance(50, 50, ABORT);
    goomba = new ImageIcon(f+"images/goomba.png").getImage().getScaledInstance(50, 50, ABORT);
  }
  if (theme.equals("underground")){
    bg_color = Color.BLACK;
    ground_block = new ImageIcon(f+"images/ground_block_2.png").getImage().getScaledInstance(50, 50, ABORT);
    hard = new ImageIcon(f+"images/hard_block_2.png").getImage().getScaledInstance(50, 50, ABORT);
    brick = new ImageIcon(f+"images/brick_block_2.png").getImage().getScaledInstance(50, 50, ABORT);
    goomba = new ImageIcon(f+"images/goomba_2.png").getImage().getScaledInstance(50, 50, ABORT);
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
}