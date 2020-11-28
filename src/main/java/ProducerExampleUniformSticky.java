import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class ProducerExampleUniformSticky {
    private static final Logger logger = LogManager.getLogger();
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "my-partition-examples");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,UniformStickyPartitioner.class.getName());
        //Regardless of partition key, it will distribute message in all available partitions.
        //https://www.confluent.io/blog/apache-kafka-producer-improvements-sticky-partitioner/
        //

        KafkaProducer<String,String> producer = new KafkaProducer<String,String>(props);
        String key="N/A";
        int start=1;
        int end=10 , mid=(start+end)/2;
        for(int i = start; i<=end; i++){
            if(i>=start && i<mid)
                key="US";
            if(i >=mid && i<(mid+end)/2)
                key="Keniya";
            if(i >=(mid+end)/2)
                key="India";
            ProducerRecord<String,String> record = new ProducerRecord<String,String>("topic-uniform-sticky-key-1",key,"Simple Message " + i);
            producer.send(record,new myCallBack2(producer,record));
            //System.out.println("Message sent with key : " + key );
        }

        producer.flush();
        producer.close();
    }
}

class myCallBack2 implements Callback {
    private static KafkaProducer<String,String> producer;
    private static ProducerRecord<String,String> record;
    public myCallBack2(KafkaProducer<String,String> producer, ProducerRecord<String,String> record){
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
