package br.grupointegrado.raul.FlappyBird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Passaro {

    private final World mundo;
    private final OrthographicCamera camera;
    private final Texture[] texturas;
    private Body corpo;

    public Passaro(World mundo, OrthographicCamera camera, Texture[] texturas) {

        this.mundo = mundo;
        this.camera = camera;
        this.texturas = texturas;

        initCorpo();
    }

    public Body getCorpo() {
        return corpo;
    }

    private void initCorpo() {
        float x = (camera.viewportWidth / 2) / Util.PIXEL_METRO;
        float y = (camera.viewportHeight / 2) / Util.PIXEL_METRO;

        corpo = Util.criarCorpo(mundo, BodyDef.BodyType.DynamicBody, x, y);

        FixtureDef definicao  = new FixtureDef();
        definicao.density = 1;
        definicao.friction = 0.4f;
        definicao.restitution = 0.3f;

        BodyEditorLoader loader  = new BodyEditorLoader(Gdx.files.internal("physics/bird.json"));

        loader.attachFixture(corpo, "bird", definicao, 1, "PASSARO");
    }

    /**
     * atualiza o comportamento do passaro
     * @param delta
     */
    public void atualizar(float delta){
        atualizarVelocidade();

    }

    private void atualizarVelocidade() {
        corpo.setLinearVelocity(2f,corpo.getLinearVelocity().y);
    }

    /**
     * aplica uma forca positiva no y para simular o pulo;
     */
    public void vamosPular(){
        corpo.setLinearVelocity(corpo.getLinearVelocity().x,0);
        corpo.applyForceToCenter(0,150,false);

    }
}
