package com.proz.jumper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Jumper extends Game {
	public FileHandle file;

	public SpriteBatch batch;
	public BitmapFont font, fontL;
	public FreeTypeFontGenerator generator;
	public FreeTypeFontGenerator.FreeTypeFontParameter parameter, parameterL;

	public void create() {
		file = Gdx.files.local("save.txt");
		if (!file.exists()) file.writeString("0", false);

		TextureManager.load();
		generator = new FreeTypeFontGenerator(Gdx.files.internal("manaspc.ttf"));
		parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameterL = new FreeTypeFontGenerator.FreeTypeFontParameter();

		parameter.size = 50;
		parameterL.size = 100;
		batch = new SpriteBatch();
		//Use LibGDX's default Arial font.
		font = generator.generateFont(parameter);
		fontL = generator.generateFont(parameterL);
		this.setScreen(new MainScreen(this));
	}

	public void render() {
		super.render(); //important!
	}

	public void dispose() {
		TextureManager.dispose();
		generator.dispose();
		batch.dispose();
		font.dispose();
	}
}