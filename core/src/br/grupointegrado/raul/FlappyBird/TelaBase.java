package br.grupointegrado.raul.FlappyBird;

import com.badlogic.gdx.Screen;

import br.grupointegrado.raul.FlappyBird.MainGame;

/**
 * Created by Raul on 30/09/2015.
 */
public abstract class TelaBase implements Screen{
    protected MainGame game;

    public TelaBase(MainGame game){
        this.game = game;
    }

    @Override
    public void hide() {
        dispose();
    }

}