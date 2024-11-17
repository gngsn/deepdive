package com.gngsn;

import com.gngsn.v1.Message;
import com.gngsn.v1.MessageRenderer;
import org.junit.jupiter.api.Test;

class MessageRendererV2Test {

    @Test
    void Passed_Rendering_a_message() {
        var sut = new MessageRenderer();
        var message = new Message(
            /* Header */ "h",
                /* Body */ "b",
                  /* Footer */ "f"
        );

        String html = sut.render(message);
        System.out.println(html);

        assert "<header>h</header><body>b</body><footer>f</footer>".equals(html);
    }
}