package br.grupointegrado.raul.FlappyBird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
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

    private void initCorpo() {
        float x = (camera.viewportWidth / 2) / Util.PIXEL_METRO;
        float y = (camera.viewportHeight / 2) / Util.PIXEL_METRO;

        corpo = Util.criarCorpo(mundo, BodyDef.BodyType.DynamicBody, x, y);

        FixtureDef definicao = new FixtureDef();
        definicao.density = 1; // densidade do corpo.
        definicao.friction = 0.4f; // fricÃ§Ã£o/atrito entre o corpo e o outro.
        definicao.restitution = 0.3f; // elasticidade do corpo.

        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("physics/bird.json"));
        loader.attachFixture(corpo, "bird", definicao, 1, "PASSARO"); //pegar cordenadas e vincula no corpo.
    }

    /**
     * Atualiza o comportamento do passaro.
     * @param delta
     */
    public void atualizar(float delta, boolean movimentar)
    {
        if(movimentar){
            atualizarVelocidade();
        }

    }

    private void atualizarVelocidade() {

        corpo.setLinearVelocity(2f, corpo.getLinearVelocity().y);

        atualizarRotacao();

    }

    private void atualizarRotacao() {
        float rotacao = 0;
        float velocidadeY = corpo.getLinearVelocity().y;

        if(velocidadeY <0) {
            rotacao++;
            //descendo`

        }else if (velocidadeY>0){
            //subindo
            rotacao = 35;
        }else{
            //reto
            rotacao = 0;
        }
        rotacao = (float) Math.toRadians(rotacao); //graus para radianos
        corpo.setTransform(corpo.getPosition(),rotacao);
    }

    /**
     * Aplica uma forÃ§a positiva no Y para simular o pulo.
     */
    public void pular() {
        corpo.setLinearVelocity(corpo.getLinearVelocity().x, 0);
        corpo.applyForceToCenter(0, 100, false);
    }

    public Body getCorpo() {
        return corpo;
    }


}
