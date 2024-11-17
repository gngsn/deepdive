package com.gngsn.v1;

public class Message {
    public String Header;
    public String Body;
    public String Footer;

    public Message(final String header, final String body, final String footer) {
        Header = header;
        Body = body;
        Footer = footer;
    }

    public String getHeader() {
        return Header;
    }

    public void setHeader(final String header) {
        Header = header;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(final String body) {
        Body = body;
    }

    public String getFooter() {
        return Footer;
    }

    public void setFooter(final String footer) {
        Footer = footer;
    }

    @Override
    public String toString() {
        return "Message{" +
                "Header='" + Header + '\'' +
                ", Body='" + Body + '\'' +
                ", Footer='" + Footer + '\'' +
                '}';
    }
}