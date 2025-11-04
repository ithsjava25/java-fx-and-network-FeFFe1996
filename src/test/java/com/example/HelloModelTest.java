package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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

}