import org.apache.kafka.clients.producer.*;
import org.apache.kafka.clients.producer.internals.ProducerMetadata;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.record.Record;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.nio.cs.UTF_32;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.nio.charset.StandardCharsets.*;
public class ProduceMessagewithHeader {
    private static Logger logger = LogManager.getLogger(ProduceMessagewithHeader.class);
    public static void main(String[] args) throws ExecutionException, InterruptedException, UnsupportedEncodingException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092,localhost:9093,localhost:9094");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        //The total bytes of memory the producer can use to buffer records waiting to be sent to the server.
        // If records are sent faster than they can be delivered to the server the producer will block for max.block.ms
        // after which it will throw an exception.
        //props.put(ProducerConfig.BUFFER_MEMORY_CONFIG,5*1024*1024*1024);
        //The producer groups together any records that arrive in between request transmissions into a single batched request
        //props.put(ProducerConfig.LINGER_MS_CONFIG,0);
        //The producer will attempt to batch records together into fewer requests whenever multiple records are being sent to the same partition.
       // props.put(ProducerConfig.BATCH_SIZE_CONFIG,500);
        //this setting will limit the number of record batches the producer will send in a single request to avoid sending huge requests.
        //props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG,200);
        //TCP SCOKET BUFFER SIZE -1 MEANS DEFAULT(OS LEVEL 131072 BYTES)
        //props.put(ProducerConfig.SEND_BUFFER_CONFIG,2);
        //Define whether the timestamp in the message is message create time or log append time.
        // The value should be either `CreateTime` or `LogAppendTime`.This is topic level not producer level
       // props.put("message.timestamp.type","LogAppendTime");

        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,MyProducerInterceptorExample.class.getName());

        System.out.println("Producer is configured....");
        KafkaProducer<String,String> producer = new KafkaProducer(props);
        logger.info("Producer is created... " );

        //ProducerRecord<String,String> precord = new ProducerRecord("topic-test50","this is test2");



        //If we want to send record with timestamp
        long MsgTimeStamp = System.currentTimeMillis();
        ProducerRecord<String,String> precord = new ProducerRecord("topic-test50",0,MsgTimeStamp,"key1","this is test2");
        //ProducerRecord<String,String> precord = new ProducerRecord<>("topic-test51","INDIA","tHIS IS INDIA");

        //Adding ProducerRecord header. To test ProducerInterceptor, comment out Headers. we will add header in ProducerInterceptor
//        Headers header =precord.headers();
//        header.add("key1","Header.key1 Value IP ADDRESS".getBytes());
//        header.add("key2","Header.key2 PORT NUMBER".getBytes());
          //Headers headers = precord.headers();
          //headers.add(new RecordHeader("key1","key1 valuessss".getBytes()));

//          headers.add(new Header() {
//              @Override
//              public String key() {
//                  return "myKey";
//              }
//
//              @Override
//              public byte[] value() {
//                  return "Values of myKeyShabbir".getBytes();
//              }
//
//          });



        //Example : Synchronous Send
        //RecordMetadata metadata =producer.send(precord).get();
        //System.out.println("Partitions : "+ metadata.partition() + ", Offset : " + metadata.offset());

        //Example : Asynchronous Send
        for(int i=0;i<=1;i++)
            try {
                producer.send(precord);
            }catch(NullPointerException e){
                e.printStackTrace();
            }
           // producer.send(precord);

        //producer.send(precord, new MyTestCallback());
        //System.out.println("------------Producer Metrics -----------------");
        //If we want to know about metrics
        // Map metrics = producer.metrics();
//        Set metric_keys = metrics.keySet();
//        for(Object key : metric_keys){
//            System.out.println(key.toString() + " : " + producer.metrics().get(key).metricValue());
//        }

        //If we want to print metric name and value
//        for (Map.Entry<MetricName, ? extends Metric> entry : producer.metrics().entrySet()) {
//            System.out.println(entry.getKey().name() + " : " + entry.getValue().metricValue());
////            if(producer.metrics().containsKey(entry.getKey().name() + "-total")) {
////                System.out.println(entry.getKey().name() + "-total" + " : " + producer.metrics().get(entry.getKey().name() + "-total").metricValue());
////            }
//        }


        producer.close();


    }
}

class  MyTestCallback implements Callback{

    @Override
    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
        if(e != null)
        {
            e.printStackTrace();
        }else{
            System.out.println("Partition : " + recordMetadata.partition() + ", Offset : "+ recordMetadata.offset() );

        }
    }
}