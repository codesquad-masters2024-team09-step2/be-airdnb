package com.airdnb.chat;

import com.airdnb.chat.entity.MessageType;
import com.airdnb.chat.service.ChatRoomService;
import com.airdnb.chat.service.ChatService;
import com.airdnb.chat.service.MessageCreation;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

@SpringBootTest
public class ChatServicePerformanceTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatRoomService chatRoomService;

    private final StopWatch stopWatch = new StopWatch();

    @Test
    void testChatCreate() throws InterruptedException {
        // 데이터 준비
        String memberId = "abc@naver.com";
        Long roomId = 1L;

        stopWatch.start("buffered chat create");

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000; i++) {
            final int messageIndex = i;
            executorService.submit(() -> {
                MessageCreation messageCreation = MessageCreation.builder()
                    .memberId(memberId)
                    .roomId(roomId)
                    .messageType(MessageType.CHAT)
                    .content("Message " + messageIndex)
                    .build();
                chatService.createChat(messageCreation);
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        stopWatch.stop();

        Thread.sleep(21000);

        stopWatch.start("DB");
        chatRoomService.getMessagesFromRoom(roomId);
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }
}
