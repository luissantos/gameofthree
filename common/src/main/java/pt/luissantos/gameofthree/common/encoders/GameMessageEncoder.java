package pt.luissantos.gameofthree.common.encoders;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import pt.luissantos.gameofthree.common.messages.GameMessage;

import java.util.List;

public class GameMessageEncoder extends MessageToMessageEncoder<GameMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, GameMessage msg, List<Object> out) throws Exception {
        out.add(new TextWebSocketFrame(msg.getRawMessage()));
    }
}
