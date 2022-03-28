package br.com.study.reactivecalendar.core;

import br.com.study.reactivecalendar.api.controller.request.UserRequest;
import br.com.study.reactivecalendar.domain.document.UserDocument;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BotGenerator<T> {

    public static BotGenerator<UserRequest> userRequestBotGenerator = new BotGenerator<>();
    public static BotGenerator<UserDocument> userDocumentBotGenerator = new BotGenerator<>();

    public T generate(final Supplier<T> factoryCallback, final Function<T, T> saveCallback){
        return saveCallback.apply(factoryCallback.get());
    }

    public List<T> generate(final Supplier<T> factoryCallback, final Integer times){
        return Stream.generate(factoryCallback)
                .limit(times)
                .collect(Collectors.toList());
    }

    public List<T> generate(final Supplier<T> factoryCallback, final Function<List<T>, List<T>> saveCallback, final Integer times){
        return saveCallback.apply(generate(factoryCallback, times));
    }

}
