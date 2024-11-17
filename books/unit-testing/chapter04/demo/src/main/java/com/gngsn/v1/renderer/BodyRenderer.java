package com.gngsn.v1.renderer;

import com.gngsn.v1.IRenderer;
import com.gngsn.v1.Message;

public class BodyRenderer implements IRenderer {
    @Override
    public String render(final Message message) {
        return "<body>%s</body>".formatted(message.getBody());
    }
}