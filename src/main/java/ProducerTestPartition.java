import org.apache.kafka.clients.producer.*;
import org.apache.kafka.clients.producer.internals.ProducerMetadata;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerTestPartition {
    private static final Logger logger = LogManager.getLogger();
//    private static final ConcurrentMap<String, AtomicInteger> topicCounterMap = new ConcurrentHashMap<>();
//    private static int nextValue(String topic) {
//        AtomicInteger counter = topicCounterMap.computeIfAbsent(topic, k -> {
//            return new AtomicInteger(0);
//        });
//        return counter.getAndIncrement();
//    }
    public static void main(String[] args) {

//        for(int i=0;i<3;i++)
//        {
//            int avalue = nextValue("mytopic");
//            System.out.println("avalue = "+ avalue);
//
//        }
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG,"my-partition-examples");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092,localhost:9093,localhost:9094");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        props.put(ProducerConfig.LINGER_MS_CONFIG,10000);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432) ; //33.55MB
        //props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384); //16384 byte =0.016384MB
        props.put(ProducerConfig.BATCH_SIZE_CONFIG,1000);
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 60000);
        props.put(ProducerConfig.ACKS_CONFIG,"all");

       props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, UniformStickyPartitioner.class.getName());
       //props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, RoundRobinPartitioner.class.getName());
        logger.info("Producer is created...");
        //KafkaProducer<String,String> producer = new KafkaProducer<String,String >(props);
        ProducerMetadata producerMetadata = null;
        KafkaProducer<String,String> producer = new KafkaProducer(props);

        int msgLength = "keys1 Mr. G, Other,English,65".length();
        byte[] msg = "keys1 Mr. G, Other,English,65".getBytes();
        int msgByteSize = "keys1 Mr. G, Other,English,65".length();
        System.out.println("msg Length : " + msgLength );
        System.out.println("msg in byte : " + msg);
        System.out.println("msg Byte size : "+ msgByteSize);
        for(int i=1;i<=75;i++){
            ProducerRecord record1 = new ProducerRecord("partition-examples-Key1", "Mr. G, Other,English,65");
            //ProducerRecord record2 = new ProducerRecord("partition-examples-Key1","Other","Mr. H, Other,English,98");
            //ProducerRecord record3 = new ProducerRecord("partition-examples-Key1","Other","Mr. I, Other,English,69");

//        producer.send(record1,(RecordMetadata metadata,Exception e)->{
//            if(e != null)
//            {
//                System.out.println("Partition Number : "+ metadata.partition() + " : Offset " + metadata.offset());
//            }
//        });
            //producer.send(record1,null);
            //producer.send(record2,null);
            producer.send(record1, new TestCallBack());
        }

        logger.info("Jobs has been completed. Now, producer is closed.");
        producer.close();




    }
}

class TestCallBack implements Callback{

    @Override
    public void onCompletion(RecordMetadata metadata, Exception e) {
        if(metadata != null)
        {
            System.out.println("Partition Number : "+ metadata.partition() + " : Offset " + metadata.offset());
        }
    }
}