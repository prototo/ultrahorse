import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class ServerGame extends NetworkGame {
    Server server;
    boolean mapSent = false;

    public ServerGame(int width, int height) {
        super(width, height);
    }

    protected void setupServer() {
        try {
            server = new Server(16384, 3084);
            server.start();
            server.bind(55555, 56666);

            kryo = server.getKryo();
            register();

            server.addListener(new Listener() {
                public void received(Connection connection, Object object) {
                    if (object instanceof Vector2) {
                        opponent.position = (Vector2) object;
                        connection.sendTCP(player.position);
                    }

                    if (!mapSent) {
                        connection.sendTCP(map.exportMap());
                        mapSent = true;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        super.show();
        setupServer();
    }
}
