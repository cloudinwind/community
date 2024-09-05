package com.nowcoder.community.event;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    // 处理事件 生产者 生产数据到 topic
    public void fireEvent(Event event) {
        // 将事件发布到指定的主题, 即触发事件后, 添加事件到topic
        // 因为存储到topic的只能是String, 因此需要将抽象出来的Event转换为JSON格式的String
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }

}

