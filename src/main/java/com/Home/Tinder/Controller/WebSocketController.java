//package com.Home.Tinder.Controller;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//
//@Slf4j
//@Controller
//public class WebSocketController {
//
//    @SendTo("/global-message/tick")
//    @MessageMapping("/from-client")
//    public String fromClient(String content) throws Exception {
//        log.info("Message from client: {}", content);
//        Thread.sleep(1000);
//        return "Hello, " + content;
//    }
//}


//Csak a kliens--> API küldés résznél kell contorller, mint bármi api hívásnál