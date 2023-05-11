package common;

import bean.RawBinlog;
import com.ververica.cdc.debezium.DebeziumDeserializationSchema;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.Collector;
import org.apache.kafka.connect.source.SourceRecord;

public class JacksonDeserialization implements DebeziumDeserializationSchema<RawBinlog> {

    /**
     * {
     * "db":"",
     * "tableName":"",
     * "before":{"id":"1001","name":""...},
     * "after":{"id":"1001","name":""...},
     * "op":""
     * }
     */
    public void deserialize(SourceRecord sourceRecord, Collector<RawBinlog> collector) throws Exception {
        //创建JSON对象用于封装结果数据

//        ObjectMapper objectMapper = new ObjectMapper();
//
//        //获取库名&表名
//        String topic = sourceRecord.topic();
//        String[] fields = topic.split("\\.");
//        result.put("db", fields[1]);
//        result.put("tableName", fields[2]);
//
//        //获取before数据
//        Struct value = (Struct) sourceRecord.value();
//        Struct before = value.getStruct("before");
//        JSONObject beforeJson = new JSONObject();
//        if (before != null) {
//            //获取列信息
//            Schema schema = before.schema();
//            List<Field> fieldList = schema.fields();
//
//            for (Field field : fieldList) {
//                beforeJson.put(field.name(), before.get(field));
//            }
//        }
//        result.put("before", beforeJson);
//
//        //获取after数据
//        Struct after = value.getStruct("after");
//        JSONObject afterJson = new JSONObject();
//        if (after != null) {
//            //获取列信息
//            Schema schema = after.schema();
//            List<Field> fieldList = schema.fields();
//
//            for (Field field : fieldList) {
//                afterJson.put(field.name(), after.get(field));
//            }
//        }
//        result.put("after", afterJson);
//
//        //获取操作类型
//        Envelope.Operation operation = Envelope.operationFor(sourceRecord);
//        result.put("op", operation);
//
//        //输出数据
//
//        collector.collect(JSONObject.parseObject(result.toJSONString(), SourceBinlog.class));
    }

    public TypeInformation<RawBinlog> getProducedType() {
        return BasicTypeInfo.getInfoFor(RawBinlog.class);
    }
}
