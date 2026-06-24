import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import javax.swing.*;

public class PacMan extends JPanel implements ActionListener, KeyListener {

    private int rowCount = MazeData.ROW_COUNT;
    private int columnCount = MazeData.COLUMN_COUNT;
    private int tileSize = 32;
    private int hudHeight = 64;
    private int boardWidth = columnCount * tileSize;
    private int mazeHeight = rowCount * tileSize;
    private int boardHeight = mazeHeight + hudHeight;

    private Image wallImage;
    private Image houseWallImage; 
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    
    private String[][] mazeMaps = MazeData.MAZE_MAPS;
    private String[] mazeNames = MazeData.MAZE_NAMES;
    private int selectedMap = 0;
    private String[] tileMap; 

    HashSet<Block> walls;       
    HashSet<Block> houseWalls;  
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    Timer gameLoop;
    char[] directions = {'U', 'D', 'L', 'R'}; 
    Random random = new Random();
    int score = 0;
    int lives = 3;
    boolean gameOver = false;

    
    
    private int houseExitTargetY;

    
    
    
    private enum GhostMode { SCATTER, CHASE }
    private GhostMode ghostMode = GhostMode.SCATTER;
    private int modeTimer = 0;
    
    private int[] scatterChaseSchedule = { 7*20, 20*20, 7*20, 20*20, 5*20, 20*20, 5*20, 999999 };
    private int scheduleIndex = 0;

    
    private boolean dying = false;
    private int deathAnimTimer = 0;
    private static final int DEATH_ANIM_LENGTH = 30; 

    
    private enum GameState { MENU, PLAYING, GAME_OVER }
    private GameState gameState = GameState.MENU;

    
    private Skin[] skins = Skin.ALL;
    private int selectedSkin = 0;

    
    private double menuPacX = -20;
    private int menuAnimTimer = 0;
    private int menuMouthAngle = 0; 
    private boolean menuMouthOpening = true;

    private MenuScreen menuScreen;

    PacMan() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        houseWallImage = wallImage;
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

        menuScreen = new MenuScreen(boardWidth, boardHeight);

        tileMap = mazeMaps[selectedMap];

        loadMap();
        GhostHouseDelays();
        gameLoop = new Timer(50, this);
        gameLoop.start();

    }

    public void loadMap() {
        walls = new HashSet<Block>();
        houseWalls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c*tileSize;
                int y = r*tileSize + hudHeight;

                if (tileMapChar == 'X') {
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                }
                else if (tileMapChar == 'H') {
                    
                    Block wall = new Block(houseWallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                    houseWalls.add(wall);
                }
                else if (tileMapChar == 'G') {
                    
                    
                    Block gate = new Block(houseWallImage, x, y, tileSize, tileSize);
                    walls.add(gate);
                    houseWalls.add(gate);
                    houseExitTargetY = y; 
                }
                else if (tileMapChar == 'b') {
                    Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghost.inHouse = true;
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'o') {
                    Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghost.inHouse = true;
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'p') {
                    Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghost.inHouse = true;
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'r') {
                    Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghost.inHouse = true;
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'P') {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                }
                else if (tileMapChar == ' ') {
                    Block food = new Block(null, x + 14, y + 14, 4, 4);
                    foods.add(food);
                }
            }
        }
    }

    
    
    private void GhostHouseDelays() {
        int delay = 0;
        for (Block ghost : ghosts) {
            ghost.inHouse = true;
            ghost.exitingHouse = false;
            ghost.velocityX = 0;
            ghost.velocityY = 0;
            ghost.houseExitDelay = delay;
            delay += 3 * 20; 
        }
    }

    
    private void tickMouthAnimation() {
        if (menuMouthOpening) {
            menuMouthAngle += 6;
            if (menuMouthAngle >= 50) menuMouthOpening = false;
        } else {
            menuMouthAngle -= 6;
            if (menuMouthAngle <= 0) menuMouthOpening = true;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameState == GameState.MENU) {
            menuScreen.draw(g, menuPacX, menuMouthAngle, menuAnimTimer,
                    skins, selectedSkin, mazeMaps, mazeNames, selectedMap);
        } else {
            draw(g);
        }
    }

    public void draw(Graphics g) {
        if (g instanceof Graphics2D) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        
        
        int houseX = 7 * tileSize;
        int houseY = 8 * tileSize + hudHeight;
        int houseW = 5 * tileSize;
        int houseH = 3 * tileSize;
        g.setColor(new Color(15, 15, 45));
        g.fillRoundRect(houseX, houseY, houseW, houseH, 12, 12);
        g.setColor(new Color(90, 50, 150));
        g.drawRoundRect(houseX, houseY, houseW, houseH, 12, 12);

        
        if (dying) {
            drawDeathAnimation(g);
        } else {
            PacmanRenderer.draw(g, pacman.x, pacman.y, pacman.width, pacman.height,
                    pacman.direction, skins[selectedSkin].color, menuMouthAngle);
        }

        for (Block ghost : ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        
        
        int gap = 3;       
        int arc = 7;       
        for (Block wall : walls) {
            if (houseWalls.contains(wall)) {
                
                g.setColor(new Color(100, 60, 180));
                g.fillRoundRect(wall.x + gap, wall.y + gap,
                        wall.width - gap * 2, wall.height - gap * 2, arc, arc);
                g.setColor(new Color(160, 110, 240));
                g.drawRoundRect(wall.x + gap, wall.y + gap,
                        wall.width - gap * 2, wall.height - gap * 2, arc, arc);
            } else {
                
                g.setColor(new Color(33, 33, 222));
                g.fillRoundRect(wall.x + gap, wall.y + gap,
                        wall.width - gap * 2, wall.height - gap * 2, arc, arc);
                g.setColor(new Color(90, 90, 255));
                g.drawRoundRect(wall.x + gap, wall.y + gap,
                        wall.width - gap * 2, wall.height - gap * 2, arc, arc);
            }
        }

        
        g.setColor(Color.WHITE);
        for (Block food : foods) {
            g.fillOval(food.x, food.y, food.width, food.height);
        }

        
        g.setColor(new Color(20, 20, 20));
        g.fillRect(0, 0, boardWidth, hudHeight);
        g.setColor(new Color(40, 40, 90));
        g.fillRect(0, hudHeight - 3, boardWidth, 3);

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 26));
            g.setColor(new Color(180, 0, 0));
            String text = "GAME OVER";
            int textWidth = g.getFontMetrics().stringWidth(text);
            g.drawString(text, (boardWidth - textWidth) / 2, 28);

            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.setColor(Color.WHITE);
            String sub = "Final Score: " + score + "   (press any key for menu)";
            int subWidth = g.getFontMetrics().stringWidth(sub);
            g.drawString(sub, (boardWidth - subWidth) / 2, 50);
        }
        else {
            
            g.setFont(new Font("Arial", Font.BOLD, 22));
            g.setColor(new Color(255, 215, 0));
            g.drawString("SCORE", 12, 24);
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(score), 12, 50);

            
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.setColor(new Color(255, 215, 0));
            String livesLabel = "LIVES";
            int labelWidth = g.getFontMetrics().stringWidth(livesLabel);
            int livesX = boardWidth - 16 - labelWidth;
            g.drawString(livesLabel, livesX, 24);

            g.setColor(skins[selectedSkin].color);
            int dotSize = 16;
            int dotSpacing = 22;
            int dotsStartX = boardWidth - 16 - (lives * dotSpacing);
            for (int i = 0; i < lives; i++) {
                g.fillArc(dotsStartX + i * dotSpacing, 32, dotSize, dotSize, 30, 300);
            }
        }
    }

    
    private void drawDeathAnimation(Graphics g) {
        double progress = deathAnimTimer / (double) DEATH_ANIM_LENGTH; 
        
        boolean visible = (deathAnimTimer / 3) % 2 == 0;
        if (!visible) return;

        double scale = Math.max(0, 1.0 - progress); 
        int w = (int) (pacman.width * scale);
        int h = (int) (pacman.height * scale);
        int drawX = pacman.x + (pacman.width - w) / 2;
        int drawY = pacman.y + (pacman.height - h) / 2;
        
        int mouthAngle = (int) (progress * 200);
        PacmanRenderer.draw(g, drawX, drawY, w, h, pacman.direction, skins[selectedSkin].color, mouthAngle);
    }

    public void move() {
        
        if (dying) {
            deathAnimTimer++;
            if (deathAnimTimer >= DEATH_ANIM_LENGTH) {
                dying = false;
                deathAnimTimer = 0;
                if (lives == 0) {
                    gameOver = true;
                } else {
                    resetPositions();
                }
            }
            return;
        }

        updateGhostMode();

        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        
        
        for (Block wall : walls) {
            if (pacman.collidesWith(wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

        for (Block ghost : ghosts) {
            if (ghost.collidesWith(pacman)) {
                lives -= 1;
                startDeathAnimation();
                return; 
            }

            updateGhostBehavior(ghost);

            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;

            
            for (Block wall : walls) {
                if (houseWalls.contains(wall)) continue;
                if (ghost.collidesWith(wall)) {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    char newDirection = directions[random.nextInt(4)];
                    ghost.direction = newDirection;
                    ghost.updateVelocity(tileSize);
                }
            }
        }

        Block foodEaten = null;
        for (Block food : foods) {
            if (pacman.collidesWith(food)) {
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);

        if (foods.isEmpty()) {
            loadMap();
            GhostHouseDelays();
            resetPositions();
        }
    }

    
    private void updateGhostBehavior(Block ghost) {
        if (ghost.inHouse) {
            if (ghost.houseExitDelay > 0) {
                ghost.houseExitDelay--;
                ghost.velocityX = 0;
                ghost.velocityY = 0;
                return;
            } else {
                
                ghost.exitingHouse = true;
                ghost.inHouse = false;
                ghost.velocityX = 0;
                ghost.velocityY = -tileSize/4;
                ghost.direction = 'U';
                return;
            }
        }

        if (ghost.exitingHouse) {
            if (ghost.y <= houseExitTargetY) {
                ghost.exitingHouse = false;
                char newDirection = directions[random.nextInt(4)];
                ghost.velocityX = 0;
                ghost.velocityY = 0;
                ghost.direction = newDirection;
                ghost.updateVelocity(tileSize);
            } else {
                ghost.velocityX = 0;
                ghost.velocityY = -tileSize/4;
                ghost.direction = 'U';
            }
        }
        
        
    }

    
    
    
    private void updateGhostMode() {
        modeTimer++;
        if (modeTimer >= scatterChaseSchedule[scheduleIndex]) {
            modeTimer = 0;
            scheduleIndex = Math.min(scheduleIndex + 1, scatterChaseSchedule.length - 1);
            ghostMode = (ghostMode == GhostMode.SCATTER) ? GhostMode.CHASE : GhostMode.SCATTER;
            for (Block ghost : ghosts) {
                if (!ghost.inHouse && !ghost.exitingHouse) {
                    char newDirection = directions[random.nextInt(4)];
                    ghost.direction = newDirection;
                    ghost.updateVelocity(tileSize);
                }
            }
        }
    }

    private void startDeathAnimation() {
        dying = true;
        deathAnimTimer = 0;
        pacman.velocityX = 0;
        pacman.velocityY = 0;
    }

    public void resetPositions() {
        pacman.reset();
        pacman.velocityX = 0;
        pacman.velocityY = 0;
        for (Block ghost : ghosts) {
            ghost.reset();
        }
        GhostHouseDelays();
        ghostMode = GhostMode.SCATTER;
        modeTimer = 0;
        scheduleIndex = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameState == GameState.MENU) {
            menuAnimTimer++;
            tickMouthAnimation();
            menuPacX += 2;
            if (menuPacX > boardWidth + 10) {
                menuPacX = -28;
            }
            repaint();
            return;
        }

        if (gameState == GameState.PLAYING) {
            tickMouthAnimation();
            move();
            repaint();
            if (gameOver) {
                gameState = GameState.GAME_OVER;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameState == GameState.MENU) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                selectedSkin = (selectedSkin - 1 + skins.length) % skins.length;
            }
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                selectedSkin = (selectedSkin + 1) % skins.length;
            }
            else if (e.getKeyCode() == KeyEvent.VK_UP) {
                selectedMap = (selectedMap - 1 + mazeMaps.length) % mazeMaps.length;
            }
            else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                selectedMap = (selectedMap + 1) % mazeMaps.length;
            }
            else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                startNewGame();
                repaint();
            }
            return;
        }

        if (gameState == GameState.GAME_OVER) {
            gameState = GameState.MENU;
            repaint();
            return;
        }

        
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDirection('U', walls, tileSize);
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDirection('D', walls, tileSize);
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDirection('L', walls, tileSize);
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('R', walls, tileSize);
        }
    }

    private void startNewGame() {
        tileMap = mazeMaps[selectedMap];
        loadMap();
        GhostHouseDelays();
        resetPositions();
        lives = 3;
        score = 0;
        gameOver = false;
        gameState = GameState.PLAYING;
    }
}