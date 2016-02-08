package khs.kafka.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class HelloKafkaConsumer extends Thread {
	final static String clientId = "SimpleConsumerDemoClient";
	final static String TOPIC = "pythontest";
	ConsumerConnector consumerConnector;

	public HelloKafkaConsumer() {
		Properties properties = new Properties();
		properties.put("zookeeper.connect", "localhost:2181");
		properties.put("group.id", "test-group");
		ConsumerConfig consumerConfig = new ConsumerConfig(properties);
		consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
	}

	@Override
	public void run() {
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(TOPIC, new Integer(1));
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector.createMessageStreams(topicCountMap);
		KafkaStream<byte[], byte[]> stream = consumerMap.get(TOPIC).get(0);
		ConsumerIterator<byte[], byte[]> it = stream.iterator();
		while (it.hasNext())
			System.out.println(new String(it.next().message()));

	}

	// private static void printMessages(ByteBufferMessageSet messageSet) throws
	// UnsupportedEncodingException {
	// for (MessageAndOffset messageAndOffset : messageSet) {
	// ByteBuffer payload = messageAndOffset.message().payload();
	// byte[] bytes = new byte[payload.limit()];
	// payload.get(bytes);
	// System.out.println(new String(bytes, "UTF-8"));
	// }
	// }
}