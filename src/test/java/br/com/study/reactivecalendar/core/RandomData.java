package br.com.study.reactivecalendar.core;

import com.github.javafaker.Faker;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RandomData {

    private static final Faker faker = new Faker(new Locale("pt-BR"));

    public static Faker getFaker(){
        return faker;
    }

    public static OffsetDateTime between(final OffsetDateTime from, final OffsetDateTime to){
        var fromDate = new Date(from.toInstant().toEpochMilli());
        var toDate = new Date(to.toInstant().toEpochMilli());
        return faker.date().between(fromDate, toDate).toInstant().atOffset(ZoneOffset.UTC);
    }

    public static LocalDate between(final LocalDate from, final LocalDate to){
        var fromDate = Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant());
        var toDate = Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return faker.date().between(fromDate, toDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static OffsetDateTime birthday(){
        return faker.date().birthday().toInstant().atOffset(ZoneOffset.UTC);
    }

    public static LocalDateTime localBirthday(){
        return RandomData.birthday().toLocalDateTime();
    }

    public static BigDecimal randomPercent(final int precision){
        var intPart = faker.number().numberBetween(0, 101);
        var decimalPart = intPart == 100 ? 0 : randomNumberWithSize(precision);
        return new BigDecimal(intPart + "." + decimalPart);
    }

    public static BigDecimal randomBigDecimal(final int min, final int max, final int scale){
        return new BigDecimal(faker.number().numberBetween(min, max) + "." + randomNumberWithSize(scale));
    }

    public static BigDecimal randomPositiveBigDecimal(final int precision, final int scale){
        return new BigDecimal(randomNumberWithSize(precision - scale) + "." + randomNumberWithSize(scale));
    }

    public static BigDecimal randomNegativeBigDecimal(final int precision, final int scale){
        return new BigDecimal("-" + randomNumberWithSize(precision - scale) + "." + randomNumberWithSize(scale));
    }

    public static BigDecimal randomBigDecimal(final int precision, final int scale){
        return faker.bool().bool() ? randomPositiveBigDecimal(precision,scale) :
                randomNegativeBigDecimal(precision,scale);
    }

    public static BigDecimal randomNoZeroBigDecimal(final int precision, final int scale){
        var value = randomPositiveBigDecimal(precision, scale);
        while(value.equals(BigDecimal.ZERO)){
            value = randomPositiveBigDecimal(precision, scale);
        }
        return value;
    }

    public static String randomUrl(final String protocol){
        return String.format("%s://%s", protocol, faker.internet().url().replaceAll("\\s", ""));
    }

    public static List<String> randomUrls(final String protocol, final int size){
        return Stream.generate(() -> randomUrl(protocol))
                .limit(size)
                .collect(Collectors.toList());
    }

    public static Object randomType(){
        var number = faker.number().randomDigit();
        return switch (number) {
            case 1 -> faker.bool().bool();
            case 2 -> faker.number().randomNumber();
            case 3 -> faker.funnyName().name();
            default -> randomBigDecimal(15, 2);
        };
    }

    private static long randomNumberWithSize(final int size){
        var multiplyToSetPosition = 1L;
        long value = 0L;
        for (int i = 0; i < size; i++) {
            value += faker.number().randomDigit() * multiplyToSetPosition;
            multiplyToSetPosition *= 10;
        }
        return value;
    }

    private static long actualUniqueValue = 0;

    public static long nextUniqueValue(){
        ++ actualUniqueValue;
        return actualUniqueValue;
    }

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        var x = new Random().nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz, List<Integer> blackList){
        int x = 0;
        do {
            x = new Random().nextInt(clazz.getEnumConstants().length);
        }while (blackList.contains(x));
        return clazz.getEnumConstants()[x];
    }

    public static String randomMongoId(){
        return ObjectId.get().toString();
    }

}
