package com.example;


import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import static org.assertj.core.api.Assertions.assertThat;


@WireMockTest
class HelloModelTest {
    @Test
    @DisplayName("Given a model with MsgToSend when calling sendMsg then send")
    void sendMessageCallsConnectionWithMessageToSend(){
        //Arrange___given
        var spy = new NtfyConnectionSpy();
        var model = new HelloModel(spy);
        model.setMsgToSend("Hello World");
        model.setMsgTopic("mytopic");
        //Act___When
        model.sendMsg();
        //Assert__then
        assertThat(spy.message).isEqualTo("Hello World");
    }

    @Test
    @DisplayName("Send data to fake server for verification, ")
    void sendMessageToFakeServer(WireMockRuntimeInfo wmRuntimeInfo) throws InterruptedException {
        var con = new NtfyConnectionImpl("http://localhost:"+wmRuntimeInfo.getHttpPort());
        var model = new HelloModel(con);
        model.setMsgToSend("Hello World");
        model.setMsgTopic("mytopic");
        String url =  "http://localhost:"+wmRuntimeInfo.getHttpPort()+"/"+model.getMsgTopic()+"/json";
        messageToJson messageToJson = new messageToJson(model.getMsgTopic(),  model.getMsgToSend());
        stubFor(post(urlMatching(url))
                .willReturn(ok())
                .withHeader("Content-Type", equalTo("application/json")));
        model.sendMsg();
        Thread.sleep(500);
        //verify call made to server
        verify(1, postRequestedFor(urlEqualTo("/"+messageToJson.topic+"/json"))
                .withRequestBody(containing("Hello World")));
    }

    @Test
    void trimMessagesToSendForNoWhiteSpaceInStartOrEnd(WireMockRuntimeInfo wmRuntimeInfo) {
        var con = new NtfyConnectionImpl("http://localhost:"+wmRuntimeInfo.getHttpPort());
        var model = new HelloModel(con);
        model.setMsgToSend("   Hello World   ");

        assertThat(model.getMsgToSend()).isEqualTo("Hello World");
    }

    @Test
    void trimTopicForToAssertItHasNoWhiteSpace(WireMockRuntimeInfo wmRuntimeInfo) {
        var con = new NtfyConnectionImpl("http://localhost:"+wmRuntimeInfo.getHttpPort());
        var model = new HelloModel(con);
        model.setMsgTopic("   this topic   ");

        assertThat(model.getMsgTopic()).isEqualTo("thistopic");
    }

    @Test
    void checkIfTopicIsNotEmpty(WireMockRuntimeInfo wmRuntimeInfo) {
        var con = new NtfyConnectionImpl("http://localhost:"+wmRuntimeInfo.getHttpPort());
        var model = new HelloModel(con);

        model.setMsgTopic("");

        assertThat(!model.checkTopicIsNotEmpty(model.getMsgTopic())).isFalse();
    }

    @Test
    void checkIfmessageIsNotAnEmptyString(WireMockRuntimeInfo wmRuntimeInfo) {
        var con = new NtfyConnectionImpl("http://localhost:"+wmRuntimeInfo.getHttpPort());
        var model = new HelloModel(con);

        model.setMsgToSend("    ");

        assertThat(model.checkMessageIsNotEmpty(model.getMsgToSend())).isTrue();
    }
}