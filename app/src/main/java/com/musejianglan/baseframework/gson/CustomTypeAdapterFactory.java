package com.musejianglan.baseframework.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CustomTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (ICustomTypeAdapterGetable.class.isAssignableFrom(type.getRawType())) {
            return new CustomeTypeAdapter<T>(type);
        } else {
            return null;
        }
    }
    
    static class CustomeTypeAdapter<T> extends TypeAdapter<T> {
        
        private TypeToken<T> type;
        
        public CustomeTypeAdapter(TypeToken<T> type) {
            this.type = type;
        }

        @Override
        public void write(JsonWriter out, T value) throws IOException {
            try {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                Method m = value.getClass().getMethod("getId");
                Object obj = m.invoke(value);
                if (obj != null) {
                    out.value(Long.parseLong(obj.toString()));
                } else {
                    out.nullValue();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        @Override
        public T read(JsonReader in) throws IOException {
            try {
                @SuppressWarnings("unchecked")
				T t = (T) type.getRawType().newInstance();
                Method m = t.getClass().getMethod("setId", long.class);
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                long id = in.nextLong();
                m.invoke(t, id);
                return (T) t;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
