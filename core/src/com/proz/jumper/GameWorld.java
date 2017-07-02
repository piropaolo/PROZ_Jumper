package com.proz.jumper;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

import static com.proz.jumper.TextureManager.*;

/**
 * This class holds information about model of the game, ie. position of objects, score etc.
 * Created by volterra on 16.04.17.
 */
public class GameWorld {
    /**
     * Platforms list.
     */
    private LinkedList<Platform> platforms;

    /**
     * Main character of the current game
     */
    private Player player;

    /**
     * GameCamera instance used in GameScreen
     */
    private GameCamera camera;

    /**
     * Random number generator used for getting new platforms positions
     */
    private Random rand;

    /**
     * Current score
     */
    private int score;
    /**
     * Number of already generated platforms
     */
    private int platformCount;

    /**
     * Width of display device
     */
    private static int width;

    /**
     * Height of a display device
     */
    private static int height;

    /**
     * PlayerRegion width
     */
    private final static int playerWidth = playerFallRegion.getRegionWidth();

    /**
     * PlayerRegion height
     */
    private final static int playerHeight = playerFallRegion.getRegionHeight();
    private final static int platformWidth = platformRegion.getRegionWidth();
    private final static int platformHeight = platformRegion.getRegionHeight();
    private static int leap;
    private TextureRegion backgroundA;
    private TextureRegion backgroundB;
    private int backgroundCounter;

    public GameWorld(){
        platforms = new LinkedList<Platform>();
        player = new Player(720 * 3 / 4 - playerWidth/2, 0, 0, this);
        camera = new GameCamera(getPlayer());
        camera.setToOrtho(false, 720, 1280);
        rand = new Random();
        score = 0;
        platformCount = 0;

        width = (int)camera.viewportWidth;
        height = (int)camera.viewportHeight;
        leap = (int)(player.getVelocity0() * player.getVelocity0() / (2 * player.getGravity0()) * 0.97f);
        initialiseBackground();
        backgroundCounter = 0;

        generatePlatforms();
    }

    /**
     * Method that is used for creating 10 random generated platforms
     */
    public void generatePlatforms(){
        for (int i = 0; i < 10; ++i )
        {
            Platform platform = new Platform(rand.nextInt(width - platformWidth),
                    rand.nextInt(leap/10) + (platformCount + 1) * leap * 0.8f - platformHeight, ++platformCount, this);
            platforms.add(platform);
        }
    }

    /**
     * If there are no new platforms above the player, another 10 are generated
     */
    public void generateMorePlatforms(){
        boolean temp = false;
        for (Platform item : platforms) {
            if (item.getY() > camera.position.y + camera.viewportHeight/2)    temp = true;
        }
        if (!temp) generatePlatforms();
    }

    /**
     * This method checks if player is colliding with any of the platforms. It is divided into two parts:
     * - first one stops player fall is he is airborne and falling
     * - second one ensures that player stays standing on a platform and starts falling when go over the ledge
     */
    public void platformsCollision(){
        for (Platform item : platforms) {
            if (player.getAirborne() && player.getY() - item.getY() > platformHeight * 0.7f && player.getY() - item.getY() < platformHeight
                    && player.getX() - item.getX() > -playerWidth/2f && player.getX() - item.getX() < platformWidth - playerWidth/2f){
                player.setAirborne(false);
                player.setY(item.getY() + platformHeight * 0.85f);
                SoundManager.playFallSound();
            }

            if (!player.getAirborne() && !player.getJump() && player.getY() - item.getY() == platformHeight * 0.85f
                    && (player.getX() - item.getX() <= -playerWidth/2f || player.getX() - item.getX() >= platformWidth - playerWidth/2f)){
                player.setAirborne(true);
            }
        }
    }

    /**
     * In this case we remove from the list platforms that are no longer on the GameScreen.
     */
    public void platformsUpdate(){
        ListIterator<Platform> iterator = platforms.listIterator();
        while (iterator.hasNext()){
            Platform plat = iterator.next();
            if (plat.getY() < camera.position.y - camera.viewportHeight/2 - platformRegion.getRegionHeight())
                iterator.remove();
        }
    }

    /**
     * This method checks if player is on the screen.
     * @return      If player is alive returns true, otherwise false.
     */
    public boolean isPlayerAlive(){
        if (player.getY() < camera.position.y - camera.viewportHeight/2) {
            player.setAlive(false);
            return false;
        }
        return true;
    }

    public void initialiseBackground(){
        backgroundA = backgroundRegion;
        switch (rand.nextInt(3) + 1){
            case 1:
                backgroundB = backgroundRegion1;
                break;
            case 2:
                backgroundB = backgroundRegion2;
                break;
            case 3:
                backgroundB = backgroundRegion3;
                break;
        }
    }

    public void updateBackground(){
        if (player.getVelocityStack() >= camera.viewportHeight * (1 + backgroundCounter)) {
            ++backgroundCounter;
            backgroundA = backgroundB;
            switch (rand.nextInt(3) + 1) {
                case 1:
                    backgroundB = backgroundRegion1;
                    break;
                case 2:
                    backgroundB = backgroundRegion2;
                    break;
                case 3:
                    backgroundB = backgroundRegion3;
                    break;
            }
        }
    }

    public Player getPlayer(){
        return player;
    }

    public LinkedList<Platform> getPlatforms(){
        return platforms;
    }

    public GameCamera getCamera() {
        return camera;
    }

    public int getScore(){
        return score;
    }

    public void setScore(int t){
        this.score = t;
    }

    public TextureRegion getBackgroundA() {
        return backgroundA;
    }

    public TextureRegion getBackgroundB() {
        return backgroundB;
    }
}
