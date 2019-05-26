package com.project.system.storemanagement.http;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.project.system.storemanagement.AppConfig;
import com.project.system.storemanagement.base.BaseBean;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2017/12/15.
 */

public class ResultJsonDeser implements JsonDeserializer<BaseBean<?>> {

    @Override
    public BaseBean<?> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        BaseBean baseBean = new BaseBean();
        if (jsonElement.isJsonObject()) {
            JsonObject object = jsonElement.getAsJsonObject();
            String code = object.get("code").getAsString();
            baseBean.setCode(code);
            try {
                String message = object.get("message").getAsString();
                baseBean.setMessage(message);
            } catch (Exception e) {

            }
            if (!code.equals(AppConfig.SUCCESS))
                return baseBean;
            try {
                Type itemType = ((ParameterizedType) type).getActualTypeArguments()[0];
                baseBean.setData(jsonDeserializationContext.deserialize(object.get("data"), itemType));
            } catch (Exception e) {
                return baseBean;
            } finally {
                return baseBean;
            }
        }
        return baseBean;
    }
}
