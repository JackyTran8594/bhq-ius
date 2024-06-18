package com.bhq.ius.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

@Slf4j
public class DataUtil {
    private DataUtil() {

    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean notNull(Object obj) {
        return !isNull(obj);
    }


    public static boolean nullOrEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    public static boolean nullOrEmpty(Collection objects) {
        return objects == null || objects.isEmpty();
    }


    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().equals("");
    }

    public static boolean isNullOrEmpty(final Object obj) {
        return obj == null || obj.toString().isEmpty();
    }

    public static boolean notNullOrEmpty(Collection<?> collection) {
        return !isNullOrEmpty(collection);
    }

    public static boolean notNullOrEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }


    public static String parseToString(Object obj) {
        if (isNull(obj)) {
            return null;
        }
        return String.valueOf(obj);
    }

    public static String parseToString(Object obj, String str) {
        return obj != null ? String.valueOf(obj) : str;
    }

    public static Integer parseToInt(Object obj, Integer value) {
        String tmp = parseToString(obj);
        if (isNull(tmp)) {
            return null;
        }
        return Integer.valueOf(tmp);
    }

    public static Integer parseToInt(String value) {
        return parseToInt(value, null);
    }

    public static Integer parseToInt(Object value) {
        String tmp = parseToString(value);
        if (isNull(tmp)) {
            return null;
        }
        return Integer.valueOf(tmp);
    }

    public static boolean nullOrZero(Long value) {
        return (value == null || value.equals(0L));
    }

    public static Character parseToCharacter(Object obj) {
        return (Character) obj;
    }

    public static Double parseToDouble(Object obj) {
        if (isNull(obj)) {
            return null;
        }
        return Double.parseDouble(parseToString(obj));
    }

    public static BigInteger parseToBigInteger(Object obj) {
        if (isNull(obj)) {
            return null;
        }
        return new BigInteger(parseToString(obj));
    }

    public static Long parseToLong(Object obj) {
        if (isNull(obj)) {
            return null;
        }
        return Long.parseLong(parseToString(obj));
    }

    public static Short parseToShort(Object obj) {
        if (isNull(obj)) {
            return null;
        }
        return Short.parseShort(parseToString(obj));
    }

    public static Integer parseToInteger(Object obj) {
        if (isNull(obj)) {
            return null;
        }
        return Integer.parseInt(parseToString(obj));
    }

    public static LocalDate parseToLocalDate(Object obj) {
        if (isNull(obj)) {
            return null;
        }
        return LocalDate.parse(parseToString(obj));
    }

    public static LocalDateTime parseToLocalDateTime(Object obj) {
        if (isNull(obj)) {
            return null;
        }
        return LocalDateTime.parse(parseToString(obj));
    }

    public static LocalDate longToLocalDate(Long input) {
        if (isNull(input) || input <= 0L) {
            return null;
        }
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(input), ZoneId.systemDefault());
        return date.toLocalDate();
    }

    public static LocalDateTime parseToLocalDatetime(Object value) {
        if (value == null)
            return null;
        String tmp = parseToString(value, null);
        if (tmp == null)
            return null;

        try {
            LocalDateTime rtn = convertStringToLocalDateTime(tmp, "yyyy-MM-dd HH:mm:ss");
            return rtn;
        } catch (Exception ex) {
//            log.error(ex.getMessage(), ex);
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static LocalDateTime longToLocalDateTime(Long input) {
        if (isNull(input) || input <= 0L) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(input), ZoneId.systemDefault());
    }

    public static Long localDateToLong(LocalDate input) {
        return input.toEpochDay();
    }

    public static Long localDateTimeToLong(LocalDateTime input) {
        ZonedDateTime zdt = input.atZone(ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }

    public static String objectToJson(Object data, String defaultValue) {
        if (isNull(data)) {
            return defaultValue;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.writeValueAsString(data);
        } catch (Exception ex) {
//            log.warn(ex.getMessage(), ex);
            System.out.println(ex.getMessage());
            return "";
        }
    }

    public static String objectToJson(Object data) {
        return objectToJson(data, "");
    }

    private static <T> T jsonToObjectFronGson(String jsonData, Class<T> classOutput) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(jsonData, classOutput);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    public static <T> T jsonToObject(String jsonData, Class<T> classOutput) {
        try {
            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    false);
            return mapper.readValue(jsonData, classOutput);
        } catch (Exception ex) {
            return jsonToObjectFronGson(jsonData, classOutput);
        }
    }

    public static LocalDateTime convertStringToLocalDateTime(String value, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        if (value == null) {
            return null;
        } else if (value.contains(".")) {
            value = value.substring(0, value.indexOf('.'));
        }
        return LocalDateTime.parse(value, formatter);
    }

    public static String localDateToString(LocalDate value, String format) {
        if (!notNull(value)) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return value.format(formatter); // "1986-04-08 12:30"
    }

    public static String localDateTimeToString(LocalDateTime value, String format) {
        if (!notNull(value)) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return value.format(formatter); // "1986-04-08 12:30"
    }

    public static String formatIsdn(String msisdn) {
        if (msisdn.startsWith("0")) {
            return msisdn.substring(1);
        } else if (msisdn.startsWith("84") && msisdn.length() == 11) {
            return msisdn.substring(2);
        } else if (msisdn.startsWith("+84")) {
            return msisdn.substring(3);
        }
        return msisdn;
    }

    public static String formatMsisdn(String isdn) {
        if (isdn.startsWith("84") && isdn.length() >= 11) {
            return isdn;
        } else if (isdn.startsWith("+84")) {
            return isdn.substring(1);
        } else if (isdn.startsWith("0")) {
            isdn = isdn.substring(1);
        }
        return String.format("84%s", isdn);
    }

    public static boolean safeEqual(Object obj1, Object obj2) {
        return ((obj1 != null) && (obj2 != null) && obj2.toString().equals(obj1.toString()));
    }

    public static boolean safeEqualIgnoreCase(Object obj1, Object obj2) {
        return ((obj1 != null) && (obj2 != null) && obj2.toString().equalsIgnoreCase(obj1.toString()));
    }

    public static boolean isInteger(Object obj) {
        return obj == parseToInteger(obj);
    }

    public static String randomNumberByDate() {
        String randomNumber = String.valueOf(System.nanoTime());
        if (randomNumber.startsWith("0")) {
            randomNumber = randomNumber.replaceFirst("0", "9");
        }
        return randomNumber;
    }

    // template\import\File_mau_import_template.xlsx
    public static InputStream readInputStreamResource(String path) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(path);
        return classPathResource.getInputStream();
    }

    public static byte[] readFileResource(String path) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(path);
        return classPathResource.getInputStream().readAllBytes();

        // return
        // DataUtils.class.getClassLoader().getResourceAsStream(path).readAllBytes();
    }

    public static <T> T base64ToObject(String encodedString, Class<T> classOutput)
            throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        String decodedString = new String(decodedBytes, StandardCharsets.UTF_8.name());

        return jsonToObject(decodedString, classOutput);
    }

    public static <T> T byteToObject(byte[] input, Class<T> classOutput) {
        String jsonData = new String(input, StandardCharsets.UTF_8);
        try {
            return DataUtil.jsonToObject(jsonData, classOutput);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }


    public static String camelToSnake(String str) {
        // Regular Expression
        String regex = "([a-z])([A-Z]+)";

        // Replacement string
        String replacement = "$1_$2";

        // Replace the given regex
        // with replacement string
        // and convert it to lower case.
        str = str
                .replaceAll(
                        regex, replacement)
                .toLowerCase();

        // return string
        return str;
    }

    public static Object convertToDataType(Class<?> target, String s) {
        if (target == Object.class || target == String.class || s == null) {
            return s;
        }
        if (target == Character.class || target == char.class) {
            return s.charAt(0);
        }
        if (target == Byte.class || target == byte.class) {
            return Byte.parseByte(s);
        }
        if (target == Short.class || target == short.class) {
            return Short.parseShort(s);
        }
        if (target == Integer.class || target == int.class) {
            return Integer.parseInt(s);
        }
        if (target == Long.class || target == long.class) {
            return Long.parseLong(s);
        }
        if (target == Float.class || target == float.class) {
            return Float.parseFloat(s);
        }
        if (target == Double.class || target == double.class) {
            return Double.parseDouble(s);
        }
        if (target == Boolean.class || target == boolean.class) {
            return Boolean.parseBoolean(s);
        }
        throw new IllegalArgumentException("Don't know how to convert to " + target);
    }

    public static Object instantiate(List<String> args, String className) throws Exception {
        // Load the class.
        Class<?> clazz = Class.forName(className);
        // Search for an "appropriate" constructor.
        for (Constructor<?> ctor : clazz.getConstructors()) {
            Class<?>[] paramTypes = ctor.getParameterTypes();

            // If the arity matches, let's use it.
            if (args.size() == paramTypes.length) {

                // Convert the String arguments into the parameters' types.
                Object[] convertedArgs = new Object[args.size()];
                for (int i = 0; i < convertedArgs.length; i++) {
                    convertedArgs[i] = convertToDataType(paramTypes[i], args.get(i));
                }

                // Instantiate the object with the converted arguments.
                return ctor.newInstance(convertedArgs);
            }
        }

        throw new IllegalArgumentException("Don't know how to instantiate " + className);
    }

}
