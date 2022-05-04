package org.rrajesh1979.devicedatapipeline;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.rrajesh1979.DeviceDataAvro;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Producer {
    private final KafkaTemplate<Long, DeviceDataAvro> deviceDataProducer;
    private final NewTopic topic;

    @EventListener(ApplicationStartedEvent.class)
    public void produce() {
        log.info("Sending message to topic {}", topic.name());
        final long deviceId = 1001L;
        final DeviceDataAvro deviceDataAvro =
                DeviceDataAvro.newBuilder()
                                .setDeviceId(deviceId)
                                        .setTemp(20.0)
                                                .setHumidity(50.0)
                                                        .setTimestamp(System.currentTimeMillis())
                                                                .build();

        deviceDataProducer.send(topic.name(), deviceId, deviceDataAvro).addCallback(
                result -> {
                    final RecordMetadata m;
                    if (result != null) {
                        m = result.getRecordMetadata();
                        log.info("Produced record to topic {} partition {} @ offset {}",
                                m.topic(),
                                m.partition(),
                                m.offset());

                    }
                },
                exception -> log.error("Failed to produce to kafka", exception)
        );

        deviceDataProducer.flush();

        log.info("Sent message to topic {}", topic.name());

    }

}
