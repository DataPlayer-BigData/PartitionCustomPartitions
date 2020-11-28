import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;

import java.util.Map;
//https://kafka.apache.org/24/javadoc/?org/apache/kafka/clients/producer/KafkaProducer.html
public class MyProducerInterceptorExample implements ProducerInterceptor {
    @Override
    public ProducerRecord onSend(ProducerRecord producerRecord) {
        Headers headers =producerRecord.headers();

        //header.add("key1","Header.key1 Value from interceptor".getBytes());
        //header.add("key2","Header.key2 from interceptor".getBytes());
        //Iterable<Header> headers = producerRecord.headers();
        try {
            headers.add(new Header() {
                @Override
                public String key() {
                    return "Key2-Interceptor";
                }

                @Override
                public byte[] value() {
                    return "value of Key2-Interceptor".getBytes();
                }
            });

            headers.add("key-IP","localhost".getBytes());
            headers.add("key-port","8080".getBytes());
        }catch(IllegalStateException e){
            //System.out.println(e.getMessage());
        }
        System.out.println("Header has been added from ProducerInterceptor");
        return producerRecord;
    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        System.out.println("This method is called when the record sent to the server has been acknowledged, " +
                "  or when sending the record fails before it gets sent to the server. " +
                "This method is generally called just before the user callback is called, and in additional cases " +
                "when KafkaProducer.send() throws an exception. " +
                "This method will generally execute in the background I/O thread, so the implementation should be " +
                "reasonably fast. Otherwise, sending of messages from other threads could be delayed.");
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
