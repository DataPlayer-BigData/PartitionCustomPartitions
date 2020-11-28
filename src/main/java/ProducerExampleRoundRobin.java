import org.apache.kafka.clients.producer.*;
import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerExampleRoundRobin {
    private static final Logger logger = LogManager.getLogger();
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "my-partition-examples");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //By Default, DefaultPartitioner.

        //The "Round-Robin" partitioner This partitioning strategy can be used when user wants to distribute the writes to all partitions equally.
        // This is the behaviour regardless of record key hash.
        //RoundRobinPartitioner partitioner1 = new RoundRobinPartitioner();
        //props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,partitioner1);
       props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,RoundRobinPartitioner.class.getName());
        //props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,UniformStickyPartitioner);

        KafkaProducer<String,String> producer = new KafkaProducer<String,String>(props);
        String key="N/A";
        int start=1;
        int end=300 , mid=(start+end)/2;
        for(int i = start; i<=end; i++){
            if(i>=start && i<mid)
                key="US";
            if(i >=mid && i<(mid+end)/2)
                key="Keniya";
            if(i >=(mid+end)/2)
                key="India";
            ProducerRecord<String,String> record = new ProducerRecord<String,String>("topic-round-robin-key-1",key,"Simple Message " + i);
            producer.send(record,new myCallBack1(producer,record));
            System.out.println("Message sent with key : " + key );
        }
        producer.flush();
        producer.close();
 }

}

class myCallBack1 implements Callback {
    private static KafkaProducer<String,String> producer;
    private static ProducerRecord<String,String> record;
    public myCallBack1(KafkaProducer<String,String> producer, ProducerRecord<String,String> record){
        this.producer = producer;
        this.record=record;
    }

    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
        if( e != null) {
            e.printStackTrace();
            producer.close();
            System.exit(0);
        }
        else {
            System.out.println(recordMetadata.topic() + " : Partition  " + recordMetadata.partition() + " : Offset " + recordMetadata.offset());
        }
    }
}
