import com.fasterxml.jackson.databind.ObjectMapper;
import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Properties;

public class App {
    public static ObjectMapper objectMapper = new ObjectMapper();
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        DebeziumSourceFunction<RawBinlog> sourceFunction = MySqlSource.<RawBinlog>builder()
//                .hostname("kubernetes.docker.internal")
//                .port(30006)
//                .username("root")
//                .password("root")
//                .databaseList("xy_order")
//                .deserializer(new JacksonDeserialization())
//                .startupOptions(StartupOptions.latest())
//                .build();
//
//        env.addSource(sourceFunction).returns(RawBinlog.class).print();
//        env.execute();


        Properties properties = new Properties();
        properties.setProperty("bigint.unsigned.handling.mode","long");
        properties.setProperty("decimal.handling.mode","double");

        MySqlSource<String> mySqlSource = MySqlSource.<String>builder()
                .hostname("kubernetes.docker.internal")
                .port(30006)
                .username("root")
                .password("root")
                .databaseList("xy_order")
                .tableList("")
                .startupOptions(StartupOptions.latest())
                .debeziumProperties(properties)
                .deserializer(new JsonDebeziumDeserializationSchema())
                .build();
        DataStreamSource<String> mysqlSourceDS = env.fromSource(mySqlSource,
                WatermarkStrategy.noWatermarks(),
                "MysqlSource");

        mysqlSourceDS.print("in ----> ");

        env.execute("MysqlSource");
    }
}
