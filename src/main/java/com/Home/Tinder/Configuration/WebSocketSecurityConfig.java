package com.Home.Tinder.Configuration;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
//import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
//import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//
//
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketSecurityConfig extends AbstractWebSocketMessageBrokerConfigurer {
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.enableSimpleBroker("/global-message");
//        config.setApplicationDestinationPrefixes("/app-receive");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/websocket-endpoint")
//                .setAllowedOrigins("*")
//                .withSockJS();
//    }
////
////    @Override
////    protected void configureInbound(MessageSecurityMetadataSourceRegistry message) {
////        message.anyMessage().authenticated();
////    }
////
////    @Override
////    protected boolean sameOriginDisabled() {
////        return true;
////    }
//}
