package com.gngsn.v1.renderer;

import com.gngsn.v1.IRenderer;
import com.gngsn.v1.Message;

public class HeaderRenderer implements IRenderer {
    @Override
    public String render(final Message message) {
        return "<header>%s</header>".formatted(message.getHeader());
    }
}
