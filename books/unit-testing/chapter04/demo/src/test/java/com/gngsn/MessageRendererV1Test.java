package com.gngsn;

import com.gngsn.v1.IRenderer;
import com.gngsn.v1.MessageRenderer;
import com.gngsn.v1.renderer.BodyRenderer;
import com.gngsn.v1.renderer.FooterRenderer;
import com.gngsn.v1.renderer.HeaderRenderer;
import org.junit.jupiter.api.Test;

import java.util.List;

class MessageRendererV1Test {

    @Test
    void Passed_MessageRenderer_uses_correct_sub_renderers() {
        var sut = new MessageRenderer();

        List<IRenderer> renderers = sut.SubRenderers;

        assert 3 == renderers.size();
        assert renderers.get(0) instanceof HeaderRenderer;
        assert renderers.get(1) instanceof BodyRenderer;
        assert renderers.get(2) instanceof FooterRenderer;
    }
}