package pt.luissantos.gameofthree.server;

import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler.HandshakeComplete;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HttpUtils {

    public static Optional<String> getQueryParam(String uri, String name){

        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
        Map<String, List<String>> params = queryStringDecoder.parameters();

        if(params.containsKey(name)){
            return  Optional.ofNullable(params.get(name).get(0));
        }

        return Optional.empty();
    }

    public static Optional<String> getQueryParam(HandshakeComplete handshake, String name){
        return getQueryParam(handshake.requestUri(),name);
    }

}
