package com.grinner.tarkov.runner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;

public class TestRunner {
    public static void main(String[] args) {
        ParserConfig.getGlobalInstance().putDeserializer(OrderActionEnum.class, new OrderActionEnumDeser());

        {
            Msg msg = JSON.parseObject("{\"actionEnum\":1,\"body\":\"A\"}", Msg.class);
        }
        {
            Msg msg = JSON.parseObject("{\"actionEnum\":0,\"body\":\"B\"}", Msg.class);
        }
    }
    public static class OrderActionEnumDeser implements ObjectDeserializer {

        @SuppressWarnings("unchecked")
        @Override
        public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
            Integer intValue = parser.parseObject(int.class);
            if (intValue == 1) {
                return (T) OrderActionEnum.FAIL;
            } else if (intValue == 0) {
                return (T) OrderActionEnum.SUCC;
            }
            throw new IllegalStateException();
        }

        @Override
        public int getFastMatchToken() {
            return JSONToken.LITERAL_INT;
        }

    }
    public static enum OrderActionEnum {
        FAIL(1), SUCC(0);

        private int code;

        OrderActionEnum(int code){
            this.code = code;
        }
    }

    public static class Msg {

        public OrderActionEnum actionEnum;
        public String          body;
    }
}
