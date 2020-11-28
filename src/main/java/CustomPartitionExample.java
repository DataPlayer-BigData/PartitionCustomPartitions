import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class CustomPartitionExample {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "my-partition-examples");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, DepartmentPartition.class);

        logger.info("Producer is created...");
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
        ProducerRecord record1 = new ProducerRecord("CustomPartition-example", "Science", "Mr. X, Science,Physics,89");
        ProducerRecord record2 = new ProducerRecord("CustomPartition-example",  "Science", "Mr. Y, Science,Physics,74");
        ProducerRecord record3 = new ProducerRecord("CustomPartition-example", "Science", "Mr. Tom, Science,Physics,78");

        producer.send(record1,new myCallBack(producer));
        producer.send(record2,new myCallBack(producer));
        producer.send(record3,new myCallBack(producer));

        logger.info("Jobs has been completed. Now, producer is closed.");
        producer.close();
    }
}

class myCallBack implements Callback{
    private static KafkaProducer<String,String> producer;
    public myCallBack(KafkaProducer<String,String> producer){
        this.producer = producer;
    }

    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
        if( e != null) {
            e.printStackTrace();
            producer.close();
            System.exit(0);
        }
        else
            System.out.println(recordMetadata.topic() + " : " + recordMetadata.partition() + " : " + recordMetadata.offset());
    }
}