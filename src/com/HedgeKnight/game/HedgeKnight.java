package com.HedgeKnight.game;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public class HedgeKnight extends ApplicationAdapter
{
  private int screenWidth; // window sizes
  private int screenHeight;

  SpriteBatch batch; // batch and camera
  OrthographicCamera camera;

  ShapeRenderer sr;

  TiledMap map; // tile map and renderer
  TiledMapRenderer renderer;

  Texture tileSheet; // tile sheet texture and array of regions for tiles
  TextureRegion[][] tiles;

  Texture playerTex;
  Sprite playerSpr;

  BitmapFont font; // debug font

  int tilesW = 4; // width and height of map in tiles
  int tilesH = 4;

  int tileSizeX = 64;
  int tileSizeY = 32;

  float camSpeed = 2f; // camera movement speed
  float camSpeedAngle = (float) Math.pow(2f, .5f);

  Random ran = new Random(12345);

  @Override
  public void create() {
    Resources.init();

    screenWidth = Gdx.graphics.getWidth();
    screenHeight = Gdx.graphics.getHeight();

    sr = new ShapeRenderer();

    camera = new OrthographicCamera(screenWidth, screenHeight);
    camera.position.set(0, 0, 0);

    batch = new SpriteBatch();

    font = new BitmapFont();

    playerTex = Resources.getTexture("player1");
    tileSheet = Resources.getTexture("tileset");

    playerSpr = new Sprite(playerTex, playerTex.getWidth(),
        playerTex.getHeight());
    playerSpr.setPosition(0, 0);

    tiles = TextureRegion.split(tileSheet, tileSizeX, tileSizeY);

    map = new TiledMap();
    MapLayers layers = map.getLayers();

    TiledMapTileLayer layer = new TiledMapTileLayer(tilesW, tilesH, tileSizeX,
        tileSizeY);

    for (int x = 0; x < tilesW; x++)
    {
      for (int y = 0; y < tilesH; y++)
      {
        int tx = (int) (ran.nextFloat() * 3);
        Cell cell = new Cell();
        cell.setTile(new StaticTiledMapTile(tiles[0][tx]));
        layer.setCell(x, y, cell);
      }
    }
    layers.add(layer);

    renderer = new IsometricTiledMapRenderer(map);
  }

  @Override
  public void render() {
    // FRAME SET UP
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    batch.setProjectionMatrix(camera.combined);

    // CAMERA MOVEMENT
    movePlayer();

    renderer.setView(camera);
    renderer.render();

    cameraDebug();

    // BATCH DRAWING
    batch.begin();
    playerSpr.draw(batch);
    debugInfo();
    batch.end();
  }

  private void movePlayer() {

    if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
    {
      playerSpr.setPosition(playerSpr.getX() - camSpeed, playerSpr.getY() - 0);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
    {
      playerSpr.setPosition(playerSpr.getX() + camSpeed, playerSpr.getY() - 0);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.UP))
    {
      playerSpr.setPosition(playerSpr.getX() - 0, playerSpr.getY() + camSpeed);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
    {
      playerSpr.setPosition(playerSpr.getX() - 0, playerSpr.getY() - camSpeed);
    }

    /*
     * if (playerSpr.getX() > screenWidth/2){
     * camera.position.set(playerSpr.getX(), camera.position.y, 0); } if
     * (playerSpr.getY() > screenHeight/2){
     * camera.position.set(camera.position.x, playerSpr.getY(), 0); }
     */

    camera.position.set(playerSpr.getX(), playerSpr.getY(), 0);
    camera.update();
  }

  private void debugInfo() {

    font.draw(batch,
        "FPS: " + Gdx.graphics.getFramesPerSecond() + " Camera X: "
            + camera.position.x + " Camera Y: " + camera.position.y,
        playerSpr.getX(), playerSpr.getY() - 10);

    font.draw(batch,
        "Tile X: " + (int) ((playerSpr.getX() - 2 * playerSpr.getY()) / 64)
            + " Tile Y: "
            + (int) ((((playerSpr.getX() / 2 + playerSpr.getY()) / 32)) - 1),
        playerSpr.getX(), playerSpr.getY() - 30);
  }

  private void cameraDebug() {
    sr.begin(ShapeType.Line);
    sr.setColor(Color.CYAN);
    sr.line(0, screenHeight / 2, screenWidth, screenHeight / 2);
    sr.line(screenWidth / 2, 0, screenWidth / 2, screenHeight);
    sr.end();
  }
}
