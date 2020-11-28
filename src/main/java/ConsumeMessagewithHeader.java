import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.LoggerAdapter;

import java.lang.reflect.Array;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ConsumeMessagewithHeader {
    private static Logger logger = LogManager.getLogger();
    public static void main(String[] args) {

        HashMap ConfigMap = new HashMap();
        ConfigMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092,localhost:9093,localhost:9094");
        ConfigMap.put(ConsumerConfig.GROUP_ID_CONFIG,"mygroupid-header");
        ConfigMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        ConfigMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());

        logger.info("Kafka Consumer is created...");
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(ConfigMap);

        consumer.subscribe(Arrays.asList("topic-test50"));
        String msgHeaders;
        while(true){
            ConsumerRecords records = consumer.poll(1000);
            for(Object record :records){
                ConsumerRecord consumerRecord = (ConsumerRecord) record;

                //Reading Message Header
                Headers headers = consumerRecord.headers();
                Header header[] = headers.toArray();
                msgHeaders="[";
                for(int i=0;i<header.length;i++)
                    msgHeaders +=  header[i].key() + " : " + new String(header[i].value()) + " , ";
                msgHeaders +=  "]";

                Date dt = new Date(consumerRecord.timestamp());
                SimpleDateFormat format = new SimpleDateFormat("dd-mm-yy HH:mm:ss SSS Z");


                System.out.println(consumerRecord.key() + ", Value :" + consumerRecord.value() +
                        ", Header : " + msgHeaders +
                        " Partition : " + consumerRecord.partition() + ", Offset :" + consumerRecord.offset() +
                        ", TimeStamp type : " + consumerRecord.timestampType() +
                        ", TimeStamp : " + format.format(dt));
            }
        }

    }
}
