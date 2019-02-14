package pt.luissantos.gameofthree.common.decoders;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import pt.luissantos.gameofthree.common.messages.GameEnded;
import pt.luissantos.gameofthree.common.messages.PlayMessage;
import pt.luissantos.gameofthree.common.messages.StartGame;

import java.util.List;

public class GameMessageDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {

    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) {

        if (msg.text().equals("START")) {
            out.add(StartGame.INSTANCE);
            return;
        }

        if (msg.text().equals(GameEnded.LOSE.getRawMessage())) {
            out.add(GameEnded.LOSE);
            return;
        }

        if (msg.text().equals(GameEnded.WIN.getRawMessage())) {
            out.add(GameEnded.WIN);
            return;
        }

        out.add(new PlayMessage(Integer.parseInt(msg.text())));

    }
}
