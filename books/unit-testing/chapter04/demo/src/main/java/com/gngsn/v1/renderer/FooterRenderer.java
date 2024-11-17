package com.gngsn.v1.renderer;

import com.gngsn.v1.IRenderer;
import com.gngsn.v1.Message;

public class FooterRenderer implements IRenderer {
    @Override
    public String render(final Message message) {
        return "<footer>%s</footer>".formatted(message.getFooter());
    }
}
