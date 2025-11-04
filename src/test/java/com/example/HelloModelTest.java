package com.example;


import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest
class HelloModelTest {
    @Test
    @DisplayName("Given a model with MsgToSend when calling sendMsg then send")
    void sendMessageCallsConnectionWithMessageToSend(){
        //Arrange___given
        var spy = new NtfyConnectionSpy();
        var model = new HelloModel(spy);
        model.setMsgToSend("Hello World");
        //Act___When
        model.sendMsg();
        //Assert__then
        assertThat(spy.message).isEqualTo("Hello World");
    }

    @Test
    @DisplayName("Send data to fake server for verification")
    void sendMessageToFakeServer(WireMockRuntimeInfo wmRuntimeInfo) {
        var con = new NtfyConnectionImpl("http://localhost:"+wmRuntimeInfo.getHttpPort());
        var model = new HelloModel(con);
        model.setMsgToSend("Hello World");
        stubFor(post("/mytopic").willReturn(ok()));
        model.sendMsg();

        //verify call made to server
        verify(1, postRequestedFor(urlEqualTo("/mytopic"))
                .withRequestBody(containing("Hello World")));
    }
}