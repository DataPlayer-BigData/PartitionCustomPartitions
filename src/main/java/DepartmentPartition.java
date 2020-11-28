import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.InvalidRecordException;
import org.apache.kafka.common.errors.InvalidPartitionsException;
import org.apache.kafka.common.errors.InvalidTopicException;
import sun.nio.cs.StandardCharsets;

import java.util.Map;

public class DepartmentPartition implements Partitioner {

    public void configure(Map<String, ?> map) {

    }

    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        if((keyBytes == null) || ((key instanceof Integer)))
             throw new InvalidRecordException("Topic Key must be a Department.");
        if(cluster.partitionCountForTopic(topic) !=3 )
            throw new InvalidTopicException("Topic must have exactly three partitions.");
        String KEY =new String(keyBytes);
        int returnValue=-1;
        if((KEY.equals("CS")) || (KEY.equals("IT")))
            returnValue= 0;
        else if(KEY.equals("CE"))
            returnValue= 1;
        else                    //if(KEY.equals("Other"))
            returnValue= 2;

        if(returnValue  == -1)
            throw new InvalidPartitionsException("There is a problem with partitions..." + KEY + " : returnValue - " +returnValue);
        //System.out.println("Partition Number : " + returnValue);
        return returnValue;
    }

    public void close() {

    }

}
