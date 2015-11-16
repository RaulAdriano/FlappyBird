package br.grupointegrado.raul.FlappyBird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;



import java.security.Key;

public class TelaJogo extends TelaBase {

    private OrthographicCamera camera; // camera do jogo.
    private World mundo; // representa o mundo do Box2D.
    private Body chao; // corpo do chÃ£o.
    private Passaro passaro; // corpo do passaro;

    private Box2DDebugRenderer debug; // desenha o mundo na tela para ajudar no desenvolvimento.

    private Array<Obstaculo> obstaculos = new Array<Obstaculo>();

    private BitmapFont fontePontuacao ;
    private Stage palco;
    private  int pontuacao = 0;
    private Label lbPontuacao;
    private Button btnPlay;
    private Button btnGameOver;
    private OrthographicCamera cameraInfo;
    private boolean gameOver = false;

    private Texture[] texturesPassaro;
    private  Texture textureObstaculoCima;
    private  Texture textureObstaculoBaixo;
    private  Texture textureChao;
    private  Texture textureFundo;
    private  Texture texturePlay;
    private  Texture textureGameOver;

    private boolean jogoIniciado = false;


    public TelaJogo(MainGame game) { super(game); }

    @Override
    public void show() {


        camera = new OrthographicCamera(Gdx.graphics.getWidth() / Util.ESCALA, Gdx.graphics.getHeight() / Util.ESCALA);
        cameraInfo = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        debug = new Box2DDebugRenderer();
        mundo = new World(new Vector2(0, -9.8f), false);
        mundo.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                detectarColisao(contact.getFixtureA(), contact.getFixtureB());
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        initTexturas();
        initChao();
        initPassaro();
        initFonts();
        initInformacoes();



    }

    private void initTexturas() {
        texturesPassaro = new Texture[3];
        texturesPassaro[0] = new Texture("sprites/bird-1.png");
        texturesPassaro[1] = new Texture("sprites/bird-2.png");
        texturesPassaro[2] = new Texture("sprites/bird-3.png");

        textureObstaculoBaixo = new Texture("sprites/bottomtube.png");
        textureObstaculoCima = new Texture("sprites/toptube.png");

        textureFundo = new Texture("sprites/bg.png");
        textureChao = new Texture("sprites/ground.png");

        texturePlay = new Texture("sprites/playbtn.png");
        textureGameOver = new Texture("sprites/gameover.png");

    }


    private void detectarColisao(Fixture fixtureA, Fixture fixtureB) {
        if("PASSARO".equals(fixtureA.getUserData())||"PASSARO".equals(fixtureB.getUserData()) ){
            gameOver = true;
        }
    }

    private void initFonts() {
        FreeTypeFontGenerator.FreeTypeFontParameter fontParam =  new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParam.size = 56;
        fontParam.color = Color.WHITE;
        fontParam.shadowColor = Color.BLACK;
        fontParam.shadowOffsetX = 4;
        fontParam.shadowOffsetY = 4;

        FreeTypeFontGenerator gerador = new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"));
        fontePontuacao = gerador.generateFont(fontParam);
        gerador.dispose();
    }

    private void initInformacoes() {
        palco = new Stage(new FillViewport(cameraInfo.viewportWidth, cameraInfo.viewportHeight , cameraInfo));
        Gdx.input.setInputProcessor(palco);

        Label.LabelStyle estilo = new Label.LabelStyle();
        estilo.font = fontePontuacao;
         lbPontuacao = new Label("0", estilo);
        palco.addActor(lbPontuacao);

        ImageButton.ImageButtonStyle estilobotao = new ImageButton.ImageButtonStyle();
        estilobotao.up = new SpriteDrawable(new Sprite(texturePlay));

        btnPlay = new ImageButton(estilobotao);
        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                jogoIniciado = true;
            }
        });
        palco.addActor(btnPlay);

        estilobotao = new ImageButton.ImageButtonStyle();
        estilobotao.up = new SpriteDrawable(new Sprite(textureGameOver));

        btnGameOver = new ImageButton(estilobotao);
        btnGameOver.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                reiniciarJogo();
            }
        });
        palco.addActor(btnGameOver);
    }

    private void reiniciarJogo() {
        //cod de reiniciar jogo

    }

    private void initChao() { chao = Util.criarCorpo(mundo, BodyDef.BodyType.StaticBody, 0, 0); }

    private void initPassaro() { passaro = new Passaro(mundo, camera, null); }

    /*
    BodyDef def = new BodyDef(); // Objeto de definiÃ§Ã£o do corpo.
        def.type = BodyDef.BodyType.DynamicBody;
        float y = (Gdx.graphics.getHeight() / ESCALA / 2) / PIXEL_METRO + 10;
        float x = (Gdx.graphics.getWidth() / ESCALA / 2) / PIXEL_METRO + 2;
        def.position.set(x ,y);
        def.fixedRotation = true;

        Body corpo = mundo.createBody(def);  // criaÃ§Ã£o do corpo.
        CircleShape shape = new CircleShape(); // forma do corpo.
        shape.setRadius(20 / PIXEL_METRO); // raio de 20 metros dividido pelo pixel_metro.

        Fixture fixacao = corpo.createFixture(shape, 1); // objeto de apresentaÃ§Ã£o do corpo.

        shape.dispose();
     */

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1); // limpa a tela e pinta a cor de fundo.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // mantem o buffer de cores.

        capturaTeclas();
        atualizar(delta);
        renderizar(delta);

        debug.render(mundo, camera.combined.cpy().scl(Util.PIXEL_METRO));
    }

    private boolean pulando = false;

    private void capturaTeclas() {
        pulando = false;
        if(Gdx.input.justTouched()) {
            pulando = true;
        }
    }

    /**
     * Arualizar/calculo dos corpos.
     *
     * @param delta
     */
    private void atualizar(float delta) {
        palco.act(delta);
        passaro.getCorpo().setFixedRotation(!gameOver);
        passaro.atualizar(delta, !gameOver);
        if(jogoIniciado){

        mundo.step(1f / 60f, 6, 2); // Um passo dentro do mundo.
        atualizarObstaculos();
        }


        atualizaInformacoes();
        if(!gameOver){
            atualizarCamera();
            atualizarChao();
        }
        if(pulando && !gameOver && jogoIniciado) {
            passaro.pular();
        }
    }

    private void atualizaInformacoes() {
         lbPontuacao.setText(pontuacao + "");
        lbPontuacao.setPosition(cameraInfo.viewportWidth / 2 - lbPontuacao.getPrefWidth() / 2, cameraInfo.viewportHeight - lbPontuacao.getPrefWidth() *2);

        btnPlay.setPosition(cameraInfo.viewportWidth / 2 - btnPlay.getPrefWidth() / 2, cameraInfo.viewportHeight - btnPlay.getPrefWidth() *5 );
        btnPlay.setVisible(!jogoIniciado);

        btnGameOver.setPosition(cameraInfo.viewportWidth / 2 - btnGameOver.getPrefWidth() / 2, cameraInfo.viewportHeight - btnGameOver.getPrefWidth()*4);
        btnGameOver.setVisible(false);
    }


    private void atualizarObstaculos() {
        //enquanto a lista tiver menos do que 4  obstaculos
        while(obstaculos.size<4) {
            Obstaculo ultimo = null;
            if(obstaculos.size>0){
                ultimo = obstaculos.peek();
            }
            Obstaculo o = new Obstaculo(mundo , camera , ultimo);
            obstaculos.add(o);

        }

        for(Obstaculo o : obstaculos){
            float iniciaCamera = passaro.getCorpo().getPosition().x -(camera.viewportHeight/2/Util.PIXEL_METRO) - o.getLargura();

            if(iniciaCamera > o.getPosX()){
                o.remover();
                obstaculos.removeValue(o,true);
            }else if (!o.isPassou() && o.getPosX() < passaro.getCorpo().getPosition().x){
                o.setPassou(true);
                pontuacao ++;
                //calcular ponto
                //reproduzir som
            }

        }
    }

    private void atualizarCamera() {
        camera.position.x = (passaro.getCorpo().getPosition().x + 34 / Util.PIXEL_METRO) * Util.PIXEL_METRO;
        camera.update();
    }

    /**
     * Atualiza a posiÃ§Ã£o do chao para acompanhar o pÃ¡ssaro.
     */
    private void atualizarChao() {
        Vector2 posicao = passaro.getCorpo().getPosition(); // Pega posiÃ§Ã£o do passaro.
        chao.setTransform(posicao.x, 0, 0); // Faz o chÃ£o acompanhar o passaro.
    }

    /**
     * Renderizar/desenhar as imagens.
     *
     * @param delta
     */
    private void renderizar(float delta) {
        palco.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / Util.ESCALA, height / Util.ESCALA);
        camera.update();

        redimensionaChao();
        cameraInfo.setToOrtho(false, width, height);
        cameraInfo.update();
    }

    /**
     * Configura o tamanho do chÃ£o de acordo com a tela.
     */
    private void redimensionaChao() {
        chao.getFixtureList().clear(); // limpa todas as formas antigas.
        float largura = camera.viewportWidth  / Util.PIXEL_METRO;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(largura / 2, Util.ALTURA_CHAO / 2);

        Fixture forma = Util.criarForma(chao, shape, "CHAO");

        shape.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        debug.dispose();
        mundo.dispose();
        textureGameOver.dispose();
        texturesPassaro[1].dispose();
        texturesPassaro[0].dispose();
        texturesPassaro[2].dispose();
        texturePlay.dispose();
        textureChao.dispose();
        textureFundo.dispose();
        textureObstaculoBaixo.dispose();
        textureObstaculoCima.dispose();
    }


}
