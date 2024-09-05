package com.nowcoder.community.event;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.entity.Event;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.util.CommunityConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class EventConsumer implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    // 发送消息其实是 对 message 表进行操作
    @Autowired
    private MessageService messageService;

    // 消费者, 对topic进行监听, 当生产者添加数据/消息 到 topic, 下面的方法自动执行
    @KafkaListener(topics = {TOPIC_COMMENT, TOPIC_LIKE, TOPIC_FOLLOW})
    public void handleCommentMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息的内容为空!");
            return;
        }

        // 因为生产者是将Eent转为 json 格式的字符串放入 topic, 因此需要将字符串转换为 Event
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("消息格式错误!");
            return;
        }

        // 得到Event后, 封装Message
        // 发送站内通知
        Message message = new Message();
        // 因为事件发生后, 是系统将这个事件发送给 实体作者
        message.setFromId(SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        // conversationId : 其实是存储了事件的类别(评论、点赞、关注)
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());

        // 设置content属性
        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());
        // Event中封装到map中的其他数据,都加入到content中
        if (!event.getData().isEmpty()) {
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }
        // 将map类型转换为json格式的字符串
        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);
    }
}
