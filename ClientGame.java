import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientGame extends NetworkGame {
    Client client;

    public ClientGame(int width, int height) {
        super(width, height);
    }

    protected void setupClient() {
        try {
            client = new Client(8192, 3084);
            client.start();
            client.connect(5000, "94.14.135.1", 55555, 56666);

            kryo = client.getKryo();
            register();

            client.addListener(new Listener() {
                public void received (Connection connection, Object object) {
                    if (object instanceof Vector2) {
                        opponent.position = (Vector2) object;
                        client.sendTCP(player.position);
                    }

                    if (object instanceof String) {
                        map.importMap((String) object);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        super.show();
        setupClient();
        client.sendTCP(player.position);
    }
}
