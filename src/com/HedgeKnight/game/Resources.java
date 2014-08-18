package com.HedgeKnight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

/**
 * Resources
 * Created by kourpa on 8/3/14.
 */
public class Resources {
  public static final int WINDOW_WIDTH = 600;
  public static final int WINDOW_HEIGHT = 800;

  private static final HashMap<String, Texture> nameTextureMap = new HashMap<>();

  public static void init(){
    loadTextures();
  }

  private static void loadTextures(){
    nameTextureMap.put("player1", new Texture(Gdx.files.internal("player1.png")));
    nameTextureMap.put("tileset",new Texture(Gdx.files.internal("tileset.png")));
  }

  public static Texture getTexture(String name){
    return nameTextureMap.get(name);
  }
}
