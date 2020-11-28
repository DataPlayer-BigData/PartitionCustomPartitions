import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class PartitionExample {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG,"my-partition-examples");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092,localhost:9093,localhost:9094");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer .class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        logger.info("Producer is created...");
        KafkaProducer<String,String> producer = new KafkaProducer<String,String >(props);
        ProducerRecord record1 = new ProducerRecord("partition-examples-Key","Other","Mr. G, Other,English,65");
        ProducerRecord record2 = new ProducerRecord("partition-examples-Key","Other","Mr. H, Other,English,98");
        ProducerRecord record3 = new ProducerRecord("partition-examples-Key","Other","Mr. I, Other,English,69");

        producer.send(record1);
        producer.send(record2);
        producer.send(record3);

        logger.info("Jobs has been completed. Now, producer is closed.");
        producer.close();


    }
}
